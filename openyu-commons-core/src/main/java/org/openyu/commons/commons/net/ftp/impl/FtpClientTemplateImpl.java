package org.openyu.commons.commons.net.ftp.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.openyu.commons.service.supporter.BaseServiceSupporter;
import org.openyu.commons.util.AssertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.commons.net.ftp.FtpClientCallback;
import org.openyu.commons.commons.net.ftp.FtpClientSession;
import org.openyu.commons.commons.net.ftp.FtpClientSessionFactory;
import org.openyu.commons.commons.net.ftp.FtpClientTemplate;
import org.openyu.commons.commons.net.ftp.ex.FtpClientTemplateException;

public class FtpClientTemplateImpl extends BaseServiceSupporter implements FtpClientTemplate {

	private static final long serialVersionUID = 8387261262129129451L;

	private static transient final Logger LOGGER = LoggerFactory.getLogger(FtpClientTemplateImpl.class);

	private FtpClientSessionFactory ftpClientSessionFactory;

	public FtpClientTemplateImpl(FtpClientSessionFactory ftpClientSessionFactory) {
		this.ftpClientSessionFactory = ftpClientSessionFactory;
	}

	public FtpClientTemplateImpl() {
		this(null);
	}

	/**
	 * 內部啟動
	 */
	@Override
	protected void doStart() throws Exception {
		// AssertHelper.notNull(ftpClientSessionFactory, "The FtpClientSessionFactory is
		// required");
	}

	/**
	 * 內部關閉
	 */
	@Override
	protected void doShutdown() throws Exception {

	}

	public FtpClientSessionFactory getFtpClientSessionFactory() {
		return ftpClientSessionFactory;
	}

	public void setFtpClientSessionFactory(FtpClientSessionFactory FtpClientSessionFactory) {
		this.ftpClientSessionFactory = FtpClientSessionFactory;
	}

	public FtpClientSession getSession() {
		try {
			return ftpClientSessionFactory.openSession();
		} catch (Exception ex) {
			throw new FtpClientTemplateException("Could not open FtpClientSession", ex);
		}
	}

	public void closeSession() {
		try {
			ftpClientSessionFactory.closeSession();
		} catch (Exception ex) {
			throw new FtpClientTemplateException("Could not close FtpClientSession", ex);
		}
	}

	public <T> T execute(FtpClientCallback<T> action) throws FtpClientTemplateException {
		return doExecute(action);
	}

	/**
	 * @param action
	 * @return
	 * @throws FtpClientTemplateException
	 */
	protected <T> T doExecute(FtpClientCallback<T> action) throws FtpClientTemplateException {
		AssertHelper.notNull(action, "The FtpClientCallback must not be null");
		//
		T result = null;
		FtpClientSession session = null;
		try {
			session = getSession();
			result = action.doInAction(session);
			return result;
		} catch (FtpClientTemplateException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new FtpClientTemplateException(ex);
		} finally {
			if (session != null) {
				closeSession();
			}
		}
	}

	// --------------------------------------------------

	public String[] listNames(final String pathname) throws IOException {
		if (pathname == null) {
			return null;
		}
		//
		return execute(new FtpClientCallback<String[]>() {
			public String[] doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.listNames(pathname);
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public String[] listNames() throws IOException {
		return execute(new FtpClientCallback<String[]>() {
			public String[] doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.listNames();
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public FTPFile[] listFiles(final String pathname) throws IOException {
		if (pathname == null) {
			return null;
		}
		//
		return execute(new FtpClientCallback<FTPFile[]>() {
			public FTPFile[] doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.listFiles(pathname);
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public FTPFile[] listFiles() throws IOException {
		return execute(new FtpClientCallback<FTPFile[]>() {
			public FTPFile[] doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.listFiles();
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public FTPFile[] listFiles(final String pathname, final FTPFileFilter filter) throws IOException {
		if (pathname == null || filter == null) {
			return null;
		}
		//
		return execute(new FtpClientCallback<FTPFile[]>() {
			public FTPFile[] doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.listFiles(pathname, filter);
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public FTPFile[] listDirectories() throws IOException {
		return execute(new FtpClientCallback<FTPFile[]>() {
			public FTPFile[] doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.listDirectories();
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public FTPFile[] listDirectories(final String parent) throws IOException {
		if (parent == null) {
			return null;
		}
		//
		return execute(new FtpClientCallback<FTPFile[]>() {
			public FTPFile[] doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.listDirectories(parent);
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	// --------------------------------------------------

	public boolean read(final String remote, final OutputStream local) throws IOException {
		if (remote == null || local == null) {
			return false;
		}
		//
		return execute(new FtpClientCallback<Boolean>() {
			public Boolean doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.retrieveFile(remote, local);
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public boolean write(final String remote, final InputStream local) throws IOException {
		if (remote == null || local == null) {
			return false;
		}
		//
		return execute(new FtpClientCallback<Boolean>() {
			public Boolean doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.storeFile(remote, local);
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public boolean mkdir(final String pathname) throws IOException {
		if (pathname == null) {
			return false;
		}
		//
		return execute(new FtpClientCallback<Boolean>() {
			public Boolean doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.makeDirectory(pathname);
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public boolean delete(final String path) throws IOException {
		if (path == null) {
			return false;
		}
		//
		return execute(new FtpClientCallback<Boolean>() {
			public Boolean doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.deleteFile(path);
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public boolean rename(final String from, final String to) throws IOException {
		if (from == null || to == null) {
			return false;
		}
		//
		return execute(new FtpClientCallback<Boolean>() {
			public Boolean doInAction(FtpClientSession session) throws FtpClientTemplateException {
				try {
					return session.rename(from, to);
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
				}
			}
		});
	}

	public boolean exists(final String path) throws IOException {
		if (path == null) {
			return false;
		}
		//
		return execute(new FtpClientCallback<Boolean>() {
			public Boolean doInAction(FtpClientSession session) throws FtpClientTemplateException {
				String currentWorkingPath = null;
				try {
					boolean exists = false;
					currentWorkingPath = session.printWorkingDirectory();
					if (session.changeWorkingDirectory(path)) {
						exists = true;
					}
					return exists;
				} catch (Exception ex) {
					throw new FtpClientTemplateException(ex);
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
