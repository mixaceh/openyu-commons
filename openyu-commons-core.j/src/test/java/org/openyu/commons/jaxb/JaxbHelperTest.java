package org.openyu.commons.jaxb;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.xml.bind.JAXBContext;

import org.junit.Rule;
import org.junit.Test;
import org.openyu.commons.dog.vo.DogCollector;
import org.openyu.commons.dog.vo.impl.DogImpl;
import org.openyu.commons.io.IoHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

public class JaxbHelperTest extends BaseTestSupporter {

	/** The benchmark rule. */
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// 1000 times: 6377 mills.
	// 1000 times: 6376 mills.
	// 1000 times: 6426 mills.
	public void marshal() throws Exception {
		DogImpl dog = new DogImpl();
		dog.setId("DOG_01");
		dog.addName(Locale.US, "LaLa");

		DogImpl dog2 = new DogImpl();
		dog2.setId("DOG_02");
		dog2.addName(Locale.US, "Golden");

		DogCollector dogCollector = new DogCollector();
		dogCollector.getDogList().add(dog);
		dogCollector.getDogList().add(dog2);
		//
		dogCollector.getDogMap().put(dog.getId(), dog);
		dogCollector.getDogMap().put(dog2.getId(), dog2);
		//
		final int COUNT = 1;
		boolean result = false;
		//
		for (int i = 0; i < COUNT; i++) {
			// 1.data/xml/xxxDog.xml

			// FileOutputStream fos = new FileOutputStream(new
			// File("data/xml/xxxDog.xml"));
			// BufferedOutputStream bos = new BufferedOutputStream(fos);

			OutputStream out = IoHelper
					.createOutputStream("data/xml/xxxDog.xml");

			result = JaxbHelper.marshal(dogCollector, out, DogCollector.class);
		}
		//
		System.out.println(result);
		assertTrue(result);

		// 2.System.out
		result = JaxbHelper.marshal(dogCollector, System.out,
				DogCollector.class);
		System.out.println(result);
		assertTrue(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	// 1000 times: 6377 mills.
	// 1000 times: 6376 mills.
	// 1000 times: 6426 mills.
	public void unmarshal() throws Exception {
		DogCollector result = null;

		final int COUNT = 1;
		for (int i = 0; i < COUNT; i++) {
			// 1.data/xml/xxxDog.xml

			// FileInputStream fis = new FileInputStream(new
			// File("data/xml/xxxDog.xml"));
			// BufferedInputStream bis = new BufferedInputStream(fis);

			InputStream in = IoHelper.createInputStream("data/xml/xxxDog.xml");
			result = JaxbHelper.unmarshal(in, DogCollector.class);
		}
		//
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	/**
	 * copy jaxb.properties to org.openyu.commons.vo.impl
	 * 
	 * @throws Exception
	 */
	public void dogImpl() throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(DogImpl.class);

		// class org.eclipse.persistence.jaxb.JAXBContext
		System.out.println(jaxbContext.getClass());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 2, warmupRounds = 1, concurrency = 1)
	public void dogCollector() throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(DogCollector.class);

		// class com.sun.xml.bind.v2.runtime.JAXBContextImpl
		System.out.println(jaxbContext.getClass());
	}
}
