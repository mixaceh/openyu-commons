package org.openyu.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;

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
	public static byte[] serialize(Object value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = serialize(value, baos);
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
	 * jdk 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean serialize(Object value, OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(outputStream);
			out.writeObject(value);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(out);
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
	public static <T> T deserialize(byte[] value) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) deserialize(bais);
		} catch (Exception ex) {
			ex.printStackTrace();
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
	 * @param inputStream
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(InputStream inputStream) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		//
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(inputStream);
			result = (T) in.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(in);
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
		} catch (Exception ex) {
			ex.printStackTrace();
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
	 * @param outputStream
	 * @return
	 */
	public static boolean fst(Object value, OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		FSTObjectOutput out = null;
		try {
			out = new FSTObjectOutput(outputStream);
			out.writeObject(value);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close((ObjectOutput) out);
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
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = defst(bais);
		} catch (Exception ex) {
			ex.printStackTrace();
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
	public static <T> T defst(InputStream inputStream) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		//
		FSTObjectInput in = null;
		try {
			in = new FSTObjectInput(inputStream);
			result = (T) in.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close((ObjectInput) in);
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
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = ___fst2(value, baos);
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
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		FSTObjectOutput out = null;
		try {
			out = fstConfiguration.getObjectOutput(outputStream);
			out.writeObject(value);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
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
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = ___defst2(bais);
		} catch (Exception ex) {
			ex.printStackTrace();
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
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		//
		FSTObjectInput in = null;
		try {
			in = fstConfiguration.getObjectInput(inputStream);
			result = (T) in.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// DON'T: in.close(); here prevents reuse and will result in an
			// exception
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
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		try {
			result = Util.objectToByteBuffer(value);
		} catch (Exception ex) {
			ex.printStackTrace();
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
		} catch (Exception ex) {
			ex.printStackTrace();
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
	 * @param value
	 * @return
	 */
	public static byte[] kryo(Object value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = kryo(value, baos);
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
	 * keyo 序列化
	 * 
	 * 不會把class資訊一同序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean kryo(Object value, OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		Kryo kryo = null;
		Output out = null;
		try {
			kryo = new Kryo();
			out = new Output(outputStream);
			kryo.writeObject(out, value);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(out);
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
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dekryo(byte[] value, Class<?> clazz) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) dekryo(bais, clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
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
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	public static <T> T dekryo(InputStream inputStream, Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(clazz, "The Class must not be null");
		//
		Kryo kryo = null;
		Input in = null;
		try {
			kryo = new Kryo();
			in = new Input(inputStream);
			result = kryo.readObject(in, clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(in);
		}
		//
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
	 * @return
	 */
	public static byte[] kryoWriteClass(Object value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		Kryo kryo = null;
		ByteArrayOutputStream baos = null;
		Output out = null;
		try {
			kryo = new Kryo();
			baos = new ByteArrayOutputStream();
			out = new Output(baos);
			kryo.writeClassAndObject(out, value);
			result = out.toBytes();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(baos);
			IoHelper.close(out);
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
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dekryoReadClass(byte[] value) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		Kryo kryo = null;
		Input in = null;
		try {
			kryo = new Kryo();
			in = new Input(value);
			result = (T) kryo.readClassAndObject(in);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(in);
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
		} catch (Exception ex) {
			ex.printStackTrace();
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
		} catch (Exception ex) {
			ex.printStackTrace();
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
		} catch (Exception ex) {
			ex.printStackTrace();
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
		} catch (Exception ex) {
			ex.printStackTrace();
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
