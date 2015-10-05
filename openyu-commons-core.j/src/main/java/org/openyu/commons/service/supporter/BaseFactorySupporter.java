package org.openyu.commons.service.supporter;

import java.util.Properties;

import org.apache.commons.collections.ExtendedProperties;
import org.openyu.commons.service.BaseFactory;
import org.openyu.commons.service.ShutdownCallback;
import org.openyu.commons.service.StartCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

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

	private transient Resource configLocation;

	private transient Properties properties;

	/**
	 * properties改成使用ExtendedProperties
	 */
	protected transient ExtendedProperties extendedProperties = new ExtendedProperties();

	public BaseFactorySupporter() {
		addServiceCallback("StartCallbacker", new StartCallbacker());
		addServiceCallback("ShutdownCallbacker", new ShutdownCallbacker());
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * 內部啟動
	 */
	protected class StartCallbacker implements StartCallback {

		@Override
		public void doInAction() throws Exception {
			mergeProperties();
		}
	}

	/**
	 * 合併設定
	 * 
	 * properties改成使用 extendedProperties 取屬性
	 * 
	 * @throws Exception
	 */
	protected void mergeProperties() throws Exception {
		Properties props = new Properties();
		if (this.configLocation != null) {
			LOGGER.info(new StringBuilder().append("Loading config from ").append(this.configLocation).toString());
			PropertiesLoaderUtils.fillProperties(props, this.configLocation);
		}
		if (this.properties != null) {
			LOGGER.info(new StringBuilder().append("Loading properties from setProperties(Properties)").toString());
			props.putAll(this.properties);
			// 清除原properties,省mem,因之後會使用extendedProperties了
			this.properties.clear();
		}
		//
		if (props.size() > 0) {
			this.extendedProperties = ExtendedProperties.convertProperties(props);
			if (this.extendedProperties.size() > 0) {
				LOGGER.info(new StringBuilder().append("Merged properties are " + extendedProperties).toString());
			}
		}
	}

	/**
	 * 內部關閉
	 */
	protected class ShutdownCallbacker implements ShutdownCallback {

		@Override
		public void doInAction() throws Exception {
			extendedProperties.clear();
		}
	}
}
