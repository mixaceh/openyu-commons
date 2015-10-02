package org.openyu.commons.fto.commons.net.ftp.supporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.openyu.commons.commons.net.ftp.CnfSessionFactory;
import org.openyu.commons.fto.commons.net.ftp.CnfFto;
import org.openyu.commons.fto.commons.net.ftp.CnfTemplate;
import org.openyu.commons.fto.commons.net.ftp.ex.CnfFtoException;
import org.openyu.commons.fto.commons.net.ftp.impl.CnfTemplateImpl;
import org.openyu.commons.commons.net.ftp.CnfSession;
import org.openyu.commons.fto.supporter.BaseFtoSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CnfFtoSupporter extends BaseFtoSupporter implements CnfFto {

	private static final long serialVersionUID = -828789251946548025L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(CnfFtoSupporter.class);

	private CnfTemplate cnfTemplate;

	public CnfFtoSupporter() {
	}

	protected final void checkConfig() {
		AssertHelper.notNull(cnfTemplate, "The CnfTemplate is required");
		AssertHelper.notNull(this.cnfTemplate.getCnfSessionFactory(),
				"The CnfSessionFactory is required");
	}

	public final CnfSessionFactory getCnfSessionFactory() {
		return ((this.cnfTemplate != null ? this.cnfTemplate
				.getCnfSessionFactory() : null));
	}

	public final void setCnfSessionFactory(
			CnfSessionFactory cnfSessionFactory) {
		if ((this.cnfTemplate == null)
				|| (cnfSessionFactory != this.cnfTemplate
						.getCnfSessionFactory()))
			this.cnfTemplate = createCnfTemplate(cnfSessionFactory);
	}

	protected CnfTemplate createCnfTemplate(CnfSessionFactory cnfSessionFactory) {
		return new CnfTemplateImpl(cnfSessionFactory);
	}

	public final CnfTemplate getCnfTemplate() {
		return cnfTemplate;
	}

	public final void setCnfTemplate(CnfTemplate cnfTemplate) {
		this.cnfTemplate = cnfTemplate;
	}

	protected final CnfSession getSession() {
		return cnfTemplate.getSession();
	}

	protected final void closeSession() {
		cnfTemplate.closeSession();
	}

	// --------------------------------------------------

	public String[] listNames(String pathname) {
		try {
			return cnfTemplate.listNames(pathname);
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public String[] listNames() {
		try {
			return cnfTemplate.listNames();
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public FTPFile[] listFiles(String pathname) {
		try {
			return cnfTemplate.listFiles(pathname);
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public FTPFile[] listFiles() {
		try {
			return cnfTemplate.listFiles();
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public FTPFile[] listFiles(String pathname, FTPFileFilter filter) {
		try {
			return cnfTemplate.listFiles(pathname, filter);
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public FTPFile[] listDirectories() throws IOException {
		try {
			return cnfTemplate.listDirectories();
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public FTPFile[] listDirectories(String parent) throws IOException {
		try {
			return cnfTemplate.listDirectories(parent);
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	// --------------------------------------------------

	public boolean read(String remote, OutputStream local) {
		try {
			return cnfTemplate.read(remote, local);
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public boolean write(String remote, InputStream local) {
		try {
			return cnfTemplate.write(remote, local);
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public boolean mkdir(String pathname) {
		try {
			return cnfTemplate.mkdir(pathname);
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public boolean delete(String path) {
		try {
			return cnfTemplate.delete(path);
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public boolean rename(String from, String to) {
		try {
			return cnfTemplate.rename(from, to);
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

	public boolean exists(String path) {
		try {
			return cnfTemplate.exists(path);
		} catch (Exception ex) {
			throw new CnfFtoException(ex);
		}
	}

}
