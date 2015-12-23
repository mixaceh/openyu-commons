package org.openyu.commons.service.supporter;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.dao.CommonDao;
import org.openyu.commons.dao.anno.CommonTx;
import org.openyu.commons.dao.aware.CommonDaoAware;
import org.openyu.commons.entity.SeqEntity;
import org.openyu.commons.lang.ClassHelper;
import org.openyu.commons.lang.event.EventAttach;
import org.openyu.commons.lang.event.EventCaster;
import org.openyu.commons.lang.event.EventHelper;
import org.openyu.commons.service.CommonService;
import org.openyu.commons.service.event.BeanEvent;
import org.openyu.commons.service.event.BeanListener;
import org.openyu.commons.service.ex.CommonServiceException;
import org.openyu.commons.util.CollectionHelper;
import org.openyu.commons.util.concurrent.MapCache;
import org.openyu.commons.util.concurrent.impl.MapCacheImpl;

/**
 * 使用@CommnoTx
 * 
 * listener,event
 * 
 * insert(): db->memory->message
 *
 * find() : db->memory->message
 *
 * update(): memory->db->message
 *
 * delete(): memory->db->message
 *
 * initialize():loadAll form db->sync to memory
 * 
 * fireServiceBeanAdding, 表新增前/查詢前/修改前/刪除前,可用於:
 * 
 * fireServiceBeanAdded, 表新增後/修改後/刪除後,可用於: 1.發送訊息通知
 */
public class CommonServiceSupporter extends BaseServiceSupporter implements CommonService, CommonDaoAware {

	private static final long serialVersionUID = 1915658408145401655L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(CommonServiceSupporter.class);

	protected transient CommonDao commonDao;

	// service建構除構用
	// private transient EventCaster serviceListeners;

	// insert,update,delete用
	private transient EventCaster beanListeners;

	/**
	 * 存放bean快取,當多緒時,物件需注意資料同步
	 * 
	 * <id,bean>
	 */
	protected MapCache<String, Object> beans = new MapCacheImpl<String, Object>();

	public CommonServiceSupporter() {
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {
		beans.clear();
	}

	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public MapCache<String, Object> getBeans() {
		return beans;
	}

	// ------------------------------------------------------------
	// 事件:當crud時,都會觸發
	// ------------------------------------------------------------
	// just for IOC
	public void setBeanListener(BeanListener beanListener) {
		addBeanListener(beanListener);
	}

	public void addBeanListener(BeanListener beanListener) {
		beanListeners = EventCaster.add(beanListeners, beanListener);
	}

	public void removeBeanListener(BeanListener beanListener) {
		beanListeners = EventCaster.remove(beanListeners, beanListener);
	}

	public BeanListener[] getBeanListeners() {
		BeanListener[] result = null;
		if (beanListeners != null) {
			result = (BeanListener[]) beanListeners.getListeners(BeanListener.class);
		}
		return result;
	}

	/**
	 * 觸發事件
	 * 
	 * 傳入vo/po,voEntity!=null,則vo,voEntity==null,則po
	 * 
	 * @param source
	 *            指的是service
	 * @param type
	 * @param attach
	 *            指的是EventAttach,觸發事件時,傳入改變的新舊值或其它可自定義的值
	 */
	protected void fireBeanEvent(Object source, int type, Object attach) {
		if (beanListeners != null) {
			BeanEvent beanEvent = new BeanEvent(source, type, attach);
			beanListeners.dispatch(beanEvent);
		}
	}

	protected void fireBeanInserting(Object source) {
		fireBeanInserting(source, null);
	}

	protected void fireBeanInserting(Object source, Object attach) {
		fireBeanEvent(source, BeanEvent.INSERTING, attach);
	}

	protected void fireBeanInserted(Object source) {
		fireBeanInserted(source, null);
	}

	protected void fireBeanInserted(Object source, Object attach) {
		fireBeanEvent(source, BeanEvent.INSERTED, attach);
	}

	//
	protected void fireBeanFinding(Object source) {
		fireBeanFinding(source, null);
	}

	protected void fireBeanFinding(Object source, Object attach) {
		fireBeanEvent(source, BeanEvent.FINDTING, attach);
	}

	//
	protected void fireBeanFound(Object source) {
		fireBeanFound(source, null);
	}

	protected void fireBeanFound(Object source, Object attach) {
		fireBeanEvent(source, BeanEvent.FOUND, attach);
	}

	//
	protected void fireBeanUpdating(Object source) {
		fireBeanUpdating(source, null);
	}

	protected void fireBeanUpdating(Object source, Object attach) {
		fireBeanEvent(source, BeanEvent.UPDATING, attach);
	}

	protected void fireBeanUpdated(Object source) {
		fireBeanUpdated(source, null);
	}

	protected void fireBeanUpdated(Object source, Object attach) {
		fireBeanEvent(source, BeanEvent.UPDATED, attach);
	}

	//
	protected void fireBeanDeleting(Object source) {
		fireBeanDeleting(source, null);
	}

	protected void fireBeanDeleting(Object source, Object attach) {
		fireBeanEvent(source, BeanEvent.DELETING, attach);
	}

	protected void fireBeanDeleted(Object source) {
		fireBeanDeleted(source, null);
	}

	protected void fireBeanDeleted(Object source, Object attach) {
		fireBeanEvent(source, BeanEvent.DELETED, attach);
	}

	// ------------------------------------------------------------
	// 1.可傳入 vo class
	// 2.可傳入 po class
	public <E> List<E> find(Class<?> entityClass) {
		// 預設轉成vo
		return findReturnVoOrNot(entityClass, true);
	}

	protected <E> List<E> findReturnVoOrNot(Class<?> entityClass, boolean is2Vo) {
		// return commonDao.find(entityClass);

		List<E> dest = new LinkedList<E>();
		try {
			boolean isVoClass = ClassHelper.isVoClass(entityClass);
			// #1 entityClass=voClass, 傳入 vo class
			if (isVoClass) {
				Class<?> poClass = ClassHelper.vo2PoClass(entityClass);
				if (poClass != null) {
					EventAttach<List<E>, List<E>> eventAttach = EventHelper.eventAttach(null, null);
					//
					fireBeanFinding(this, eventAttach);
					List<E> orig = commonDao.find(poClass);
					if (orig != null && !orig.isEmpty()) {
						if (is2Vo) {
							dest = ClassHelper.copyProperties(orig);
						} else {
							dest = orig;
						}
					}
					eventAttach = EventHelper.eventAttach(null, dest);
					fireBeanFound(this, eventAttach);
				}
			}
			// #2 entityClass=poClass,傳入 po class
			else {
				EventAttach<List<E>, List<E>> eventAttach = EventHelper.eventAttach(null, null);
				fireBeanFinding(this, eventAttach);
				List<E> orig = commonDao.find(entityClass);
				if (orig != null && !orig.isEmpty()) {
					// 是否要轉成is2vo=true,則轉成vo
					if (is2Vo) {
						dest = ClassHelper.copyProperties(orig);
					}
					// 若無,則直接傳回po
					else {
						dest = orig;
					}
				}
				eventAttach = EventHelper.eventAttach(null, dest);
				fireBeanFound(this, eventAttach);
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return dest;
	}

	// 1.可傳入 vo class
	// 2.可傳入 po class
	//
	// 原傳回 po: org.openyu.adm.authz.user.po.impl.UserPoImpl
	// 多新增 vo: org.openyu.adm.authz.user.vo.impl.UserImpl
	public <T> T find(Class<?> entityClass, Serializable seq) {
		// 預設轉成vo
		return findReturnVoOrNot(entityClass, seq, true);
	}

	public <E> List<E> find(Class<?> entityClass, Collection<Serializable> seqs) {
		List<E> list = new LinkedList<E>();
		try {
			if (CollectionHelper.notEmpty(seqs)) {
				for (Serializable seq : seqs) {
					E entity = find(entityClass, seq);
					if (entity != null) {
						list.add(entity);
					}
				}
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return list;
	}

	// is2Vo=是否要將找到的po轉為vo
	protected <T> T findReturnVoOrNot(Class<?> entityClass, Serializable seq, boolean is2Vo) {
		// return commonDao.find(entityClass, seq);
		T dest = null;
		try {
			boolean isVoClass = ClassHelper.isVoClass(entityClass);
			// #1 entityClass=voClass, 傳入 vo class
			if (isVoClass) {
				Class<?> poClass = ClassHelper.vo2PoClass(entityClass);
				if (poClass != null) {
					EventAttach<T, T> eventAttach = EventHelper.eventAttach(null, null);
					fireBeanFinding(this, eventAttach);
					T orig = commonDao.find(poClass, seq);
					if (orig != null) {
						// 是否要轉為vo
						if (is2Vo) {
							dest = ClassHelper.copyProperties(orig, entityClass);// po->vo
						} else {
							dest = orig;
						}
					}
					eventAttach = EventHelper.eventAttach(null, dest);
					fireBeanFound(this, eventAttach);
				}
			}
			// #2 entityClass=poClass,傳入 po class
			else {
				EventAttach<T, T> eventAttach = EventHelper.eventAttach(null, null);
				fireBeanFinding(this, eventAttach);
				T orig = commonDao.find(entityClass, seq);// po
				if (orig != null) {
					Class<?> voClass = ClassHelper.po2VoClass(entityClass);
					// 若有相對應的vo,及是否要轉成is2vo=true,則轉成vo
					if (voClass != null && is2Vo) {
						dest = ClassHelper.copyProperties(orig, voClass);// po->vo
					}
					// 若無,則直接傳回po
					else {
						dest = orig;
					}
				}
				eventAttach = EventHelper.eventAttach(null, dest);
				fireBeanFound(this, eventAttach);
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return dest;
	}

	// 1.可傳入 vo class
	// 2.可傳入 po class
	public <E> List<E> find(Class<?> entityClass, List<Serializable> seqs) {
		List<E> dest = new LinkedList<E>();
		try {
			if (seqs != null) {
				for (Serializable seq : seqs) {
					E entity = find(entityClass, seq);
					if (entity != null) {
						dest.add(entity);
					}
				}
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return dest;
	}

	@CommonTx
	public <T> Serializable insert(T entity) {
		return insert(entity, null);
	}

	// 1.可傳入 vo class
	// 2.可傳入 po class
	@CommonTx
	@SuppressWarnings("unchecked")
	public <T> Serializable insert(T entity, String modifiedUser) {
		Serializable result = null;
		try {
			if (entity != null) {
				Class<?> entityClass = entity.getClass();
				Object poEntity = null;// po
				Object voEntity = null;// vo
				boolean isVoClass = ClassHelper.isVoClass(entityClass);
				// #1 entityClass=voClass, 傳入 vo class
				if (isVoClass) {
					// po
					poEntity = ClassHelper.copyProperties(entity);
					// vo
					voEntity = entity;
				}
				// #2 entityClass=poClass,傳入 po class
				else {
					// po
					poEntity = entity;
					// vo
					voEntity = ClassHelper.copyProperties(entity);
				}
				//
				if (poEntity != null) {
					// 觸發事件傳入vo/po,voEntity!=null,則vo,voEntity==null,則po
					EventAttach<T, T> eventAttach = EventHelper.eventAttach(null,
							(voEntity != null ? (T) voEntity : (T) poEntity));
					fireBeanInserting(this, eventAttach);
					result = commonDao.insert(poEntity, modifiedUser);
					// System.out.println("poEntity: "+poEntity);
					if (result != null) {
						// 因insert到db,有些欄位是寫入db時才會有值
						// 所以將po再塞回到vo
						// #issue,當集合時,會重複添加,原本+新的
						if (isVoClass) {
							// System.out.println("poEntity: " + poEntity);
							voEntity = ClassHelper.copyProperties(poEntity, voEntity);
							// System.out.println("entity: " + entity);
						}
						eventAttach = EventHelper.eventAttach(null, (voEntity != null ? (T) voEntity : (T) poEntity));
						fireBeanInserted(this, eventAttach);
					}
				}
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return result;
	}

	@CommonTx
	public <T> int update(T entity) {
		return update(entity, null);
	}

	// 1.可傳入 vo class
	// 2.可傳入 po class
	@SuppressWarnings("unchecked")
	@CommonTx
	public <T> int update(T entity, String modifiedUser) {
		int ret = 0;
		try {
			if (entity != null) {
				Class<?> entityClass = entity.getClass();
				Object poEntity = null;// po
				Object voEntity = null;// vo
				boolean isVoClass = ClassHelper.isVoClass(entityClass);

				// #1 entityClass=voClass, 傳入 vo class
				if (isVoClass) {
					// po
					poEntity = ClassHelper.copyProperties(entity);
					// vo
					voEntity = entity;
				}
				// #2 entityClass=poClass,傳入 po class
				else {
					// po
					poEntity = entity;
					// vo
					voEntity = ClassHelper.copyProperties(entity);
				}
				//
				if (poEntity != null) {
					// 觸發事件傳入vo/po,voEntity!=null,則vo,voEntity==null,則po
					EventAttach<T, T> eventAttach = EventHelper.eventAttach(null,
							(voEntity != null ? (T) voEntity : (T) poEntity));
					fireBeanUpdating(this, eventAttach);
					// System.out.println("before version: "
					// + ((SeqEntity) poEntity).getVersion());
					ret = commonDao.update(poEntity, modifiedUser);
					// System.out.println("after version: "
					// + ((SeqEntity) poEntity).getVersion());
					if (ret > 0) {
						// 因update到db,有些欄位是寫入db時才會有值
						// 所以將po再塞回到vo
						if (isVoClass) {
							// #issue version沒+1,怪了
							// 原因還沒commit所以version沒+1,不是這個原因
							//
							// #fix 手動version+1
							// 2014/09/30
							if (poEntity instanceof SeqEntity) {
								SeqEntity idEntity = (SeqEntity) poEntity;
								idEntity.setVersion(idEntity.getVersion() + 1);
							}

							// #fix version已有加1,不要再加了
							// 2014/09/30又沒加1,所以又改成手動+1

							voEntity = ClassHelper.copyProperties(poEntity, voEntity);
							// System.out.println("copy version: "
							// + ((SeqBean) voEntity).getVersion());
						}
						eventAttach = EventHelper.eventAttach(null, (voEntity != null ? (T) voEntity : (T) poEntity));
						fireBeanUpdated(this, eventAttach);
					}
				}
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return ret;
	}

	@CommonTx
	public <T> int delete(T entity) {
		return delete(entity, null);
	}

	/**
	 * 會觸發事件
	 */
	@CommonTx
	@SuppressWarnings("unchecked")
	public <T> int delete(T entity, String modifiedUser) {
		int ret = 0;
		try {
			if (entity != null) {
				Class<?> entityClass = entity.getClass();
				Object poEntity = null;// po
				Object voEntity = null;// vo
				boolean isVoClass = ClassHelper.isVoClass(entityClass);
				// #1 entityClass=voClass, 傳入 vo class
				if (isVoClass) {
					Class<?> poClass = ClassHelper.vo2PoClass(entityClass);
					if (poClass != null) {
						// po
						poEntity = ClassHelper.copyProperties(entity);
						// vo
						voEntity = entity;
					}
				}
				// #2 entityClass=poClass,傳入 po class
				else {
					// po
					poEntity = entity;
					// vo
					voEntity = ClassHelper.copyProperties(entity);
				}
				// System.out.println(poEntity);
				//
				if (poEntity != null) {
					// 觸發事件傳入vo/po,voEntity!=null,則vo,voEntity==null,則po
					EventAttach<T, T> eventAttach = EventHelper.eventAttach(null,
							(voEntity != null ? (T) voEntity : (T) poEntity));
					fireBeanDeleting(this, eventAttach);
					// if (isVoClass)
					// {
					//
					// }
					// org.hibernate.NonUniqueObjectException
					ret = commonDao.delete(poEntity, modifiedUser);// po
					if (ret > 0) {
						eventAttach = EventHelper.eventAttach(null, (voEntity != null ? (T) voEntity : (T) poEntity));
						fireBeanDeleted(this, eventAttach);
					}
				}
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return ret;
	}

	@CommonTx
	public <T> T delete(Class<?> entityClass, Serializable seq) {
		return delete(entityClass, seq, null);
	}

	@CommonTx
	@SuppressWarnings("unchecked")
	public <T> T delete(Class<?> entityClass, Serializable seq, String modifiedUser) {
		T result = null;
		// 此時為po
		Object entity = findReturnVoOrNot(entityClass, seq, false);

		int del = delete(entity, modifiedUser);
		if (del > 0) {
			boolean isVoClass = ClassHelper.isVoClass(entityClass);
			if (isVoClass) {
				result = ClassHelper.copyProperties(entity, entityClass);// po->vo
			} else {
				result = (T) entity;
			}
		}
		return result;
	}

	@CommonTx
	public <E> List<E> delete(Class<?> entityClass, Collection<Serializable> seqs) {
		return delete(entityClass, seqs, null);
	}

	@CommonTx
	public <E> List<E> delete(Class<?> entityClass, Collection<Serializable> seqs, String modifiedUser) {
		List<E> result = new LinkedList<E>();
		if (CollectionHelper.notEmpty(seqs)) {
			for (Serializable seq : seqs) {
				if (seq != null) {
					E ret = delete(entityClass, seq, modifiedUser);
					if (ret != null) {
						result.add(ret);
					}
				}
			}
		}
		return result;
	}

	public long rowCount(Class<?> entityClass) {
		long result = 0L;
		try {
			boolean isVoClass = ClassHelper.isVoClass(entityClass);
			if (isVoClass) {
				Class<?> poClass = ClassHelper.vo2PoClass(entityClass);
				if (poClass != null) {
					result = commonDao.rowCount(poClass);
				}
			} else {
				result = commonDao.rowCount(entityClass);
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return result;
	}

	// ------------------------------------
	// sql find(select)
	// ------------------------------------
	public <E> List<E> find(String sqlString, String[] paramNames, Object[] values, String[] columnAliases,
			Object[] types) {
		return commonDao.find(sqlString, paramNames, values, columnAliases, types);
	}

	public <E> List<E> find(String sqlString, String[] paramNames, Object[] values, Map<String, Object> scalars) {
		return commonDao.find(sqlString, paramNames, values, scalars);
	}

	public <E> List<E> find(String sqlString, Map<String, Object> params, Map<String, Object> scalars) {
		return commonDao.find(sqlString, params, scalars);
	}

	// ------------------------------------
	// sql insert
	// ------------------------------------
	@CommonTx
	public int insert(String sqlString, String[] paramNames, Object[] values) {
		return insert(sqlString, paramNames, values, null);
	}

	@CommonTx
	public int insert(String sqlString, String[] paramNames, Object[] values, String modifiedUser) {
		int result = 0;
		try {
			Map<String, Object> params = CollectionHelper.toMap(paramNames, values);
			fireBeanInserting(params, this);
			result = commonDao.insert(sqlString, paramNames, values, modifiedUser);
			if (result > 0) {
				fireBeanInserted(params, this);
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return result;
	}

	@CommonTx
	public int insert(String sqlString, Map<String, Object> params) {
		return insert(sqlString, params, null);
	}

	@CommonTx
	public int insert(String sqlString, Map<String, Object> params, String modifiedUser) {
		int result = 0;
		try {
			fireBeanInserting(params, this);
			result = commonDao.insert(sqlString, params, modifiedUser);
			if (result > 0) {
				fireBeanInserted(params, this);
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return result;
	}

	// ------------------------------------
	// sql update
	// ------------------------------------
	@CommonTx
	public int update(String sqlString, String[] paramNames, Object[] values) {
		return update(sqlString, paramNames, values, null);
	}

	@CommonTx
	public int update(String sqlString, String[] paramNames, Object[] values, String modifiedUser) {
		int result = 0;
		try {
			Map<String, Object> params = CollectionHelper.toMap(paramNames, values);
			fireBeanUpdating(params, this);
			result = commonDao.update(sqlString, paramNames, values, modifiedUser);
			if (result > 0) {
				fireBeanUpdated(params, this);
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return result;
	}

	@CommonTx
	public int update(String sqlString, Map<String, Object> params) {
		return update(sqlString, params, null);
	}

	@CommonTx
	public int update(String sqlString, Map<String, Object> params, String modifiedUser) {
		int result = 0;
		try {
			fireBeanUpdating(params, this);
			result = commonDao.update(sqlString, params, modifiedUser);
			if (result > 0) {
				fireBeanUpdated(params, this);
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return result;
	}

	// ------------------------------------
	// sql delete
	// ------------------------------------
	@CommonTx
	public int delete(String sqlString, String[] paramNames, Object[] values) {
		return delete(sqlString, paramNames, values, null);
	}

	@CommonTx
	public int delete(String sqlString, String[] paramNames, Object[] values, String modifiedUser) {
		int result = 0;
		try {
			Map<String, Object> params = CollectionHelper.toMap(paramNames, values);
			fireBeanDeleting(params, this);
			result = commonDao.delete(sqlString, paramNames, values, modifiedUser);
			if (result > 0) {
				fireBeanDeleted(params, this);
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return result;
	}

	@CommonTx
	public int delete(String sqlString, Map<String, Object> params) {
		return delete(sqlString, params, null);
	}

	@CommonTx
	public int delete(String sqlString, Map<String, Object> params, String modifiedUser) {
		int result = 0;
		try {
			fireBeanDeleting(params, this);
			result = commonDao.delete(sqlString, params, modifiedUser);
			if (result > 0) {
				fireBeanDeleted(params, this);
			}
		} catch (Exception ex) {
			throw new CommonServiceException(ex);
		}
		return result;
	}

	// ------------------------------------------------------------

	public <E> InputStream write(Collection<E> list) {
		return commonDao.write(list);
	}

	public <E> List<E> read(InputStream inputStream) {
		return commonDao.read(inputStream);
	}

	public boolean reindex(Class<?> entityClass) {
		return commonDao.reindex(entityClass);
	}

	public <T> boolean reindex(T entity) {
		return commonDao.reindex(entity);
	}
}
