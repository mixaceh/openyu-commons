package org.openyu.commons.fto.commons.net.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.openyu.commons.fto.BaseFto;

/**
 * Apache Commons Net FTP File Transfer Object
 */
public interface FtpClientFto extends BaseFto {

	String[] listNames(String pathname) throws IOException;

	String[] listNames() throws IOException;

	FTPFile[] listFiles(String pathname) throws IOException;

	FTPFile[] listFiles() throws IOException;

	FTPFile[] listFiles(String pathname, FTPFileFilter filter)
			throws IOException;

	FTPFile[] listDirectories() throws IOException;

	FTPFile[] listDirectories(String parent) throws IOException;

	// --------------------------------------------------
	
	boolean read(String remote, OutputStream local);

	boolean write(String remote, InputStream local);

	boolean mkdir(String pathname);

	boolean delete(String path);

	boolean rename(String from, String to);

	boolean exists(String path);

}
