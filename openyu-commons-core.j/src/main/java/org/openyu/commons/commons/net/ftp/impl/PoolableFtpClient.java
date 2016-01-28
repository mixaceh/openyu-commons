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
		this.delegate.connect(host, port);
	}

	public void connect(String hostname, int port) throws SocketException, IOException {
		this.delegate.connect(hostname, port);
	}

	public void connect(InetAddress host, int port, InetAddress localAddr, int localPort)
			throws SocketException, IOException {
		this.delegate.connect(host, port, localAddr, localPort);
	}

	public void connect(String hostname, int port, InetAddress localAddr, int localPort)
			throws SocketException, IOException {
		this.delegate.connect(hostname, port, localAddr, localPort);
	}

	public void connect(InetAddress host) throws SocketException, IOException {
		this.delegate.connect(host);
	}

	public void connect(String hostname) throws SocketException, IOException {
		this.delegate.connect(hostname);
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
		return this.delegate.sendCommand(command, args);
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

	public void setSocketFactory(SocketFactory factory) {
		this.delegate.setSocketFactory(factory);
	}

	public void setServerSocketFactory(ServerSocketFactory factory) {
		this.delegate.setServerSocketFactory(factory);
	}

	public int sendCommand(int command, String args) throws IOException {
		checkOpen();
		return this.delegate.sendCommand(command, args);
	}

	public int sendCommand(String command) throws IOException {
		checkOpen();
		return this.delegate.sendCommand(command);
	}

	public int sendCommand(int command) throws IOException {
		checkOpen();
		return this.delegate.sendCommand(command);
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
		return this.delegate.user(username);
	}

	public int pass(String password) throws IOException {
		checkOpen();
		return this.delegate.pass(password);
	}

	public int acct(String account) throws IOException {
		checkOpen();
		return this.delegate.acct(account);
	}

	public int abor() throws IOException {
		checkOpen();
		return this.delegate.abor();
	}

	public int cwd(String directory) throws IOException {
		checkOpen();
		return this.delegate.cwd(directory);
	}

	public int cdup() throws IOException {
		checkOpen();
		return this.delegate.cdup();
	}

	public int quit() throws IOException {
		checkOpen();
		return this.delegate.quit();
	}

	public int rein() throws IOException {
		checkOpen();
		return this.delegate.rein();
	}

	public int smnt(String dir) throws IOException {
		checkOpen();
		return this.delegate.smnt(dir);
	}

	public int port(InetAddress host, int port) throws IOException {
		checkOpen();
		return this.delegate.port(host, port);
	}

	public int eprt(InetAddress host, int port) throws IOException {
		checkOpen();
		return this.delegate.eprt(host, port);
	}

	public int pasv() throws IOException {
		checkOpen();
		return this.delegate.pasv();
	}

	public int epsv() throws IOException {
		checkOpen();
		return this.delegate.epsv();
	}

	public int type(int fileType, int formatOrByteSize) throws IOException {
		checkOpen();
		return this.delegate.type(fileType, formatOrByteSize);
	}

	public int type(int fileType) throws IOException {
		checkOpen();
		return this.delegate.type(fileType);
	}

	public int stru(int structure) throws IOException {
		checkOpen();
		return this.delegate.stru(structure);
	}

	public int mode(int mode) throws IOException {
		checkOpen();
		return this.delegate.mode(mode);
	}

	public int retr(String pathname) throws IOException {
		checkOpen();
		return this.delegate.retr(pathname);
	}

	public int stor(String pathname) throws IOException {
		checkOpen();
		return this.delegate.stor(pathname);
	}

	public int stou() throws IOException {
		checkOpen();
		return this.delegate.stou();
	}

	public int stou(String pathname) throws IOException {
		checkOpen();
		return this.delegate.stou(pathname);
	}

	public int appe(String pathname) throws IOException {
		checkOpen();
		return this.delegate.appe(pathname);
	}

	public int allo(int bytes) throws IOException {
		checkOpen();
		return this.delegate.allo(bytes);
	}

	public int feat() throws IOException {
		checkOpen();
		return this.delegate.feat();
	}

	public int allo(int bytes, int recordSize) throws IOException {
		checkOpen();
		return this.delegate.allo(bytes, recordSize);
	}

	public int rest(String marker) throws IOException {
		checkOpen();
		return this.delegate.rest(marker);
	}

	public int mdtm(String file) throws IOException {
		checkOpen();
		return this.delegate.mdtm(file);
	}

	public int mfmt(String pathname, String timeval) throws IOException {
		checkOpen();
		return this.delegate.mfmt(pathname, timeval);
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
		return this.delegate.mkd(pathname);
	}

	public boolean login(String username, String password) throws IOException {
		checkOpen();
		return this.delegate.login(username, password);
	}

	public int pwd() throws IOException {
		checkOpen();
		return this.delegate.pwd();
	}

	public int list() throws IOException {
		checkOpen();
		return this.delegate.list();
	}

	public int list(String pathname) throws IOException {
		checkOpen();
		return this.delegate.list(pathname);
	}

	public int mlsd() throws IOException {
		checkOpen();
		return this.delegate.mlsd();
	}

	public boolean login(String username, String password, String account) throws IOException {
		checkOpen();
		return this.delegate.login(username, password, account);
	}

	public int mlsd(String path) throws IOException {
		checkOpen();
		return this.delegate.mlsd(path);
	}

	public int mlst() throws IOException {
		checkOpen();
		return this.delegate.mlst();
	}

	public int mlst(String path) throws IOException {
		checkOpen();
		return this.delegate.mlst(path);
	}

	public int nlst() throws IOException {
		checkOpen();
		return this.delegate.nlst();
	}

	public int nlst(String pathname) throws IOException {
		checkOpen();
		return this.delegate.nlst(pathname);
	}

	public int site(String parameters) throws IOException {
		checkOpen();
		return this.delegate.site(parameters);
	}

	public boolean logout() throws IOException {
		checkOpen();
		return this.delegate.logout();
	}

	public int syst() throws IOException {
		checkOpen();
		return this.delegate.syst();
	}

	public boolean changeWorkingDirectory(String pathname) throws IOException {
		return this.delegate.changeWorkingDirectory(pathname);
	}

	public int stat() throws IOException {
		checkOpen();
		return this.delegate.stat();
	}

	public int stat(String pathname) throws IOException {
		checkOpen();
		return this.delegate.stat(pathname);
	}

	public boolean changeToParentDirectory() throws IOException {
		checkOpen();
		return this.delegate.changeToParentDirectory();
	}

	public int help() throws IOException {
		checkOpen();
		return this.delegate.help();
	}

	public boolean structureMount(String pathname) throws IOException {
		checkOpen();
		return this.delegate.structureMount(pathname);
	}

	public int help(String command) throws IOException {
		checkOpen();
		return this.delegate.help(command);
	}

	public int noop() throws IOException {
		checkOpen();
		return this.delegate.noop();
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
		return this.delegate.retrieveFile(remote, local);
	}

	public InputStream retrieveFileStream(String remote) throws IOException {
		checkOpen();
		return delegate.retrieveFileStream(remote);
	}

	public boolean storeFile(String remote, InputStream local) throws IOException {
		checkOpen();
		return this.delegate.storeFile(remote, local);
	}

	public OutputStream storeFileStream(String remote) throws IOException {
		checkOpen();
		return this.delegate.storeFileStream(remote);
	}

	public boolean appendFile(String remote, InputStream local) throws IOException {
		checkOpen();
		return this.delegate.appendFile(remote, local);
	}

	public OutputStream appendFileStream(String remote) throws IOException {
		checkOpen();
		return this.delegate.appendFileStream(remote);
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
		return this.delegate.allocate(bytes, recordSize);
	}

	public boolean doCommand(String command, String params) throws IOException {
		checkOpen();
		return this.delegate.doCommand(command, params);
	}

	public String[] doCommandAsStrings(String command, String params) throws IOException {
		checkOpen();
		return this.delegate.doCommandAsStrings(command, params);
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
		return this.delegate.rename(from, to);
	}

	public boolean abort() throws IOException {
		checkOpen();
		return this.delegate.abort();
	}

	public boolean deleteFile(String pathname) throws IOException {
		checkOpen();
		return this.delegate.deleteFile(pathname);
	}

	public boolean removeDirectory(String pathname) throws IOException {
		checkOpen();
		return this.delegate.removeDirectory(pathname);
	}

	public boolean makeDirectory(String pathname) throws IOException {
		checkOpen();
		return this.delegate.makeDirectory(pathname);
	}

	public String printWorkingDirectory() throws IOException {
		checkOpen();
		return delegate.printWorkingDirectory();
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
		return this.delegate.listNames(pathname);
	}

	public String[] listNames() throws IOException {
		checkOpen();
		return this.delegate.listNames();
	}

	public FTPFile[] listFiles(String pathname) throws IOException {
		checkOpen();
		return this.delegate.listFiles(pathname);
	}

	public FTPFile[] listFiles() throws IOException {
		checkOpen();
		return this.delegate.listFiles();
	}

	public FTPFile[] listFiles(String pathname, FTPFileFilter filter) throws IOException {
		checkOpen();
		return this.delegate.listFiles(pathname, filter);
	}

	public FTPFile[] listDirectories() throws IOException {
		checkOpen();
		return this.delegate.listDirectories();
	}

	public FTPFile[] listDirectories(String parent) throws IOException {
		checkOpen();
		return this.delegate.listDirectories(parent);
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

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("closed", closed);
		builder.append("delegate", delegate);
		return builder.toString();
	}
}
