package org.openyu.commons.model.aware.impl;

import org.openyu.commons.mark.Supporter;
import org.openyu.commons.model.BaseModel;
import org.openyu.commons.model.aware.BaseModelAware;

/**
 * The Class BaseModelAwareImpl.
 */
public class BaseModelAwareImpl implements BaseModelAware, Supporter {

	/** The base model. */
	private BaseModel baseModel;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openyu.commons.model.aware.BaseModelAware#getBaseModel()
	 */
	public BaseModel getBaseModel() {
		return baseModel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openyu.commons.model.aware.BaseModelAware#setBaseModel(org.openyu.commons.model.BaseModel
	 * )
	 */
	public void setBaseModel(BaseModel baseModel) {
		this.baseModel = baseModel;
	}

}
