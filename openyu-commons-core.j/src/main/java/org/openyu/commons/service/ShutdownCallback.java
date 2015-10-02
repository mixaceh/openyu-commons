package org.openyu.commons.service;

/**
 * 關閉callback
 */
public interface ShutdownCallback extends ServiceCallback {

	void doInAction() throws Exception;
}
