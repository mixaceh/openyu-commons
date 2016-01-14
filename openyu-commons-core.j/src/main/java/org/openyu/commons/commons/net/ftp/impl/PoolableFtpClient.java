package org.openyu.commons.commons.net.ftp.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.pool.ObjectPool;
import org.openyu.commons.util.Delegateable;

public class PoolableFtpClient extends FTPClient implements Delegateable<FTPClient> {

	private boolean closed = false;

	private FTPClient delegate;

	private ObjectPool<FTPClient> pool = null;

	private static final int DEFAULT_PORT = 21;

	public PoolableFtpClient(FTPClient delegate, ObjectPool<FTPClient> pool) {
		this.delegate = delegate;
		this.pool = pool;
		// #fix
		setDefaultPort(DEFAULT_PORT);
	}

	public FTPClient getDelegate() {
		return delegate;
	}

	protected void checkOpen() throws SocketException, IOException {
		if (this.closed) {
			if (null != this.delegate) {
				String label = "";
				try {
					label = this.delegate.toString();
				} catch (Exception ex) {
				}
				throw new SocketException("FTPClient " + label + " was already closed");
			}
			throw new SocketException("FTPClient is null");
		}
	}

	/**
	 * 返回pool
	 */
	public synchronized void disconnect() {
		if (this.closed) {
			return;
		}
		boolean isUnderlyingConectionClosed;
		try {
			isUnderlyingConectionClosed = !(this.delegate.isConnected());
		} catch (Exception e) {
			try {
				this.pool.invalidateObject(this);
			} catch (Exception ex) {
				this.closed = true;
				reallyClose();
			}
			throw new RuntimeException("Cannot close FTPClient (isClosed check failed)");
		}
		if (!(isUnderlyingConectionClosed)) {
			try {
				this.pool.returnObject(this);
				// }
			} catch (Exception e) {
				this.closed = true;
				reallyClose();
				throw new RuntimeException("Cannot close FTPClient (return to pool failed)");
			}
		} else {
			try {
				this.pool.invalidateObject(this);
			} catch (Exception e) {
				this.closed = true;
				reallyClose();
			}
			throw new RuntimeException("Already closed.");
		}
	}

	/**
	 * 真正關閉連線
	 */
	public synchronized void reallyClose() {
		try {
			delegate.disconnect();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public boolean isConnected() {
		return (!closed) || (this.delegate.isConnected());
	}

	public void connect(InetAddress host, int port) throws SocketException, IOException {
		try {
			this.delegate.connect(host, port);
		} catch (SocketException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void connect(String hostname, int port) throws SocketException, IOException {
		try {
			this.delegate.connect(hostname, port);
		} catch (SocketException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void connect(InetAddress host, int port, InetAddress localAddr, int localPort)
			throws SocketException, IOException {
		try {
			this.delegate.connect(host, port, localAddr, localPort);
		} catch (SocketException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void connect(String hostname, int port, InetAddress localAddr, int localPort)
			throws SocketException, IOException {
		try {
			this.delegate.connect(hostname, port, localAddr, localPort);
		} catch (SocketException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void connect(InetAddress host) throws SocketException, IOException {
		try {
			this.delegate.connect(host);
		} catch (SocketException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void connect(String hostname) throws SocketException, IOException {
		try {
			this.delegate.connect(hostname);
		} catch (SocketException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public boolean isAvailable() {
		return this.delegate.isAvailable();
	}

	public void setDefaultPort(int port) {
		if (this.delegate != null) {
			this.delegate.setDefaultPort(port);
		}
	}

	public int getDefaultPort() {
		return this.delegate.getDefaultPort();
	}

	public void setDefaultTimeout(int timeout) {
		this.delegate.setDefaultTimeout(timeout);
	}

	public int getDefaultTimeout() {
		return this.delegate.getDefaultTimeout();
	}

	public void setSoTimeout(int timeout) throws SocketException {
		this.delegate.setSoTimeout(timeout);
	}

	public void setSendBufferSize(int size) throws SocketException {
		this.delegate.setSendBufferSize(size);
	}

	public void setControlEncoding(String encoding) {
		this.delegate.setControlEncoding(encoding);
	}

	public String getControlEncoding() {
		return this.delegate.getControlEncoding();
	}

	public void setReceiveBufferSize(int size) throws SocketException {
		this.delegate.setReceiveBufferSize(size);
	}

	public int getSoTimeout() throws SocketException {
		return this.delegate.getSoTimeout();
	}

	public int sendCommand(String command, String args) throws IOException {
		checkOpen();
		try {
			return this.delegate.sendCommand(command, args);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public void setTcpNoDelay(boolean on) throws SocketException {
		this.delegate.setTcpNoDelay(on);
	}

	public boolean getTcpNoDelay() throws SocketException {
		return this.delegate.getTcpNoDelay();
	}

	public void setKeepAlive(boolean keepAlive) throws SocketException {
		this.delegate.setKeepAlive(keepAlive);
	}

	public boolean getKeepAlive() throws SocketException {
		return this.delegate.getKeepAlive();
	}

	public void setSoLinger(boolean on, int val) throws SocketException {
		this.delegate.setSoLinger(on, val);
	}

	public int getSoLinger() throws SocketException {
		return this.delegate.getSoLinger();
	}

	public int getLocalPort() {
		return this.delegate.getLocalPort();
	}

	public InetAddress getLocalAddress() {
		return this.delegate.getLocalAddress();
	}

	public int getRemotePort() {
		return this.delegate.getRemotePort();
	}

	public InetAddress getRemoteAddress() {
		return this.delegate.getRemoteAddress();
	}

	public boolean verifyRemote(Socket socket) {
		return this.delegate.verifyRemote(socket);
	}

	public int sendCommand(int command, String args) throws IOException {
		checkOpen();
		try {
			return this.delegate.sendCommand(command, args);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public void setSocketFactory(SocketFactory factory) {
		this.delegate.setSocketFactory(factory);
	}

	public int sendCommand(String command) throws IOException {
		checkOpen();
		try {
			return this.delegate.sendCommand(command);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public void setServerSocketFactory(ServerSocketFactory factory) {
		this.delegate.setServerSocketFactory(factory);
	}

	public int sendCommand(int command) throws IOException {
		checkOpen();
		try {
			return this.delegate.sendCommand(command);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int getReplyCode() {
		return this.delegate.getReplyCode();
	}

	public int getReply() throws IOException {
		return this.delegate.getReply();
	}

	public void setConnectTimeout(int connectTimeout) {
		this.delegate.setConnectTimeout(connectTimeout);
	}

	public String[] getReplyStrings() {
		return this.delegate.getReplyStrings();
	}

	public int getConnectTimeout() {
		return this.delegate.getConnectTimeout();
	}

	public ServerSocketFactory getServerSocketFactory() {
		return this.delegate.getServerSocketFactory();
	}

	public String getReplyString() {
		return this.delegate.getReplyString();
	}

	public void addProtocolCommandListener(ProtocolCommandListener listener) {
		this.delegate.addProtocolCommandListener(listener);
	}

	public void removeProtocolCommandListener(ProtocolCommandListener listener) {
		this.delegate.removeProtocolCommandListener(listener);
	}

	public int user(String username) throws IOException {
		checkOpen();
		try {
			return this.delegate.user(username);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int pass(String password) throws IOException {
		checkOpen();
		try {
			return this.delegate.pass(password);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int acct(String account) throws IOException {
		checkOpen();
		try {
			return this.delegate.acct(account);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int abor() throws IOException {
		checkOpen();
		try {
			return this.delegate.abor();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int cwd(String directory) throws IOException {
		checkOpen();
		try {
			return this.delegate.cwd(directory);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int cdup() throws IOException {
		checkOpen();
		try {
			return this.delegate.cdup();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int quit() throws IOException {
		checkOpen();
		try {
			return this.delegate.quit();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int rein() throws IOException {
		checkOpen();
		try {
			return this.delegate.rein();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int smnt(String dir) throws IOException {
		checkOpen();
		try {
			return this.delegate.smnt(dir);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int port(InetAddress host, int port) throws IOException {
		checkOpen();
		try {
			return this.delegate.port(host, port);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int eprt(InetAddress host, int port) throws IOException {
		checkOpen();
		try {
			return this.delegate.eprt(host, port);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int pasv() throws IOException {
		checkOpen();
		try {
			return this.delegate.pasv();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int epsv() throws IOException {
		checkOpen();
		try {
			return this.delegate.epsv();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int type(int fileType, int formatOrByteSize) throws IOException {
		checkOpen();
		try {
			return this.delegate.type(fileType, formatOrByteSize);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int type(int fileType) throws IOException {
		checkOpen();
		try {
			return this.delegate.type(fileType);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int stru(int structure) throws IOException {
		checkOpen();
		try {
			return this.delegate.stru(structure);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int mode(int mode) throws IOException {
		checkOpen();
		try {
			return this.delegate.mode(mode);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int retr(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.retr(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int stor(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.stor(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int stou() throws IOException {
		checkOpen();
		try {
			return this.delegate.stou();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int stou(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.stou(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int appe(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.appe(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int allo(int bytes) throws IOException {
		checkOpen();
		try {
			return this.delegate.allo(bytes);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int feat() throws IOException {
		checkOpen();
		try {
			return this.delegate.feat();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int allo(int bytes, int recordSize) throws IOException {
		checkOpen();
		try {
			return this.delegate.allo(bytes, recordSize);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int rest(String marker) throws IOException {
		checkOpen();
		try {
			return this.delegate.rest(marker);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int mdtm(String file) throws IOException {
		checkOpen();
		try {
			return this.delegate.mdtm(file);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int mfmt(String pathname, String timeval) throws IOException {
		checkOpen();
		try {
			return this.delegate.mfmt(pathname, timeval);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public void setDataTimeout(int timeout) {
		this.delegate.setDataTimeout(timeout);
	}

	public void setParserFactory(FTPFileEntryParserFactory parserFactory) {
		this.delegate.setParserFactory(parserFactory);
	}

	public int rnfr(String pathname) throws IOException {
		return this.delegate.rnfr(pathname);
	}

	public int rnto(String pathname) throws IOException {
		return this.delegate.rnto(pathname);
	}

	public int dele(String pathname) throws IOException {
		return this.delegate.dele(pathname);
	}

	public void setRemoteVerificationEnabled(boolean enable) {
		this.delegate.setRemoteVerificationEnabled(enable);
	}

	public int rmd(String pathname) throws IOException {
		return this.delegate.rmd(pathname);
	}

	public boolean isRemoteVerificationEnabled() {
		return this.delegate.isRemoteVerificationEnabled();
	}

	public int mkd(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.mkd(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public boolean login(String username, String password) throws IOException {
		checkOpen();
		try {
			return this.delegate.login(username, password);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public int pwd() throws IOException {
		checkOpen();
		try {
			return this.delegate.pwd();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int list() throws IOException {
		checkOpen();
		try {
			return this.delegate.list();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int list(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.list(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int mlsd() throws IOException {
		checkOpen();
		try {
			return this.delegate.mlsd();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public boolean login(String username, String password, String account) throws IOException {
		checkOpen();
		try {
			return this.delegate.login(username, password, account);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public int mlsd(String path) throws IOException {
		checkOpen();
		try {
			return this.delegate.mlsd(path);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int mlst() throws IOException {
		checkOpen();
		try {
			return this.delegate.mlst();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int mlst(String path) throws IOException {
		checkOpen();
		try {
			return this.delegate.mlst(path);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int nlst() throws IOException {
		checkOpen();
		try {
			return this.delegate.nlst();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int nlst(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.nlst(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int site(String parameters) throws IOException {
		checkOpen();
		try {
			return this.delegate.site(parameters);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public boolean logout() throws IOException {
		checkOpen();
		try {
			return this.delegate.logout();
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public int syst() throws IOException {
		checkOpen();
		try {
			return this.delegate.syst();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public boolean changeWorkingDirectory(String pathname) throws IOException {
		return this.delegate.changeWorkingDirectory(pathname);
	}

	public int stat() throws IOException {
		checkOpen();
		try {
			return this.delegate.stat();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int stat(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.stat(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public boolean changeToParentDirectory() throws IOException {
		checkOpen();
		try {
			return this.delegate.changeToParentDirectory();
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public int help() throws IOException {
		checkOpen();
		try {
			return this.delegate.help();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public boolean structureMount(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.structureMount(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public int help(String command) throws IOException {
		checkOpen();
		try {
			return this.delegate.help(command);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int noop() throws IOException {
		checkOpen();
		try {
			return this.delegate.noop();
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public boolean isStrictMultilineParsing() {
		return this.delegate.isStrictMultilineParsing();
	}

	public void setStrictMultilineParsing(boolean strictMultilineParsing) {
		this.delegate.setStrictMultilineParsing(strictMultilineParsing);
	}

	public void enterLocalActiveMode() {
		this.delegate.enterLocalActiveMode();
	}

	public void enterLocalPassiveMode() {
		this.delegate.enterLocalPassiveMode();
	}

	public boolean enterRemoteActiveMode(InetAddress host, int port) throws IOException {
		return this.delegate.enterRemoteActiveMode(host, port);
	}

	public boolean enterRemotePassiveMode() throws IOException {
		return this.delegate.enterRemotePassiveMode();
	}

	public String getPassiveHost() {
		return this.delegate.getPassiveHost();
	}

	public int getPassivePort() {
		return this.delegate.getPassivePort();
	}

	public int getDataConnectionMode() {
		return this.delegate.getDataConnectionMode();
	}

	public void setActivePortRange(int minPort, int maxPort) {
		this.delegate.setActivePortRange(minPort, maxPort);
	}

	public void setActiveExternalIPAddress(String ipAddress) throws UnknownHostException {
		this.delegate.setActiveExternalIPAddress(ipAddress);
	}

	public boolean setFileType(int fileType) throws IOException {
		return this.delegate.setFileType(fileType);
	}

	public boolean setFileType(int fileType, int formatOrByteSize) throws IOException {
		return this.delegate.setFileType(fileType, formatOrByteSize);
	}

	public boolean setFileStructure(int structure) throws IOException {
		return this.delegate.setFileStructure(structure);
	}

	public boolean setFileTransferMode(int mode) throws IOException {
		return this.delegate.setFileTransferMode(mode);
	}

	public boolean remoteRetrieve(String filename) throws IOException {
		return this.delegate.remoteRetrieve(filename);
	}

	public boolean remoteStore(String filename) throws IOException {
		return this.delegate.remoteStore(filename);
	}

	public boolean remoteStoreUnique(String filename) throws IOException {
		return this.delegate.remoteStoreUnique(filename);
	}

	public boolean remoteStoreUnique() throws IOException {
		return this.delegate.remoteStoreUnique();
	}

	public boolean remoteAppend(String filename) throws IOException {
		return this.delegate.remoteAppend(filename);
	}

	public boolean completePendingCommand() throws IOException {
		return this.delegate.completePendingCommand();
	}

	public boolean retrieveFile(String remote, OutputStream local) throws IOException {
		checkOpen();
		try {
			return this.delegate.retrieveFile(remote, local);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public InputStream retrieveFileStream(String remote) throws IOException {
		checkOpen();
		try {
			return delegate.retrieveFileStream(remote);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public boolean storeFile(String remote, InputStream local) throws IOException {
		checkOpen();
		try {
			return this.delegate.storeFile(remote, local);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public OutputStream storeFileStream(String remote) throws IOException {
		checkOpen();
		try {
			return this.delegate.storeFileStream(remote);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public boolean appendFile(String remote, InputStream local) throws IOException {
		checkOpen();
		try {
			return this.delegate.appendFile(remote, local);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public OutputStream appendFileStream(String remote) throws IOException {
		checkOpen();
		try {
			return this.delegate.appendFileStream(remote);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public boolean storeUniqueFile(String remote, InputStream local) throws IOException {
		return this.delegate.storeUniqueFile(remote, local);
	}

	public OutputStream storeUniqueFileStream(String remote) throws IOException {
		return this.delegate.storeUniqueFileStream(remote);
	}

	public boolean storeUniqueFile(InputStream local) throws IOException {
		return this.delegate.storeUniqueFile(local);
	}

	public OutputStream storeUniqueFileStream() throws IOException {
		return this.delegate.storeUniqueFileStream();
	}

	public boolean allocate(int bytes) throws IOException {
		return this.delegate.allocate(bytes);
	}

	public boolean features() throws IOException {
		return this.delegate.features();
	}

	public String[] featureValues(String feature) throws IOException {
		return this.delegate.featureValues(feature);
	}

	public String featureValue(String feature) throws IOException {
		return this.delegate.featureValue(feature);
	}

	public boolean hasFeature(String feature) throws IOException {
		return this.delegate.hasFeature(feature);
	}

	public boolean hasFeature(String feature, String value) throws IOException {
		return this.delegate.hasFeature(feature, value);
	}

	public boolean allocate(int bytes, int recordSize) throws IOException {
		checkOpen();
		try {
			return this.delegate.allocate(bytes, recordSize);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean doCommand(String command, String params) throws IOException {
		checkOpen();
		try {
			return this.delegate.doCommand(command, params);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public String[] doCommandAsStrings(String command, String params) throws IOException {
		checkOpen();
		try {
			return this.delegate.doCommandAsStrings(command, params);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public FTPFile mlistFile(String pathname) throws IOException {
		return this.delegate.mlistFile(pathname);
	}

	public FTPFile[] mlistDir() throws IOException {
		return this.delegate.mlistDir();
	}

	public FTPFile[] mlistDir(String pathname) throws IOException {
		return this.delegate.mlistDir(pathname);
	}

	public FTPFile[] mlistDir(String pathname, FTPFileFilter filter) throws IOException {
		return this.delegate.mlistDir(pathname, filter);
	}

	public void setRestartOffset(long offset) {
		this.delegate.setRestartOffset(offset);
	}

	public long getRestartOffset() {
		return this.delegate.getRestartOffset();
	}

	public boolean rename(String from, String to) throws IOException {
		checkOpen();
		try {
			return this.delegate.rename(from, to);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean abort() throws IOException {
		checkOpen();
		try {
			return this.delegate.abort();
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean deleteFile(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.deleteFile(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean removeDirectory(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.removeDirectory(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public boolean makeDirectory(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.makeDirectory(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return false;
	}

	public String printWorkingDirectory() throws IOException {
		checkOpen();
		try {
			return delegate.printWorkingDirectory();
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public boolean sendSiteCommand(String arguments) throws IOException {
		return this.delegate.sendSiteCommand(arguments);
	}

	public String getSystemType() throws IOException {
		return this.delegate.getSystemType();
	}

	public String listHelp() throws IOException {
		return this.delegate.listHelp();
	}

	public String listHelp(String command) throws IOException {
		return this.delegate.listHelp(command);
	}

	public boolean sendNoOp() throws IOException {
		return this.delegate.sendNoOp();
	}

	public String[] listNames(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.listNames(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public String[] listNames() throws IOException {
		checkOpen();
		try {
			return this.delegate.listNames();
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public FTPFile[] listFiles(String pathname) throws IOException {
		checkOpen();
		try {
			return this.delegate.listFiles(pathname);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public FTPFile[] listFiles() throws IOException {
		checkOpen();
		try {
			return this.delegate.listFiles();
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public FTPFile[] listFiles(String pathname, FTPFileFilter filter) throws IOException {
		checkOpen();
		try {
			return this.delegate.listFiles(pathname, filter);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public FTPFile[] listDirectories() throws IOException {
		checkOpen();
		try {
			return this.delegate.listDirectories();
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public FTPFile[] listDirectories(String parent) throws IOException {
		checkOpen();
		try {
			return this.delegate.listDirectories(parent);
		} catch (IOException e) {
			handleException(e);
		}
		return null;
	}

	public FTPListParseEngine initiateListParsing() throws IOException {
		return this.delegate.initiateListParsing();
	}

	public FTPListParseEngine initiateListParsing(String pathname) throws IOException {
		return this.delegate.initiateListParsing(pathname);
	}

	public FTPListParseEngine initiateListParsing(String parserKey, String pathname) throws IOException {
		return this.delegate.initiateListParsing(parserKey, pathname);
	}

	public String getStatus() throws IOException {
		return this.delegate.getStatus();
	}

	public String getStatus(String pathname) throws IOException {
		return this.delegate.getStatus(pathname);
	}

	public String getModificationTime(String pathname) throws IOException {
		return this.delegate.getModificationTime(pathname);
	}

	public boolean setModificationTime(String pathname, String timeval) throws IOException {
		return this.delegate.setModificationTime(pathname, timeval);
	}

	public void setBufferSize(int bufSize) {
		this.delegate.setBufferSize(bufSize);
	}

	public int getBufferSize() {
		return this.delegate.getBufferSize();
	}

	public void configure(FTPClientConfig config) {
		this.delegate.configure(config);
	}

	public void setListHiddenFiles(boolean listHiddenFiles) {
		this.delegate.setListHiddenFiles(listHiddenFiles);
	}

	public boolean getListHiddenFiles() {
		return this.delegate.getListHiddenFiles();
	}

	public boolean isUseEPSVwithIPv4() {
		return this.delegate.isUseEPSVwithIPv4();
	}

	public void setUseEPSVwithIPv4(boolean selected) {
		this.delegate.setUseEPSVwithIPv4(selected);
	}

	public void setCopyStreamListener(CopyStreamListener listener) {
		this.delegate.setCopyStreamListener(listener);
	}

	public CopyStreamListener getCopyStreamListener() {
		return this.delegate.getCopyStreamListener();
	}

	public void setControlKeepAliveTimeout(long controlIdle) {
		this.delegate.setControlKeepAliveTimeout(controlIdle);
	}

	public long getControlKeepAliveTimeout() {
		return this.delegate.getControlKeepAliveTimeout();
	}

	public void setControlKeepAliveReplyTimeout(int timeout) {
		this.delegate.setControlKeepAliveReplyTimeout(timeout);
	}

	public int getControlKeepAliveReplyTimeout() {
		return this.delegate.getControlKeepAliveReplyTimeout();
	}

	public void setAutodetectUTF8(boolean autodetect) {
		this.delegate.setAutodetectUTF8(autodetect);
	}

	public boolean getAutodetectUTF8() {
		return this.delegate.getAutodetectUTF8();
	}

	public String getSystemName() throws IOException {
		return this.delegate.getSystemName();
	}

	protected void handleException(IOException e) throws IOException {
		throw e;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("closed", closed);
		builder.append("delegate", delegate);
		return builder.toString();
	}
}
