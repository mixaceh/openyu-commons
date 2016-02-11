package org.openyu.commons.dao.operation;

import java.io.Serializable;

public interface Operation extends Cloneable, Serializable {

	String KEY = Operation.class.getName();

	Object clone();

	// -----------------------------------
	// action status
	// -----------------------------------

	void setInserted(boolean inserted);

	boolean isInserted();

	void setModified(boolean modified);

	boolean isModified();

	void setDeleted(boolean deleted);

	boolean isDeleted();

	void setUpdated(boolean updated);

	boolean isUpdated();

	void setRefreshed(boolean refreshed);

	boolean isRefreshed();

	void setGaveUp(boolean gaveUp);

	boolean isGaveUp();

	void setQueried(boolean queried);

	boolean isQueried();

	void setSelected(boolean selected);

	boolean isSelected();

	void setSorted(boolean sorted);

	boolean isSorted();

	// -----------------------------------
	void setInsertedDetail(boolean insertedDetail);

	boolean isInsertedDetail();

	void setDeletedDetail(boolean deletedDetail);

	boolean isDeletedDetail();

	void setSelectedDetail(boolean selectedDetail);

	boolean isSelectedDetail();

	// -----------------------------------
	// enable
	// -----------------------------------
	void setInsertEnable(boolean insertEnable);

	boolean isInsertEnable();

	void setModifyEnable(boolean modifyEnable);

	boolean isModifyEnable();

	void setDeleteEnable(boolean deleteEnable);

	boolean isDeleteEnable();

	void setUpdateEnable(boolean updateEnable);

	boolean isUpdateEnable();

	//
	void setRefreshEnable(boolean refreshEnable);

	boolean isRefreshEnable();

	void setGiveUpEnable(boolean giveUpEnable);

	boolean isGiveUpEnable();

	void setQueryEnable(boolean queryEnable);

	boolean isQueryEnable();

	void setSelectEnable(boolean selectEnable);

	boolean isSelectEnable();

	void setSortEnable(boolean sortEnable);

	boolean isSortEnable();

	// -----------------------------------
	// 20071001
	// 表operation是否強制設定
	void setForceEnable(boolean forceEnable);

	boolean isForceEnable();

	// -----------------------------------

	void setListEnable(boolean listEnable);

	boolean isListEnable();

	//

	void setInsertDetailEnable(boolean insertDetailEnable);

	boolean isInsertDetailEnable();

	void setDeleteDetailEnable(boolean deleteDetailEnable);

	boolean isDeleteDetailEnable();

	void setSelectDetailEnable(boolean selectDetailEnable);

	boolean isSelectDetailEnable();

	//

	void setReadOnly(boolean readOnly);

	boolean isReadOnly();

	// -----------------------------------

	void setCurrentProcessing(int currentProcessing);

	int getCurrentProcessing();

	// 20110801
	// 模擬用
	boolean isMock();

	void setMock(boolean mock);

}
