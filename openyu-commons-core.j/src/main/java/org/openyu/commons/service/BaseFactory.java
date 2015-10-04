package org.openyu.commons.service;

import org.springframework.beans.factory.FactoryBean;

/**
 * 工廠類
 * 
 * 1.configLocation
 * 
 * 2.properties
 */
public interface BaseFactory<T> extends BaseService, FactoryBean<T> {

	T createInstance();

	T shutdownInstance();

	T restartInstance();

}
