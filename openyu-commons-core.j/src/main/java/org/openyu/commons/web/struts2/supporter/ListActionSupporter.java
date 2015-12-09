package org.openyu.commons.web.struts2.supporter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.openyu.commons.web.struts2.ListAction;

/**
 * 列表控制器
 */
public class ListActionSupporter extends BaseActionSupporter implements ListAction
{

	private static final long serialVersionUID = -2327135166014191447L;

	private static transient final Logger log = LogManager.getLogger(ListActionSupporter.class);

	public ListActionSupporter()
	{}

	/**
	 * 列表
	 * 
	 * @return
	 */
	public String list()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 查詢, sql
	 * 
	 * @return
	 */
	public String find()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 搜尋, 全文檢索 full text search
	 * 
	 * @return
	 */
	public String search()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 顯示單筆
	 * 
	 * @return
	 */
	public String view()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public String add()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 新增存檔
	 * 
	 * @return
	 */
	public String addSave()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 編輯
	 * 
	 * @return
	 */
	public String edit()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 編輯存檔
	 * 
	 * @return
	 */
	public String editSave()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 存檔
	 * 
	 * @return
	 */
	public String save()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 刪除
	 * 
	 * @return
	 */
	public String delete()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 下載
	 * 
	 * @return
	 */
	public String download()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 上傳
	 * 
	 * @return
	 */
	public String upload()
	{
		throw new UnsupportedOperationException();
	}

}
