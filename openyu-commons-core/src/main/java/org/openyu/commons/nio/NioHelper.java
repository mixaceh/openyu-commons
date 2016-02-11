package org.openyu.commons.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.misc.UnsafeHelper;
import org.openyu.commons.thread.ThreadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Nio輔助類
 */
public final class NioHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(NioHelper.class);

	/**
	 * 預設接收緩衝區大小
	 */
	public static final int DEFAULT_RECEIVE_BUFFER_SIZE = 128 * 1024;// 128k

	/**
	 * 預設發送緩衝區大小
	 */
	public static final int DEFAULT_SEND_BUFFER_SIZE = (int) (DEFAULT_RECEIVE_BUFFER_SIZE * 1.5d);// 195k

	/**
	 * 預設重試次數
	 */
	public static final int DEFAULT_RETRY_NUMBER = 3;

	/**
	 * 預設重試暫停毫秒
	 */
	public static final long DEFAULT_RETRY_PAUSE_MILLS = 1 * 1000L;

	/**
	 * 重試補償
	 */
	public static int[] RETRY_BACK_OFF = { 1, 1, 1, 2, 2, 4, 4, 8, 16, 32 };

	/**
	 * 預設等待連線毫秒
	 */
	public static final long DEFAULT_WAIT_CONNECT_MILLS = 3 * 1000L;

	/**
	 * Instantiates a new blank helper.
	 */
	private NioHelper() {
		throw new HelperException(
				new StringBuilder().append(NioHelper.class.getName()).append(" can not construct").toString());
	}

	public static InetSocketAddress createInetSocketAddress(String ip, int port) {
		InetSocketAddress address = null;
		if (ip == null) {
			address = new InetSocketAddress(port);
		} else {
			address = new InetSocketAddress(ip, port);
		}
		return address;
	}

	public static void cancel(SelectionKey key) {
		if (key != null) {
			try {
				key.cancel();
				// key.channel().close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void close(SelectableChannel channel) {
		if (channel != null && channel.isOpen()) {
			try {
				channel.close();
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
	}

	public static void close(Selector selector) {
		if (selector != null && selector.isOpen()) {
			try {
				selector.close();
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
		}
	}

	// public static byte[] ___read(SocketChannel socketChannel, ByteBuffer
	// byteBuffer)
	// {
	// byte[] result = null;
	// try
	// {
	// if (socketChannel != null && socketChannel.isConnected() && byteBuffer !=
	// null)
	// {
	// int read = socketChannel.read(byteBuffer);
	// //System.out.println("read..." + read);
	// byteBuffer.flip();
	// //
	// if (read > 0)
	// {
	// result = new byte[read];
	// byteBuffer.get(result);
	// }
	// //
	// byteBuffer.clear();
	// }
	// }
	// catch (Exception ex)
	// {
	// //ex.printStackTrace();
	// }
	// return result;
	// }

	/**
	 * read -> flip -> get -> clear
	 * 
	 * @param socketChannel
	 * @param byteBuffer
	 * @return
	 */
	public static byte[] read(SocketChannel socketChannel, ByteBuffer byteBuffer) {
		byte[] result = null;
		try {
			if (socketChannel != null && socketChannel.isConnected() && byteBuffer != null) {
				// int totalRead = 0;
				int read = 0;
				// #fix 改用loop處理
				while ((read = socketChannel.read(byteBuffer)) > 0) {
					if (result == null) {
						result = new byte[0];
					}
					//
					byteBuffer.flip();
					byte[] buff = new byte[read];
					byteBuffer.get(buff);
					byteBuffer.clear();
					//
					// result = ArrayHelper.add(result, buf);
					result = UnsafeHelper.putByteArray(result, result.length, buff);
					// totalRead += read;
				}
				//
				// System.out.println("read..." + totalRead);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * put -> flip -> write -> clear
	 * 
	 * @param socketChannel
	 * @param byteBuffer
	 * @param values
	 * @return
	 */
	public static int write(SocketChannel socketChannel, ByteBuffer byteBuffer, byte[]... values) {
		int result = 0;
		try {
			if (socketChannel != null && socketChannel.isConnected() && byteBuffer != null) {

				// 把要寫出的bytes放到buffer
				for (byte[] buff : values) {
					byteBuffer.put(buff);
				}
				byteBuffer.flip();

				// #issue 有可能會沒寫完
				// socketChannel.write(byteBuffer);

				// #fix 改用loop處理
				while (byteBuffer.hasRemaining()) {
					result += socketChannel.write(byteBuffer);
				}
				byteBuffer.clear();

				// int send = socketChannel.write(byteBuffer);
				// result = send;
				// // 服务器端可能因为缓存区满，而导致数据传输失败，需要重新发送
				// while (send == 0) {
				// Thread.sleep(10);
				// send = socketChannel.write(byteBuffer);
				// result += send;
				// }
				// byteBuffer.clear();

				// System.out.println("write..." + result);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * socket是否連上
	 * 
	 * @param socketChannel
	 * @return
	 */
	public static boolean finishConnect(SocketChannel socketChannel) {
		boolean result = false;
		try {
			if (socketChannel != null) {
				result = socketChannel.finishConnect();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return result;
	}

	/**
	 * socket未連上時,最多等待幾毫秒
	 * 
	 * @param socketChannel
	 * @param waitMillis
	 * @return
	 */
	public static boolean waitConnect(SocketChannel socketChannel, long waitMillis) {
		boolean result = false;
		long beg = System.currentTimeMillis();
		long end = 0;
		//
		result = finishConnect(socketChannel);
		//
		while (!result) {
			end = System.currentTimeMillis();
			//
			if ((end - beg) >= waitMillis) {
				break;
			}
			//
			ThreadHelper.sleep(1 * 1000);// 間隔一秒
			result = finishConnect(socketChannel);
			// System.out.println((end - beg));
		}
		return result;
	}

	public static void interestOps(SelectionKey selectionKey, int ops) {
		try {
			if (selectionKey != null) {
				if ((selectionKey.interestOps() & ops) == 0) {
					selectionKey.interestOps(selectionKey.interestOps() | ops);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 計算重試暫停毫秒
	 *
	 * @param retryPauseMills
	 * @param tries
	 * @return
	 */
	public static long retryPause(int tries, long retryPauseMills) {
		int triesCount = tries;
		if (triesCount >= RETRY_BACK_OFF.length) {
			triesCount = RETRY_BACK_OFF.length - 1;
		}
		return (retryPauseMills * RETRY_BACK_OFF[triesCount]);
	}
}
