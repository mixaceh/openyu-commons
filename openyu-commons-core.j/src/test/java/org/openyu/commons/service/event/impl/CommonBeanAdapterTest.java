package org.openyu.commons.service.event.impl;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.openyu.commons.dao.impl.CommonDaoImpl;
import org.openyu.commons.dao.supporter.CommonDaoSupporter;
import org.openyu.commons.dog.po.impl.DogPoImpl;
import org.openyu.commons.dog.vo.impl.DogImpl;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.service.event.impl.CommonBeanAdapter;
import org.openyu.commons.service.impl.CommonServiceImpl;

public class CommonBeanAdapterTest {
	private static CommonServiceImpl commonServiceImpl;

	private static CommonDaoImpl commonDaoImpl;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// service
		commonServiceImpl = new CommonServiceImpl();
		// dao
		commonDaoImpl = new CommonDaoImpl();

		// 建構HibernateTemplate,因HibernateDaoSupporter需要
		HibernateTemplate hibernateTemplate = new HibernateTemplate();
		Configuration config = new Configuration().configure("hibernate.cfg.xml");
		// SessionFactory sessionFactory = config.buildSessionFactory();

		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties())
				.buildServiceRegistry();
		SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
		hibernateTemplate.setSessionFactory(sessionFactory);
		commonDaoImpl.setHibernateTemplate(hibernateTemplate);
		//
		commonServiceImpl.setCommonDao(commonDaoImpl);
		commonServiceImpl.start();

	}

	@Test
	public void openSession() throws Exception {
		Configuration config = new Configuration().configure("hibernate.cfg.xml");

		// SessionFactory sessionFactory = config.buildSessionFactory();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties())
				.buildServiceRegistry();
		SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);

		System.out.println("sessionFactory: " + sessionFactory);
		Session session = sessionFactory.openSession();
		session.doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				System.out.println("connection: " + connection);
				System.out.println("getAutoCommit: " + connection.getAutoCommit());
				System.out.println("getTransactionIsolation: " + connection.getTransactionIsolation());
			}
		});
	}

	@Test
	public void commonServiceSupporter() {
		System.out.println(commonServiceImpl);
		assertNotNull(commonServiceImpl);
	}

	@Test
	public void event() {
		final String ID = "TEST_DOG";

		// 註冊listener
		CommonBeanAdapter commonBeanAdapter = new CommonBeanAdapter();
		commonServiceImpl.addBeanListener(commonBeanAdapter);
		//
		int count = 1;// 100w
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// random
			int randomNumber = NumberHelper.randomInt(0, 1000000);

			DogPoImpl dogPo = new DogPoImpl();
			String id = ID + randomNumber;
			dogPo.setId(id);
			// create
			commonServiceImpl.insert(dogPo);
			System.out.println("insert: " + commonServiceImpl.getBeans());

			// retrieve
			DogImpl existDog = commonServiceImpl.find(DogPoImpl.class, dogPo.getSeq());
			System.out.println("find: " + commonServiceImpl.getBeans());
			assertEquals(id, existDog.getId());

			// update
			dogPo.setValid(false);
			commonServiceImpl.update(dogPo);
			System.out.println("update: " + commonServiceImpl.getBeans());

			// delete
			commonServiceImpl.delete(dogPo);
			System.out.println("delete: " + commonServiceImpl.getBeans());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(commonServiceImpl.getBeanListeners().length);

		commonServiceImpl.removeBeanListener(commonBeanAdapter);
		System.out.println(commonServiceImpl.getBeanListeners());
	}

}