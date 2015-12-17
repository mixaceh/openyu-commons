package org.openyu.commons.jaxb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.io.IoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jaxb輔助類
 */
public final class JaxbHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(JaxbHelper.class);

	private JaxbHelper() {
		throw new HelperException(
				new StringBuilder().append(JaxbHelper.class.getName()).append(" can not construct").toString());
	}

	public static boolean marshal(Object value, OutputStream outputStream, Class<?> clazz) {
		return marshal(value, outputStream, new Class[] { clazz });
	}

	/**
	 * 物件 -> aaa.xml
	 * 
	 * @param outputStream
	 * @param clazz
	 * @param value
	 * @return
	 */
	public static boolean marshal(Object value, OutputStream outputStream, Class<?>[] clazz) {
		boolean result = false;
		//
		if (outputStream == null) {
			throw new IllegalArgumentException("The OutputStream must not be null");
		}
		//
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(outputStream);
			if (out != null) {
				JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
				// System.out.println(jaxbContext.getClass());
				Marshaller marshaller = jaxbContext.createMarshaller();
				// marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(value, out);
				//
				result = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// system.out不需關閉,以供輸出
			// if (outputStream != System.out)
			// {
			IoHelper.close(out);
			// }
		}
		return result;
	}

	public static <T> T unmarshal(InputStream inputStream, Class<?> clazz) {
		return unmarshal(inputStream, new Class[] { clazz });
	}

	/**
	 * aaa.xml -> 物件
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(InputStream inputStream, Class<?>[] clazz) {
		T result = null;
		InputStream in = null;
		try {
			in = new BufferedInputStream(inputStream);
			if (in != null) {
				JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				result = (T) unmarshaller.unmarshal(in);// JAXBElement

			}
		} catch (Exception ex) {
			// log.warn(ex);
			ex.printStackTrace();
		} finally {
			// system.in不需關閉,以供輸入
			// if (inputStream != System.in)
			// {
			IoHelper.close(in);
			// }
		}
		return result;
	}

}
