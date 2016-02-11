package org.openyu.commons.mina.impl;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderException;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.mina.HorseService;
import org.openyu.commons.misc.UnsafeHelper;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorseServiceImpl extends BaseServiceSupporter implements
		HorseService {

	private static final long serialVersionUID = 8486351372297601296L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(HorseServiceImpl.class);

	private ProtocolEncoder encoder;

	private ProtocolDecoder decoder;

	public HorseServiceImpl() {
		this.encoder = new BinaryEncoder();
		// this.decoder = new BinaryDecoder();
		this.decoder = new SheepDecoder();
	}


	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return this.encoder;
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return this.decoder;
	}

	protected class BinaryEncoder extends ProtocolEncoderAdapter {

		public void encode(IoSession session, Object message,
				ProtocolEncoderOutput out) throws Exception {
			if (!(message instanceof byte[])) {
				throw new ProtocolEncoderException("Message is not byte[].");
			}
			//
			IoBuffer buff = IoBuffer.wrap((byte[]) message);
			out.write(buff);
		}
	}

	protected class BinaryDecoder extends ProtocolDecoderAdapter {

		public void decode(IoSession session, IoBuffer in,
				ProtocolDecoderOutput out) throws Exception {
			if (in == null) {
				throw new ProtocolDecoderException("IoBuffer is null");
			}
			//
			byte[] buff = new byte[in.limit()];
			in.get(buff);
			out.write(buff);
		}
	}

	protected class SheepDecoder extends CumulativeProtocolDecoder {

		public SheepDecoder() {
		}

		protected boolean doDecode(IoSession session, IoBuffer in,
				ProtocolDecoderOutput out) throws Exception {
			if (in.remaining() < 8) {
				return false;
			}
			//
			int start = in.position();
			int pack = in.getInt();
			int length = in.getInt();
			int remaining = in.remaining();
			if (remaining == (length - 8)) {
				try {
					doDecode(session, in.slice(), out, pack, length);
				} finally {
					in.position(in.limit());
				}
				return true;
			} else if (remaining < ((length - 8))) {
				in.position(start);
				return false;
			} else {
				int limit = in.limit();
				in.limit(start + length);
				try {
					doDecode(session, in.slice(), out, pack, length);
				} finally {
					in.position(in.limit());
					in.limit(limit);
				}
				doDecode(session, in, out);
				return true;
			}
		}

		protected void doDecode(IoSession session, IoBuffer in,
				ProtocolDecoderOutput out, int pack, int length) {
			byte[] result = new byte[0];
			int limit = in.limit();
			byte[] bodyBytes = new byte[limit];
			in.get(bodyBytes);
			in.position(limit);
			//
			byte[] headBytes = ByteHelper.toByteArray(pack);
			byte[] lengthBytes = ByteHelper.toByteArray(length);
			//
			// result = ArrayHelper.add(headBytes, lengthBytes);
			// result = ArrayHelper.add(result, bodyBytes);

			result = headBytes;
			result = UnsafeHelper.putByteArray(result, result.length,
					lengthBytes);
			result = UnsafeHelper
					.putByteArray(result, result.length, bodyBytes);
			//
			out.write(result);
		}
	}
}
