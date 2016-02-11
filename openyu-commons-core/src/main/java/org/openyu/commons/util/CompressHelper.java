package org.openyu.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.openyu.commons.commons.pool.CacheCallback;
import org.openyu.commons.commons.pool.SoftReferenceCacheFactory;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.commons.pool.impl.SoftReferenceCacheFactoryFactoryBean;
import org.openyu.commons.commons.pool.supporter.CacheableObjectFactorySupporter;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.util.impl.CompressProcessorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import lzma.sdk.lzma.Decoder;
//import lzma.sdk.lzma.Encoder;
import org.xerial.snappy.Snappy;

import SevenZip.Compression.LZMA.Decoder;
import SevenZip.Compression.LZMA.Encoder;

import com.ning.compress.lzf.LZFDecoder;
import com.ning.compress.lzf.LZFEncoder;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

/**
 * 壓縮 The Class CompressHelper.
 * 
 * @see CompressType
 * 
 *      1.LZMA
 * 
 *      2.GZIP
 * 
 *      3.DEFLATE
 * 
 *      4.DEFLATER
 * 
 *      5.SNAPPY
 * 
 *      6.LZF
 * 
 *      7.LZ4
 */
public final class CompressHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(CompressHelper.class);

	private static final int BUFFER_LENGTH = 1024;

	/**
	 * lzma use
	 */
	private static final int propSize = 5;

	/**
	 * lzma use
	 */
	private static final byte[] props = new byte[propSize];
	/**
	 * 壓縮處理器工廠
	 */
	private static SoftReferenceCacheFactoryFactoryBean<CompressProcessor, SoftReferenceCacheFactory<CompressProcessor>> compressProcessorCacheFactoryFactoryBean;

	/**
	 * 壓縮處理器
	 */
	private static SoftReferenceCacheFactory<CompressProcessor> compressProcessorCacheFactory;

	static {
		new Static();
	}

	protected static class Static {
		@SuppressWarnings("unchecked")
		public Static() {
			props[0] = 0x5d;
			props[1] = 0x00;
			props[2] = 0x00;
			props[3] = 0x10;
			props[4] = 0x00;
			//
			try {
				compressProcessorCacheFactoryFactoryBean = new SoftReferenceCacheFactoryFactoryBean<CompressProcessor, SoftReferenceCacheFactory<CompressProcessor>>();
				compressProcessorCacheFactoryFactoryBean
						.setCacheableObjectFactory(new CacheableObjectFactorySupporter<CompressProcessor>() {

							private static final long serialVersionUID = -2745795176962911555L;

							public CompressProcessor makeObject() throws Exception {
								CompressProcessor obj = new CompressProcessorImpl();
								obj.setCompress(ConfigHelper.isCompress());
								obj.setCompressType(ConfigHelper.getCompressType());
								return obj;
							}

							public boolean validateObject(CompressProcessor obj) {
								return true;
							}

							public void activateObject(CompressProcessor obj) throws Exception {
								obj.setCompress(ConfigHelper.isCompress());
								obj.setCompressType(ConfigHelper.getCompressType());
							}

							public void passivateObject(CompressProcessor obj) throws Exception {
								obj.reset();
							}
						});
				compressProcessorCacheFactoryFactoryBean.start();
				compressProcessorCacheFactory = (SoftReferenceCacheFactory<CompressProcessor>) compressProcessorCacheFactoryFactoryBean
						.getObject();

			} catch (Exception ex) {
				throw new HelperException("new Static() Initializing failed", ex);
			}
		}
	}

	private CompressHelper() {
		throw new HelperException(
				new StringBuilder().append(CompressHelper.class.getName()).append(" can not construct").toString());
	}

	/**
	 * lz4 壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] lz4(byte[] value) {
		byte[] result = new byte[0];
		try {
			LZ4Factory factory = LZ4Factory.fastestInstance();
			LZ4Compressor compressor = factory.fastCompressor();
			//
			final int INTEGER_BYTES = 4;
			int length = value.length;
			int maxCompressedLength = compressor.maxCompressedLength(length);
			byte[] buff = new byte[INTEGER_BYTES + maxCompressedLength];
			// 用4個byte紀錄原始長度
			buff[0] = (byte) (length >>> 24);
			buff[1] = (byte) (length >>> 16);
			buff[2] = (byte) (length >>> 8);
			buff[3] = (byte) (length);

			// 壓縮後長度
			int written = compressor.compress(value, 0, length, buff, INTEGER_BYTES, maxCompressedLength);
			// 新長度=4+壓縮後長度
			int newLength = INTEGER_BYTES + written;
			result = new byte[newLength];
			System.arraycopy(buff, 0, result, 0, newLength);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * lz4 解壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] unlz4(byte[] value) {
		byte[] result = new byte[0];
		try {
			LZ4Factory factory = LZ4Factory.fastestInstance();
			LZ4FastDecompressor decompressor = factory.fastDecompressor();
			//
			final int INTEGER_BYTES = 4;
			// 取原始長度
			int uncompressedLength = ((value[0] & 0xFF) << 24) | ((value[1] & 0xFF) << 16) | ((value[2] & 0xFF) << 8)
					| ((value[3] & 0xFF));
			result = new byte[uncompressedLength];
			int read = decompressor.decompress(value, INTEGER_BYTES, result, 0, uncompressedLength);
			if (read != (value.length - INTEGER_BYTES)) {
				result = new byte[0];
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * snappy 壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] snappy(byte[] value) {
		byte[] result = new byte[0];
		try {
			result = Snappy.compress(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * snappy 解壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] unsnappy(byte[] value) {
		byte[] result = new byte[0];
		try {
			result = Snappy.uncompress(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * lzma 壓縮
	 *
	 * MODE_1(1, 0, 16, 64, Encoder.EMatchFinderTypeBT2, 3, 0, 2),
	 *
	 * MODE_2(2, 0, 20, 64, Encoder.EMatchFinderTypeBT2, 3, 0, 2),
	 *
	 * MODE_3(3, 1, 19, 64, Encoder.EMatchFinderTypeBT4, 3, 0, 2),
	 *
	 * MODE_4(4, 2, 20, 64, Encoder.EMatchFinderTypeBT4, 3, 0, 2),
	 *
	 * MODE_5(5, 2, 21, 128, Encoder.EMatchFinderTypeBT4, 3, 0, 2),
	 *
	 * MODE_6(6, 2, 22, 128, Encoder.EMatchFinderTypeBT4, 3, 0, 2),
	 *
	 * MODE_7(7, 2, 23, 128, Encoder.EMatchFinderTypeBT4, 3, 0, 2),
	 *
	 * MODE_8(8, 2, 24, 255, Encoder.EMatchFinderTypeBT4, 3, 0, 2),
	 *
	 * MODE_9(9, 2, 25, 255, Encoder.EMatchFinderTypeBT4, 3, 0, 2);
	 *
	 * @param value
	 * @return
	 */
	public static byte[] lzma(byte[] value) {
		byte[] result = new byte[0];
		//
		ByteArrayInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			Encoder encoder = new Encoder();
			in = new ByteArrayInputStream(value);
			out = new ByteArrayOutputStream();
			// fast
			encoder.SetEndMarkerMode(true);
			encoder.SetAlgorithm(0);
			// dictionarysize是主要佔用記憶體的地方（0-30）
			encoder.SetDictionarySize(1 << 20);
			// fb是主要速度的地方（越小越快壓縮率越低，5-273）
			encoder.SetNumFastBytes(64);
			encoder.SetMatchFinder(Encoder.EMatchFinderTypeBT2);
			encoder.SetLcLpPb(3, 0, 2);
			encoder.WriteCoderProperties(out);
			//
			encoder.Code(in, out, -1, -1, null);
			result = out.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(in);
			IoHelper.close(out);
		}
		return result;
	}

	/**
	 * lzma 解壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] unlzma(byte[] value) {
		byte[] result = new byte[0];
		//
		ByteArrayInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			Decoder decoder = new Decoder();
			byte[] properties = new byte[5];
			System.arraycopy(value, 0, properties, 0, 5);
			if (properties.length != 5) {
				throw new RuntimeException("input .lzma is too short");
			}
			decoder.SetDecoderProperties(properties);
			//
			int length = value.length - properties.length;
			byte[] data = new byte[length];
			System.arraycopy(value, properties.length, data, 0, length);
			//
			in = new ByteArrayInputStream(data);
			out = new ByteArrayOutputStream();
			decoder.Code(in, out, -1);
			result = out.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(in);
			IoHelper.close(out);
		}
		return result;
	}

	/**
	 * lzf 壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] lzf(byte[] value) {
		byte[] result = new byte[0];
		try {
			result = LZFEncoder.encode(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * lzf 解壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] unlzf(byte[] value) {
		byte[] result = new byte[0];
		try {
			result = LZFDecoder.decode(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * deflater 壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] deflater(byte[] value) {
		byte[] result = new byte[0];
		Deflater deflater = null;
		ByteArrayOutputStream out = null;
		//
		try {
			deflater = new Deflater();
			deflater.setLevel(Deflater.BEST_SPEED);// fast
			deflater.setInput(value);
			deflater.finish();
			//
			out = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			while (!deflater.finished()) {
				int count = deflater.deflate(buff);
				out.write(buff, 0, count);
			}
			//
			result = out.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (deflater != null) {
				deflater.end();// 需end,不然會有oom(約5000次時)
			}
			IoHelper.close(out);
		}
		return result;
	}

	/**
	 * inflater 解壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] inflater(byte[] value) {
		byte[] result = new byte[0];
		Inflater inflater = null;
		ByteArrayOutputStream out = null;
		try {
			inflater = new Inflater();
			inflater.setInput(value);
			//
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				out.write(buffer, 0, count);
			}
			//
			result = out.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (inflater != null) {
				inflater.end();// 不end也不會有oom,不過還是關了吧
			}
			IoHelper.close(out);
		}
		return result;
	}

	/**
	 * gzip 壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] gzip(byte[] value) {
		byte[] result = new byte[0];
		GZIPOutputStream gout = null;
		ByteArrayOutputStream out = null;
		//
		try {
			out = new ByteArrayOutputStream();
			gout = new GZIPOutputStream(out, BUFFER_LENGTH);
			gout.write(value);
			gout.finish();
			gout.flush();
			//
			result = out.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(gout);
			IoHelper.close(out);
		}
		return result;
	}

	/**
	 * gzip 解壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] ungzip(byte[] value) {
		byte[] result = new byte[0];
		ByteArrayInputStream in = null;
		GZIPInputStream gin = null;
		ByteArrayOutputStream out = null;
		try {
			//
			in = new ByteArrayInputStream(value);
			gin = new GZIPInputStream(in, BUFFER_LENGTH);
			out = new ByteArrayOutputStream();
			//
			byte[] buff = new byte[BUFFER_LENGTH];
			int read = 0;
			while ((read = gin.read(buff)) > -1) {
				out.write(buff, 0, read);
			}
			//
			result = out.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(in);
			IoHelper.close(gin);
			IoHelper.close(out);
		}
		return result;
	}

	/**
	 * deflate stream 壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] deflate(byte[] value) {
		byte[] result = new byte[0];
		//
		DeflaterOutputStream deflater = null;
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			deflater = new DeflaterOutputStream(out);
			deflater.write(value, 0, value.length);
			deflater.finish();
			deflater.flush();
			//
			result = out.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(deflater);
			IoHelper.close(out);
		}
		return result;
	}

	/**
	 * inflate stream 解壓縮
	 *
	 * @param value
	 * @return
	 */
	public static byte[] inflate(byte[] value) {
		byte[] result = new byte[0];
		//
		InflaterInputStream inflater = null;
		ByteArrayInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = new ByteArrayInputStream(value);
			out = new ByteArrayOutputStream();
			inflater = new InflaterInputStream(in);
			//
			byte[] buff = new byte[BUFFER_LENGTH];
			int read = 0;
			while ((read = inflater.read(buff)) > -1) {
				out.write(buff, 0, read);
			}
			//
			result = out.toByteArray();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(in);
			IoHelper.close(inflater);
			IoHelper.close(out);
		}
		return result;
	}

	public static byte[] compressWithProcessor(final byte[] value) {
		byte[] result = new byte[0];
		//
		result = (byte[]) compressProcessorCacheFactory.execute(new CacheCallback<CompressProcessor>() {
			public Object doInAction(CompressProcessor obj) throws CacheException {
				return obj.compress(value);
			}
		});
		//
		return result;
	}

	public static byte[] decompressWithProcessor(final byte[] value) {
		byte[] result = new byte[0];
		//
		result = (byte[]) compressProcessorCacheFactory.execute(new CacheCallback<CompressProcessor>() {
			public Object doInAction(CompressProcessor obj) throws CacheException {
				return obj.uncompress(value);
			}
		});
		//
		return result;
	}

	// refactor to Compresable
	// /**
	// * 壓縮
	// *
	// * @param compressTypeValue
	// * 壓縮類別
	// * @see CompressType
	// * @param values
	// * @return
	// */
	// public static byte[] execute(String compressTypeValue, byte[] values) {
	// CompressType compressType = EnumHelper.valueOf(CompressType.class,
	// compressTypeValue);
	// return execute(compressType, values);
	// }
	//
	// /**
	// * 壓縮
	// *
	// * @param compressTypeValue
	// * 壓縮類別
	// * @see CompressType
	// * @param values
	// * @return
	// */
	// public static byte[] execute(int compressTypeValue, byte[] values) {
	// CompressType compressType = EnumHelper.valueOf(CompressType.class,
	// compressTypeValue);
	// return execute(compressType, values);
	// }
	//
	// /**
	// * 壓縮
	// *
	// * @param compressType
	// * 壓縮類別
	// * @see CompressType
	// * @param values
	// * @return
	// */
	// public static byte[] execute(CompressType compressType, byte[] values) {
	// byte[] result = new byte[0];
	// //
	// if (compressType == null) {
	// throw new IllegalArgumentException(
	// "The CompressType must not be null");
	// }
	// //
	// switch (compressType) {
	// case LZMA: {
	// result = lzma(values);
	// break;
	//
	// }
	// case GZIP: {
	// result = gzip(values);
	// break;
	// }
	// case DEFLATE: {
	// result = deflate(values);
	// break;
	// }
	// case DEFLATER: {
	// result = deflater(values);
	// break;
	// }
	// case SNAPPY: {
	// result = snappy(values);
	// break;
	// }
	// case LZF: {
	// result = lzf(values);
	// break;
	// }
	// case LZ4: {
	// result = lz4(values);
	// break;
	// }
	// default: {
	// throw new UnsupportedOperationException(
	// "The CompressType is not unsupported" + compressType);
	// }
	// }
	// //
	// return result;
	// }
	//
	// /**
	// * 解壓
	// *
	// * @param compressTypeValue
	// * 壓縮類別
	// * @see CompressType
	// * @param values
	// * @return
	// */
	// public static byte[] unexecute(String compressTypeValue, byte[] values) {
	// CompressType compressType = EnumHelper.valueOf(CompressType.class,
	// compressTypeValue);
	// return unexecute(compressType, values);
	// }
	//
	// /**
	// * 解壓
	// *
	// * @param compressTypeValue
	// * 壓縮類別
	// * @see CompressType
	// * @param values
	// * @return
	// */
	// public static byte[] unexecute(int compressTypeValue, byte[] values) {
	// CompressType compressType = EnumHelper.valueOf(CompressType.class,
	// compressTypeValue);
	// return unexecute(compressType, values);
	// }
	//
	// /**
	// * 解壓
	// *
	// * @param compressType
	// * 壓縮類別
	// * @see CompressType
	// * @param values
	// * @return
	// */
	// public static byte[] unexecute(CompressType compressType, byte[] values)
	// {
	// byte[] result = new byte[0];
	// //
	// if (compressType == null) {
	// throw new IllegalArgumentException(
	// "The CompressType must not be null");
	// }
	// //
	// switch (compressType) {
	// case LZMA: {
	// result = unlzma(values);
	// break;
	//
	// }
	// case GZIP: {
	// result = ungzip(values);
	// break;
	// }
	// case DEFLATE: {
	// result = inflate(values);
	// break;
	// }
	// case DEFLATER: {
	// result = inflater(values);
	// break;
	// }
	// case SNAPPY: {
	// result = unsnappy(values);
	// break;
	// }
	// case LZF: {
	// result = unlzf(values);
	// break;
	// }
	// case LZ4: {
	// result = unlz4(values);
	// break;
	// }
	// default: {
	// throw new UnsupportedOperationException(
	// "The CompressType is not unsupported" + compressType);
	// }
	// }
	// //
	// return result;
	// }
}
