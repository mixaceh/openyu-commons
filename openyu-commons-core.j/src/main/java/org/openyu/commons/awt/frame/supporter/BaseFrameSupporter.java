package org.openyu.commons.awt.frame.supporter;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.openyu.commons.awt.frame.BaseFrame;
import org.openyu.commons.mark.Supporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseFrameSupporter extends JFrame implements BaseFrame, Supporter {

	private static final long serialVersionUID = 7358946866504632420L;

	/** The Constant LOGGER. */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(BaseFrameSupporter.class);

	public BaseFrameSupporter(String title) throws HeadlessException {
		super(title);
		doStart();
	}

	public BaseFrameSupporter() throws HeadlessException {
		super();
		doStart();
	}

	public BaseFrameSupporter(GraphicsConfiguration gc) {
		super(gc);
		doStart();
	}

	public BaseFrameSupporter(String title, GraphicsConfiguration gc) {
		super(title, gc);
		doStart();
	}

	/**
	 * 內部啟動
	 */
	protected abstract void doStart();

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
