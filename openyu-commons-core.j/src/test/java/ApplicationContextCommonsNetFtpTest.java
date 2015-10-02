import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.openyu.commons.commons.net.ftp.CnfDataSource;
import org.openyu.commons.commons.net.ftp.CnfSession;
import org.openyu.commons.commons.net.ftp.CnfSessionFactory;
import org.openyu.commons.fto.commons.net.ftp.CnfTemplate;
import org.openyu.commons.fto.commons.net.ftp.supporter.CnfFtoSupporter;
import org.apache.commons.net.ftp.FTPClient;

public class ApplicationContextCommonsNetFtpTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"applicationContext-init.xml",//
				"META-INF/applicationContext-commons-core.xml",//
				"applicationContext-commons-net-ftp.xml",//
		});
	}

	@Test
	public void cnfDataSource() throws Exception {
		CnfDataSource bean = (CnfDataSource) applicationContext
				.getBean("cnfDataSource");
		System.out.println(bean);
		assertNotNull(bean);
		//
		FTPClient ftpClient = bean.getFTPClient();
		System.out.println(ftpClient);
		assertNotNull(ftpClient);
	}

	@Test
	public void cnfSessionFactory() throws Exception {
		CnfSessionFactory bean = (CnfSessionFactory) applicationContext
				.getBean("cnfSessionFactory");
		System.out.println(bean);
		assertNotNull(bean);
		//
		CnfSession session = bean.openSession();
		System.out.println(session);
		assertNotNull(session);
	}

	@Test
	public void cnfTemplate() throws Exception {
		CnfTemplate bean = (CnfTemplate) applicationContext
				.getBean("cnfTemplate");
		System.out.println(bean);
		assertNotNull(bean);
		//
		CnfSession session = bean.getSession();
		System.out.println(session);
		assertNotNull(session);
	}

	@Test
	public void cnfFtoSupporter() {
		CnfFtoSupporter bean = (CnfFtoSupporter) applicationContext
				.getBean("cnfFtoSupporter");
		System.out.println(bean);
		assertNotNull(bean);
	}

}
