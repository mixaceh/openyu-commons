package org.openyu.commons.service;

/**
 * 重啟callback
 */
public interface RestartCallback extends ServiceCallback {

	void doInAction() throws Exception;
}
