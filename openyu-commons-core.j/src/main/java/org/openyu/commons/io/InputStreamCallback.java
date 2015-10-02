package org.openyu.commons.io;

public interface InputStreamCallback {

	/**
	 * 
	 * @param blockArray
	 *            讀取到的byte[]
	 * @return 是否繼續讀取下一段byte[],
	 * 
	 *         true=繼續往下一段byte[], false=中斷
	 */
	boolean doInAction(byte[] blockArray);
}
