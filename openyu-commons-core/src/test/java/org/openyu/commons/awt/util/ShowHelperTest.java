package org.openyu.commons.awt.util;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;

import org.junit.Test;
import org.openyu.commons.awt.icon.IconHelper;
import org.openyu.commons.awt.util.ShowHelper;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.thread.ThreadHelper;

public class ShowHelperTest extends BaseTestSupporter {

	@Test
	// no edt
	// 1 times: 469 mills.
	// 1 times: 471 mills.
	// 1 times: 460 mills.
	//
	// edt
	// 1 times: 8 mills.
	// 1 times: 8 mills.
	// 1 times: 8 mills.
	public void showIcon() {
		Icon value = IconHelper.createIcon("src/test/config/icon/java-site.png");
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ShowHelper.showIcon(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");

		Icon[] values = new Icon[2];
		values[0] = IconHelper.createIcon("src/test/config/icon/java-logo-250.png");
		values[1] = IconHelper.createIcon("src/test/config/icon/blog-logo-250.png");
		//
		ShowHelper.showIcon(values);
		//
		ThreadHelper.sleep(10 * 1000);
	}

	@Test
	public void showInternalFrame() {
		JInternalFrame value = new JInternalFrame("JInternalFrame", true, true,
				true, true);
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ShowHelper.showInternalFrame(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		JInternalFrame[] values = new JInternalFrame[10];
		values[0] = new JInternalFrame("JInternalFrame1", true, true, true,
				true);
		values[1] = new JInternalFrame("JInternalFrame2", true, true, true,
				true);
		values[2] = new JInternalFrame("JInternalFrame3", true, true, true,
				true);
		values[3] = new JInternalFrame("JInternalFrame4", true, true, true,
				true);
		values[4] = new JInternalFrame("JInternalFrame5", true, true, true,
				true);
		//
		ShowHelper.showInternalFrame(values);
		//
		ThreadHelper.sleep(10 * 1000);
	}

	@Test
	// no edt
	// 1 times: 465 mills.
	// 1 times: 458 mills.
	// 1 times: 512 mills.
	// edt
	// 1 times: 100 mills.
	// 1 times: 103 mills.
	// 1 times: 104 mills.
	public void showComponent() {
		JButton value = new JButton("JButton");
		//
		int count = 1;
		long beg = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			ShowHelper.showComponent(value);
		}
		long end = System.currentTimeMillis();
		System.out.println(count + " times: " + (end - beg) + " mills. ");
		//
		Component[] values = new Component[10];
		values[0] = new JComboBox();
		values[1] = new JTree();
		values[2] = new JLabel("JLabel");
		values[3] = new JTextField("JTextField");
		values[4] = new JTextArea("JTextArea");
		//
		ShowHelper.showComponent(values);
		//
		ThreadHelper.sleep(10 * 1000);
	}
}
