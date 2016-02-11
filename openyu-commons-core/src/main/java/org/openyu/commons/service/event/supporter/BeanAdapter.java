package org.openyu.commons.service.event.supporter;

import org.openyu.commons.bean.SeqBean;
import org.openyu.commons.entity.SeqEntity;
import org.openyu.commons.lang.event.supporter.BaseListenerSupporter;
import org.openyu.commons.service.event.BeanEvent;
import org.openyu.commons.service.event.BeanListener;

/**
 * 1.若設計成singleton, 使用getInstance
 * 
 * 2.若用spring,不需使用getInstance,直接在xml上設定即可
 * 
 * 3.當insert,update,delete,find時所觸發的事件處理
 * 
 * 4.觸發之後,會將bean/params value,存到 xxxServiceImpl.beanCache上,要取出就用getBeanCache()
 */
public class BeanAdapter extends BaseListenerSupporter implements BeanListener {

	public BeanAdapter() {

	}

	/**
	 * 初始
	 */
	public void initialize() {

	}

	/**
	 * 新增前
	 * 
	 * @param beanEvent
	 */
	public void beanInserting(BeanEvent beanEvent) {

	}

	/**
	 * 新增後
	 * 
	 * @param beanEvent
	 */
	public void beanInserted(BeanEvent beanEvent) {

	}

	/**
	 * 查詢前
	 * 
	 * @param beanEvent
	 */
	public void beanFinding(BeanEvent beanEvent) {

	}

	/**
	 * 查詢後
	 * 
	 * @param beanEvent
	 */
	public void beanFound(BeanEvent beanEvent) {

	}

	/**
	 * 修改前
	 * 
	 * @param beanEvent
	 */
	public void beanUpdating(BeanEvent beanEvent) {

	}

	/**
	 * 修改後
	 * 
	 * @param beanEvent
	 */
	public void beanUpdated(BeanEvent beanEvent) {

	}

	/**
	 * 刪除前
	 * 
	 * @param beanEvent
	 */
	public void beanDeleting(BeanEvent beanEvent) {

	}

	/**
	 * 刪除後
	 * 
	 * @param beanEvent
	 */
	public void beanDeleted(BeanEvent beanEvent) {

	}

	/**
	 * 
	 * 判斷key
	 * 
	 * @param object
	 * @return
	 */
	protected String getKey(Object object) {
		String key = null;
		StringBuilder tempKey = new StringBuilder();
		if (object instanceof SeqEntity) {
			SeqEntity idEntity = (SeqEntity) object;
			if (idEntity.getSeq() != null) {
				tempKey.append(String.valueOf(idEntity.getSeq()));
			}
		} else if (object instanceof SeqBean) {
			SeqBean idBean = (SeqBean) object;
			tempKey.append(String.valueOf(idBean.getSeq()));
		} else if (object != null) {
			tempKey.append(String.valueOf(object.hashCode()));
		}
		if (tempKey.length() > 0) {
			key = tempKey.toString();
		}
		return key;
	}

}
