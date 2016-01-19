import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.commons.net.ftp.FtpClientConnectionFactory;
import org.openyu.commons.commons.net.ftp.FtpClientSession;
import org.openyu.commons.commons.net.ftp.FtpClientSessionFactory;
import org.openyu.commons.fto.commons.net.ftp.FtpClientTemplate;
import org.openyu.commons.fto.commons.net.ftp.supporter.FtpClientFtoSupporter;
import org.apache.commons.net.ftp.FTPClient;

public class ApplicationContextFtpClientTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] { //
				"applicationContext-init.xml", //
				"META-INF/applicationContext-commons-core.xml", //
				"applicationContext-ftp-client.xml",//
		});
	}

	@Test
	public void ftpClientConnectionFactory() throws Exception {
		FtpClientConnectionFactory bean = (FtpClientConnectionFactory) applicationContext
				.getBean("ftpClientConnectionFactory");
		System.out.println(bean);
		assertNotNull(bean);
		//
		FTPClient ftpClient = bean.getFTPClient();
		System.out.println(ftpClient);
		assertNotNull(ftpClient);
	}

	@Test
	public void ftpClientSessionFactory() throws Exception {
		FtpClientSessionFactory bean = (FtpClientSessionFactory) applicationContext.getBean("ftpClientSessionFactory");
		System.out.println(bean);
		assertNotNull(bean);
		//
		FtpClientSession session = bean.openSession();
		System.out.println(session);
		assertNotNull(session);
	}

	@Test
	public void ftpClientTemplate() throws Exception {
		FtpClientTemplate bean = (FtpClientTemplate) applicationContext.getBean("ftpClientTemplate");
		System.out.println(bean);
		assertNotNull(bean);
		//
		FtpClientSession session = bean.getSession();
		System.out.println(session);
		assertNotNull(session);
	}

	@Test
	public void ftpClientFtoSupporter() {
		FtpClientFtoSupporter bean = (FtpClientFtoSupporter) applicationContext.getBean("ftpClientFtoSupporter");
		System.out.println(bean);
		assertNotNull(bean);
	}

}
