package org.openyu.commons.fto.commons.net.ftp.supporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.openyu.commons.commons.net.ftp.FtpClientSessionFactory;
import org.openyu.commons.fto.commons.net.ftp.FtpClientFto;
import org.openyu.commons.fto.commons.net.ftp.FtpClientTemplate;
import org.openyu.commons.fto.commons.net.ftp.ex.FtpClientFtoException;
import org.openyu.commons.fto.commons.net.ftp.impl.FtpClientTemplateImpl;
import org.openyu.commons.commons.net.ftp.FtpClientSession;
import org.openyu.commons.fto.supporter.BaseFtoSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpClientFtoSupporter extends BaseFtoSupporter implements FtpClientFto {

	private static final long serialVersionUID = -828789251946548025L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(FtpClientFtoSupporter.class);

	private FtpClientTemplate ftpClientTemplate;

	public FtpClientFtoSupporter() {
	}

	protected final void checkConfig() {
		AssertHelper.notNull(ftpClientTemplate, "The FtpClientTemplate is required");
		AssertHelper.notNull(this.ftpClientTemplate.getFtpClientSessionFactory(),
				"The FtpClientSessionFactory is required");
	}

	public final FtpClientSessionFactory getFtpClientSessionFactory() {
		return ((this.ftpClientTemplate != null ? this.ftpClientTemplate
				.getFtpClientSessionFactory() : null));
	}

	public final void setFtpClientSessionFactory(
			FtpClientSessionFactory ftpClientSessionFactory) {
		if ((this.ftpClientTemplate == null)
				|| (ftpClientSessionFactory != this.ftpClientTemplate
						.getFtpClientSessionFactory()))
			this.ftpClientTemplate = createFtpClientTemplate(ftpClientSessionFactory);
	}

	protected FtpClientTemplate createFtpClientTemplate(FtpClientSessionFactory ftpClientSessionFactory) {
		return new FtpClientTemplateImpl(ftpClientSessionFactory);
	}

	public final FtpClientTemplate getFtpClientTemplate() {
		return ftpClientTemplate;
	}

	public final void setFtpClientTemplate(FtpClientTemplate ftpClientTemplate) {
		this.ftpClientTemplate = ftpClientTemplate;
	}

	protected final FtpClientSession getSession() {
		return ftpClientTemplate.getSession();
	}

	protected final void closeSession() {
		ftpClientTemplate.closeSession();
	}

	// --------------------------------------------------

	public String[] listNames(String pathname) {
		try {
			return ftpClientTemplate.listNames(pathname);
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public String[] listNames() {
		try {
			return ftpClientTemplate.listNames();
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public FTPFile[] listFiles(String pathname) {
		try {
			return ftpClientTemplate.listFiles(pathname);
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public FTPFile[] listFiles() {
		try {
			return ftpClientTemplate.listFiles();
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public FTPFile[] listFiles(String pathname, FTPFileFilter filter) {
		try {
			return ftpClientTemplate.listFiles(pathname, filter);
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public FTPFile[] listDirectories() throws IOException {
		try {
			return ftpClientTemplate.listDirectories();
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public FTPFile[] listDirectories(String parent) throws IOException {
		try {
			return ftpClientTemplate.listDirectories(parent);
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	// --------------------------------------------------

	public boolean read(String remote, OutputStream local) {
		try {
			return ftpClientTemplate.read(remote, local);
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public boolean write(String remote, InputStream local) {
		try {
			return ftpClientTemplate.write(remote, local);
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public boolean mkdir(String pathname) {
		try {
			return ftpClientTemplate.mkdir(pathname);
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public boolean delete(String path) {
		try {
			return ftpClientTemplate.delete(path);
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public boolean rename(String from, String to) {
		try {
			return ftpClientTemplate.rename(from, to);
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

	public boolean exists(String path) {
		try {
			return ftpClientTemplate.exists(path);
		} catch (Exception ex) {
			throw new FtpClientFtoException(ex);
		}
	}

}
