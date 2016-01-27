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
import org.apache.hadoop.hbase.TableNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.commons.net.ftp.FtpClientSession;
import org.openyu.commons.commons.net.ftp.ex.FtpClientSessionException;

public class FtpClientSessionImpl extends BaseModelSupporter implements
		FtpClientSession {

	private static final long serialVersionUID = 7538640140017769305L;

	private static transient final Logger LOGGER = LoggerFactory
			.getLogger(FtpClientSessionImpl.class);

	protected boolean closed = false;

	private transient FtpClientSessionFactoryImpl ftpClientSessionFactoryImpl;

	protected FTPClient delegate;

	public FtpClientSessionImpl(FtpClientSessionFactoryImpl ftpClientSessionFactoryImpl,
			FTPClient ftpClient) {
		this.ftpClientSessionFactoryImpl = ftpClientSessionFactoryImpl;
		this.delegate = ftpClient;
	}

	public boolean isClosed() {
		return this.closed;
	}

	protected void errorIfClosed() {
		if (this.closed)
			throw new FtpClientSessionException("FtpClientSession was already closed");
	}

	public void close() throws FtpClientSessionException {
		if (isClosed()) {
			throw new FtpClientSessionException(
					"FtpClientSession was already closed");
		}
		this.closed = true;
		//
		try {
			this.ftpClientSessionFactoryImpl.closeSession();
		} catch (Exception ex) {
			throw new FtpClientSessionException("Cannot close FtpClientSession");
		}
	}

	public boolean isConnected() {
		return (!isClosed()) && (this.delegate != null)
				&& (this.delegate.isConnected());
	}

	public void connect(InetAddress host, int port) throws SocketException,
			IOException {
		delegate.connect(host, port);
	}

	public void connect(String hostname, int port) throws SocketException,
			IOException {
		delegate.connect(hostname, port);
	}

	public void connect(InetAddress host, int port, InetAddress localAddr,
			int localPort) throws SocketException, IOException {
		delegate.connect(host, port, localAddr, localPort);
	}

	public void connect(String hostname, int port, InetAddress localAddr,
			int localPort) throws SocketException, IOException {
		delegate.connect(hostname, port, localAddr, localPort);
	}

	public void connect(InetAddress host) throws SocketException, IOException {
		delegate.connect(host);
	}

	public void connect(String hostname) throws SocketException, IOException {
		delegate.connect(hostname);
	}

	public boolean isAvailable() {
		return delegate.isAvailable();
	}

	public void setDefaultPort(int port) {
		delegate.setDefaultPort(port);
	}

	public int getDefaultPort() {
		return delegate.getDefaultPort();
	}

	public void setDefaultTimeout(int timeout) {
		delegate.setDefaultTimeout(timeout);
	}

	public int getDefaultTimeout() {
		return delegate.getDefaultTimeout();
	}

	public void setSoTimeout(int timeout) throws SocketException {
		delegate.setSoTimeout(timeout);
	}

	public void setSendBufferSize(int size) throws SocketException {
		delegate.setSendBufferSize(size);
	}

	public void setControlEncoding(String encoding) {
		delegate.setControlEncoding(encoding);
	}

	public String getControlEncoding() {
		return delegate.getControlEncoding();
	}

	public void setReceiveBufferSize(int size) throws SocketException {
		delegate.setReceiveBufferSize(size);
	}

	public int getSoTimeout() throws SocketException {
		return delegate.getSoTimeout();
	}

	public int sendCommand(String command, String args) throws IOException {
		errorIfClosed();
		try {
			return delegate.sendCommand(command, args);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public void setTcpNoDelay(boolean on) throws SocketException {
		delegate.setTcpNoDelay(on);
	}

	public boolean getTcpNoDelay() throws SocketException {
		return delegate.getTcpNoDelay();
	}

	public void setKeepAlive(boolean keepAlive) throws SocketException {
		delegate.setKeepAlive(keepAlive);
	}

	public boolean getKeepAlive() throws SocketException {
		return delegate.getKeepAlive();
	}

	public void setSoLinger(boolean on, int val) throws SocketException {
		delegate.setSoLinger(on, val);
	}

	public int getSoLinger() throws SocketException {
		return delegate.getSoLinger();
	}

	public int getLocalPort() {
		return delegate.getLocalPort();
	}

	public InetAddress getLocalAddress() {
		return delegate.getLocalAddress();
	}

	public int getRemotePort() {
		return delegate.getRemotePort();
	}

	public InetAddress getRemoteAddress() {
		return delegate.getRemoteAddress();
	}

	public boolean verifyRemote(Socket socket) {
		return delegate.verifyRemote(socket);
	}

	public int sendCommand(int command, String args) throws IOException {
		errorIfClosed();
		try {
			return delegate.sendCommand(command, args);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public void setSocketFactory(SocketFactory factory) {
		delegate.setSocketFactory(factory);
	}

	public int sendCommand(String command) throws IOException {
		errorIfClosed();
		try {
			return delegate.sendCommand(command);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public void setServerSocketFactory(ServerSocketFactory factory) {
		delegate.setServerSocketFactory(factory);
	}

	public int sendCommand(int command) throws IOException {
		errorIfClosed();
		try {
			return delegate.sendCommand(command);
		} catch (IOException e) {
			handleException(e);
		}
		return 0;
	}

	public int getReplyCode() {
		return delegate.getReplyCode();
	}

	public int getReply() throws IOException {
		return delegate.getReply();
	}

	public void setConnectTimeout(int connectTimeout) {
		delegate.setConnectTimeout(connectTimeout);
	}

	public String[] getReplyStrings() {
		return delegate.getReplyStrings();
	}

	public int getConnectTimeout() {
		return delegate.getConnectTimeout();
	}

	public ServerSocketFactory getServerSocketFactory() {
		return delegate.getServerSocketFactory();
	}

	public String getReplyString() {
		return delegate.getReplyString();
	}

	public void addProtocolCommandListener(ProtocolCommandListener listener) {
		delegate.addProtocolCommandListener(listener);
	}

	public void removeProtocolCommandListener(ProtocolCommandListener listener) {
		delegate.removeProtocolCommandListener(listener);
	}

	public int user(String username) throws IOException {
		return delegate.user(username);
	}

	public int pass(String password) throws IOException {
		return delegate.pass(password);
	}

	public int acct(String account) throws IOException {
		return delegate.acct(account);
	}

	public int abor() throws IOException {
		return delegate.abor();
	}

	public int cwd(String directory) throws IOException {
		return delegate.cwd(directory);
	}

	public int cdup() throws IOException {
		return delegate.cdup();
	}

	public int quit() throws IOException {
		return delegate.quit();
	}

	public int rein() throws IOException {
		return delegate.rein();
	}

	public int smnt(String dir) throws IOException {
		return delegate.smnt(dir);
	}

	public int port(InetAddress host, int port) throws IOException {
		return delegate.port(host, port);
	}

	public int eprt(InetAddress host, int port) throws IOException {
		return delegate.eprt(host, port);
	}

	public int pasv() throws IOException {
		return delegate.pasv();
	}

	public int epsv() throws IOException {
		return delegate.epsv();
	}

	public int type(int fileType, int formatOrByteSize) throws IOException {
		return delegate.type(fileType, formatOrByteSize);
	}

	public int type(int fileType) throws IOException {
		return delegate.type(fileType);
	}

	public int stru(int structure) throws IOException {
		return delegate.stru(structure);
	}

	public int mode(int mode) throws IOException {
		return delegate.mode(mode);
	}

	public int retr(String pathname) throws IOException {
		return delegate.retr(pathname);
	}

	public int stor(String pathname) throws IOException {
		return delegate.stor(pathname);
	}

	public int stou() throws IOException {
		return delegate.stou();
	}

	public int stou(String pathname) throws IOException {
		return delegate.stou(pathname);
	}

	public int appe(String pathname) throws IOException {
		return delegate.appe(pathname);
	}

	public int allo(int bytes) throws IOException {
		return delegate.allo(bytes);
	}

	public int feat() throws IOException {
		return delegate.feat();
	}

	public int allo(int bytes, int recordSize) throws IOException {
		return delegate.allo(bytes, recordSize);
	}

	public int rest(String marker) throws IOException {
		return delegate.rest(marker);
	}

	public int mdtm(String file) throws IOException {
		return delegate.mdtm(file);
	}

	public int mfmt(String pathname, String timeval) throws IOException {
		return delegate.mfmt(pathname, timeval);
	}

	public void setDataTimeout(int timeout) {
		delegate.setDataTimeout(timeout);
	}

	public void setParserFactory(FTPFileEntryParserFactory parserFactory) {
		delegate.setParserFactory(parserFactory);
	}

	public int rnfr(String pathname) throws IOException {
		return delegate.rnfr(pathname);
	}

	public int rnto(String pathname) throws IOException {
		return delegate.rnto(pathname);
	}

	public void disconnect() throws IOException {
		delegate.disconnect();
	}

	public int dele(String pathname) throws IOException {
		return delegate.dele(pathname);
	}

	public void setRemoteVerificationEnabled(boolean enable) {
		delegate.setRemoteVerificationEnabled(enable);
	}

	public int rmd(String pathname) throws IOException {
		return delegate.rmd(pathname);
	}

	public boolean isRemoteVerificationEnabled() {
		return delegate.isRemoteVerificationEnabled();
	}

	public int mkd(String pathname) throws IOException {
		return delegate.mkd(pathname);
	}

	public boolean login(String username, String password) throws IOException {
		return delegate.login(username, password);
	}

	public int pwd() throws IOException {
		return delegate.pwd();
	}

	public int list() throws IOException {
		return delegate.list();
	}

	public int list(String pathname) throws IOException {
		return delegate.list(pathname);
	}

	public int mlsd() throws IOException {
		return delegate.mlsd();
	}

	public boolean login(String username, String password, String account)
			throws IOException {
		return delegate.login(username, password, account);
	}

	public int mlsd(String path) throws IOException {
		return delegate.mlsd(path);
	}

	public int mlst() throws IOException {
		return delegate.mlst();
	}

	public int mlst(String path) throws IOException {
		return delegate.mlst(path);
	}

	public int nlst() throws IOException {
		return delegate.nlst();
	}

	public int nlst(String pathname) throws IOException {
		return delegate.nlst(pathname);
	}

	public int site(String parameters) throws IOException {
		return delegate.site(parameters);
	}

	public boolean logout() throws IOException {
		return delegate.logout();
	}

	public int syst() throws IOException {
		return delegate.syst();
	}

	public boolean changeWorkingDirectory(String pathname) throws IOException {
		return delegate.changeWorkingDirectory(pathname);
	}

	public int stat() throws IOException {
		return delegate.stat();
	}

	public int stat(String pathname) throws IOException {
		return delegate.stat(pathname);
	}

	public boolean changeToParentDirectory() throws IOException {
		return delegate.changeToParentDirectory();
	}

	public int help() throws IOException {
		return delegate.help();
	}

	public boolean structureMount(String pathname) throws IOException {
		return delegate.structureMount(pathname);
	}

	public int help(String command) throws IOException {
		return delegate.help(command);
	}

	public int noop() throws IOException {
		return delegate.noop();
	}

	public boolean isStrictMultilineParsing() {
		return delegate.isStrictMultilineParsing();
	}

	public void setStrictMultilineParsing(boolean strictMultilineParsing) {
		delegate.setStrictMultilineParsing(strictMultilineParsing);
	}

	public void enterLocalActiveMode() {
		delegate.enterLocalActiveMode();
	}

	public void enterLocalPassiveMode() {
		delegate.enterLocalPassiveMode();
	}

	public boolean enterRemoteActiveMode(InetAddress host, int port)
			throws IOException {
		return delegate.enterRemoteActiveMode(host, port);
	}

	public boolean enterRemotePassiveMode() throws IOException {
		return delegate.enterRemotePassiveMode();
	}

	public String getPassiveHost() {
		return delegate.getPassiveHost();
	}

	public int getPassivePort() {
		return delegate.getPassivePort();
	}

	public int getDataConnectionMode() {
		return delegate.getDataConnectionMode();
	}

	public void setActivePortRange(int minPort, int maxPort) {
		delegate.setActivePortRange(minPort, maxPort);
	}

	public void setActiveExternalIPAddress(String ipAddress)
			throws UnknownHostException {
		delegate.setActiveExternalIPAddress(ipAddress);
	}

	public boolean setFileType(int fileType) throws IOException {
		return delegate.setFileType(fileType);
	}

	public boolean setFileType(int fileType, int formatOrByteSize)
			throws IOException {
		return delegate.setFileType(fileType, formatOrByteSize);
	}

	public boolean setFileStructure(int structure) throws IOException {
		return delegate.setFileStructure(structure);
	}

	public boolean setFileTransferMode(int mode) throws IOException {
		return delegate.setFileTransferMode(mode);
	}

	public boolean remoteRetrieve(String filename) throws IOException {
		return delegate.remoteRetrieve(filename);
	}

	public boolean remoteStore(String filename) throws IOException {
		return delegate.remoteStore(filename);
	}

	public boolean remoteStoreUnique(String filename) throws IOException {
		return delegate.remoteStoreUnique(filename);
	}

	public boolean remoteStoreUnique() throws IOException {
		return delegate.remoteStoreUnique();
	}

	public boolean remoteAppend(String filename) throws IOException {
		return delegate.remoteAppend(filename);
	}

	public boolean completePendingCommand() throws IOException {
		return delegate.completePendingCommand();
	}

	public boolean retrieveFile(String remote, OutputStream local)
			throws IOException {
		return delegate.retrieveFile(remote, local);
	}

	public InputStream retrieveFileStream(String remote) throws IOException {
		return delegate.retrieveFileStream(remote);
	}

	public boolean storeFile(String remote, InputStream local)
			throws IOException {
		return delegate.storeFile(remote, local);
	}

	public OutputStream storeFileStream(String remote) throws IOException {
		return delegate.storeFileStream(remote);
	}

	public boolean appendFile(String remote, InputStream local)
			throws IOException {
		return delegate.appendFile(remote, local);
	}

	public OutputStream appendFileStream(String remote) throws IOException {
		return delegate.appendFileStream(remote);
	}

	public boolean storeUniqueFile(String remote, InputStream local)
			throws IOException {
		return delegate.storeUniqueFile(remote, local);
	}

	public OutputStream storeUniqueFileStream(String remote) throws IOException {
		return delegate.storeUniqueFileStream(remote);
	}

	public boolean storeUniqueFile(InputStream local) throws IOException {
		return delegate.storeUniqueFile(local);
	}

	public OutputStream storeUniqueFileStream() throws IOException {
		return delegate.storeUniqueFileStream();
	}

	public boolean allocate(int bytes) throws IOException {
		return delegate.allocate(bytes);
	}

	public boolean features() throws IOException {
		return delegate.features();
	}

	public String[] featureValues(String feature) throws IOException {
		return delegate.featureValues(feature);
	}

	public String featureValue(String feature) throws IOException {
		return delegate.featureValue(feature);
	}

	public boolean hasFeature(String feature) throws IOException {
		return delegate.hasFeature(feature);
	}

	public boolean hasFeature(String feature, String value) throws IOException {
		return delegate.hasFeature(feature, value);
	}

	public boolean allocate(int bytes, int recordSize) throws IOException {
		return delegate.allocate(bytes, recordSize);
	}

	public boolean doCommand(String command, String params) throws IOException {
		return delegate.doCommand(command, params);
	}

	public String[] doCommandAsStrings(String command, String params)
			throws IOException {
		return delegate.doCommandAsStrings(command, params);
	}

	public FTPFile mlistFile(String pathname) throws IOException {
		return delegate.mlistFile(pathname);
	}

	public FTPFile[] mlistDir() throws IOException {
		return delegate.mlistDir();
	}

	public FTPFile[] mlistDir(String pathname) throws IOException {
		return delegate.mlistDir(pathname);
	}

	public FTPFile[] mlistDir(String pathname, FTPFileFilter filter)
			throws IOException {
		return delegate.mlistDir(pathname, filter);
	}

	public void setRestartOffset(long offset) {
		delegate.setRestartOffset(offset);
	}

	public long getRestartOffset() {
		return delegate.getRestartOffset();
	}

	public boolean rename(String from, String to) throws IOException {
		return delegate.rename(from, to);
	}

	public boolean abort() throws IOException {
		return delegate.abort();
	}

	public boolean deleteFile(String pathname) throws IOException {
		return delegate.deleteFile(pathname);
	}

	public boolean removeDirectory(String pathname) throws IOException {
		return delegate.removeDirectory(pathname);
	}

	public boolean makeDirectory(String pathname) throws IOException {
		return delegate.makeDirectory(pathname);
	}

	public String printWorkingDirectory() throws IOException {
		return delegate.printWorkingDirectory();
	}

	public boolean sendSiteCommand(String arguments) throws IOException {
		return delegate.sendSiteCommand(arguments);
	}

	public String getSystemType() throws IOException {
		return delegate.getSystemType();
	}

	public String listHelp() throws IOException {
		return delegate.listHelp();
	}

	public String listHelp(String command) throws IOException {
		return delegate.listHelp(command);
	}

	public boolean sendNoOp() throws IOException {
		return delegate.sendNoOp();
	}

	public String[] listNames(String pathname) throws IOException {
		return delegate.listNames(pathname);
	}

	public String[] listNames() throws IOException {
		return delegate.listNames();
	}

	public FTPFile[] listFiles(String pathname) throws IOException {
		return delegate.listFiles(pathname);
	}

	public FTPFile[] listFiles() throws IOException {
		return delegate.listFiles();
	}

	public FTPFile[] listFiles(String pathname, FTPFileFilter filter)
			throws IOException {
		return delegate.listFiles(pathname, filter);
	}

	public FTPFile[] listDirectories() throws IOException {
		return delegate.listDirectories();
	}

	public FTPFile[] listDirectories(String parent) throws IOException {
		return delegate.listDirectories(parent);
	}

	public FTPListParseEngine initiateListParsing() throws IOException {
		return delegate.initiateListParsing();
	}

	public FTPListParseEngine initiateListParsing(String pathname)
			throws IOException {
		return delegate.initiateListParsing(pathname);
	}

	public FTPListParseEngine initiateListParsing(String parserKey,
			String pathname) throws IOException {
		return delegate.initiateListParsing(parserKey, pathname);
	}

	public String getStatus() throws IOException {
		return delegate.getStatus();
	}

	public String getStatus(String pathname) throws IOException {
		return delegate.getStatus(pathname);
	}

	public String getModificationTime(String pathname) throws IOException {
		return delegate.getModificationTime(pathname);
	}

	public boolean setModificationTime(String pathname, String timeval)
			throws IOException {
		return delegate.setModificationTime(pathname, timeval);
	}

	public void setBufferSize(int bufSize) {
		delegate.setBufferSize(bufSize);
	}

	public int getBufferSize() {
		return delegate.getBufferSize();
	}

	public void configure(FTPClientConfig config) {
		delegate.configure(config);
	}

	public void setListHiddenFiles(boolean listHiddenFiles) {
		delegate.setListHiddenFiles(listHiddenFiles);
	}

	public boolean getListHiddenFiles() {
		return delegate.getListHiddenFiles();
	}

	public boolean isUseEPSVwithIPv4() {
		return delegate.isUseEPSVwithIPv4();
	}

	public void setUseEPSVwithIPv4(boolean selected) {
		delegate.setUseEPSVwithIPv4(selected);
	}

	public void setCopyStreamListener(CopyStreamListener listener) {
		delegate.setCopyStreamListener(listener);
	}

	public CopyStreamListener getCopyStreamListener() {
		return delegate.getCopyStreamListener();
	}

	public void setControlKeepAliveTimeout(long controlIdle) {
		delegate.setControlKeepAliveTimeout(controlIdle);
	}

	public long getControlKeepAliveTimeout() {
		return delegate.getControlKeepAliveTimeout();
	}

	public void setControlKeepAliveReplyTimeout(int timeout) {
		delegate.setControlKeepAliveReplyTimeout(timeout);
	}

	public int getControlKeepAliveReplyTimeout() {
		return delegate.getControlKeepAliveReplyTimeout();
	}

	public void setAutodetectUTF8(boolean autodetect) {
		delegate.setAutodetectUTF8(autodetect);
	}

	public boolean getAutodetectUTF8() {
		return delegate.getAutodetectUTF8();
	}

	public String getSystemName() throws IOException {
		return delegate.getSystemName();
	}

	protected void handleException(TableNotFoundException e)
			throws TableNotFoundException {
		throw e;
	}

	protected void handleException(IOException e) throws IOException {
		throw e;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("closed", closed);
		builder.append("delegate", delegate);
		return builder.toString();
	}
}
