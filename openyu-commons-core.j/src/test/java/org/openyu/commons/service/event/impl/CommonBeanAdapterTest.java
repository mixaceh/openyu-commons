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
import org.openyu.commons.dao.supporter.CommonDaoSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.po.impl.DogPoImpl;
import org.openyu.commons.service.event.impl.CommonBeanAdapter;
import org.openyu.commons.service.supporter.CommonServiceSupporter;
import org.openyu.commons.vo.impl.DogImpl;

public class CommonBeanAdapterTest {
	private static CommonServiceSupporter commonServiceSupporter;

	private static CommonDaoSupporter commonDaoSupporter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		commonServiceSupporter = new CommonServiceSupporter();

		// dao
		commonDaoSupporter = new CommonDaoSupporter();

		// 建構HibernateTemplate,因HibernateDaoSupporter需要
		HibernateTemplate hibernateTemplate = new HibernateTemplate();
		Configuration config = new Configuration()
				.configure("hibernate.cfg.xml");
		// SessionFactory sessionFactory = config.buildSessionFactory();

		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(config.getProperties()).buildServiceRegistry();
		SessionFactory sessionFactory = config
				.buildSessionFactory(serviceRegistry);
		hibernateTemplate.setSessionFactory(sessionFactory);
		commonDaoSupporter.setHibernateTemplate(hibernateTemplate);
		//
		commonServiceSupporter.setCommonDao(commonDaoSupporter);

	}

	@Test
	public void openSession() throws Exception {
		Configuration config = new Configuration()
				.configure("hibernate.cfg.xml");

		// SessionFactory sessionFactory = config.buildSessionFactory();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(config.getProperties()).buildServiceRegistry();
		SessionFactory sessionFactory = config
				.buildSessionFactory(serviceRegistry);

		System.out.println("sessionFactory: " + sessionFactory);
		Session session = sessionFactory.openSession();
		session.doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				System.out.println("connection: " + connection);
				System.out.println("getAutoCommit: "
						+ connection.getAutoCommit());
				System.out.println("getTransactionIsolation: "
						+ connection.getTransactionIsolation());
			}
		});
	}

	@Test
	public void ojServiceSupporter() {
		System.out.println(commonServiceSupporter);
		assertNotNull(commonServiceSupporter);
	}

	@Test
	public void event() {
		final String ID = "TEST_DOG";

		// 註冊listener
		CommonBeanAdapter ojBeanAdapter = new CommonBeanAdapter();
		commonServiceSupporter.addBeanListener(ojBeanAdapter);
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
			commonServiceSupporter.insert(dogPo);
			System.out.println("insert: " + commonServiceSupporter.getBeanCache());

			// retrieve
			DogImpl existDog = commonServiceSupporter.find(DogPoImpl.class,
					dogPo.getSeq());
			System.out.println("find: " + commonServiceSupporter.getBeanCache());
			assertEquals(id, existDog.getId());

			// update
			dogPo.setValid(false);
			commonServiceSupporter.update(dogPo);
			System.out.println("update: " + commonServiceSupporter.getBeanCache());

			// delete
			commonServiceSupporter.delete(dogPo);
			System.out.println("delete: " + commonServiceSupporter.getBeanCache());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(commonServiceSupporter.getBeanListeners().length);

		commonServiceSupporter.removeBeanListener(ojBeanAdapter);
		System.out.println(commonServiceSupporter.getBeanListeners());
	}

}