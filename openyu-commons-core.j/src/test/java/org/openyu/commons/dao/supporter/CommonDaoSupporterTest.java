package org.openyu.commons.dao.supporter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.openyu.commons.bean.NamesBean;
import org.openyu.commons.bean.supporter.NamesBeanSupporter;
import org.openyu.commons.cat.po.impl.CatPoImpl;
import org.openyu.commons.cat.vo.impl.CatImpl;
import org.openyu.commons.dao.supporter.CommonDaoSupporter;
import org.openyu.commons.dog.po.impl.DogPoImpl;
import org.openyu.commons.entity.LocaleNameEntity;
import org.openyu.commons.entity.NamesEntity;
import org.openyu.commons.entity.supporter.LocaleNameEntitySupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.lang.CloneHelper;
import org.openyu.commons.lang.NumberHelper;

/**
 * 1.不使用spring ioc測試,直接以建構方式, new HibernateDaoSupporter()測試
 * 
 * 2.hibernate.cfg.xml
 * 
 * 3.測試物件,/test/org.openyu.commons.entity.Dog
 * 
 */
public class CommonDaoSupporterTest extends BaseTestSupporter {

	private static Configuration configuration;

	private static SessionFactory sessionFactory;

	private static CommonDaoSupporter commonDaoSupporter;

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
		System.out.println("flushMode: " + session.getFlushMode());
		System.out.println(session);
		assertNotNull(session);
	}

	protected static void createCommonDaoSupporter() {
		if (commonDaoSupporter != null) {
			return;
		}
		//
		createSessionFactory();
		//
		// dao
		commonDaoSupporter = new CommonDaoSupporter();

		// 建構HibernateTemplate,因HibernateDaoSupporter需要
		HibernateTemplate hibernateTemplate = new HibernateTemplate();
		hibernateTemplate.setSessionFactory(sessionFactory);
		//
		commonDaoSupporter.setHibernateTemplate(hibernateTemplate);
		System.out.println(commonDaoSupporter);
	}

	@Test
	public void commonDaoSupporter() {
		createCommonDaoSupporter();
		//
		System.out.println(commonDaoSupporter);
		assertNotNull(commonDaoSupporter);
	}

	@Test
	// insert -> find -> delete
	//
	// 10 times: 1052 mills.
	// 10 times: 1172 mills.
	// 10 times: 1009 mills.
	//
	// verified: ok
	public void crud() {
		createCommonDaoSupporter();
		//
		final String ID = "TEST_DOG";
		// final String NAME = "TEST_NAME";

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// random
			int randomNumber = NumberHelper.randomInt(0, 1000000);

			DogPoImpl dogPo = new DogPoImpl();
			String id = ID + randomNumber;
			dogPo.setId(id);

			// #deprecated, 改成 addName
			// LocaleNameEntity name = new LocaleNameEntitySupporter();
			// name.setLocale(Locale.TRADITIONAL_CHINESE);
			// name.setName("拉拉");
			// dogPo.getNames().add(name);
			// //
			// name = new LocaleNameEntitySupporter();
			// name.setLocale(Locale.US);
			// name.setName("LaLa");
			// dogPo.getNames().add(name);

			// #new 加入name
			dogPo.addName(Locale.TRADITIONAL_CHINESE, "拉拉");
			dogPo.addName(Locale.US, "LaLa");

			// create
			Serializable pk = commonDaoSupporter.insert(dogPo);
			System.out.println("#." + i + " insert: "
					+ (pk != null ? "[success]" : "[fail]"));
			assertNotNull(pk);

			// retrieve
			DogPoImpl foundEntity = commonDaoSupporter.find(DogPoImpl.class,
					dogPo.getSeq());
			System.out.println("#." + i + " find: "
					+ (foundEntity != null ? "[success]" : "[fail]"));
			assertEquals(id, foundEntity.getId());

			// update
			dogPo.setValid(true);
			int updated = commonDaoSupporter.update(dogPo);
			System.out.println("#." + i + " update: "
					+ (updated > 0 ? "[success]" : "[fail]"));
			assertTrue(updated > 0);

			// delete
			DogPoImpl deletedEntity = commonDaoSupporter.delete(
					DogPoImpl.class, dogPo.getSeq());
			System.out.println("#." + i + " delete: "
					+ (deletedEntity != null ? "[success]" : "[fail]"));
			assertNotNull(deletedEntity);

		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// verified: ok
	public void insert() {
		createCommonDaoSupporter();
		//
		final String ID = "TEST_DOG";

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// random
			int randomNumber = NumberHelper.randomInt(0, 1000000);

			DogPoImpl dogPo = new DogPoImpl();
			dogPo.setId(ID + randomNumber);
			dogPo.setValid(true);
			// audit 已在EntityHelper.doAudit處理,故不需自行處理

			// AuditEntity audit = new AuditEntitySupporter();
			// audit.setCreateUser("sys");
			// audit.setCreateDate(DateHelper.today());
			// dogPo.setAudit(audit);

			// #deprecated, 改成 addName
			// LocaleNameEntity name = new LocaleNameEntitySupporter();
			// name.setLocale(Locale.TRADITIONAL_CHINESE);
			// name.setName("拉拉");
			// dogPo.getNames().add(name);
			// //
			// name = new LocaleNameEntitySupporter();
			// name.setLocale(Locale.US);
			// name.setName("LaLa");
			// dogPo.getNames().add(name);

			// #new 加入name
			dogPo.addName(Locale.TRADITIONAL_CHINESE, "拉拉");
			dogPo.addName(Locale.US, "LaLa");

			Serializable pk = commonDaoSupporter.insert(dogPo);
			System.out.println("insert: "
					+ (pk != null ? "[success]" : "[fail]"));
			assertNotNull(pk);

			DogPoImpl foundEntity = commonDaoSupporter.find(DogPoImpl.class,
					dogPo.getSeq());
			assertEquals(dogPo.getId(), foundEntity.getId());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// 1000 times: 982 mills.
	// 1000 times: 977 mills.
	// 1000 times: 978 mills.
	public void find() {
		createCommonDaoSupporter();
		//
		final String ID = "TEST_DOG917751";
		DogPoImpl result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = commonDaoSupporter.find(DogPoImpl.class, 96L);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(ID, result.getId());
	}

	@Test
	// 1000 times: 1243 mills.
	// 1000 times: 1263 mills.
	// 1000 times: 1291 mills.
	// verified: ok
	public void findAll() {
		createCommonDaoSupporter();
		//
		List<DogPoImpl> result = null;
		int count = 1;

		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = commonDaoSupporter.find(DogPoImpl.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);

		assertNotNull(result);
	}

	@Test
	// 1000 times: 982 mills.
	// 1000 times: 977 mills.
	// 1000 times: 978 mills.
	public void findDog() {
		createCommonDaoSupporter();
		//
		final String ID = "TEST_DOG917751";
		DogPoImpl result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = mockFindDog(Locale.TRADITIONAL_CHINESE, ID);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertEquals(ID, result.getId());
	}

	private DogPoImpl mockFindDog(String id) {
		return mockFindDog(null, id);
	}

	/**
	 * 測試用資料
	 * 
	 * @param id
	 * @return
	 */
	private DogPoImpl mockFindDog(Locale locale, String id) {
		DogPoImpl result = null;
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		//
		StringBuilder hql = new StringBuilder();
		hql.append("from ");
		hql.append(DogPoImpl.class.getName() + " ");
		hql.append("dogPo ");
		hql.append("where 1=1 ");
		hql.append("and id = :id ");
		//
		params.put("id", id);
		//
		result = commonDaoSupporter.findUniqueByHql(locale, hql.toString(),
				params);
		//
		return result;
	}

	@Test
	// verified: ok
	public void update() {
		createCommonDaoSupporter();
		//
		final String ID = "TEST_DOG889474";
		//
		DogPoImpl dogPo = mockFindDog(ID);
		Boolean beforeValid = null;
		if (dogPo != null) {
			beforeValid = dogPo.getValid();
			System.out.println("beforeValid: " + beforeValid);
			dogPo.setValid(beforeValid != null ? !beforeValid : Boolean.TRUE);
		}
		int result = 0;
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = commonDaoSupporter.update(dogPo);
			System.out.println("#" + i + " update: "
					+ (result > 0 ? "[success]" : "[fail]"));
			System.out.println("version: " + dogPo.getVersion());
			assertTrue(result > 0);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		assertTrue(result > 0);
	}

	@Test
	public void updateNonUniqueObjectException() {
		createCommonDaoSupporter();
		//
		final String ID = "TEST_DOG";

		DogPoImpl dogPo = mockFindDog(ID);
		//
		DogPoImpl anotherDogPo = CloneHelper.clone(dogPo);
		if (anotherDogPo != null) {
			anotherDogPo.setSeq(dogPo.getSeq());
			System.out.println("anotherDogPo seq: " + anotherDogPo.getSeq());
			anotherDogPo.getNames().clear();
			//
			// org.hibernate.NonUniqueObjectException
			int update = commonDaoSupporter.update(anotherDogPo);
			System.out.println("update: "
					+ (update > 0 ? "[success]" : "[fail]"));
			System.out.println(update);
		} else {
			System.out.println("nothing to update: " + anotherDogPo);
		}
	}

	@Test
	// verified: ok
	public void delete() {
		createCommonDaoSupporter();
		//
		final String ID = "TEST_DOG917751";
		//
		DogPoImpl dogPo = mockFindDog(ID);
		int result = commonDaoSupporter.delete(dogPo);
		System.out.println("delete: " + (result > 0 ? "[success]" : "[fail]"));
		System.out.println(result);
		assertTrue(result > 0);
	}

	@Test
	public void deleteBySeqs() {
		createCommonDaoSupporter();
		//
		Collection<Serializable> values = new LinkedList<Serializable>();
		values.add(1L);
		values.add(3L);
		values.add(5L);
		List<DogPoImpl> result = commonDaoSupporter.delete(DogPoImpl.class,
				values);
		System.out.println("delete: "
				+ (result.size() > 0 ? "[success]" : "[fail]"));
		System.out.println(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void deleteNonUniqueObjectException() {
		createCommonDaoSupporter();
		//
		final String ID = "TEST_DOG";
		//
		DogPoImpl dogPo = mockFindDog(ID);
		DogPoImpl anotherDogPo = CloneHelper.clone(dogPo);
		if (anotherDogPo != null) {
			anotherDogPo.setSeq(dogPo.getSeq());
			System.out.println("anotherDogPo seq: " + anotherDogPo.getSeq());
			//
			// org.hibernate.NonUniqueObjectException
			int delete = commonDaoSupporter.delete(anotherDogPo);
			System.out.println("delete: "
					+ (delete > 0 ? "[success]" : "[fail]"));
			System.out.println(delete);
		} else {
			System.out.println("nothing to delete: " + anotherDogPo);
		}

	}

	@Test
	// 1000 times: 692 mills.
	// 1000 times: 697 mills.
	// 1000 times: 700 mills.
	// verified: ok
	public void rowCount() {
		createCommonDaoSupporter();
		//
		long result = 0;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = commonDaoSupporter.rowCount(DogPoImpl.class);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result);
		assertTrue(result > 0);
	}

	@Test
	// 1000 times: 1593 mills.
	// 1000 times: 1615 mills.
	// 1000 times: 1604 mills.
	//
	// 10000 times: 9076 mills.
	// verified: ok
	public void findByHql() {
		createCommonDaoSupporter();
		//
		final String ID = "TEST";

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		//
		StringBuilder hql = new StringBuilder();
		hql.append("from ");
		hql.append(DogPoImpl.class.getName() + " ");
		hql.append("dogPo ");
		hql.append("where 1=1 ");
		hql.append("and lower(id) like lower(:id) ");
		//
		params.put("id", "%" + ID + "%");
		//
		List<?> result = null;

		int count = 1000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = commonDaoSupporter.findByHql(hql, params);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.size() + ", " + result);
		assertNotNull(result);
	}

	@Test
	public void reindex() {
		createCommonDaoSupporter();
		//
		commonDaoSupporter.reindex(DogPoImpl.class);
	}

	@Test
	// 1000 times: 2973 mills.
	// 1000 times: 2927 mills.
	// 1000 times: 2967 mills.
	//
	// 10000 times: 21268 mills.
	// verified: ok
	public void search() throws Exception {
		createCommonDaoSupporter();
		//
		FullTextSession fullTextSession = Search
				.getFullTextSession(sessionFactory.openSession());

		// StopAnalyzer 完全相同才能找到資料,同=,無法查中文
		// StandardAnalyzer 能找到資料,同like

		Analyzer analyzer = new KeywordAnalyzer();
		// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_34);
		QueryParser parser = new QueryParser(Version.LUCENE_31, "id", analyzer);
		parser.setAllowLeadingWildcard(true);
		parser.setLowercaseExpandedTerms(true);
		//

		// name:Marry
		// name:瑪莉
		// String search = String.Format("name:{0} AND title:{1}", "中国建设银行",
		// "doc1");

		StringBuilder lql = new StringBuilder();
		// #issue: 大寫找不到???
		// lql.append("id:*a*");
		// lql.append("audit:*sys*");
		lql.append("names:*a*");

		org.apache.lucene.search.Query luceneQuery = parser.parse(lql
				.toString());
		FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(
				luceneQuery, DogPoImpl.class);

		//
		List result = null;

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = fullTextQuery.list();
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(result.size() + ", " + result);
		assertNotNull(result);
	}

	// TODO
	@Test
	public void searchByLql() {
		// createHibernateDaoSupporter();
		// //
		// final String ID = "a";
		//
		// Map<String, Object> params = new LinkedHashMap<String, Object>();
		// //
		// StringBuilder hql = new StringBuilder();
		// hql.append("from ");
		// hql.append(DogPo.class.getName() + " ");
		// hql.append("dogPo ");
		// hql.append("where 1=1 ");
		// hql.append("and lower(id) like lower(:id) ");
		// //
		// params.put("id", "%" + ID + "%");
	}

	@Test
	// verified: ok
	// 1000000 times: 86 mills.
	// 1000000 times: 95 mills.
	// 1000000 times: 88 mills.
	public void addName() {
		DogPoImpl dogPo = new DogPoImpl();
		Set<LocaleNameEntity> names = dogPo.getNames();

		// add
		dogPo.addName(Locale.TRADITIONAL_CHINESE, "拉拉");
		dogPo.addName(Locale.TRADITIONAL_CHINESE, "拉拉");

		System.out.println("addName: " + names.size() + ", " + names);
		assertEquals(1, names.size());

		// get
		LocaleNameEntity localeNameEntity = dogPo
				.getNameEntry(Locale.TRADITIONAL_CHINESE);
		assertNotNull(localeNameEntity);

		// getName
		String name = dogPo.getName(Locale.TRADITIONAL_CHINESE);
		System.out.println("getName: " + name);
		assertEquals("拉拉", name);

		// set
		dogPo.setName(Locale.TRADITIONAL_CHINESE, "拉拉123");
		System.out.println("setName: " + names.size() + ", " + names);
		assertEquals("拉拉123", dogPo.getName(Locale.TRADITIONAL_CHINESE));

		// remove
		boolean result = dogPo.removeName(Locale.TRADITIONAL_CHINESE);
		assertTrue(result);
		System.out.println("removeName: " + names.size() + ", " + names);

		//
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			result = dogPo.addName(Locale.US, "LaLa");
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println("size: " + dogPo.getNames().size());
	}

	@Test
	public void localeEquals() {
		LocaleNameEntity localeNameEntity = new LocaleNameEntitySupporter();
		localeNameEntity.setLocale(Locale.TRADITIONAL_CHINESE);
		localeNameEntity.setName("拉拉");
		//
		LocaleNameEntity localeNameEntity2 = new LocaleNameEntitySupporter();
		localeNameEntity2.setLocale(Locale.TRADITIONAL_CHINESE);
		localeNameEntity2.setName("拉拉");

		System.out.println(localeNameEntity.equals(localeNameEntity2));
	}

	@Test
	// verified: ok
	public void insertCat() {
		createCommonDaoSupporter();
		//
		final String ID = "TEST_CAT";

		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			// random
			int randomNumber = NumberHelper.randomInt(0, 1000000);

			CatPoImpl catPo = new CatPoImpl();
			catPo.setId(ID + randomNumber);
			catPo.setValid(true);

			// #new 加入name
			catPo.addName(Locale.TRADITIONAL_CHINESE, "金吉拉");
			catPo.addName(Locale.US, "Gin Gi La");

			Serializable pk = commonDaoSupporter.insert(catPo);
			System.out.println("insert: "
					+ (pk != null ? "[success]" : "[fail]"));
			assertNotNull(pk);

			CatPoImpl existCat = commonDaoSupporter.find(CatPoImpl.class,
					catPo.getSeq());
			assertEquals(catPo.getId(), existCat.getId());
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
	}

	@Test
	// 1000000 times: 8924 mills.
	// 1000000 times: 8910 mills.
	// 1000000 times: 8851 mills.
	public void copyNames2Po() {
		NamesBean namesBean = new NamesBeanSupporter();
		namesBean.addName(Locale.TRADITIONAL_CHINESE, "拉拉");
		System.out.println(namesBean);
		//
		Class<?> result = ClassHelper.getConventionClass(namesBean);
		System.out.println(result);// NamesEntitySupporter
		//
		NamesEntity namesEntity = null;
		int count = 1000000;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			namesEntity = ClassHelper.copyProperties(namesBean);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(namesEntity);
	}

	@Test
	// 1000000 times: 23410 mills.
	// 1000000 times: 23572 mills.
	public void copyCat2Po() {
		final String ID = "TEST_CAT";
		// random
		int randomNumber = NumberHelper.randomInt(0, 1000000);
		//
		CatImpl cat = new CatImpl();
		cat.setId(ID + randomNumber);
		cat.setValid(true);
		// #new 加入name
		cat.addName(Locale.TRADITIONAL_CHINESE, "金吉拉");
		cat.addName(Locale.US, "Gin Gi La");
		// System.out.println(cat);
		cat.getAudit().setCreateDate(new Date());
		cat.getAudit().setCreateUser("sys");
		//
		Class<?> result = ClassHelper.getConventionClass(cat);
		System.out.println(result);// CatPoImpl
		//
		CatPoImpl catPo = null;
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			catPo = ClassHelper.copyProperties(cat);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		System.out.println(catPo);
	}

}
