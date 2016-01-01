package org.openyu.commons.dao.supporter;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.search.SortField;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.Search;
import org.hibernate.type.Type;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.util.StopWatch;
import org.openyu.commons.dao.CommonDao;
import org.openyu.commons.dao.ex.CommonDaoException;
import org.openyu.commons.dao.inquiry.Inquiry;
import org.openyu.commons.dao.inquiry.Order;
import org.openyu.commons.dao.inquiry.Pagination;
import org.openyu.commons.dao.inquiry.Sort;
import org.openyu.commons.dao.inquiry.Order.OrderType;
import org.openyu.commons.entity.EntityHelper;
import org.openyu.commons.entity.SeqEntity;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.util.AssertHelper;
import org.openyu.commons.util.CollectionHelper;

/**
 * 20140923, upgrade to hibernate4
 */
// public class OjDaoSupporter extends HibernateDaoSupport implements
// ApplicationContextAware, OjDao, Supporter {

// 2014/10/09, 改為不繼承spring HibernateDaoSupport
public abstract class CommonDaoSupporter extends BaseDaoSupporter implements CommonDao {

	private static final long serialVersionUID = 565018661823278805L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(CommonDaoSupporter.class);

	protected transient HibernateTemplate hibernateTemplate;

	private static final String JOIN_FETCH_PATTERN = "(?:(?:(?:(?:left)|(?:right)){1}(?:\\souter)?)|(?:inner))?\\s+join\\s+fetch\\s+\\w+(?:[.]\\w+)*";

	private static final String SELECT_DISTINCT_PATTERN = "select\\s+((?:distinct\\s+)?\\w+(?:[.]\\w+)*)\\s+from";

	private static final String SELECT_DISTINCT_REPLACE = "select count($1) from";

	public CommonDaoSupporter() {
	}

	/**
	 * 檢查設置
	 * 
	 * @throws Exception
	 */
	protected final void checkConfig() throws Exception {
		AssertHelper.notNull(this.hibernateTemplate, "The HibernateTemplate is required");
		AssertHelper.notNull(this.hibernateTemplate.getSessionFactory(), "The SessionFactory is required");
	}

	public final SessionFactory getSessionFactory() {
		return ((this.hibernateTemplate != null ? this.hibernateTemplate.getSessionFactory() : null));
	}

	public final void setSessionFactory(SessionFactory sessionFactory) {
		if ((this.hibernateTemplate == null) || (sessionFactory != this.hibernateTemplate.getSessionFactory()))
			this.hibernateTemplate = createHibernateTemplate(sessionFactory);
	}

	protected HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory) {
		return new HibernateTemplate(sessionFactory);
	}

	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public <E> InputStream write(Collection<E> list) {
		throw new UnsupportedOperationException();
	}

	public <E> List<E> read(InputStream inputStream) {
		throw new UnsupportedOperationException();
	}

	// ----------------------------------------------------------------
	// getHibernateTemplate()
	// getSession()

	// query 可做 select,insert,update,delete(query.executeUpdate)
	// Query query = session.createQuery
	// SQLQuery query = session.createSQLQuery
	// query.setParameter("id",1L); //paramName
	// query.setParameter(0,1L); //位置
	// Query query = session.getNamedQuery() (不使用)
	//
	// Criteria criteria = session.createCriteria
	// --------------------------------------------------

	// implement commonDao
	// --------------------------------------------------
	// oo find(select), = findAll
	// --------------------------------------------------

	/**
	 * hql查詢所有資料, = findAll
	 * 
	 * @param entityClass
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> find(Class<?> entityClass) {
		List<T> result = new LinkedList<T>();
		try {
			result = (List<T>) hibernateTemplate.loadAll(entityClass);
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// --------------------------------------------------
	// oo find
	// 1.不用,改用callback
	// --------------------------------------------------
	// @SuppressWarnings("unchecked")
	// public <T> T ___find(Class<?> entityClass, Serializable seq)
	// {
	// T result = null;
	// try
	// {
	// //1.用load時, 當找不到, 會有ObjectNotFoundException,且都會printStackTrace
	// //entity = (T) getHibernateTemplate().load(entityClass, id);
	//
	// //2.改用此方式
	// result = (T) getHibernateTemplate().get(entityClass, seq);
	// }
	// catch (Exception ex)
	// {
	// ex.printStackTrace();
	// }
	// return result;
	// }

	// --------------------------------------------------
	// 2.利用callback方法，實作HibernateCallback介面，在doInHibernate()中進行操作
	// --------------------------------------------------
	/**
	 * hql查詢單筆pk資料
	 * 
	 * @param entityClass
	 * @param seq
	 */
	// @SuppressWarnings({ "unchecked", "rawtypes" })
	@SuppressWarnings("unchecked")
	public <T> T find(final Class<?> entityClass, final Serializable seq) {
		T result = null;
		try {
			// result = (T) getHibernateTemplate().executeWithNativeSession(new
			// HibernateCallback()
			// {
			// public Object doInHibernate(Session session) throws
			// HibernateException
			// {
			// Object entity = null;
			//
			// //1.用load時, 當找不到, 會有ObjectNotFoundException,且都會printStackTrace
			// //entity= session.load(entityClass, id);
			//
			// //2.改用此方式, 有多筆資料時,取第一筆
			// entity = session.get(entityClass, seq);
			// return entity;
			// }
			// });

			// #issue: 在web上,當filed為自訂物件audit,list,set都讀不到
			// 不是以上callback造成的,用 getHibernateTemplate().get()一樣不行
			// result = (T) getHibernateTemplate().get(entityClass, seq);

			// #fix 在web上真不知為啥,(與 OpenSessionInViewFilter 有關???)
			// 暫時先用hql,此方式處理
			// Map<String, Object> params = new LinkedHashMap<String, Object>();
			// //
			// StringBuilder hql = new StringBuilder();
			// hql.append("from ");
			// hql.append(entityClass.getName() + " ");
			// hql.append("where 1=1 ");
			// hql.append("and seq=:seq ");// pk欄位先寫固定
			// //
			// params.put("seq", seq);
			// //
			// result = findUniqueByHql(hql.toString(), params);

			// 使用load, 若讀不到資料時會報ObjectNotFoundException
			// 使用get, 若讀不到資料則傳回null
			result = (T) hibernateTemplate.load(entityClass, seq);
		} catch (Exception ex) {
			// throw new CommonDaoException(ex);
		}
		return result;
	}

	/**
	 * hql查詢多筆pk資料
	 * 
	 * @param entityClass
	 * @param seqs
	 */
	public <E> List<E> find(Class<?> entityClass, Collection<Serializable> seqs) {
		List<E> result = new LinkedList<E>();
		try {
			if (CollectionHelper.notEmpty(seqs)) {
				for (Serializable seq : seqs) {
					E entity = find(entityClass, seq);
					if (entity != null) {
						result.add(entity);
					}
				}
			}
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	protected <T> T findUniqueByHql(String hqlString) {
		return findUniqueByHql(hqlString, null);
	}

	protected <T> T findUniqueByHql(String hqlString, Map<String, Object> params) {
		return findUniqueByHql(null, hqlString, params);
	}

	protected <T> T findUniqueByHql(StringBuilder hql) {
		return findUniqueByHql(hql, null);
	}

	protected <T> T findUniqueByHql(StringBuilder hql, Map<String, Object> params) {
		return findUniqueByHql(null, hql, params);
	}

	protected <T> T findUniqueByHql(Locale locale, StringBuilder hql, Map<String, Object> params) {
		return findUniqueByHql(locale, hql.toString(), params);
	}

	/**
	 * hql查詢一筆資料
	 * 
	 * 2012/04/03 多加locale
	 * 
	 * @param locale
	 * @param hqlString
	 * @param params
	 * @return
	 */
	public <T> T findUniqueByHql(final Locale locale, final String hqlString, final Map<String, Object> params) {
		T result = null;
		try {
			result = hibernateTemplate.executeWithNativeSession(new HibernateCallback<T>() {
				@SuppressWarnings("unchecked")
				public T doInHibernate(Session session) throws HibernateException {
					Query query = session.createQuery(hqlString);
					cacheQuery(query);
					processQueryParamters(query, params);
					query.setMaxResults(1);
					Object buff = query.uniqueResult();
					//
					if (locale != null) {
						// 2012/03/27 處理多語系的name
						EntityHelper.filterName(buff, locale);
					}
					return (T) buff;
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	protected <T> List<T> findByHql(String hqlString, Map<String, Object> params) {
		return findByHql(null, null, hqlString, params);
	}

	protected <T> List<T> findByHql(Inquiry inquiry, String hqlString, Map<String, Object> params) {
		return findByHql(inquiry, null, hqlString, params);
	}

	protected <T> List<T> findByHql(StringBuilder hql, Map<String, Object> params) {
		return findByHql(null, null, hql, params);
	}

	protected <T> List<T> findByHql(Inquiry inquiry, StringBuilder hql, Map<String, Object> params) {
		return findByHql(inquiry, null, hql, params);
	}

	protected <T> List<T> findByHql(Inquiry inquiry, Locale locale, StringBuilder hql, Map<String, Object> params) {
		return findByHql(inquiry, locale, hql.toString(), params);
	}

	// #issue: join fetch 之後 where 條件,尚未知如何取count
	/**
	 * hql查詢分頁多筆資料
	 * 
	 * 2012/04/03 多加locale
	 * 
	 * @param inquiry
	 * @param locale
	 * @param hqlString
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByHql(final Inquiry inquiry, final Locale locale, final String hqlString,
			final Map<String, Object> params) {
		List<T> result = new LinkedList<T>();
		try {
			if (inquiry == null) {
				result = hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<T>>() {
					public List<T> doInHibernate(Session session) throws HibernateException {
						Query query = session.createQuery(hqlString);
						cacheQuery(query);
						processQueryParamters(query, params);
						return query.list();
					}
				});

			} else {
				final StopWatch stopWatch = new StopWatch();
				stopWatch.start();
				//
				final StringBuffer buff = new StringBuffer(hqlString);
				// System.out.println("hql: " + buff);
				// 處理排序
				processSort(inquiry, buff);
				//
				result = hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<T>>() {
					public List<T> doInHibernate(Session session) throws HibernateException {
						Query query = session.createQuery(buff.toString());
						cacheQuery(query);
						processQueryParamters(query, params);
						//
						Pagination pagination = inquiry.getPagination();
						if (pagination == null) {
							LOGGER.warn("The Pagination is null, no data can be queried");
							return new LinkedList<T>();
						} else {
							if (!inquiry.isExport()) {
								// 從哪個index開始傳回，Index是從0開始算
								query.setFirstResult(pagination.getFirstResult());
								// count：由Index開始，總共要傳回幾筆資料
								query.setMaxResults(pagination.getMaxResults());
								// TODO 該放到外面取rowCount
								Long rowCount = rowCountByHql(hqlString, params);
								pagination.setRowCount(rowCount.intValue());
							}
							//
							List<T> buff = query.list();
							//
							stopWatch.stop();
							pagination.setProcessTime(stopWatch.getTotalTimeSeconds());
							return buff;
						}
					}
				});
			}
			// 2012/03/27 處理多語系的name
			if (locale != null) {
				EntityHelper.filterName(result, locale);
			}
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	/**
	 * 處理排序
	 * 
	 * @param inquiry
	 * @param buff
	 */
	protected void processSort(Inquiry inquiry, StringBuffer buff) {
		// 排序
		Sort sort = inquiry.getSort();
		// 排序欄位選項
		List<Sort> sorts = inquiry.getSorts();
		if (sort == null || StringHelper.isBlank(sort.getId())) {
			// 若目前排序欄位為null,則取排序欄位選項第一個
			if (CollectionHelper.notEmpty(sorts)) {
				sort = sorts.get(0);
			}
		}
		//
		if (sort != null && StringHelper.notBlank(sort.getId())) {
			// TODO 當HQL中有order by的情況,需判斷
			buff.append("order by ");
			buff.append(sort.getId());// 排序欄位

			// 排序方向
			Order order = inquiry.getOrder();
			if (order == null || order.getId() == null) {
				order = sort.getOrder();
				if (order == null || order.getId() == null) {
					// 排序方向選項
					List<Order> orders = inquiry.getOrders();
					// 若目前排序方向為null,則取排序方向選項第一個
					if (CollectionHelper.notEmpty(orders)) {
						order = orders.get(0);
					}
				}
			}
			//
			if (order != null && order.getId() != null) {
				// 不是自然方向
				if (order.getId() != OrderType.NATURAL) {
					buff.append(" ");
					buff.append(order.getId().getValue());// asc/desc
				}
			}
		}
	}

	// --------------------------------------------------
	// sql 查詢
	// --------------------------------------------------
	protected <T> List<T> findBySql(Inquiry inquiry, StringBuilder sql, Map<String, Object> params) {
		return findBySql(inquiry, sql.toString(), params);
	}

	/**
	 * sql查詢分頁多筆資料
	 * 
	 * @param inquiry
	 * @param sqlString
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> findBySql(final Inquiry inquiry, final String sqlString, final Map<String, Object> params) {
		List<T> result = new LinkedList<T>();
		try {
			if (inquiry == null) {
				result = hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<T>>() {
					public List<T> doInHibernate(Session session) throws HibernateException {
						SQLQuery query = session.createSQLQuery(sqlString);
						cacheQuery(query);
						processQueryParamters(query, params);
						return query.list();
					}
				});
			} else {
				final StopWatch stopWatch = new StopWatch();
				stopWatch.start();
				//
				final StringBuffer buff = new StringBuffer(sqlString);
				// System.out.println("sql: " + buff);

				// 處理排序
				processSort(inquiry, buff);
				//
				result = hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<T>>() {
					public List<T> doInHibernate(Session session) throws HibernateException {
						SQLQuery query = session.createSQLQuery(buff.toString());
						cacheQuery(query);
						processQueryParamters(query, params);
						//
						Pagination pagination = inquiry.getPagination();
						if (pagination == null) {
							LOGGER.warn("pagination is null, no data can be queried");
							return new LinkedList<T>();
						} else {
							if (!inquiry.isExport()) {
								query.setFirstResult(pagination.getFirstResult());
								query.setMaxResults(pagination.getMaxResults());
								// TODO 該放到外面取rowCount
								Long rowCount = rowCountBySql(sqlString, params);
								pagination.setRowCount(rowCount.intValue());
							}
							//
							List<T> buff = query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
							//
							stopWatch.stop();
							pagination.setProcessTime(stopWatch.getTotalTimeSeconds());
							return buff;
						}
					}
				});
			}
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	//
	protected <T> List<T> searchByLql(Inquiry inquiry, org.apache.lucene.search.Query luceneQuery,
			Class<?>... entities) {
		return searchByLql(inquiry, null, luceneQuery, entities);
	}

	/**
	 * lql查詢分頁多筆資料
	 * 
	 * @param inquiry
	 * @param locale
	 * @param luceneQuery
	 * @param entities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> searchByLql(final Inquiry inquiry, final Locale locale,
			final org.apache.lucene.search.Query luceneQuery, final Class<?>... entities) {
		List<T> result = new LinkedList<T>();
		//
		try {
			result = hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<T>>() {
				public List<T> doInHibernate(Session session) throws HibernateException {
					if (inquiry == null) {
						FullTextQuery fullTextQuery = createFullTextQuery(session, luceneQuery, entities);
						return fullTextQuery.list();
					} else {
						StopWatch stopWatch = new StopWatch();
						stopWatch.start();

						FullTextQuery fullTextQuery = createFullTextQuery(session, luceneQuery, entities);

						// 處理排序
						// 在全文檢索下,會影響效率,第一次會變慢,之後似乎恢復正常
						processSort(inquiry, fullTextQuery);

						Pagination pagination = inquiry.getPagination();
						if (!inquiry.isExport()) {
							fullTextQuery.setFirstResult(pagination.getFirstResult());
							fullTextQuery.setMaxResults(pagination.getMaxResults());
							// TODO 該放到外面取rowCount
							long rowCount = fullTextQuery.getResultSize();
							// System.out.println("rowCount: "+rowCount);
							pagination.setRowCount(rowCount);
						}
						//
						List<T> buff = fullTextQuery.list();
						//
						stopWatch.stop();
						pagination.setProcessTime(stopWatch.getTotalTimeSeconds());
						return buff;
					}
				}
			});

			// 2012/03/27 處理多語系的name
			if (locale != null) {
				EntityHelper.filterName(result, locale);
			}
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	/**
	 * 處理排序
	 * 
	 * @param inquiry
	 * @param fullTextQuery
	 */
	protected void processSort(Inquiry inquiry, FullTextQuery fullTextQuery) {
		// 排序
		Sort sort = inquiry.getSort();
		// 排序欄位選項
		List<Sort> sorts = inquiry.getSorts();
		if (sort == null || StringHelper.isBlank(sort.getId())) {
			// 若目前排序欄位為null,則取排序欄位選項第一個
			if (CollectionHelper.notEmpty(sorts)) {
				sort = sorts.get(0);
			}
		}
		//
		if (sort != null && StringHelper.notBlank(sort.getId())) {
			// 排序方向
			Order order = inquiry.getOrder();
			if (order == null || order.getId() == null) {
				order = sort.getOrder();
				if (order == null || order.getId() == null) {
					// 排序方向選項
					List<Order> orders = inquiry.getOrders();
					// 若目前排序方向為null,則取排序方向選項第一個
					if (CollectionHelper.notEmpty(orders)) {
						order = orders.get(0);
					}
				}
			}
			//
			if (order != null && order.getId() != null) {
				// 不是自然方向
				if (order.getId() != OrderType.NATURAL) {
					// 升冪
					boolean reverse = (OrderType.ASC != order.getId());
					// false:升序, true:降序
					SortField sortField = new SortField(sort.getId(), SortField.STRING, reverse);
					//
					org.apache.lucene.search.Sort luceneSort = new org.apache.lucene.search.Sort();
					luceneSort.setSort(sortField);
					//
					fullTextQuery.setSort(luceneSort);
				}
			}
		}
	}

	protected Long rowCountByHql(StringBuilder hql, Map<String, Object> params) {
		return rowCountByHql(hql.toString(), params);
	}

	/**
	 * hql計算筆數
	 * 
	 * @param hqlString
	 * @param params
	 * @return
	 */
	protected Long rowCountByHql(String hqlString, final Map<String, Object> params) {
		Long result = 0L;
		try {
			String hqlProcessed = hqlString.replaceAll(JOIN_FETCH_PATTERN, "");
			// String hQLProcessed = hqlString;
			int oldLen = hqlProcessed.length();

			// select distinct user from ->select count(distinct user) from
			hqlProcessed = hqlProcessed.replaceFirst(SELECT_DISTINCT_PATTERN, SELECT_DISTINCT_REPLACE);
			final StringBuffer hqlBuffer = new StringBuffer(hqlProcessed);

			if (oldLen == hqlProcessed.length()) {
				hqlBuffer.insert(0, "SELECT COUNT(1) ");
			}
			// System.out.println("rowCount:" + hqlBuffer.toString());

			result = hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
				public Long doInHibernate(Session session) throws HibernateException {
					Query query = session.createQuery(hqlBuffer.toString());
					cacheQuery(query);
					processQueryParamters(query, params);
					return (Long) query.iterate().next();
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	protected long rowCountBySql(StringBuilder hql, Map<String, Object> params) {
		return rowCountBySql(hql.toString(), params);
	}

	/**
	 * sql計算筆數
	 * 
	 * @param sqlString
	 * @param params
	 * @return
	 */
	protected long rowCountBySql(String sqlString, final Map<String, Object> params) {
		long result = 0L;
		try {
			final String sQLProcessed = sqlString.replaceFirst("select\\s+(?:.*)\\s+from", "SELECT COUNT(1) from");

			Long buff = hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
				public Long doInHibernate(Session session) throws HibernateException {
					SQLQuery query = session.createSQLQuery(sQLProcessed);
					cacheQuery(query);
					processQueryParamters(query, params);
					return (Long) query.iterate().next();
				}
			});
			result = safeGet(buff);
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// --------------------------------------------------

	// --------------------------------------------------
	// oo save(insert/update)
	// 1.不用,改用callback
	// --------------------------------------------------
	// public int ___save(Object entity)
	// {
	// if (entity != null)
	// {
	// getHibernateTemplate().saveOrUpdate(entity);
	// return 1;
	// }
	// else
	// {
	// log.warn("entity is null!!!");
	// }
	// return 0;
	// }

	// --------------------------------------------------
	// 2.利用callback方法，實作HibernateCallback介面，在doInHibernate()中進行操作
	// --------------------------------------------------
	protected <T> int saveOrUpdate(T entity) {
		return saveOrUpdate(entity, null);
	}

	/**
	 * orm儲存
	 * 
	 * @param entity
	 * @param modifiedUser
	 * @return
	 */
	protected <T> int saveOrUpdate(final T entity, final String modifiedUser) {
		int result = 0;
		try {
			if (entity != null) {
				// Integer buff =
				// getHibernateTemplate().executeWithNativeSession(
				// new HibernateCallback<Integer>() {
				// public Integer doInHibernate(Session session)
				// throws HibernateException {
				// // session.clear();
				// session.saveOrUpdate(entity);
				// return 1;
				// }
				// });
				// result = safeGet(buff);

				hibernateTemplate.setCheckWriteOperations(false);
				hibernateTemplate.saveOrUpdate(entity);
				result = 1;
			}
			//
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// 20111101
	// for BeanEvent.ADDING,ADDED
	/**
	 * orm新增
	 * 
	 * =save()
	 * 
	 * @param entity
	 * @param modifiedUser
	 * @return pk
	 */
	public <T> Serializable insert(T entity) {
		return insert(entity, null);
	}

	/**
	 * orm新增
	 * 
	 * =save()
	 * 
	 * @param entity
	 * @param modifiedUser
	 * @return pk
	 */
	public <T> Serializable insert(T entity, String modifiedUser) {
		Serializable result = null;
		//
		try {
			if (entity instanceof SeqEntity) {
				// hibernate用,當新增時,version非null,塞null
				SeqEntity seqEntity = (SeqEntity) entity;
				Integer version = seqEntity.getVersion();
				if (version != null) {
					seqEntity.setVersion(null);
				}
			}
			// 20140926
			hibernateTemplate.setCheckWriteOperations(false);
			result = hibernateTemplate.save(entity);
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// for BeanEvent.MODIFYING,MODIFIED
	/**
	 * orm修改
	 * 
	 * @param entity
	 * @return
	 */
	public <T> int update(T entity) {
		return update(entity, null);
	}

	/**
	 * orm修改
	 * 
	 * @param entity
	 * @param modifiedUser
	 * @return
	 */
	public <T> int update(T entity, String modifiedUser) {
		int result = 0;
		try {
			// 2014/09/26
			hibernateTemplate.setCheckWriteOperations(false);
			// org.springframework.dao.DuplicateKeyException: A different object
			// with the same identifier value was already associated with the
			// session
			// hibernateTemplate.update(entity);

			// #fix
			hibernateTemplate.merge(entity);
			result = 1;
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// --------------------------------------------------
	// oo delete
	// 1.不用,改用callback
	// --------------------------------------------------
	// public int ___delete(Object entity)
	// {
	// int result = 0;
	// if (entity != null)
	// {
	// getHibernateTemplate().delete(entity);
	// reindex(entity);
	// result = 1;
	// }
	// return result;
	// }

	// --------------------------------------------------
	// 2.利用callback方法，實作HibernateCallback介面，在doInHibernate()中進行操作
	// --------------------------------------------------
	/**
	 * orm刪除
	 * 
	 * @param entity
	 * @return
	 */
	public <T> int delete(T entity) {
		return delete(entity, null);
	}

	/**
	 * orm刪除
	 * 
	 * @param entity
	 * @param modifiedUser
	 * @return
	 */
	public <T> int delete(T entity, final String modifiedUser) {
		int result = 0;
		try {
			// Integer buff = getHibernateTemplate().executeWithNativeSession(
			// new HibernateCallback<Integer>() {
			// public Integer doInHibernate(Session session)
			// throws HibernateException {
			// session.delete(entity);
			// return 1;
			// }
			// });
			// result = safeGet(buff);

			// 20140926
			hibernateTemplate.setCheckWriteOperations(false);
			hibernateTemplate.delete(entity);
			result = 1;
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// --------------------------------------------------
	// oo delete(get first, then delete)
	// 1.不用,改用callback
	// --------------------------------------------------
	// public int ___delete(Class<?> entityClass, Serializable id)
	// {
	// int result = 0;
	// try
	// {
	// Object entity = find(entityClass, id);
	// if (entity != null)
	// {
	// getHibernateTemplate().delete(entity);
	// reindex(entity);
	// result = 1;
	// }
	// }
	// catch (Exception ex)
	// {
	// ex.printStackTrace();
	// }
	// return result;
	// }

	/**
	 * orm刪除
	 * 
	 * @param entity
	 * @param seq
	 * @return
	 */
	public <T> T delete(Class<?> entityClass, Serializable seq) {
		return delete(entityClass, seq, null);
	}

	// --------------------------------------------------
	// 2.利用callback方法，實作HibernateCallback介面，在doInHibernate()中進行操作
	// --------------------------------------------------
	/**
	 * orm刪除
	 * 
	 * @param entity
	 * @param seq
	 * @param modifiedUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T delete(Class<?> entityClass, Serializable seq, String modifiedUser) {
		T result = null;
		try {
			final Object entity = find(entityClass, seq);
			if (entity != null) {
				int deleted = delete(entity, modifiedUser);
				if (deleted > 0) {
					result = (T) entity;
				}
			}
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	/**
	 * orm刪除
	 * 
	 * @param entity
	 * @param seqs
	 * @return
	 */
	public <E> List<E> delete(Class<?> entityClass, Collection<Serializable> seqs) {
		return delete(entityClass, seqs, null);
	}

	/**
	 * orm刪除
	 * 
	 * @param entity
	 * @param seqs
	 * @param modifiedUser
	 * @return
	 */
	public <E> List<E> delete(Class<?> entityClass, Collection<Serializable> seqs, String modifiedUser) {
		List<E> result = new LinkedList<E>();
		if (CollectionHelper.notEmpty(seqs)) {
			for (Serializable seq : seqs) {
				if (seq != null) {
					E entity = delete(entityClass, seq, modifiedUser);
					if (entity != null) {
						result.add(entity);
					}
				}
			}
		}
		return result;
	}

	/**
	 * orm計算筆數
	 * 
	 * @param entityClass
	 * @return
	 */
	// select count(entity) from entity
	public long rowCount(Class<?> entityClass) {
		long result = 0L;
		if (entityClass != null) {
			try {
				final StringBuffer hql = new StringBuffer();
				hql.append("SELECT COUNT(1) FROM ");
				hql.append(entityClass.getName());
				hql.append(" ENTITY");
				// System.out.println(hql);
				Long buff = hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
					public Long doInHibernate(Session session) throws HibernateException {
						Query query = session.createQuery(hql.toString());
						cacheQuery(query);
						return (Long) query.uniqueResult();
					}
				});
				result = safeGet(buff);
			} catch (Exception ex) {
				throw new CommonDaoException(ex);
			}
		}
		return result;
	}

	protected int executeByHql(StringBuilder hql, Map<String, Object> params) {
		return executeByHql(hql.toString(), params);
	}

	/**
	 * 2012/05/31
	 * 
	 * 執行hql
	 * 
	 * @param hqlString
	 * @param params
	 * @return
	 */
	protected int executeByHql(final String hqlString, final Map<String, Object> params) {
		int result = 0;
		try {
			Integer buff = getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
				public Integer doInHibernate(Session session) throws HibernateException {
					Query query = session.createQuery(hqlString);
					cacheQuery(query);
					processQueryParamters(query, params);
					return query.executeUpdate();
				}
			});
			//
			result = safeGet(buff);
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// --------------------------------------------------
	// implement JdbcDao
	// --------------------------------------------------
	// sql find(select)
	// --------------------------------------------------
	// select id from adm_user where id = ?
	// select id from adm_user where id = :id
	// 傳回 list(Object[]) or one column
	// --------------------------------------------------

	/**
	 * sql查詢多筆資料
	 * 
	 * @param sqlString
	 * @param paramNames
	 * @param values
	 * @param columnAliases
	 * @param types
	 */
	public <E> List<E> find(String sqlString, String[] paramNames, Object[] values, String[] columnAliases,
			Object[] types) {
		return findBySql(sqlString, paramNames, values, columnAliases, types, null);
	}

	/**
	 * sql查詢多筆資料
	 * 
	 * @param sqlString
	 * @param paramNames
	 * @param values
	 * @param columnAliases
	 * @param types
	 * @param top
	 */
	protected <E> List<E> findBySql(final String sqlString, final String[] paramNames, final Object[] values,
			final String[] columnAliases, final Object[] types, final Integer top) {
		List<E> result = new LinkedList<E>();
		try {
			result = (List<E>) getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<E>>() {
				@SuppressWarnings("unchecked")
				public List<E> doInHibernate(Session session) throws HibernateException {
					SQLQuery query = session.createSQLQuery(sqlString);
					cacheQuery(query);
					processQueryParamters(query, paramNames, values);
					processSQLQueryScalar(query, columnAliases, types);
					if (top != null) {
						query.setMaxResults(top);
					}
					return query.list();
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	protected Object[] findUniqueBySql(final String sqlString, final String[] paramNames, final Object[] values,
			final String[] columnAliases, final Object[] types) {
		Object[] result = null;
		try {
			result = getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object[]>() {
				public Object[] doInHibernate(Session session) throws HibernateException {
					SQLQuery query = session.createSQLQuery(sqlString);
					cacheQuery(query);
					processQueryParamters(query, paramNames, values);
					processSQLQueryScalar(query, columnAliases, types);
					query.setMaxResults(1);
					return (Object[]) query.uniqueResult();
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// ------------------------------------
	// sql find(select)
	// ------------------------------------
	/**
	 * sql查詢多筆資料
	 * 
	 * sql find use map to set scalars
	 * 
	 * @param sqlString
	 * @param paramNames
	 * @param values
	 * @param scalars
	 */
	public <E> List<E> find(String sqlString, String[] paramNames, Object[] values, Map<String, Object> scalars) {
		return findBySql(sqlString, paramNames, values, scalars, null);
	}

	/**
	 * sql查詢多筆資料
	 * 
	 * sql find use map to set scalars
	 * 
	 * @param sqlString
	 * @param paramNames
	 * @param values
	 * @param scalars
	 */
	public <E> List<E> find(String sqlString, Map<String, Object> params, Map<String, Object> scalars) {
		return findBySql(sqlString, params, scalars, null);
	}

	// select id from adm_user where id = ?
	// select id from adm_user where id = :id
	// 傳回 list(Object[]) or one column
	protected <E> List<E> findBySql(final String sqlString, final String[] paramNames, final Object[] values,
			final Map<String, Object> scalars, final Integer top) {
		List<E> result = new LinkedList<E>();
		try {
			result = (List<E>) getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<E>>() {
				@SuppressWarnings("unchecked")
				public List<E> doInHibernate(Session session) throws HibernateException {
					SQLQuery query = session.createSQLQuery(sqlString);
					cacheQuery(query);
					processQueryParamters(query, paramNames, values);
					processSQLQueryScalar(query, scalars);
					if (top != null) {
						query.setMaxResults(top);
					}
					return query.list();
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	protected Object[] findUniqueBySql(final String sqlString, final String[] paramNames, final Object[] values,
			final Map<String, Object> scalars) {
		Object[] result = null;
		try {
			result = getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object[]>() {
				public Object[] doInHibernate(Session session) throws HibernateException {
					SQLQuery query = session.createSQLQuery(sqlString);
					cacheQuery(query);
					processQueryParamters(query, paramNames, values);
					processSQLQueryScalar(query, scalars);
					query.setMaxResults(1);
					return (Object[]) query.uniqueResult();
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// --------------------------------------------------
	protected <E> List<E> findBySql(String sqlString, Map<String, Object> params, Map<String, Object> scalars) {
		return findBySql(sqlString, params, scalars, null);
	}

	// select id from adm_user where id = ?
	// select id from adm_user where id = :id
	// 傳回 list(Object[]) or one column
	@SuppressWarnings("unchecked")
	protected <E> List<E> findBySql(final String sqlString, final Map<String, Object> params,
			final Map<String, Object> scalars, final Integer top) {
		List<E> result = new LinkedList<E>();
		try {
			result = (List<E>) getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<E>>() {
				public List<E> doInHibernate(Session session) throws HibernateException {
					SQLQuery query = session.createSQLQuery(sqlString);
					cacheQuery(query);
					processQueryParamters(query, params);
					processSQLQueryScalar(query, scalars);
					if (top != null) {
						query.setMaxResults(top);
					}
					return query.list();
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// 只有一個column
	protected List<Object> findObjectBySql(final String sqlString, final Map<String, Object> params,
			final Map<String, Object> scalars) {
		return findObjectBySql(sqlString, params, scalars, null);
	}

	// 只有一個column
	@SuppressWarnings({ "unchecked" })
	protected List<Object> findObjectBySql(final String sqlString, final Map<String, Object> params,
			final Map<String, Object> scalars, final Integer top) {
		List<Object> result = new LinkedList<Object>();
		try {
			result = (List<Object>) getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<?>>() {
				public List<?> doInHibernate(Session session) throws HibernateException {
					SQLQuery query = session.createSQLQuery(sqlString);
					cacheQuery(query);
					processQueryParamters(query, params);
					processSQLQueryScalar(query, scalars);
					if (top != null) {
						query.setMaxResults(top);
					}
					return query.list();
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	protected Object[] findUniqueBySql(final String sqlString, final Map<String, Object> params,
			final Map<String, Object> scalars) {
		Object[] result = null;
		try {
			result = getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object[]>() {
				public Object[] doInHibernate(Session session) throws HibernateException {
					SQLQuery query = session.createSQLQuery(sqlString);
					cacheQuery(query);
					processQueryParamters(query, params);
					processSQLQueryScalar(query, scalars);
					query.setMaxResults(1);
					return (Object[]) query.uniqueResult();
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// --------------------------------------------------
	// sql insert
	// insert, update ,delete 都一樣使用 sqlQuery.executeUpdate()
	// 若直接使用jdbc 新增修改刪除,將會失去orm特性
	// 1.version 無法自動遞增
	// --------------------------------------------------
	public int insert(String sqlString, String[] paramNames, Object[] values) {
		return insert(sqlString, paramNames, values, null);
	}

	public int insert(String sqlString, String[] paramNames, Object[] values, String modifiedUser) {
		return executeBySql(sqlString, paramNames, values, modifiedUser);
	}

	public int insert(String sqlString, Map<String, Object> params) {
		return insert(sqlString, params, null);
	}

	public int insert(String sqlString, Map<String, Object> params, String modifiedUser) {
		return executeBySql(sqlString, params, modifiedUser);
	}

	// --------------------------------------------------
	// sql update
	// --------------------------------------------------
	public int update(String sqlString, String[] paramNames, Object[] values) {
		return update(sqlString, paramNames, values, null);
	}

	public int update(String sqlString, String[] paramNames, Object[] values, String modifiedUser) {
		return executeBySql(sqlString, paramNames, values, modifiedUser);
	}

	public int update(String sqlString, Map<String, Object> params) {
		return update(sqlString, params, null);
	}

	public int update(String sqlString, Map<String, Object> params, String modifiedUser) {
		return executeBySql(sqlString, params, modifiedUser);
	}

	// --------------------------------------------------
	// sql delete
	// --------------------------------------------------
	public int delete(String sqlString, String[] paramNames, Object[] values) {
		return delete(sqlString, paramNames, values, null);
	}

	public int delete(String sqlString, String[] paramNames, Object[] values, String modifiedUser) {
		return executeBySql(sqlString, paramNames, values, modifiedUser);
	}

	public int delete(String sqlString, Map<String, Object> params) {
		return delete(sqlString, params, null);
	}

	public int delete(String sqlString, Map<String, Object> params, String modifiedUser) {
		return executeBySql(sqlString, params, modifiedUser);
	}

	protected int executeBySql(final String sqlString, final String[] paramNames, final Object[] values,
			String modifiedUser) {
		int result = 0;
		try {
			Integer buff = getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
				public Integer doInHibernate(Session session) throws HibernateException {
					SQLQuery query = session.createSQLQuery(sqlString);
					cacheQuery(query);
					processQueryParamters(query, paramNames, values);
					return query.executeUpdate();
				}
			});
			//
			result = safeGet(buff);
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	protected int executeBySql(final String sqlString, final Map<String, Object> params, String modifiedUser) {
		int result = 0;
		try {
			Integer buff = getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
				public Integer doInHibernate(Session session) throws HibernateException {
					SQLQuery query = session.createSQLQuery(sqlString);
					cacheQuery(query);
					processQueryParamters(query, params);
					return query.executeUpdate();
				}
			});
			//
			result = safeGet(buff);
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// --------------------------------------------------
	// query
	// from UserImpl user where user.id=?
	// from UserImpl user where user.id=:id
	// public Query createQuery(String hqlString) {
	// Query query = getSession().createQuery(hqlString);
	// cacheQuery(query);
	// return query;
	// }
	//
	// public Query createQuery(StringBuffer hql) {
	// return createQuery(hql.toString());
	// }
	//
	// // SQL query
	// public SQLQuery createSQLQuery(String sqlString) {
	// SQLQuery sqlQuery = getSession().createSQLQuery(sqlString);
	// cacheQuery(sqlQuery);
	// return sqlQuery;
	// }
	//
	// public Query createSQLQuery(StringBuffer sql) {
	// return createSQLQuery(sql.toString());
	// }
	//
	// // criteria
	// public Criteria createCriteria(String hqlString) {
	// Criteria criteria = getSession().createCriteria(hqlString);
	// cacheCriteria(criteria);
	// return criteria;
	// }
	//
	// public Criteria createCriteria(StringBuffer hql) {
	// return createCriteria(hql.toString());
	// }

	// cache query
	// getHibernateTemplate().isCacheQueries()=false
	// getHibernateTemplate().getQueryCacheRegion()=null
	// getHibernateTemplate().getFetchSize()=0
	// getHibernateTemplate().getMaxResults()=0
	protected void cacheQuery(Query query) {
		// query.setCacheable(true); //不使用,會造成第2次sql不一致
		// query.setCacheRegion(getClass().getName());

		if (getHibernateTemplate().getFetchSize() > 0) {
			query.setFetchSize(getHibernateTemplate().getFetchSize());
		}
		if (getHibernateTemplate().getMaxResults() > 0) {
			query.setMaxResults(getHibernateTemplate().getMaxResults());
		}
		// SessionFactoryUtils.applyTransactionTimeout(query,
		// getHibernateTemplate().getSessionFactory());
	}

	// cache criteria
	protected void cacheCriteria(Criteria criteria) {
		// criteria.setCacheable(true);//不使用,會造成第2次sql不一致
		// criteria.setCacheRegion(getClass().getName());

		if (getHibernateTemplate().getFetchSize() > 0) {
			criteria.setFetchSize(getHibernateTemplate().getFetchSize());
		}
		if (getHibernateTemplate().getMaxResults() > 0) {
			criteria.setMaxResults(getHibernateTemplate().getMaxResults());
		}
		// SessionFactoryUtils.applyTransactionTimeout(criteria,
		// getHibernateTemplate().getSessionFactory());
	}

	public FullTextQuery createFullTextQuery(Session session, org.apache.lucene.search.Query luceneQuery,
			Class<?>... entities) {
		FullTextQuery fullTextQuery = getFullTextSession(session).createFullTextQuery(luceneQuery, entities);
		return fullTextQuery;
	}

	// select * from user where user.id = ? and user.code = ?
	protected void processQueryParamters(Query query, Object[] values) {
		// paramNames=null
		processQueryParamters(query, null, values);
	}

	// select * from user where user.id = :id and user.code = :code
	@SuppressWarnings("rawtypes")
	protected void processQueryParamters(Query query, String[] paramNames, Object[] values) {
		if (values == null || values.length == 0) {
			return;
		}
		//
		for (int i = 0; i < values.length; i++) {
			Object value = values[i];
			if (paramNames != null && paramNames.length > 0) {
				// log.info("query.setParameter: " + paramNames[i] + " = "
				// + param);
				String name = paramNames[i];
				if (value instanceof List) {
					query.setParameterList(name, (List) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(name, (Object[]) value);
				} else {
					query.setParameter(name, value); // 名稱
				}
			} else {
				// log.info("query.setParameter: " + values[i]);
				query.setParameter(i, value); // 位置
			}
		}

	}

	@SuppressWarnings("rawtypes")
	protected void processQueryParamters(Query query, Map<String, Object> params) {
		if (params == null || params.size() == 0) {
			return;
		}
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String name = entry.getKey();
			Object value = entry.getValue();
			// System.out.println("name: " + name + " " + value);
			if (value instanceof List) {
				query.setParameterList(name, (List) value);
			} else if (value instanceof Object[]) {
				query.setParameterList(name, (Object[]) value);
			} else {
				query.setParameter(name, value); // 名稱
			}
		}
	}

	// use by sqlQuery
	protected void processSQLQueryScalar(SQLQuery sqlQuery, String[] columnAliases, Object[] types) {
		if (columnAliases == null || columnAliases.length == 0 || !(types instanceof Type[] || types.length == 0)
				|| columnAliases.length != types.length) {
			throw new RuntimeException("columnAliases=" + trace(columnAliases) + " ,types=" + trace(types));

		}
		//
		for (int i = 0; i < columnAliases.length; i++) {
			Type type = (Type) types[i];
			sqlQuery.addScalar(columnAliases[i], type);
		}
	}

	protected void processSQLQueryScalar(SQLQuery sqlQuery, Map<String, Object> scalars) {
		if (scalars == null || scalars.size() == 0) {
			throw new RuntimeException("columnAliases and types are null!!!");
		}
		//
		for (Map.Entry<String, Object> entry : scalars.entrySet()) {
			Object value = entry.getValue();
			if (!(value instanceof Type)) {
				throw new RuntimeException("value not instaoceof Hibernate.Type");
			}
			sqlQuery.addScalar(entry.getKey(), (Type) value);
		}
	}

	public String trace(Object[] objects) {
		StringBuffer sb = new StringBuffer();
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				sb.append("[" + i + "]");
				if (objects[i] instanceof Type) {
					Type type = (Type) objects[i];
					sb.append(type.getName());
				} else {
					sb.append(objects[i] != null ? objects[i].toString() : "null");
				}
				if (i < objects.length - 1) {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}

	public List<Object[]> convertToList(ResultSet resultSet) {
		List<Object[]> list = new LinkedList<Object[]>();
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int numcols = rsmd.getColumnCount();

			while (resultSet.next()) {
				Object[] data = new Object[numcols];
				for (int j = 0; j < numcols; j++) {
					data[j] = resultSet.getObject(j + 1);
				}
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	protected FullTextSession getFullTextSession(Session session) {
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		return fullTextSession;
	}

	protected Highlighter createHighlighter(org.apache.lucene.search.Query luceneQuery) {
		SimpleHTMLFormatter format = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
		Highlighter highlighter = new Highlighter(format, new QueryScorer(luceneQuery));// 高亮
		// highlighter.setTextFragmenter(new
		// SimpleFragmenter(Integer.MAX_VALUE));
		highlighter.setTextFragmenter(new SimpleFragmenter(200));
		return highlighter;
	}

	/**
	 * 全文檢索重建索引, 2011/10/12
	 * 
	 * @param entityClass
	 * @return
	 */
	public boolean reindex(final Class<?> entityClass) {
		boolean result = false;
		try {
			result = getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Boolean>() {
				public Boolean doInHibernate(Session session) throws HibernateException {
					return reindex(session, entityClass);
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	protected boolean reindex(Session session, Class<?> entityClass) {
		boolean result = false;
		try {
			LOGGER.info("reindex: [" + entityClass.getSimpleName() + "]");
			FullTextSession fullTextSession = getFullTextSession(session);
			MassIndexer massIndexer = fullTextSession.createIndexer(entityClass);
			massIndexer.startAndWait();
			result = true;
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	/**
	 * 全文檢索重建索引, 2011/10/12
	 * 
	 * @param entityClass
	 * @return
	 */
	public <T> boolean reindex(final T entity) {
		boolean result = false;
		try {
			result = getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Boolean>() {
				public Boolean doInHibernate(Session session) throws HibernateException {
					return reindex(session, entity);
				}
			});
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	protected <T> boolean reindex(Session session, T entity) {
		boolean result = false;
		try {
			FullTextSession fullTextSession = getFullTextSession(session);
			fullTextSession.index(entity);
			result = true;
		} catch (Exception ex) {
			throw new CommonDaoException(ex);
		}
		return result;
	}

	// ----------------------------------------------------------------
	// safeGet 只是為了簡化寫法
	// ----------------------------------------------------------------
	// 因BaseModelSupporter已有, 以下可以註解掉, 201/11/14
	// public boolean safeGet(Boolean value) {
	// return BooleanHelper.safeGet(value);
	// }
	//
	// public char safeGet(Character value) {
	// return CharHelper.safeGet(value);
	// }
	//
	// public String safeGet(String value) {
	// return StringHelper.safeGet(value);
	// }
	//
	// public byte safeGet(Byte value) {
	// return NumberHelper.safeGet(value);
	// }
	//
	// public short safeGet(Short value) {
	// return NumberHelper.safeGet(value);
	// }
	//
	// public int safeGet(Integer value) {
	// return NumberHelper.safeGet(value);
	// }
	//
	// public long safeGet(Long value) {
	// return NumberHelper.safeGet(value);
	// }
	//
	// public float safeGet(Float value) {
	// return NumberHelper.safeGet(value);
	// }
	//
	// public double safeGet(Double value) {
	// return NumberHelper.safeGet(value);
	// }
	//
	// // ----------------------------------------------------------------
	// // 列舉safeGet 只是為了簡化寫法
	// // ----------------------------------------------------------------
	// public byte safeGet(ByteEnum value) {
	// return EnumHelper.safeGet(value);
	// }
	//
	// public short safeGet(ShortEnum value) {
	// return EnumHelper.safeGet(value);
	// }
	//
	// public int safeGet(IntEnum value) {
	// return EnumHelper.safeGet(value);
	// }
	//
	// public long safeGet(LongEnum value) {
	// return EnumHelper.safeGet(value);
	// }
	//
	// public float safeGet(FloatEnum value) {
	// return EnumHelper.safeGet(value);
	// }
	//
	// public double safeGet(DoubleEnum value) {
	// return EnumHelper.safeGet(value);
	// }

}