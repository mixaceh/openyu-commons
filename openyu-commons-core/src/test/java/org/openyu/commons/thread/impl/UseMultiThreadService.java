package org.openyu.commons.thread.impl;

import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.thread.ThreadService;
import org.openyu.commons.thread.anno.DefaultThreadService;

public class UseMultiThreadService extends BaseServiceSupporter {

	private static final long serialVersionUID = -2161362940754084178L;

	@DefaultThreadService
	private transient ThreadService threadService;

	public UseMultiThreadService() {

	}

	@Override
	protected void doStart() throws Exception {
	}

	@Override
	protected void doShutdown() throws Exception {
	}
}
