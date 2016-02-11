package org.openyu.commons.service.impl;

import com.google.common.util.concurrent.AbstractService;

public class GoogleServiceImpl extends AbstractService {

	@Override
	protected void doStart() {
		System.out.println("T[" + Thread.currentThread().getId() + "] doStart");
	}

	@Override
	protected void doStop() {
		System.out.println("T[" + Thread.currentThread().getId() + "] doStop");
	}

}
