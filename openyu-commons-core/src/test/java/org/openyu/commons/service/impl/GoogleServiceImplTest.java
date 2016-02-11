package org.openyu.commons.service.impl;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class GoogleServiceImplTest {

	@Test
	public void service() throws Exception {
		GoogleServiceImpl service = new GoogleServiceImpl();
		// GoogleServiceImpl [NEW]
		System.out.println(service);
		//
		service.start();
		// GoogleServiceImpl [STARTING]
		System.out.println(service);
		//
		service.stop();
		// GoogleServiceImpl [STOPPING]
		System.out.println(service);
		//
		TimeUnit.SECONDS.sleep(5);
	}

}
