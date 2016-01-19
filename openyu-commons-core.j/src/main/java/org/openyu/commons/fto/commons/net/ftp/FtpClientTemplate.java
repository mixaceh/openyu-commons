package org.openyu.commons.fto.commons.net.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.openyu.commons.commons.net.ftp.FtpClientSession;
import org.openyu.commons.commons.net.ftp.FtpClientSessionFactory;
import org.openyu.commons.fto.commons.net.ftp.ex.FtpClientTemplateException;

public interface FtpClientTemplate {

	FtpClientSessionFactory getFtpClientSessionFactory();

	void setFtpClientSessionFactory(FtpClientSessionFactory ftpClientSessionFactory);

	FtpClientSession getSession();

	void closeSession();

	<T> T execute(FtpClientCallback<T> action) throws FtpClientTemplateException;

	// --------------------------------------------------
	
	String[] listNames(String pathname) throws IOException;

	String[] listNames() throws IOException;

	FTPFile[] listFiles(String pathname) throws IOException;

	FTPFile[] listFiles() throws IOException;

	FTPFile[] listFiles(String pathname, FTPFileFilter filter)
			throws IOException;

	FTPFile[] listDirectories() throws IOException;

	FTPFile[] listDirectories(String parent) throws IOException;

	// --------------------------------------------------
	boolean read(String remote, OutputStream local) throws IOException;

	boolean write(String remote, InputStream local) throws IOException;

	boolean mkdir(String pathname) throws IOException;

	boolean delete(String path) throws IOException;

	boolean rename(String from, String to) throws IOException;

	boolean exists(String path) throws IOException;
}
