package org.openyu.commons.bean;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.bean.TrueFalseOption.TrueFalseType;
import org.openyu.commons.bean.WhetherOption.WhetherType;
import org.openyu.commons.bean.adapter.OrderTypeXmlAdapter;
import org.openyu.commons.bean.adapter.TrueFalseTypeXmlAdapter;
import org.openyu.commons.bean.adapter.WhetherTypeXmlAdapter;
import org.openyu.commons.bean.impl.TrueFalseOptionImpl;
import org.openyu.commons.bean.impl.WhetherOptionImpl;
import org.openyu.commons.collector.CollectorHelper;
import org.openyu.commons.collector.supporter.BaseCollectorSupporter;
import org.openyu.commons.dao.inquiry.Inquiry;
import org.openyu.commons.dao.inquiry.Order.OrderType;
import org.openyu.commons.dao.inquiry.impl.InquiryImpl;
import org.openyu.commons.enumz.EnumHelper;

/**
 * Bean收集器
 */
// --------------------------------------------------
// jaxb
// --------------------------------------------------
@XmlRootElement(name = "beanCollector")
@XmlAccessorType(XmlAccessType.FIELD)
public final class BeanCollector extends BaseCollectorSupporter {

	private static final long serialVersionUID = -3761878632678512470L;

	private static BeanCollector instance;

	// --------------------------------------------------
	// 此有系統值,只是為了轉出xml,並非給企劃編輯用
	// --------------------------------------------------
	/**
	 * 是否類別
	 */
	@XmlJavaTypeAdapter(TrueFalseTypeXmlAdapter.class)
	private Set<TrueFalseType> trueFalseTypes = new LinkedHashSet<TrueFalseType>();

	/**
	 * 全部是否類別
	 */
	@XmlJavaTypeAdapter(WhetherTypeXmlAdapter.class)
	private Set<WhetherType> whetherTypes = new LinkedHashSet<WhetherType>();

	/**
	 * 排序方向類別
	 */
	@XmlJavaTypeAdapter(OrderTypeXmlAdapter.class)
	private Set<OrderType> orderTypes = new LinkedHashSet<OrderType>();

	// --------------------------------------------------
	// 企劃編輯用
	// --------------------------------------------------
	/**
	 * 查詢條件
	 */
	@XmlElement(type = InquiryImpl.class)
	private Inquiry inquiry;

	/**
	 * 是否(true/false)選項
	 */
	@XmlElement(type = TrueFalseOptionImpl.class)
	private List<TrueFalseOption> trueFalseOptions = new LinkedList<TrueFalseOption>();

	/**
	 * 全部是否("all"/"true"/"false")選項
	 */
	@XmlElement(type = WhetherOptionImpl.class)
	private List<WhetherOption> whetherOptions = new LinkedList<WhetherOption>();

	public BeanCollector() {
		setId(BeanCollector.class.getName());
	}

	// --------------------------------------------------
	public synchronized static BeanCollector getInstance() {
		return getInstance(true);
	}

	/**
	 * 單例啟動
	 * 
	 * @return
	 */
	public synchronized static BeanCollector getInstance(boolean start) {
		if (instance == null) {
			instance = CollectorHelper.readFromSer(BeanCollector.class);
			// 此時有可能會沒有ser檔案,或舊的結構造成ex,只要再轉出一次ser,覆蓋原本ser即可
			if (instance == null) {
				instance = new BeanCollector();
			}
			//
			if (start) {
				// 啟動
				instance.start();
			}
			// 此有系統預設值,只是為了轉出xml,並非給企劃編輯用
			instance.trueFalseTypes = EnumHelper.valuesSet(TrueFalseType.class);
			instance.whetherTypes = EnumHelper.valuesSet(WhetherType.class);
			instance.orderTypes = EnumHelper.valuesSet(OrderType.class);
		}
		return instance;
	}

	/**
	 * 單例關閉
	 * 
	 * @return
	 */
	public synchronized static BeanCollector shutdownInstance() {
		if (instance != null) {
			BeanCollector oldInstance = instance;
			instance = null;
			//
			if (oldInstance != null) {
				oldInstance.shutdown();
			}
		}
		return instance;
	}

	/**
	 * 單例重啟
	 * 
	 * @return
	 */
	public synchronized static BeanCollector restartInstance() {
		if (instance != null) {
			instance.restart();
		}
		return instance;
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
		trueFalseOptions.clear();
		whetherOptions.clear();
	}

	public Set<TrueFalseType> getTrueFalseTypes() {
		if (trueFalseTypes == null) {
			trueFalseTypes = new LinkedHashSet<TrueFalseType>();
		}
		return trueFalseTypes;
	}

	public void setTrueFalseTypes(Set<TrueFalseType> trueFalseTypes) {
		this.trueFalseTypes = trueFalseTypes;
	}

	public Set<WhetherType> getWhetherTypes() {
		if (whetherTypes == null) {
			whetherTypes = new LinkedHashSet<WhetherType>();
		}
		return whetherTypes;
	}

	public void setWhetherTypes(Set<WhetherType> whetherTypes) {
		this.whetherTypes = whetherTypes;
	}

	public Inquiry getInquiry() {
		return inquiry;
	}

	public void setInquiry(Inquiry inquiry) {
		this.inquiry = inquiry;
	}

	// --------------------------------------------------
	// 選項
	// --------------------------------------------------

	/**
	 * 是否(true/false)選項
	 * 
	 * @return
	 */
	public List<TrueFalseOption> getTrueFalseOptions() {
		if (trueFalseOptions == null) {
			trueFalseOptions = new LinkedList<TrueFalseOption>();
		}
		return trueFalseOptions;
	}

	public void setTrueFalseOptions(List<TrueFalseOption> trueFalseOptions) {
		this.trueFalseOptions = trueFalseOptions;
	}

	/**
	 * 取得,是否(true/false)選項名稱
	 * 
	 * @param value
	 *            , TrueFalse.getId().getValue()
	 * @param locale
	 * @return
	 */
	public String getTrueFalseName(boolean value, Locale locale) {
		String result = null;
		for (TrueFalseOption entry : trueFalseOptions) {
			if (entry == null) {
				continue;
			}
			if (entry.getId().getValue() == value) {
				result = entry.getName(locale);
				break;
			}
		}
		return result;
	}

	/**
	 * 全部是否("all"/"true"/"false")選項
	 * 
	 * @return
	 */
	public List<WhetherOption> getWhetherOptions() {
		if (whetherOptions == null) {
			whetherOptions = new LinkedList<WhetherOption>();
		}
		return whetherOptions;
	}

	public void setWhetherOptions(List<WhetherOption> whetherOptions) {
		this.whetherOptions = whetherOptions;
	}

	/**
	 * 取得,全部是否("all"/"true"/"false")選項名稱
	 * 
	 * @param value
	 *            , Whether.getId().getValue()
	 * @param locale
	 * @return
	 */
	public String getWhetherName(String value, Locale locale) {
		String result = null;
		for (WhetherOption entry : whetherOptions) {
			if (entry == null) {
				continue;
			}
			//
			if (entry.getId().getValue() == value) {
				result = entry.getName(locale);
				break;
			}
		}
		return result;
	}

	// --------------------------------------------------
	/**
	 * 建構查詢條件
	 * 
	 * @return
	 */
	public Inquiry createInquiry() {
		return (inquiry != null ? (Inquiry) inquiry.clone() : null);
	}
}
