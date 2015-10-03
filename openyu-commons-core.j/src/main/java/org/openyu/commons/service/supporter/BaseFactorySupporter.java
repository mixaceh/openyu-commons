package org.openyu.commons.service.supporter;

import java.util.Properties;

import org.apache.commons.collections.ExtendedProperties;
import org.openyu.commons.service.BaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * 工廠類
 * 
 * 1.configLocation
 * 
 * 2.properties
 */
public abstract class BaseFactorySupporter<T> extends BaseServiceSupporter implements BaseFactory<T> {

	private static final long serialVersionUID = -1565481677571797118L;

	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseFactorySupporter.class);

	private Resource configLocation;

	private Properties properties;

	protected ExtendedProperties extendedProperties;

	public BaseFactorySupporter() {
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
