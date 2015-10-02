package org.openyu.commons.awt.frame;

import java.awt.event.WindowListener;

/**
 * 基礎Frame
 */
public interface BaseFrame extends WindowListener {

	/**
	 * 標題
	 * 
	 * @return
	 */
	String getTitle();

	void setTitle(String title);

	/**
	 * 顯示Frame
	 * 
	 * @param visible
	 */
	void setVisible(boolean visible);
}
