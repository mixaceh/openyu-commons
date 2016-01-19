package org.openyu.commons.service;

import org.springframework.beans.factory.FactoryBean;

/**
 * 工廠類
 * 
 * 1.configLocation
 * 
 * 2.properties
 */
public interface BaseFactoryBean<T> extends BaseService, FactoryBean<T> {

}
