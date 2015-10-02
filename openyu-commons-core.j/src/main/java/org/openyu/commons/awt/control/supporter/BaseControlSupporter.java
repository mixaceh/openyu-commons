package org.openyu.commons.awt.control.supporter;

import java.awt.*;

import org.openyu.commons.awt.control.BaseControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.service.supporter.BaseServiceSupporter;

/**
 * 基礎控制器
 */
public abstract class BaseControlSupporter extends BaseServiceSupporter
		implements BaseControl {

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(BaseControlSupporter.class);
	/**
	 * id
	 */
	protected String id;

	/**
	 * 元件
	 */
	protected Component component;

	// gc
	private final Object finalizerGuardian = new Object() {
		protected void finalize() throws Throwable {
		}
	};

	//
	public BaseControlSupporter(String id) {
		this.id = id;
	}

	public BaseControlSupporter() {
		this(null);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

}
