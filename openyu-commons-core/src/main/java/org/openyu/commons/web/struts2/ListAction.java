package org.openyu.commons.web.struts2;

/**
 * 列表控制器
 */
public interface ListAction extends BaseAction {

	String KEY = ListAction.class.getName();

	/**
	 * 列表, result用
	 * 
	 * @Result(name = "list", location = "list.jsp") })
	 */
	String LIST = "list";

	/**
	 * 顯示, result用
	 * 
	 * @Result(name = "view", location = "view.jsp") })
	 */
	String VIEW = "view";

	/**
	 * 列表
	 * 
	 * @return
	 */
	String list();

	/**
	 * 查詢, sql
	 * 
	 * @return
	 */
	String find();

	/**
	 * 搜尋, 全文檢索 full text search
	 * 
	 * @return
	 */
	String search();

	/**
	 * 顯示單筆
	 * 
	 * @return
	 */
	String view();

	/**
	 * 新增
	 * 
	 * @return
	 */
	String add();

	/**
	 * 新增存檔
	 * 
	 * @return
	 */
	String addSave();

	/**
	 * 編輯
	 * 
	 * @return
	 */
	String edit();

	/**
	 * 編輯存檔
	 * 
	 * @return
	 */
	String editSave();

	/**
	 * 存檔
	 * 
	 * @return
	 */
	String save();

	/**
	 * 刪除
	 * 
	 * @return
	 */
	String delete();

	/**
	 * 下載
	 * 
	 * @return
	 */
	String download();

	/**
	 * 上傳
	 * 
	 * @return
	 */
	String upload();

}
