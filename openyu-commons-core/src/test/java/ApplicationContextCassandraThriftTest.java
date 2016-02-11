import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.cassandra.thrift.CtDataSource;
import org.openyu.commons.cassandra.thrift.CtSession;
import org.openyu.commons.cassandra.thrift.CtSessionFactory;
//import org.openyu.commons.bao.hbase.thrift.CtTemplate;
//import org.openyu.commons.bao.hbase.thrift.supporter.HtBaoSupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;
import org.apache.thrift.transport.TTransport;

public class ApplicationContextCassandraThriftTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"applicationContext-init.xml",//
				"META-INF/applicationContext-commons-core.xml",//
				"applicationContext-cassandra-thrift.xml",//
		});
	}

	@Test
	public void ctDataSource() throws Exception {
		CtDataSource bean = (CtDataSource) applicationContext
				.getBean("ctDataSource");
		System.out.println(bean);
		assertNotNull(bean);
		//
		TTransport ttransport = bean.getTTransport();
		System.out.println(ttransport);
		assertNotNull(ttransport);
	}

	@Test
	public void CtSessionFactory() throws Exception {
		CtSessionFactory bean = (CtSessionFactory) applicationContext
				.getBean("ctSessionFactory");
		System.out.println(bean);
		assertNotNull(bean);
		//
		CtSession session = bean.openSession();
		System.out.println(session);
		assertNotNull(session);
	}

	// @Test
	// public void ctTemplate() throws Exception {
	// CtTemplate bean = (CtTemplate) applicationContext.getBean("ctTemplate");
	// System.out.println(bean);
	// assertNotNull(bean);
	// //
	// CtSession session = bean.getSession();
	// System.out.println(session);
	// assertNotNull(session);
	// }
	//
	// @Test
	// public void ctBaoSupporter() {
	// HtBaoSupporter bean = (HtBaoSupporter) applicationContext
	// .getBean("ctBaoSupporter");
	// System.out.println(bean);
	// assertNotNull(bean);
	// }

}
