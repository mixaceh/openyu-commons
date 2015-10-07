package org.openyu.commons.fto.commons.net.ftp.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.openyu.commons.fto.commons.net.ftp.CnfCallback;
import org.openyu.commons.fto.commons.net.ftp.CnfTemplate;
import org.openyu.commons.fto.commons.net.ftp.ex.CnfTemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.openyu.commons.commons.net.ftp.CnfSession;
import org.openyu.commons.commons.net.ftp.CnfSessionFactory;
import org.openyu.commons.util.AssertHelper;

public class CnfTemplateImpl implements CnfTemplate, InitializingBean,
		DisposableBean {

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(CnfTemplateImpl.class);

	private CnfSessionFactory cnfSessionFactory;

	public CnfTemplateImpl(CnfSessionFactory cnfSessionFactory) {
		this.cnfSessionFactory = cnfSessionFactory;
		afterPropertiesSet();
	}

	public CnfTemplateImpl() {
	}

	public CnfSessionFactory getCnfSessionFactory() {
		return cnfSessionFactory;
	}

	public void setCnfSessionFactory(CnfSessionFactory CnfSessionFactory) {
		this.cnfSessionFactory = CnfSessionFactory;
	}

	public CnfSession getSession() {
		try {
			return cnfSessionFactory.openSession();
		} catch (Exception ex) {
			throw new CnfTemplateException("Could not open CnfSession", ex);
		}
	}

	public void closeSession() {
		try {
			cnfSessionFactory.closeSession();
		} catch (Exception ex) {
			throw new CnfTemplateException("Could not close CnfSession", ex);
		}
	}

	public void afterPropertiesSet() {
		AssertHelper
				.notNull(cnfSessionFactory, "The CnfSessionFactory is required");
	}

	public void destroy() {

	}

	public <T> T execute(CnfCallback<T> action) throws CnfTemplateException {
		return doExecute(action);
	}

	/**
	 * @param action
	 * @return
	 * @throws CnfTemplateException
	 */
	protected <T> T doExecute(CnfCallback<T> action)
			throws CnfTemplateException {
		Assert.notNull(action, "CnfCallback must not be null");
		//
		T result = null;
		CnfSession session = null;
		try {
			session = getSession();
			result = action.doInAction(session);
			return result;
		} catch (Exception ex) {
			throw new CnfTemplateException(ex);
		} finally {
			if (session != null) {
				closeSession();
			}
		}
	}

	// --------------------------------------------------

	public String[] listNames(final String pathname) throws IOException {
		return execute(new CnfCallback<String[]>() {
			public String[] doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.listNames(pathname);
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public String[] listNames() throws IOException {
		return execute(new CnfCallback<String[]>() {
			public String[] doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.listNames();
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public FTPFile[] listFiles(final String pathname) throws IOException {
		return execute(new CnfCallback<FTPFile[]>() {
			public FTPFile[] doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.listFiles(pathname);
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public FTPFile[] listFiles() throws IOException {
		return execute(new CnfCallback<FTPFile[]>() {
			public FTPFile[] doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.listFiles();
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public FTPFile[] listFiles(final String pathname, final FTPFileFilter filter)
			throws IOException {
		return execute(new CnfCallback<FTPFile[]>() {
			public FTPFile[] doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.listFiles(pathname, filter);
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public FTPFile[] listDirectories() throws IOException {
		return execute(new CnfCallback<FTPFile[]>() {
			public FTPFile[] doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.listDirectories();
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public FTPFile[] listDirectories(final String parent) throws IOException {
		return execute(new CnfCallback<FTPFile[]>() {
			public FTPFile[] doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.listDirectories(parent);
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	// --------------------------------------------------

	public boolean read(final String remote, final OutputStream local)
			throws IOException {
		return execute(new CnfCallback<Boolean>() {
			public Boolean doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.retrieveFile(remote, local);
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public boolean write(final String remote, final InputStream local)
			throws IOException {
		return execute(new CnfCallback<Boolean>() {
			public Boolean doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.storeFile(remote, local);
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public boolean mkdir(final String pathname) throws IOException {
		return execute(new CnfCallback<Boolean>() {
			public Boolean doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.makeDirectory(pathname);
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public boolean delete(final String path) throws IOException {
		return execute(new CnfCallback<Boolean>() {
			public Boolean doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.deleteFile(path);
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public boolean rename(final String from, final String to)
			throws IOException {
		return execute(new CnfCallback<Boolean>() {
			public Boolean doInAction(CnfSession session)
					throws CnfTemplateException {
				try {
					return session.rename(from, to);
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				}
			}
		});
	}

	public boolean exists(final String path) throws IOException {
		return execute(new CnfCallback<Boolean>() {
			public Boolean doInAction(CnfSession session)
					throws CnfTemplateException {
				String currentWorkingPath = null;
				try {
					boolean exists = false;
					currentWorkingPath = session.printWorkingDirectory();
					if (session.changeWorkingDirectory(path)) {
						exists = true;
					}
					return exists;
				} catch (Exception ex) {
					throw new CnfTemplateException(ex);
				} finally {
					if (currentWorkingPath != null) {
						try {
							session.changeWorkingDirectory(currentWorkingPath);
						} catch (Exception ex2) {
						}
					}
				}
			}
		});
	}

}
