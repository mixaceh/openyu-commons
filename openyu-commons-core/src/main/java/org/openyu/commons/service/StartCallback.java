package org.openyu.commons.service;

/**
 * 起動callback
 */
public interface StartCallback extends ServiceCallback {

	void doInAction() throws Exception;
}
