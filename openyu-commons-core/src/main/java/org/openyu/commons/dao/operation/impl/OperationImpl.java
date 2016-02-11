package org.openyu.commons.dao.operation.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.openyu.commons.dao.operation.Operation;

public class OperationImpl implements Operation {

	private static final long serialVersionUID = 1L;

	private boolean inserted;

	private boolean modified;

	private boolean deleted;

	private boolean updated;

	private boolean refreshed;

	private boolean gaveUp;

	private boolean queried;

	private boolean selected;

	private boolean sorted;

	//
	private boolean insertedDetail;

	private boolean deletedDetail;

	private boolean selectedDetail;

	// -------------------------------------------------------------

	private boolean insertEnable = true;

	private boolean modifyEnable = true;

	private boolean deleteEnable = true;

	private boolean updateEnable = true;

	//

	private boolean refreshEnable = true;

	private boolean giveUpEnable = true;

	private boolean queryEnable = true;

	private boolean selectEnable = true;

	private boolean sortEnable = true;

	// default false
	private boolean forceEnable = false;

	//

	private boolean listEnable = true;

	private boolean insertDetailEnable = true;

	private boolean deleteDetailEnable = true;

	private boolean selectDetailEnable = true;

	//
	private int currentProcessing;

	private boolean mock;

	public OperationImpl() {
	}

	// -------------------------------------------------------------
	public void setInserted(boolean inserted) {
		this.inserted = inserted;
	}

	public boolean isInserted() {
		return inserted;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isModified() {
		return modified;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setRefreshed(boolean refreshed) {
		this.refreshed = refreshed;
	}

	public boolean isRefreshed() {
		return refreshed;
	}

	public void setGaveUp(boolean gaveUp) {
		this.gaveUp = gaveUp;
	}

	public boolean isGaveUp() {
		return gaveUp;
	}

	public void setQueried(boolean queried) {
		this.queried = queried;
	}

	public boolean isQueried() {
		return queried;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	public boolean isSorted() {
		return sorted;
	}

	// -------------------------------------------------------------
	public void setInsertedDetail(boolean insertedDetail) {
		this.insertedDetail = insertedDetail;
	}

	public boolean isInsertedDetail() {
		return insertedDetail;
	}

	public void setDeletedDetail(boolean deletedDetail) {
		this.deletedDetail = deletedDetail;
	}

	public boolean isDeletedDetail() {
		return deletedDetail;
	}

	public void setSelectedDetail(boolean selectedDetail) {
		this.selectedDetail = selectedDetail;
	}

	public boolean isSelectedDetail() {
		return selectedDetail;
	}

	// -------------------------------------------------------------
	public void setInsertEnable(boolean insertEnable) {
		this.insertEnable = insertEnable;
	}

	public boolean isInsertEnable() {
		return insertEnable;
	}

	public void setModifyEnable(boolean modifyEnable) {
		this.modifyEnable = modifyEnable;
	}

	public boolean isModifyEnable() {
		return modifyEnable;
	}

	public void setDeleteEnable(boolean deleteEnable) {
		this.deleteEnable = deleteEnable;
	}

	public boolean isDeleteEnable() {
		return deleteEnable;
	}

	public void setUpdateEnable(boolean updateEnable) {
		this.updateEnable = updateEnable;
	}

	public boolean isUpdateEnable() {
		return updateEnable;
	}

	// -------------------------------------------------------------
	public void setRefreshEnable(boolean refreshEnable) {
		this.refreshEnable = refreshEnable;
	}

	public boolean isRefreshEnable() {
		return refreshEnable;
	}

	public void setGiveUpEnable(boolean giveUpEnable) {
		this.giveUpEnable = giveUpEnable;
	}

	public boolean isGiveUpEnable() {
		return giveUpEnable;
	}

	public boolean isQueryEnable() {
		return queryEnable;
	}

	public void setQueryEnable(boolean queryEnable) {
		this.queryEnable = queryEnable;
	}

	public void setSelectEnable(boolean selectEnable) {
		this.selectEnable = selectEnable;
	}

	public boolean isSelectEnable() {
		return selectEnable;
	}

	public void setSortEnable(boolean sortEnable) {
		this.sortEnable = sortEnable;
	}

	public boolean isSortEnable() {
		return sortEnable;
	}

	public void setForceEnable(boolean forceEnable) {
		this.forceEnable = forceEnable;
	}

	public boolean isForceEnable() {
		return forceEnable;
	}

	// -------------------------------------------------------------
	public void setListEnable(boolean listEnable) {
		this.listEnable = listEnable;
	}

	public boolean isListEnable() {
		return listEnable;
	}

	public void setInsertDetailEnable(boolean insertDetailEnable) {
		this.insertDetailEnable = insertDetailEnable;
	}

	public boolean isInsertDetailEnable() {
		return insertDetailEnable;
	}

	public void setDeleteDetailEnable(boolean deleteDetailEnable) {
		this.deleteDetailEnable = deleteDetailEnable;
	}

	public boolean isDeleteDetailEnable() {
		return deleteDetailEnable;
	}

	public void setSelectDetailEnable(boolean selectDetailEnable) {
		this.selectDetailEnable = selectDetailEnable;
	}

	public boolean isSelectDetailEnable() {
		return selectDetailEnable;
	}

	// -------------------------------------------------------------
	public void setReadOnly(boolean readOnly) {
		setInsertEnable(!readOnly);
		setModifyEnable(!readOnly);
		setDeleteEnable(!readOnly);
		setUpdateEnable(!readOnly);
		//
	}

	//
	public void setCurrentProcessing(int currentProcessing) {
		this.currentProcessing = currentProcessing;
	}

	public int getCurrentProcessing() {
		return currentProcessing;
	}

	//

	public boolean isReadOnly() {
		return (!isInsertEnable() && !isModifyEnable() && !isDeleteEnable() && !isUpdateEnable());
	}

	public boolean isMock() {
		return mock;
	}

	public void setMock(boolean mock) {
		this.mock = mock;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("inserted", inserted).append("modified", modified)
				.append("deleted", deleted).append("updated", updated)
				.append("refreshed", refreshed).append("gaveUp", gaveUp)
				.append("queried", queried).append("selected", selected)
				.append("sorted", sorted)
				.append("insertedDetail", insertedDetail)
				.append("deletedDetail", deletedDetail)
				.append("selectedDetail", selectedDetail)
				.append("insertEnable", insertEnable)
				.append("modifyEnable", modifyEnable)
				.append("deleteEnable", deleteEnable)
				.append("updateEnable", updateEnable)
				.append("refreshEnable", refreshEnable)
				.append("giveUpEnable", giveUpEnable)
				.append("queryEnable", queryEnable)
				.append("selectEnable", selectEnable)
				.append("sortEnable", sortEnable)
				.append("forceEnable", forceEnable)
				.append("listEnable", listEnable)
				.append("insertDetailEnable", insertDetailEnable)
				.append("deleteDetailEnable", deleteDetailEnable)
				.append("selectDetailEnable", selectDetailEnable)
				.append("currentProcessing", currentProcessing)
				.append("readOnly", isReadOnly()).append("mock", mock)
				.toString();
	}

	public Object clone() {
		OperationImpl copy = null;
		try {
			copy = (OperationImpl) super.clone();
		} catch (CloneNotSupportedException ex) {
			throw new InternalError();
		}
		return copy;
	}

}
