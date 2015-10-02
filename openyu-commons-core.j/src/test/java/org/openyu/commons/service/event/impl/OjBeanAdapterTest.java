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
import org.openyu.commons.dao.supporter.OjDaoSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.po.impl.DogPoImpl;
import org.openyu.commons.service.event.impl.OjBeanAdapter;
import org.openyu.commons.service.supporter.OjServiceSupporter;
import org.openyu.commons.vo.impl.DogImpl;

public class OjBeanAdapterTest {
	private static OjServiceSupporter ojServiceSupporter;

	private static OjDaoSupporter hibernateDaoSupporter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ojServiceSupporter = new OjServiceSupporter();

		// dao
		hibernateDaoSupporter = new OjDaoSupporter();

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
		hibernateDaoSupporter.setHibernateTemplate(hibernateTemplate);
		//
		ojServiceSupporter.setOjDao(hibernateDaoSupporter);

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
		System.out.println(ojServiceSupporter);
		assertNotNull(ojServiceSupporter);
	}

	@Test
	public void event() {
		final String ID = "TEST_DOG";

		// 註冊listener
		OjBeanAdapter ojBeanAdapter = new OjBeanAdapter();
		ojServiceSupporter.addBeanListener(ojBeanAdapter);
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
			ojServiceSupporter.insert(dogPo);
			System.out.println("insert: " + ojServiceSupporter.getBeanCache());

			// retrieve
			DogImpl existDog = ojServiceSupporter.find(DogPoImpl.class,
					dogPo.getSeq());
			System.out.println("find: " + ojServiceSupporter.getBeanCache());
			assertEquals(id, existDog.getId());

			// update
			dogPo.setValid(false);
			ojServiceSupporter.update(dogPo);
			System.out.println("update: " + ojServiceSupporter.getBeanCache());

			// delete
			ojServiceSupporter.delete(dogPo);
			System.out.println("delete: " + ojServiceSupporter.getBeanCache());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		System.out.println(ojServiceSupporter.getBeanListeners().length);

		ojServiceSupporter.removeBeanListener(ojBeanAdapter);
		System.out.println(ojServiceSupporter.getBeanListeners());
	}

}