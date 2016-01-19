package org.openyu.commons.commons.net.ftp;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
import org.apache.commons.net.io.CopyStreamListener;
import org.openyu.commons.model.BaseModel;
import org.openyu.commons.commons.net.ftp.ex.FtpClientSessionException;

public interface FtpClientSession extends BaseModel, Closeable {

	void close() throws FtpClientSessionException;

	boolean isClosed();

	boolean isConnected();

	// --------------------------------------------------
	// 由此控制native方法是否開放給end使用
	// --------------------------------------------------

	void connect(InetAddress host, int port) throws SocketException, IOException;

	void connect(String hostname, int port) throws SocketException, IOException;

	void connect(InetAddress host, int port, InetAddress localAddr, int localPort) throws SocketException, IOException;

	void connect(String hostname, int port, InetAddress localAddr, int localPort) throws SocketException, IOException;

	void connect(InetAddress host) throws SocketException, IOException;

	void connect(String hostname) throws SocketException, IOException;

	boolean equals(Object obj);

	boolean isAvailable();

	void setDefaultPort(int port);

	int getDefaultPort();

	void setDefaultTimeout(int timeout);

	int getDefaultTimeout();

	void setSoTimeout(int timeout) throws SocketException;

	void setSendBufferSize(int size) throws SocketException;

	void setControlEncoding(String encoding);

	String getControlEncoding();

	void setReceiveBufferSize(int size) throws SocketException;

	int getSoTimeout() throws SocketException;

	int sendCommand(String command, String args) throws IOException;

	void setTcpNoDelay(boolean on) throws SocketException;

	boolean getTcpNoDelay() throws SocketException;

	void setKeepAlive(boolean keepAlive) throws SocketException;

	boolean getKeepAlive() throws SocketException;

	void setSoLinger(boolean on, int val) throws SocketException;

	int getSoLinger() throws SocketException;

	int getLocalPort();

	InetAddress getLocalAddress();

	int getRemotePort();

	InetAddress getRemoteAddress();

	boolean verifyRemote(Socket socket);

	int sendCommand(int command, String args) throws IOException;

	void setSocketFactory(SocketFactory factory);

	int sendCommand(String command) throws IOException;

	void setServerSocketFactory(ServerSocketFactory factory);

	int sendCommand(int command) throws IOException;

	int getReplyCode();

	int getReply() throws IOException;

	void setConnectTimeout(int connectTimeout);

	String[] getReplyStrings();

	int getConnectTimeout();

	ServerSocketFactory getServerSocketFactory();

	String getReplyString();

	void addProtocolCommandListener(ProtocolCommandListener listener);

	void removeProtocolCommandListener(ProtocolCommandListener listener);

	int user(String username) throws IOException;

	int pass(String password) throws IOException;

	int acct(String account) throws IOException;

	int abor() throws IOException;

	int cwd(String directory) throws IOException;

	int cdup() throws IOException;

	int quit() throws IOException;

	int rein() throws IOException;

	int smnt(String dir) throws IOException;

	int port(InetAddress host, int port) throws IOException;

	int eprt(InetAddress host, int port) throws IOException;

	int pasv() throws IOException;

	int epsv() throws IOException;

	int type(int fileType, int formatOrByteSize) throws IOException;

	int type(int fileType) throws IOException;

	int stru(int structure) throws IOException;

	int mode(int mode) throws IOException;

	int retr(String pathname) throws IOException;

	int stor(String pathname) throws IOException;

	int stou() throws IOException;

	int stou(String pathname) throws IOException;

	int appe(String pathname) throws IOException;

	int allo(int bytes) throws IOException;

	int feat() throws IOException;

	int allo(int bytes, int recordSize) throws IOException;

	int rest(String marker) throws IOException;

	int mdtm(String file) throws IOException;

	int mfmt(String pathname, String timeval) throws IOException;

	void setDataTimeout(int timeout);

	void setParserFactory(FTPFileEntryParserFactory parserFactory);

	int rnfr(String pathname) throws IOException;

	int rnto(String pathname) throws IOException;

	void disconnect() throws IOException;

	int dele(String pathname) throws IOException;

	void setRemoteVerificationEnabled(boolean enable);

	int rmd(String pathname) throws IOException;

	boolean isRemoteVerificationEnabled();

	int mkd(String pathname) throws IOException;

	boolean login(String username, String password) throws IOException;

	int pwd() throws IOException;

	int list() throws IOException;

	int list(String pathname) throws IOException;

	int mlsd() throws IOException;

	boolean login(String username, String password, String account) throws IOException;

	int mlsd(String path) throws IOException;

	int mlst() throws IOException;

	int mlst(String path) throws IOException;

	int nlst() throws IOException;

	int nlst(String pathname) throws IOException;

	int site(String parameters) throws IOException;

	boolean logout() throws IOException;

	int syst() throws IOException;

	boolean changeWorkingDirectory(String pathname) throws IOException;

	int stat() throws IOException;

	int stat(String pathname) throws IOException;

	boolean changeToParentDirectory() throws IOException;

	int help() throws IOException;

	boolean structureMount(String pathname) throws IOException;

	int help(String command) throws IOException;

	int noop() throws IOException;

	boolean isStrictMultilineParsing();

	void setStrictMultilineParsing(boolean strictMultilineParsing);

	void enterLocalActiveMode();

	void enterLocalPassiveMode();

	boolean enterRemoteActiveMode(InetAddress host, int port) throws IOException;

	boolean enterRemotePassiveMode() throws IOException;

	String getPassiveHost();

	int getPassivePort();

	int getDataConnectionMode();

	void setActivePortRange(int minPort, int maxPort);

	void setActiveExternalIPAddress(String ipAddress) throws UnknownHostException;

	boolean setFileType(int fileType) throws IOException;

	boolean setFileType(int fileType, int formatOrByteSize) throws IOException;

	boolean setFileStructure(int structure) throws IOException;

	boolean setFileTransferMode(int mode) throws IOException;

	boolean remoteRetrieve(String filename) throws IOException;

	boolean remoteStore(String filename) throws IOException;

	boolean remoteStoreUnique(String filename) throws IOException;

	boolean remoteStoreUnique() throws IOException;

	boolean remoteAppend(String filename) throws IOException;

	boolean completePendingCommand() throws IOException;

	boolean retrieveFile(String remote, OutputStream local) throws IOException;

	InputStream retrieveFileStream(String remote) throws IOException;

	boolean storeFile(String remote, InputStream local) throws IOException;

	OutputStream storeFileStream(String remote) throws IOException;

	boolean appendFile(String remote, InputStream local) throws IOException;

	OutputStream appendFileStream(String remote) throws IOException;

	boolean storeUniqueFile(String remote, InputStream local) throws IOException;

	OutputStream storeUniqueFileStream(String remote) throws IOException;

	boolean storeUniqueFile(InputStream local) throws IOException;

	OutputStream storeUniqueFileStream() throws IOException;

	boolean allocate(int bytes) throws IOException;

	boolean features() throws IOException;

	String[] featureValues(String feature) throws IOException;

	String featureValue(String feature) throws IOException;

	boolean hasFeature(String feature) throws IOException;

	boolean hasFeature(String feature, String value) throws IOException;

	boolean allocate(int bytes, int recordSize) throws IOException;

	boolean doCommand(String command, String params) throws IOException;

	String[] doCommandAsStrings(String command, String params) throws IOException;

	FTPFile mlistFile(String pathname) throws IOException;

	FTPFile[] mlistDir() throws IOException;

	FTPFile[] mlistDir(String pathname) throws IOException;

	FTPFile[] mlistDir(String pathname, FTPFileFilter filter) throws IOException;

	void setRestartOffset(long offset);

	long getRestartOffset();

	boolean rename(String from, String to) throws IOException;

	boolean abort() throws IOException;

	boolean deleteFile(String pathname) throws IOException;

	boolean removeDirectory(String pathname) throws IOException;

	boolean makeDirectory(String pathname) throws IOException;

	String printWorkingDirectory() throws IOException;

	boolean sendSiteCommand(String arguments) throws IOException;

	String getSystemType() throws IOException;

	String listHelp() throws IOException;

	String listHelp(String command) throws IOException;

	boolean sendNoOp() throws IOException;

	String[] listNames(String pathname) throws IOException;

	String[] listNames() throws IOException;

	FTPFile[] listFiles(String pathname) throws IOException;

	FTPFile[] listFiles() throws IOException;

	FTPFile[] listFiles(String pathname, FTPFileFilter filter) throws IOException;

	FTPFile[] listDirectories() throws IOException;

	FTPFile[] listDirectories(String parent) throws IOException;

	FTPListParseEngine initiateListParsing() throws IOException;

	FTPListParseEngine initiateListParsing(String pathname) throws IOException;

	FTPListParseEngine initiateListParsing(String parserKey, String pathname) throws IOException;

	String getStatus() throws IOException;

	String getStatus(String pathname) throws IOException;

	String getModificationTime(String pathname) throws IOException;

	boolean setModificationTime(String pathname, String timeval) throws IOException;

	void setBufferSize(int bufSize);

	int getBufferSize();

	void configure(FTPClientConfig config);

	void setListHiddenFiles(boolean listHiddenFiles);

	boolean getListHiddenFiles();

	boolean isUseEPSVwithIPv4();

	void setUseEPSVwithIPv4(boolean selected);

	void setCopyStreamListener(CopyStreamListener listener);

	CopyStreamListener getCopyStreamListener();

	void setControlKeepAliveTimeout(long controlIdle);

	long getControlKeepAliveTimeout();

	void setControlKeepAliveReplyTimeout(int timeout);

	int getControlKeepAliveReplyTimeout();

	void setAutodetectUTF8(boolean autodetect);

	boolean getAutodetectUTF8();

	String getSystemName() throws IOException;

}
