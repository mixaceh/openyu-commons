package org.openyu.commons.thread.impl;

import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.anno.BlockingThreadService;
import org.springframework.beans.factory.annotation.Autowired;

public class UseMultiThreadService2 extends BaseServiceSupporter {

	private static final long serialVersionUID = -2161362940754084178L;

	@BlockingThreadService
	private transient ThreadService threadService;

	@Autowired
	private transient UseMultiThreadService useMultiThreadService;

	public UseMultiThreadService2() {

	}

	@Override
	protected void doStart() throws Exception {
	}

	@Override
	protected void doShutdown() throws Exception {
	}
}
