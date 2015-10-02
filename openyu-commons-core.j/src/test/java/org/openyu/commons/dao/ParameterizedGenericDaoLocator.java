package org.openyu.commons.dao;

import java.lang.reflect.*;

import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
public class ParameterizedGenericDaoLocator {
	
	private final Class<?> daoClass;
	
	public ParameterizedGenericDaoLocator(Class<?> daoClass) {
		this.daoClass = daoClass;
	}
	
	private ParameterizedType locate() {
		Type type = daoClass.getGenericSuperclass();
		
		Assert.isTrue(type != null && type instanceof ParameterizedType == true,
				daoClass + " is not parameterized");
		
		ParameterizedType parameterizedType = (ParameterizedType) type;
		
//		Assert.isTrue(parameterizedType.getRawType() == HibernateDaoSupport.class,
//				daoClass + " is not a direct subclass of " + HibernateDaoSupporter.class);
		
		return parameterizedType;
	}
	
	public Class getEntityClass() {
		ParameterizedType parameterizedGenericDao = locate();
		Class entityClass = (Class) parameterizedGenericDao.getActualTypeArguments()[0];
		Assert.isTrue(entityClass != Object.class, "Entity class cannot be " + Object.class.getName());
		return entityClass;
	}
}
