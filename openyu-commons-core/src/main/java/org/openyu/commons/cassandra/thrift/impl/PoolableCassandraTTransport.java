package org.openyu.commons.cassandra.thrift.impl;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.pool.ObjectPool;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import org.openyu.commons.util.Delegateable;

public class PoolableCassandraTTransport extends TTransport implements
		Delegateable<Cassandra.Client> {
	/**
	 * 真正關閉連線
	 */
	private boolean closed = false;

	private TTransport ttransport = null;

	private ObjectPool<TTransport> pool = null;
	//
	private boolean compactProtocol;

	private TProtocol tprotocol;

	private Cassandra.Client delegate;

	public PoolableCassandraTTransport(TTransport ttransport,
			ObjectPool<TTransport> pool, boolean compactProtocol) {
		this.ttransport = ttransport;
		this.pool = pool;
		//
		this.compactProtocol = compactProtocol;
		if (!this.compactProtocol) {
			this.tprotocol = new TBinaryProtocol(ttransport);
		} else {
			this.tprotocol = new TCompactProtocol(ttransport);
		}
		this.delegate = new Cassandra.Client(this.tprotocol);
	}

	public Cassandra.Client getDelegate() {
		return delegate;
	}

	protected void checkOpen() throws TTransportException {
		if (this.closed) {
			if (null != this.ttransport) {
				String label = "";
				try {
					label = this.ttransport.toString();
				} catch (Exception ex) {
				}
				throw new TTransportException("TTransport " + label
						+ " was already closed");
			}
			throw new TTransportException("TTransport is null");
		}
	}

	public synchronized void close() {
		if (this.closed) {
			return;
		}
		boolean isUnderlyingConectionClosed;
		try {
			isUnderlyingConectionClosed = !(this.ttransport.isOpen());
		} catch (Exception e) {
			try {
				this.pool.invalidateObject(this);
			} catch (IllegalStateException ise) {
				this.closed = true;
				reallyClose();
			} catch (Exception ie) {
			}
			throw new RuntimeException(
					"Cannot close TTransport (isClosed check failed)");
		}
		if (!(isUnderlyingConectionClosed)) {
			try {
				this.pool.returnObject(this);
				// }
			} catch (IllegalStateException e) {
				this.closed = true;
				reallyClose();
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(
						"Cannot close TTransport (return to pool failed)");
			}
		} else {
			try {
				this.pool.invalidateObject(this);
			} catch (IllegalStateException e) {
				this.closed = true;
				reallyClose();
			} catch (Exception ie) {
			}
			throw new RuntimeException("Already closed.");
		}
	}

	public void reallyClose() {
		ttransport.close();
	}

	public boolean isOpen() {
		return (!closed) || (this.ttransport.isOpen());
	}

	public void open() throws TTransportException {
		try {
			this.ttransport.open();
		} catch (TTransportException e) {
			handleException(e);
		}
	}

	public boolean peek() {
		try {
			checkOpen();
			return this.ttransport.peek();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int read(byte[] buf, int off, int len) throws TTransportException {
		checkOpen();
		try {
			return this.ttransport.read(buf, off, len);
		} catch (TTransportException e) {
			handleException(e);
		}
		return 0;
	}

	public int readAll(byte[] buf, int off, int len) throws TTransportException {
		checkOpen();
		try {
			return this.ttransport.readAll(buf, off, len);
		} catch (TTransportException e) {
			handleException(e);
		}
		return 0;
	}

	public void write(byte[] buf) throws TTransportException {
		checkOpen();
		try {
			this.ttransport.write(buf);
		} catch (TTransportException e) {
			handleException(e);
		}
	}

	public void write(byte[] buf, int off, int len) throws TTransportException {
		checkOpen();
		try {
			this.ttransport.write(buf, off, len);
		} catch (TTransportException e) {
			handleException(e);
		}
	}

	public void flush() throws TTransportException {
		checkOpen();
		try {
			this.ttransport.flush();
		} catch (TTransportException e) {
			handleException(e);
		}
	}

	public byte[] getBuffer() {
		try {
			checkOpen();
			return this.ttransport.getBuffer();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int getBufferPosition() {
		try {
			checkOpen();
			return this.ttransport.getBufferPosition();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int getBytesRemainingInBuffer() {
		try {
			checkOpen();
			return this.ttransport.getBytesRemainingInBuffer();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void consumeBuffer(int len) {
		try {
			checkOpen();
			this.ttransport.consumeBuffer(len);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void handleException(TTransportException e)
			throws TTransportException {
		throw e;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("closed", closed);
		builder.append("target", delegate);
		return builder.toString();
	}
}
