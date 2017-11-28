package org.openyu.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jgroups.util.Util;
import org.openyu.commons.commons.pool.CacheCallback;
import org.openyu.commons.commons.pool.SoftReferenceCacheFactory;
import org.openyu.commons.commons.pool.ex.CacheException;
import org.openyu.commons.commons.pool.impl.SoftReferenceCacheFactoryFactoryBean;
import org.openyu.commons.commons.pool.supporter.CacheableObjectFactorySupporter;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.lang.ByteHelper;
import org.openyu.commons.lang.NumberHelper;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import de.ruedigermoeller.serialization.FSTConfiguration;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.jaxrs.smile.JacksonSmileProvider;

import org.openyu.commons.util.impl.SerializeProcessorImpl;

/**
 * 序列化輔助類
 * 
 * @see SerializeType
 */
public final class SerializeHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(SerializeHelper.class);

	private static FSTConfiguration fstConfiguration = FSTConfiguration.createDefaultConfiguration();

	/**
	 * 序列化處理器工廠
	 */
	private static SoftReferenceCacheFactoryFactoryBean<SerializeProcessor, SoftReferenceCacheFactory<SerializeProcessor>> serializeProcessorCacheFactoryFactoryBean;

	/**
	 * 序列化處理器
	 */
	private static SoftReferenceCacheFactory<SerializeProcessor> serializeProcessorCacheFactory;

	public static final int BUFFER_SIZE = 512 * 1024;

	static {
		new Static();
	}

	protected static class Static {
		@SuppressWarnings("unchecked")
		public Static() {
			try {
				serializeProcessorCacheFactoryFactoryBean = new SoftReferenceCacheFactoryFactoryBean<SerializeProcessor, SoftReferenceCacheFactory<SerializeProcessor>>();
				serializeProcessorCacheFactoryFactoryBean
						.setCacheableObjectFactory(new CacheableObjectFactorySupporter<SerializeProcessor>() {

							private static final long serialVersionUID = -7294494524764181899L;

							public SerializeProcessor makeObject() throws Exception {
								SerializeProcessor obj = new SerializeProcessorImpl();
								obj.setSerialize(ConfigHelper.isSerialize());
								obj.setSerializeType(ConfigHelper.getSerializeType());
								return obj;
							}

							public boolean validateObject(SerializeProcessor obj) {
								return true;
							}

							public void activateObject(SerializeProcessor obj) throws Exception {
								obj.setSerialize(ConfigHelper.isSerialize());
								obj.setSerializeType(ConfigHelper.getSerializeType());
							}

							public void passivateObject(SerializeProcessor obj) throws Exception {
								obj.reset();
							}
						});
				serializeProcessorCacheFactoryFactoryBean.start();
				serializeProcessorCacheFactory = (SoftReferenceCacheFactory<SerializeProcessor>) serializeProcessorCacheFactoryFactoryBean
						.getObject();

			} catch (Exception ex) {
				throw new HelperException("new Static() Initializing failed", ex);
			}
		}
	}

	private SerializeHelper() {
		throw new HelperException(
				new StringBuilder().append(SerializeHelper.class.getName()).append(" can not construct").toString());

	}

	/**
	 * jdk 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] jdk(Object value) {
		AssertHelper.notNull(value, "The Value must not be null");
		//
		byte[] result = new byte[0];
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = jdk(value, baos);
			if (serialized) {
				result = baos.toByteArray();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during jdk()").toString(), e);
		} finally {
			IoHelper.close(baos);
		}
		return result;
	}

	/**
	 * jdk 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param out
	 * @return
	 */
	public static boolean jdk(Object value, OutputStream out) {
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(out, "The OutputStream must not be null");
		//
		boolean result = false;
		ObjectOutput output = null;
		try {
			output = new ObjectOutputStream(out);
			output.writeObject(value);
			output.flush();
			result = true;
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during jdk()").toString(), e);
		} finally {
			IoHelper.close(output);
		}
		return result;
	}

	/**
	 * jdk 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dejdk(byte[] value) {
		AssertHelper.notNull(value, "The Value must not be null");
		//
		T result = null;
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) dejdk(bais);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during dejdk()").toString(), e);
		} finally {
			IoHelper.close(bais);
		}
		return result;
	}

	/**
	 * jdk 反序列化
	 * 
	 * inputStream -> object
	 * 
	 * @param in
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dejdk(InputStream in) {
		AssertHelper.notNull(in, "The InputStream must not be null");
		//
		T result = null;
		ObjectInput input = null;
		try {
			input = new ObjectInputStream(in);
			// https://community.oracle.com/thread/1153653?start=0
			try {
				while (true)
					result = (T) input.readObject();
			} catch (EOFException eof) {
				// end of file reached, do nothing
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during dejdk()").toString(), e);
		} finally {
			IoHelper.close(input);
		}
		return result;
	}

	/**
	 * fst 序列化
	 * 
	 * use simple factory method to obtain input/outputstream instances. it is
	 * thread safe.
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] ___fst2(Serializable value) {
		AssertHelper.notNull(value, "The Value must not be null");
		//
		byte[] result = new byte[0];
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = ___fst2(value, baos);
			if (serialized) {
				result = baos.toByteArray();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during ___fst2()").toString(), e);
		} finally {
			IoHelper.close(baos);
		}
		return result;
	}

	/**
	 * fst 序列化
	 * 
	 * use simple factory method to obtain input/outputstream instances. it is
	 * thread safe.
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean ___fst2(Serializable value, OutputStream outputStream) {
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		boolean result = false;
		FSTObjectOutput out = null;
		try {
			out = fstConfiguration.getObjectOutput(outputStream);
			out.writeObject(value);
			result = true;
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during ___fst2()").toString(), e);
		} finally {
			try {
				if (out != null) {
					// DON'T out.close() when using factory method;
					out.flush();
				}
			} catch (Exception ex) {
			}
		}
		return result;
	}

	/**
	 * fst 反序列化
	 * 
	 * use simple factory method to obtain input/outputstream instances. it is
	 * thread safe.
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @return
	 */
	public static <T> T ___defst2(byte[] value) {
		AssertHelper.notNull(value, "The Value must not be null");
		//
		T result = null;
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = ___defst2(bais);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during ___defst2()").toString(), e);
		} finally {
			IoHelper.close(bais);
		}
		return result;
	}

	/**
	 * fst 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param inputStream
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T ___defst2(InputStream inputStream) {
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		//
		T result = null;
		FSTObjectInput in = null;
		try {
			in = fstConfiguration.getObjectInput(inputStream);
			result = (T) in.readObject();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during ___defst2()").toString(), e);
		} finally {
			// DON'T: in.close(); here prevents reuse and will result in an
			// exception
		}
		return result;
	}

	/**
	 * fst 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] fst(Object value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = fst(value, baos);
			if (serialized) {
				result = baos.toByteArray();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during fst()").toString(), e);
		} finally {
			IoHelper.close(baos);
		}
		return result;
	}

	/**
	 * fst 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param out
	 * @return
	 */
	public static boolean fst(Object value, OutputStream out) {
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(out, "The OutputStream must not be null");
		//
		boolean result = false;
		ObjectOutput output = null;
		try {
			output = new FSTObjectOutput(out);
			output.writeObject(value);
			output.flush();
			result = true;
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during fst()").toString(), e);
		} finally {
			IoHelper.close(output);
		}
		return result;
	}

	/**
	 * fst 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @return
	 */
	public static <T> T defst(byte[] value) {
		AssertHelper.notNull(value, "The Value must not be null");
		//
		T result = null;
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = defst(bais);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during defst()").toString(), e);
		} finally {
			IoHelper.close(bais);
		}
		return result;
	}

	/**
	 * fst 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param in
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T defst(InputStream in) {
		AssertHelper.notNull(in, "The InputStream must not be null");
		//
		T result = null;
		ObjectInput input = null;
		try {
			input = new FSTObjectInput(in);
			result = (T) input.readObject();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during defst()").toString(), e);
		} finally {
			IoHelper.close(input);
		}
		return result;
	}

	/**
	 * JGroup 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] jgroup(Object value) {
		AssertHelper.notNull(value, "The Value must not be null");
		//
		byte[] result = new byte[0];
		try {
			result = Util.objectToByteBuffer(value);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during jgroup()").toString(), e);
		} finally {
		}
		return result;
	}

	/**
	 * JGroup 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dejgroup(byte[] value) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		try {
			result = (T) Util.objectFromByteBuffer(value);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during dejgroup()").toString(), e);
		} finally {
		}
		return result;
	}

	/**
	 * kryo 序列化
	 * 
	 * 不會把class資訊一同序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param kryo
	 * @param value
	 * @return
	 */
	public static byte[] kryo(Kryo kryo, Object value) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		double sizeOf = MemoryHelper.sizeOf(value);
		AssertHelper.isTrue(sizeOf > 0, "The SizeOf must be greater than zero");
		//
		int bufferSize = NumberHelper.toInt(sizeOf) + 128;
		return kryo(kryo, value, bufferSize);
	}

	/**
	 * kryo 序列化
	 * 
	 * 不會把class資訊一同序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param kryo
	 * @param value
	 * @param bufferSize
	 * @return
	 */
	public static byte[] kryo(Kryo kryo, Object value, int bufferSize) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		byte[] result = new byte[0];
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream(bufferSize);
			boolean serialized = kryo(kryo, value, baos, bufferSize);
			if (serialized) {
				result = baos.toByteArray();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during kryo()").toString(), e);
		} finally {
			IoHelper.close(baos);
		}
		return result;
	}

	/**
	 * keyo 序列化
	 * 
	 * 不會把class資訊一同序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param kryo
	 * @param value
	 * @param out
	 * @param bufferSize
	 * @return
	 */
	public static boolean kryo(Kryo kryo, Object value, OutputStream out, int bufferSize) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(out, "The OutputStream must not be null");
		//
		boolean result = false;
		Output output = null;
		try {
			output = new Output(out, bufferSize);
			kryo.writeObject(output, value);
			result = true;
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during kryo()").toString(), e);
		} finally {
			IoHelper.close(output);
		}
		//
		return result;
	}

	/**
	 * kryo 反序列化
	 * 
	 * 需指定反序列化class
	 * 
	 * byte[] -> object
	 * 
	 * @param kryo
	 * @param value
	 * @param clazz
	 * @return
	 */
	public static <T> T dekryo(Kryo kryo, byte[] value, Class<?> clazz) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		int bufferSize = value.length + 128;
		return dekryo(kryo, value, bufferSize, clazz);
	}

	/**
	 * kryo 反序列化
	 * 
	 * 需指定反序列化class
	 * 
	 * byte[] -> object
	 * 
	 * @param kryo
	 * @param value
	 * @param bufferSize
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dekryo(Kryo kryo, byte[] value, int bufferSize, Class<?> clazz) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		T result = null;
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) dekryo(kryo, bais, bufferSize, clazz);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during dekryo()").toString(), e);
		} finally {
			IoHelper.close(bais);
		}
		return result;
	}

	/**
	 * kryo 反序列化
	 * 
	 * 需指定反序列化class
	 * 
	 * byte[] -> object
	 * 
	 * @param in
	 * @param bufferSize
	 * @param clazz
	 * @return
	 */
	public static <T> T dekryo(Kryo kryo, InputStream in, int bufferSize, Class<T> clazz) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(in, "The InputStream must not be null");
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		T result = null;
		Input input = null;
		try {
			input = new Input(in, bufferSize);
			result = kryo.readObject(input, clazz);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during dekryo()").toString(), e);
		} finally {
			IoHelper.close(input);
		}
		//
		return result;
	}

	public static byte[] kryoWriteClass(Kryo kryo, Object value) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		double sizeOf = MemoryHelper.sizeOf(value);
		AssertHelper.isTrue(sizeOf > 0, "The SizeOf must be greater than zero");
		//
		int bufferSize = NumberHelper.toInt(sizeOf) + 128;
		return kryoWriteClass(kryo, value, bufferSize);
	}

	/**
	 * kryo 序列化
	 * 
	 * 也會把class資訊一同序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param bufferSize
	 * @return
	 */
	public static byte[] kryoWriteClass(Kryo kryo, Object value, int bufferSize) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		byte[] result = new byte[0];
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream(bufferSize);
			boolean serialized = kryoWriteClass(kryo, value, baos, bufferSize);
			if (serialized) {
				result = baos.toByteArray();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during kryoWriteClass()").toString(), e);
		} finally {
			IoHelper.close(baos);
		}
		return result;
	}

	/**
	 * kryo 序列化
	 * 
	 * 也會把class資訊一同序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param out
	 * @param bufferSize
	 * @return
	 */
	public static boolean kryoWriteClass(Kryo kryo, Object value, OutputStream out, int bufferSize) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(out, "The OutputStream must not be null");
		//
		boolean result = false;
		Output output = null;
		try {
			output = new Output(out, bufferSize);
			kryo.writeClassAndObject(output, value);
			result = true;
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during kryoWriteClass()").toString(), e);
		} finally {
			IoHelper.close(output);
		}
		//
		return result;
	}

	public static <T> T dekryoReadClass(Kryo kryo, byte[] value) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		int bufferSize = value.length + 128;
		return dekryoReadClass(kryo, value, bufferSize);
	}

	/**
	 * kryo 反序列化
	 * 
	 * 也會把class資訊一同反序列化, 不需指定反序列化class
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @param bufferSize
	 * @return
	 */
	public static <T> T dekryoReadClass(Kryo kryo, byte[] value, int bufferSize) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(value, "The Value must not be null");
		//
		T result = null;
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = dekryoReadClass(kryo, bais, bufferSize);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during dekryoReadClass()").toString(), e);
		} finally {
			IoHelper.close(bais);
		}
		return result;
	}

	/**
	 * kryo 反序列化
	 * 
	 * 也會把class資訊一同反序列化, 不需指定反序列化class
	 * 
	 * byte[] -> object
	 * 
	 * @param in
	 * @param bufferSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dekryoReadClass(Kryo kryo, InputStream in, int bufferSize) {
		AssertHelper.notNull(kryo, "The Kryo must not be null");
		AssertHelper.notNull(in, "The InputStream must not be null");
		//
		T result = null;
		Input input = null;
		try {
			input = new Input(in, bufferSize);
			result = (T) kryo.readClassAndObject(input);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during dekryoReadClass()").toString(), e);
		} finally {
			IoHelper.close(input);
		}
		return result;
	}

	/**
	 * jackson 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] jackson(Object value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = jackson(value, baos);
			if (serialized) {
				result = baos.toByteArray();
			}
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during jackson()").toString(), e);
		} finally {
			IoHelper.close(baos);
		}
		return result;
	}

	/**
	 * jackson 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean jackson(final Object value, final OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		ObjectMapper mapper = null;
		try {
			mapper = new ObjectMapper();
			byte[] buff = mapper.writeValueAsBytes(value);
			outputStream.write(buff);
			result = true;
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during jackson()").toString(), e);
		} finally {
		}
		//
		return result;
	}

	/**
	 * jackson 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dejackson(byte[] value, Class<?> clazz) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) dejackson(bais, clazz);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during dejackson()").toString(), e);
		} finally {
			IoHelper.close(bais);
		}
		return result;
	}

	/**
	 * jackson 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	public static <T> T dejackson(InputStream inputStream, Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(inputStream, "The Class must not be null");
		//
		ObjectMapper mapper = null;
		try {
			mapper = new ObjectMapper();
			result = mapper.readValue(inputStream, clazz);
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("Exception encountered during dejackson()").toString(), e);
		} finally {
		}
		//
		return result;
	}

	/**
	 * jackson 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dejacksonFromString(String value, Class<?> clazz) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(ByteHelper.toByteArray(value));
			result = (T) dejackson(bais, clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(bais);
		}
		return result;
	}

	/**
	 * smile 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] smile(Object value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = smile(value, baos);
			if (serialized) {
				result = baos.toByteArray();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(baos);
		}
		return result;
	}

	/**
	 * smile 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean smile(Object value, OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		ObjectMapper mapper = null;
		try {
			mapper = new ObjectMapper(new SmileFactory());
			byte[] buff = mapper.writeValueAsBytes(value);
			outputStream.write(buff);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		//
		return result;
	}

	/**
	 * smile 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T desmile(byte[] value, Class<?> clazz) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) desmile(bais, clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(bais);
		}
		return result;
	}

	/**
	 * smile 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	public static <T> T desmile(InputStream inputStream, Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(inputStream, "The Class must not be null");
		//
		ObjectMapper mapper = null;
		try {
			mapper = new ObjectMapper(new SmileFactory());
			result = mapper.readValue(inputStream, clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		//
		return result;
	}

	/**
	 * smile jaxrs序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] smileJaxrs(Object value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = smileJaxrs(value, baos);
			if (serialized) {
				result = baos.toByteArray();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(baos);
		}
		return result;
	}

	/**
	 * smile jaxrs序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean smileJaxrs(Object value, OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		JacksonSmileProvider provider = null;
		try {
			provider = new JacksonSmileProvider();
			provider.writeTo(value, value.getClass(), null, null, null, null, outputStream);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		//
		return result;
	}

	/**
	 * smile jaxrs反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T desmileJaxrs(byte[] value, Class<?> clazz) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) desmileJaxrs(bais, clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(bais);
		}
		return result;
	}

	/**
	 * smile 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T desmileJaxrs(InputStream inputStream, Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(inputStream, "The Class must not be null");
		//
		JacksonSmileProvider provider = null;
		try {
			provider = new JacksonSmileProvider();
			result = (T) provider.readFrom(Object.class, clazz, null, null, null, inputStream);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		//
		return result;
	}

	public static byte[] serializeWithProcessor(final Object value) {
		byte[] result = new byte[0];
		//
		result = (byte[]) serializeProcessorCacheFactory.execute(new CacheCallback<SerializeProcessor>() {
			public Object doInAction(SerializeProcessor obj) throws CacheException {
				return obj.serialize(value);
			}
		});
		//
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserializeWithProcessor(final byte[] value, final Class<?> clazz) {
		T result = null;
		//
		Object retObj = serializeProcessorCacheFactory.execute(new CacheCallback<SerializeProcessor>() {
			public Object doInAction(SerializeProcessor obj) throws CacheException {
				return obj.deserialize(value, clazz);
			}
		});
		result = (T) retObj;
		//
		return result;
	}
}

// XMLOutputFactory2 xmlif = (XMLOutputFactory2) XMLOutputFactory2
// .newInstance();
// xmlif.configureForSpeed();
// XMLEventWriter writer = xmlif
// .createXMLEventWriter(outputStream);
