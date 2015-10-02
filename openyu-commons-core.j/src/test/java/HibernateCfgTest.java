import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.NumberHelper;
import org.openyu.commons.po.impl.DogPoImpl;

public class HibernateCfgTest extends BaseTestSupporter {

	private static Configuration configuration;

	private static SessionFactory sessionFactory;

	protected static void createConfiguration() {
		if (configuration != null) {
			return;
		}
		configuration = new Configuration().configure("hibernate.cfg.xml");

		System.out.println("configuration: " + configuration);
		System.out.println("configuration.getProperties(): "
				+ configuration.getProperties());
	}

	@Test
	public void configuration() {
		createConfiguration();
		//
		System.out.println(configuration);
		assertNotNull(configuration);
	}

	protected static void createSessionFactory() {
		if (sessionFactory != null) {
			return;
		}
		createConfiguration();
		//
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		System.out.println(sessionFactory);
	}

	@Test
	public void sessionFactory() throws Exception {
		createSessionFactory();
		//
		System.out.println(sessionFactory);
		assertNotNull(sessionFactory);
	}

	@Test
	public void openSession() throws Exception {
		createSessionFactory();
		//
		Session session = sessionFactory.openSession();
		session.doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				System.out.println("connection: " + connection);
				System.out.println("autoCommit: " + connection.getAutoCommit());
				System.out.println("transactionIsolation: "
						+ connection.getTransactionIsolation());
			}
		});
		System.out.println("flushMode: " + session.getFlushMode());//FlushMode.AUTO
		System.out.println(session);
		assertNotNull(session);
	}

	@Test
	public void crudNoTx() throws Exception {
		createSessionFactory();
		//
		Session session = sessionFactory.openSession();
		try {
			//
			final String ID = "TEST_DOG";
			DogPoImpl dogPo = new DogPoImpl();
			// random
			int randomNumber = NumberHelper.randomInt(0, 1000000);
			String id = ID + randomNumber;
			dogPo.setId(id);

			int result = 0;
			// create
			session.save(dogPo);
			result = 1;
			printInsert(0, result);
			assertTrue(result > 0);

			// retrieve
			// 使用load, 若讀不到資料時會報ObjectNotFoundException
			// 使用get, 若讀不到資料則傳回null
			DogPoImpl foundEntity = (DogPoImpl) session.load(DogPoImpl.class,
					dogPo.getSeq());
			printFind(0, foundEntity);
			assertEquals(id, foundEntity.getId());

			// update
			dogPo.setValid(true);
			result = 0;
			session.update(dogPo);
			result = 1;
			printUpdate(0, result);
			assertTrue(result > 0);

			// delete
			result = 0;
			session.delete(dogPo);
			result = 1;
			printDelete(0, result);
			assertTrue(result > 0);
			//
		} finally {
			// 若設定
			// hibernate.transaction.auto_close_session=true,再呼叫session.close()
			// 會報 org.hibernate.SessionException: Session was already closed
			session.close();
		}
	}

	@Test
	public void crudTx() throws Exception {
		createSessionFactory();
		//
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			final String ID = "TEST_DOG";
			DogPoImpl dogPo = new DogPoImpl();
			// random
			int randomNumber = NumberHelper.randomInt(0, 1000000);
			String id = ID + randomNumber;
			dogPo.setId(id);

			// create
			tx = session.beginTransaction();
			int result = 0;
			session.save(dogPo);
			result = 1;
			printInsert(0, result);
			assertTrue(result > 0);

			// tx.commit();

			// retrieve
			// 使用load, 若讀不到資料時會報ObjectNotFoundException
			// 使用get, 若讀不到資料則傳回null
			DogPoImpl foundEntity = (DogPoImpl) session.load(DogPoImpl.class,
					dogPo.getSeq());
			printFind(0, foundEntity);
			assertEquals(id, foundEntity.getId());

			// update
			// tx = session.beginTransaction();
			dogPo.setValid(true);
			result = 0;
			session.update(dogPo);
			result = 1;
			printUpdate(0, result);
			assertTrue(result > 0);

			// tx.commit();

			// delete
			result = 0;
			session.delete(dogPo);
			result = 1;
			printDelete(0, result);
			assertTrue(result > 0);

			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			//
			if (tx != null)
				tx.rollback();
		} finally {
			// 若設定
			// hibernate.transaction.auto_close_session=true,再呼叫session.close()
			// 會報 org.hibernate.SessionException: Session was already closed
			session.close();
		}
	}
}
