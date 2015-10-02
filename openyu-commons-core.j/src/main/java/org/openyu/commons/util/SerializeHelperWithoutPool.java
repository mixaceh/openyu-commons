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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jgroups.util.Util;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
//import org.openyu.commons.commons.pool.CacheCallback;
//import org.openyu.commons.commons.pool.SoftReferenceCacheFactory;
//import org.openyu.commons.commons.pool.ex.CacheException;
//import org.openyu.commons.commons.pool.impl.SoftReferenceCacheFactoryImpl;
//import org.openyu.commons.commons.pool.supporter.CacheableObjectFactorySupporter;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.lang.ByteHelper;
//import org.openyu.commons.lang.BooleanHelper;

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
 * 序列化 The Class SerializeHelper.
 * 
 * @see SerializeType
 */
public class SerializeHelperWithoutPool extends BaseHelperSupporter {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(SerializeHelperWithoutPool.class);

	private static FSTConfiguration fstConfiguration = FSTConfiguration
			.createDefaultConfiguration();

	// 2014/11/24, 會很耗mem, 先不使用
	// private static SoftReferenceCacheFactory<FSTObjectOutput>
	// fstObjectOutputCacheFactory;
	//
	// private static SoftReferenceCacheFactory<FSTObjectInput>
	// fstObjectInputCacheFactory;
	//
	// private static SoftReferenceCacheFactory<Kryo> kryoCacheFactory;
	//
	// private static SoftReferenceCacheFactory<ObjectMapper>
	// jacksonCacheFactory;
	//
	// private static SoftReferenceCacheFactory<ObjectMapper> smileCacheFactory;
	//
	// private static SoftReferenceCacheFactory<JacksonSmileProvider>
	// smileJaxrsCacheFactory;
	//
	// /** 序列化處理器 */
	// private static SoftReferenceCacheFactory<SerializeProcessor>
	// serializeProcessorCacheFactory;

	static {
		new Static();
	}

	protected static class Static {
		public Static() {
			try {

				// // FSTObjectOutput
				// fstObjectOutputCacheFactory = new
				// SoftReferenceCacheFactoryImpl<FSTObjectOutput>(
				// FSTObjectOutput.class.getSimpleName(),
				// new CacheableObjectFactorySupporter<FSTObjectOutput>() {
				//
				// private static final long serialVersionUID =
				// -7462758716430774639L;
				//
				// public FSTObjectOutput makeObject()
				// throws Exception {
				// return new FSTObjectOutput();
				// }
				//
				// public boolean validateObject(FSTObjectOutput obj) {
				// return true;
				// }
				// });
				//
				// // FSTObjectInput
				// fstObjectInputCacheFactory = new
				// SoftReferenceCacheFactoryImpl<FSTObjectInput>(
				// FSTObjectInput.class.getSimpleName(),
				// new CacheableObjectFactorySupporter<FSTObjectInput>() {
				//
				// private static final long serialVersionUID =
				// 1860762668217964912L;
				//
				// public FSTObjectInput makeObject() throws Exception {
				// return new FSTObjectInput();
				// }
				//
				// public boolean validateObject(FSTObjectInput obj) {
				// return true;
				// }
				// });
				//
				// // kryo
				// kryoCacheFactory = new SoftReferenceCacheFactoryImpl<Kryo>(
				// Kryo.class.getSimpleName(),
				// new CacheableObjectFactorySupporter<Kryo>() {
				//
				// private static final long serialVersionUID =
				// -9123962698682864084L;
				//
				// public Kryo makeObject() throws Exception {
				// Kryo obj = new Kryo();
				// // obj.register(List.class);
				// // obj.register(ArrayList.class);
				// // obj.register(LinkedList.class);
				// return obj;
				// }
				//
				// public boolean validateObject(Kryo obj) {
				// return true;
				// }
				// });
				//
				// // jackson
				// jacksonCacheFactory = new
				// SoftReferenceCacheFactoryImpl<ObjectMapper>(
				// ObjectMapper.class.getSimpleName(),
				// new CacheableObjectFactorySupporter<ObjectMapper>() {
				//
				// private static final long serialVersionUID =
				// 6880124212137012746L;
				//
				// public ObjectMapper makeObject() throws Exception {
				// return new ObjectMapper();
				// }
				//
				// public boolean validateObject(ObjectMapper obj) {
				// return true;
				// }
				// });
				//
				// // smile
				// smileCacheFactory = new
				// SoftReferenceCacheFactoryImpl<ObjectMapper>(
				// SmileFactory.class.getSimpleName(),
				// new CacheableObjectFactorySupporter<ObjectMapper>() {
				//
				// private static final long serialVersionUID =
				// -7780789651449541685L;
				//
				// public ObjectMapper makeObject() throws Exception {
				// return new ObjectMapper(new SmileFactory());
				// }
				//
				// public boolean validateObject(ObjectMapper obj) {
				// return true;
				// }
				// });
				//
				// // smileJaxrs
				// smileJaxrsCacheFactory = new
				// SoftReferenceCacheFactoryImpl<JacksonSmileProvider>(
				// JacksonSmileProvider.class.getSimpleName(),
				// new CacheableObjectFactorySupporter<JacksonSmileProvider>() {
				//
				// private static final long serialVersionUID =
				// 4074975001368215557L;
				//
				// public JacksonSmileProvider makeObject()
				// throws Exception {
				// return new JacksonSmileProvider();
				// }
				//
				// public boolean validateObject(
				// JacksonSmileProvider obj) {
				// return true;
				// }
				// });
				//
				// // serializeProcessor
				// serializeProcessorCacheFactory = new
				// SoftReferenceCacheFactoryImpl<SerializeProcessor>(
				// SerializeProcessor.class.getSimpleName(),
				// new CacheableObjectFactorySupporter<SerializeProcessor>() {
				//
				// private static final long serialVersionUID =
				// -7294494524764181899L;
				//
				// public SerializeProcessor makeObject()
				// throws Exception {
				// SerializeProcessor obj = new SerializeProcessorImpl();
				// obj.setSerialize(ConfigHelper.isSerialize());
				// obj.setSerializeType(ConfigHelper
				// .getSerializeType());
				// return obj;
				// }
				//
				// public boolean validateObject(SerializeProcessor obj) {
				// return true;
				// }
				//
				// public void activateObject(SerializeProcessor obj)
				// throws Exception {
				// obj.setSerialize(ConfigHelper.isSerialize());
				// obj.setSerializeType(ConfigHelper
				// .getSerializeType());
				// }
				//
				// public void passivateObject(SerializeProcessor obj)
				// throws Exception {
				// obj.reset();
				// }
				// });
			} catch (Exception ex) {
				throw new HelperException("new Static() Initializing failed",
						ex);
			}
		}
	}

	/**
	 * Instantiates a new blank helper.
	 */
	private SerializeHelperWithoutPool() {
		if (InstanceHolder.INSTANCE != null) {
			throw new UnsupportedOperationException("Can not construct.");
		}
	}

	/**
	 * The Class InstanceHolder.
	 */
	private static class InstanceHolder {

		/** The Constant INSTANCE. */
		private static final SerializeHelperWithoutPool INSTANCE = new SerializeHelperWithoutPool();
	}

	/**
	 * Gets the single instance of SerializeHelper.
	 *
	 * @return single instance of SerializeHelper
	 */
	public static SerializeHelperWithoutPool getInstance() {
		return InstanceHolder.INSTANCE;
	}

	public static byte[] serialize(Object value) {
		return serialize((Serializable) value);
	}

	/**
	 * jdk 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] serialize(Serializable value) {
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

	public static boolean serialize(Object value, OutputStream outputStream) {
		return serialize((Serializable) value, outputStream);
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
	public static boolean serialize(Serializable value,
			OutputStream outputStream) {
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
	public static byte[] ___fst(Serializable value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = ___fst(value, baos);
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
	public static boolean ___fst(Serializable value, OutputStream outputStream) {
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
	public static <T> T ___defst(byte[] value) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = ___defst(bais);
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
	public static <T> T ___defst(InputStream inputStream) {
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
	public static byte[] fst(Serializable value) {
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
	 * use simple factory method to obtain input/outputstream instances. it is
	 * thread safe.
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean fst(Serializable value, OutputStream outputStream) {
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
	 * fst 序列化
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
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean ___fst2(final Serializable value,
			final OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		// Boolean retObj = (Boolean) fstObjectOutputCacheFactory
		// .execute(new CacheCallback<FSTObjectOutput>() {
		// public Object doInAction(FSTObjectOutput obj)
		// throws CacheException {
		// Output out = null;
		// try {
		// obj.resetForReUse(outputStream);
		// obj.writeObject(value);
		// return Boolean.TRUE;
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// IoHelper.close((OutputStream) out);
		// }
		// }
		// });
		// result = BooleanHelper.safeGet(retObj);
		//
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
	public static <T> T ___defst2(final InputStream inputStream) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		//
		// Object retObj = fstObjectInputCacheFactory
		// .execute(new CacheCallback<FSTObjectInput>() {
		// public Object doInAction(FSTObjectInput obj)
		// throws CacheException {
		// Output out = null;
		// try {
		// obj.resetForReuse(inputStream);
		// return obj.readObject();
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// } finally {
		// IoHelper.close((OutputStream) out);
		// }
		// return null;
		// }
		// });
		// result = (T) retObj;
		//
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
	public static byte[] jgroup(Serializable value) {
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
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] kryo(Serializable value) {
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
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean kryo(final Serializable value,
			final OutputStream outputStream) {
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
			IoHelper.close((OutputStream) out);
		}
		//
		return result;
	}

	public static byte[] kryo(Object value) {
		return kryo((Serializable) value);
	}

	/**
	 * kryo 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] ___kryo(Serializable value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = ___kryo(value, baos);
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

	public static boolean kryo(Object value, OutputStream outputStream) {
		return kryo((Serializable) value, outputStream);
	}

	/**
	 * keyo 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean ___kryo(final Serializable value,
			final OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		// Boolean retObj = (Boolean) kryoCacheFactory
		// .execute(new CacheCallback<Kryo>() {
		// public Object doInAction(Kryo obj) throws CacheException {
		// Output out = null;
		// try {
		// out = new Output(outputStream);
		// obj.writeObject(out, value);
		// return Boolean.TRUE;
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// IoHelper.close((OutputStream) out);
		// }
		// }
		// });
		// result = BooleanHelper.safeGet(retObj);
		//
		return result;
	}

	/**
	 * kryo 反序列化
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
	 * byte[] -> object
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	public static <T> T dekryo(final InputStream inputStream,
			final Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(inputStream, "The Class must not be null");
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
			IoHelper.close((InputStream) in);
		}
		//
		return result;
	}

	/**
	 * kryo 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param value
	 * @param clazz
	 * @return
	 */
	public static <T> T ___dekryo(byte[] value, Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = ___dekryo(bais, clazz);
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
	 * byte[] -> object
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T ___dekryo(final InputStream inputStream,
			final Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(inputStream, "The Class must not be null");
		//
		// Object retObj = kryoCacheFactory.execute(new CacheCallback<Kryo>() {
		// public Object doInAction(Kryo obj) throws CacheException {
		// Input in = null;
		// try {
		// in = new Input(inputStream);
		// return obj.readObject(in, clazz);
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// IoHelper.close((InputStream) in);
		// }
		// }
		// });
		// result = (T) retObj;
		//
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
	public static byte[] jackson(Serializable value) {
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
	public static boolean jackson(final Serializable value,
			final OutputStream outputStream) {
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
	public static <T> T dejackson(final InputStream inputStream,
			final Class<T> clazz) {
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
	 * jackson 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] ___jackson(Serializable value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = ___jackson(value, baos);
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
	public static boolean ___jackson(final Serializable value,
			final OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		// Boolean retObj = (Boolean) jacksonCacheFactory
		// .execute(new CacheCallback<ObjectMapper>() {
		// public Object doInAction(ObjectMapper obj)
		// throws CacheException {
		// try {
		// byte[] buff = obj.writeValueAsBytes(value);
		// // obj.writeValue(outputStream, value); //write to
		// // json
		// // XmlMapper stax2-api-3.1.2.jar// write to xml
		// outputStream.write(buff);
		// return Boolean.TRUE;
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });
		// result = BooleanHelper.safeGet(retObj);
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
	public static <T> T ___dejackson(byte[] value, Class<?> clazz) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) ___dejackson(bais, clazz);
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
	@SuppressWarnings("unchecked")
	public static <T> T ___dejackson(final InputStream inputStream,
			final Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(inputStream, "The Class must not be null");
		//
		// Object retObj = jacksonCacheFactory
		// .execute(new CacheCallback<ObjectMapper>() {
		// public Object doInAction(ObjectMapper obj)
		// throws CacheException {
		// try {
		// // obj.readValue(jsonString, clazz);//read from json
		// return obj.readValue(inputStream, clazz);
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });
		// result = (T) retObj;
		//
		return result;
	}

	/**
	 * jackson 序列化
	 * 
	 * object -> json string
	 * 
	 * @param value
	 * @return
	 */
	public static String jacksonToString(Serializable value) {
		String result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = jacksonToString(value, baos);
			if (serialized) {
				result = baos.toString();
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
	 * object -> json string
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean jacksonToString(final Serializable value,
			final OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		// Boolean retObj = (Boolean) jacksonCacheFactory
		// .execute(new CacheCallback<ObjectMapper>() {
		// public Object doInAction(ObjectMapper obj)
		// throws CacheException {
		// try {
		// obj.writeValue(outputStream, value);
		// return Boolean.TRUE;
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });
		// result = BooleanHelper.safeGet(retObj);

		try {
			ObjectMapper obj = new ObjectMapper();
			obj.writeValue(outputStream, value);
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
	 * jackson 反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T dejacksonFromString(final InputStream inputStream,
			final Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(inputStream, "The Class must not be null");
		//
		// Object retObj = jacksonCacheFactory
		// .execute(new CacheCallback<ObjectMapper>() {
		// public Object doInAction(ObjectMapper obj)
		// throws CacheException {
		// try {
		// return obj.readValue(inputStream, clazz);
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });
		// result = (T) retObj;

		try {
			ObjectMapper obj = new ObjectMapper();
			result = obj.readValue(inputStream, clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		//
		//
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
	public static byte[] smile(Serializable value) {
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
	public static boolean smile(final Serializable value,
			final OutputStream outputStream) {
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
			result = (T) ___desmile(bais, clazz);
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
	public static <T> T desmile(final InputStream inputStream,
			final Class<T> clazz) {
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
	 * smile 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] ___smile(Serializable value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = ___smile(value, baos);
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
	public static boolean ___smile(final Serializable value,
			final OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		// Boolean retObj = (Boolean) smileCacheFactory
		// .execute(new CacheCallback<ObjectMapper>() {
		// public Object doInAction(ObjectMapper obj)
		// throws CacheException {
		// try {
		// byte[] buff = obj.writeValueAsBytes(value);
		// outputStream.write(buff);
		// return Boolean.TRUE;
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });
		// result = BooleanHelper.safeGet(retObj);
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
	public static <T> T ___desmile(byte[] value, Class<?> clazz) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) ___desmile(bais, clazz);
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
	public static <T> T ___desmile(final InputStream inputStream,
			final Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(inputStream, "The Class must not be null");
		//
		// Object retObj = smileCacheFactory
		// .execute(new CacheCallback<ObjectMapper>() {
		// public Object doInAction(ObjectMapper obj)
		// throws CacheException {
		// try {
		// return obj.readValue(inputStream, clazz);
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });
		// result = (T) retObj;
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
	public static byte[] smileJaxrs(Serializable value) {
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
	public static boolean smileJaxrs(final Serializable value,
			final OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		JacksonSmileProvider provider = null;
		try {
			provider = new JacksonSmileProvider();
			provider.writeTo(value, value.getClass(), null, null, null, null,
					outputStream);
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
	public static <T> T desmileJaxrs(final InputStream inputStream,
			final Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(inputStream, "The Class must not be null");
		//
		JacksonSmileProvider provider = null;
		try {
			provider = new JacksonSmileProvider();
			result = (T) provider.readFrom(Object.class, clazz, null, null,
					null, inputStream);
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
	public static byte[] ___smileJaxrs(Serializable value) {
		byte[] result = new byte[0];
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			boolean serialized = ___smileJaxrs(value, baos);
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
	 * smile jaxrs 序列化
	 * 
	 * object -> byte[]
	 * 
	 * @param value
	 * @param outputStream
	 * @return
	 */
	public static boolean ___smileJaxrs(final Serializable value,
			final OutputStream outputStream) {
		boolean result = false;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		AssertHelper.notNull(outputStream, "The OutputStream must not be null");
		//
		// Boolean retObj = (Boolean) smileJaxrsCacheFactory
		// .execute(new CacheCallback<JacksonSmileProvider>() {
		// public Object doInAction(JacksonSmileProvider obj)
		// throws CacheException {
		// try {
		// obj.writeTo(value, value.getClass(), null, null,
		// null, null, outputStream);
		// return Boolean.TRUE;
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });
		// result = BooleanHelper.safeGet(retObj);
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
	public static <T> T ___desmileJaxrs(byte[] value, Class<?> clazz) {
		T result = null;
		//
		AssertHelper.notNull(value, "The Value must not be null");
		//
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(value);
			result = (T) ___desmileJaxrs(bais, clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IoHelper.close(bais);
		}
		return result;
	}

	/**
	 * smile jaxrs反序列化
	 * 
	 * byte[] -> object
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T ___desmileJaxrs(final InputStream inputStream,
			final Class<T> clazz) {
		T result = null;
		//
		AssertHelper.notNull(inputStream, "The InputStream must not be null");
		AssertHelper.notNull(inputStream, "The Class must not be null");
		//
		// Object retObj = smileJaxrsCacheFactory
		// .execute(new CacheCallback<JacksonSmileProvider>() {
		// public Object doInAction(JacksonSmileProvider obj)
		// throws CacheException {
		// try {
		// return obj.readFrom(Object.class, clazz, null,
		// null, null, inputStream);
		// } catch (Exception ex) {
		// throw new CacheException(ex);
		// } finally {
		// }
		// }
		// });
		// result = (T) retObj;
		//
		return result;
	}

	public static byte[] serializeWithProcessor(final Serializable value) {
		byte[] result = new byte[0];
		//
		// result = (byte[]) serializeProcessorCacheFactory
		// .execute(new CacheCallback<SerializeProcessor>() {
		// public Object doInAction(SerializeProcessor obj)
		// throws CacheException {
		// return obj.serialize(value);
		// }
		// });
		//
		SerializeProcessor obj = new SerializeProcessorImpl();
		obj.setSerialize(ConfigHelper.isSerialize());
		obj.setSerializeType(ConfigHelper.getSerializeType());
		result = obj.serialize(value);
		//
		return result;
	}

	public static <T> T deserializeWithProcessor(final byte[] value,
			final Class<?> clazz) {
		T result = null;
		//
		// Object retObj = serializeProcessorCacheFactory
		// .execute(new CacheCallback<SerializeProcessor>() {
		// public Object doInAction(SerializeProcessor obj)
		// throws CacheException {
		// return obj.deserialize(value, clazz);
		// }
		// });
		// result = (T) retObj;

		SerializeProcessor obj = new SerializeProcessorImpl();
		obj.setSerialize(ConfigHelper.isSerialize());
		obj.setSerializeType(ConfigHelper.getSerializeType());
		result = obj.deserialize(value, clazz);
		//
		return result;
	}
}

// XMLOutputFactory2 xmlif = (XMLOutputFactory2) XMLOutputFactory2
// .newInstance();
// xmlif.configureForSpeed();
// XMLEventWriter writer = xmlif
// .createXMLEventWriter(outputStream);
