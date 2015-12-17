package org.openyu.commons.awt.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Window;
import java.io.File;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import org.openyu.commons.awt.icon.IconHelper;
import org.openyu.commons.helper.ex.HelperException;
import org.openyu.commons.helper.supporter.BaseHelperSupporter;
import org.openyu.commons.lang.StringHelper;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ShowHelper extends BaseHelperSupporter {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(ShowHelper.class);

	private ShowHelper() {
		throw new HelperException(
				new StringBuilder().append(AssertHelper.class.getName()).append(" can not construct").toString());
	}

	public static void showIcon(File file) {
		Icon icon = IconHelper.createIcon(file);
		showIcon(new Icon[] { icon });
	}

	public static void showIcon(URL url) {
		Icon icon = IconHelper.createIcon(url);
		showIcon(new Icon[] { icon });
	}

	public static void showIcon(Icon icon) {
		showIcon(new Icon[] { icon });
	}

	/**
	 * 顯示 icon
	 * 
	 * @param icons
	 */
	public static void showIcon(final Icon[] icons) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				buildShowIcon(icons);
			}
		});
	}

	protected static void buildShowIcon(Icon[] icons) {
		final int WIDTH = 800;
		final int HEIGHT = 600;
		// status
		JTextArea statusTextArea = new JTextArea(5, 69);
		statusTextArea.setLineWrap(true);
		statusTextArea.setWrapStyleWord(true);
		statusTextArea.setEditable(false);
		//
		JScrollPane statusScroll = new JScrollPane();
		statusScroll.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		statusScroll.getViewport().add(statusTextArea);

		// icon
		JPanel iconPanel = new JPanel();
		JScrollPane iconScroll = new JScrollPane();
		iconScroll.getViewport().add(iconPanel);
		for (Icon icon : icons) {
			if (icon != null) {
				JLabel iconLabel = new JLabel(icon);
				iconPanel.add(iconLabel);
				statusTextArea.append(icon + StringHelper.LF);
				statusTextArea.setCaretPosition(statusTextArea.getText().length());
			}
		}
		//
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(WIDTH, HEIGHT));
		frame.getContentPane().add(iconScroll, BorderLayout.CENTER);
		frame.getContentPane().add(statusScroll, BorderLayout.PAGE_END);
		//
		frame.setVisible(true);
	}

	// --------------------------------------------------
	public static void showInternalFrame(JInternalFrame internalFrame) {
		showInternalFrame(new JInternalFrame[] { internalFrame });
	}

	/**
	 * 顯示internalFrame
	 * 
	 * @param internalFrames
	 */
	public static void showInternalFrame(final JInternalFrame[] internalFrames) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				buildShowInternalFrame(internalFrames);
			}
		});
	}

	protected static void buildShowInternalFrame(JInternalFrame[] internalFrames) {
		final int WIDTH = 800;
		final int HEIGHT = 600;
		/*
		 * 需設定 size 才能 reshape 顯示,先加入到desktopPane 再 setVisible(true);
		 * 1.internalFrame.setSize(new Dimension(750, 400)); 2.JDesktopPane
		 * desktopPane = new JDesktopPane(); desktopPane.add(internalFrame);
		 * 3.internalFrame.setVisible(true);
		 */
		// status
		JTextArea statusTextArea = new JTextArea(5, 69);
		statusTextArea.setLineWrap(true);
		statusTextArea.setWrapStyleWord(true);
		statusTextArea.setEditable(false);
		//
		JScrollPane statusScroll = new JScrollPane();
		statusScroll.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		statusScroll.getViewport().add(statusTextArea);

		// deskttop
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		for (JInternalFrame internalFrame : internalFrames) {
			if (internalFrame != null) {
				internalFrame.setSize(new Dimension(650, 450));
				desktopPane.add(internalFrame);
				internalFrame.setVisible(true);
				//
				statusTextArea.append(internalFrame.getTitle() + StringHelper.LF);
				statusTextArea.setCaretPosition(statusTextArea.getText().length());
			}
		}
		//
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(WIDTH, HEIGHT));
		frame.getContentPane().add(desktopPane, BorderLayout.CENTER);
		frame.getContentPane().add(statusScroll, BorderLayout.PAGE_END);
		//
		frame.setVisible(true);
	}

	// --------------------------------------------------
	public static void showComponent(Component component) {
		showComponent(new Component[] { component });
	}

	/**
	 * 顯示ui元件
	 * 
	 * @param components
	 */
	public static void showComponent(final Component[] components) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				buildShowComponent(components);
			}
		});
	}

	protected static void buildShowComponent(Component[] components) {
		final int WIDTH = 800;
		final int HEIGHT = 600;
		//
		if (components instanceof JInternalFrame[]) {
			showInternalFrame((JInternalFrame[]) components);
		} else if (components instanceof Window[]) {
			Window[] windows = (Window[]) components;
			for (int i = 0; i < windows.length; i++) {
				windows[i].setVisible(true);
			}
		} else {
			// status
			JTextArea statusTextArea = new JTextArea(5, 69);
			statusTextArea.setLineWrap(true);
			statusTextArea.setWrapStyleWord(true);
			statusTextArea.setEditable(false);
			//
			JScrollPane statusScroll = new JScrollPane();
			statusScroll.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			statusScroll.getViewport().add(statusTextArea);

			// component
			JPanel componentPanel = new JPanel();
			JScrollPane componentScroll = new JScrollPane();
			componentScroll.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			componentScroll.getViewport().add(componentPanel);
			//
			for (Component component : components) {
				if (component != null) {
					componentPanel.add(component);
					//
					statusTextArea.append(component.getClass().getName() + StringHelper.LF);
					statusTextArea.setCaretPosition(statusTextArea.getText().length());
				}
			}
			//
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(new Dimension(WIDTH, HEIGHT));
			frame.getContentPane().add(componentScroll, BorderLayout.CENTER);
			frame.getContentPane().add(statusScroll, BorderLayout.PAGE_END);
			//
			frame.setVisible(true);
		}
	}

}
