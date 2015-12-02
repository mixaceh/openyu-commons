package org.openyu.commons.dao.inquiry;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.openyu.commons.model.BaseModel;
import com.sun.xml.bind.AnyTypeAdapter;

/**
 * 分頁條件
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface Pagination extends BaseModel {

	/**
	 * 預設第0頁
	 */
	int DEFAULT_PAGE_INDEX = 0;

	/**
	 * 預設每頁筆數
	 */
	int DEFAULT_PAGE_SIZE = 20;

	/**
	 * 預設總計頁數
	 */
	int DEFAULT_PAGE_COUNT = 0;

	//
	int DEFAULT_NAV_SIZE = 10;

	/**
	 * 目前頁數,第?頁
	 * 
	 * @return
	 */
	int getPageIndex();

	void setPageIndex(int pageIndex);

	/**
	 * 每頁筆數
	 * 
	 * @return
	 */
	int getPageSize();

	void setPageSize(int pageSize);

	/**
	 * 每頁筆數選項
	 * 
	 * @return
	 */
	List<Integer> getPageSizes();

	void setPageSizes(List<Integer> pageSizes);

	/**
	 * 起始筆次 start
	 * 
	 * @return
	 */
	int getFirstResult();

	/**
	 * 最大筆數,(每頁筆數)
	 * 
	 * @return
	 */
	int getMaxResults();

	/**
	 * 總計頁數
	 * 
	 * @return
	 */
	int getPageCount();

	// --------------------------------------------------------

	/**
	 * 總計筆數
	 * 
	 * @return
	 */
	long getRowCount();

	void setRowCount(long rowCount);

	/**
	 * 搜尋時間,秒數
	 * 
	 * @return
	 */
	double getProcessTime();

	void setProcessTime(double processTime);

	// --------------------------------------------------------
	// 導覽頁
	// --------------------------------------------------------
	/**
	 * 目前導覽頁,第?導覽頁
	 * 
	 * @return
	 */
	int getNavIndex();

	/**
	 * 導覽頁數
	 * 
	 * @return
	 */
	int getNavSize();

	void setNavSize(int navSize);

	/**
	 * 總計導覽頁數
	 * 
	 * @return
	 */
	int getNavCount();

}