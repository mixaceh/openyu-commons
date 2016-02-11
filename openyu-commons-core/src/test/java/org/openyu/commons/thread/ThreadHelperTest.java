package org.openyu.commons.thread;

import org.junit.Test;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class ThreadHelperTest extends BaseTestSupporter {

	@Test
	public void sleep() {
		System.out.println("start");
		ThreadHelper.sleep(5 * 1000);
		//
		System.out.println("end");
	}

	@Test
	public void loop() {
		ThreadHelper.loop(50);
	}

	@Test
	public void loopWithNoSleep() {
		ThreadHelper.loop(0);
	}
}
