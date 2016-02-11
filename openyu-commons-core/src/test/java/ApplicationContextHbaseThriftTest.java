import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.hbase.thrift.HtDataSource;
import org.openyu.commons.hbase.thrift.HtSession;
import org.openyu.commons.hbase.thrift.HtSessionFactory;
//import org.openyu.commons.bao.hbase.thrift.HtTemplate;
//import org.openyu.commons.bao.hbase.thrift.supporter.HtBaoSupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.apache.thrift.transport.TTransport;

public class ApplicationContextHbaseThriftTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"applicationContext-init.xml",//
				"META-INF/applicationContext-commons-core.xml",//
				"applicationContext-hbase-thrift.xml",//
		});
	}

	@Test
	public void htDataSource() throws Exception {
		HtDataSource bean = (HtDataSource) applicationContext
				.getBean("htDataSource");
		System.out.println(bean);
		assertNotNull(bean);
		//
		TTransport ttransport = bean.getTTransport();
		System.out.println(ttransport);
		assertNotNull(ttransport);
	}

	@Test
	public void htSessionFactory() throws Exception {
		HtSessionFactory bean = (HtSessionFactory) applicationContext
				.getBean("htSessionFactory");
		System.out.println(bean);
		assertNotNull(bean);
		//
		HtSession session = bean.openSession();
		System.out.println(session);
		assertNotNull(session);
	}

	// @Test
	// public void htTemplate() throws Exception {
	// HtTemplate bean = (HtTemplate) applicationContext.getBean("htTemplate");
	// System.out.println(bean);
	// assertNotNull(bean);
	// //
	// HtSession session = bean.getSession();
	// System.out.println(session);
	// assertNotNull(session);
	// }
	//
	// @Test
	// public void htBaoSupporter() {
	// HtBaoSupporter bean = (HtBaoSupporter) applicationContext
	// .getBean("htBaoSupporter");
	// System.out.println(bean);
	// assertNotNull(bean);
	// }

}
