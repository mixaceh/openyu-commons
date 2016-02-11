import static org.junit.Assert.assertNotNull;

import org.apache.hadoop.hbase.client.HConnection;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.openyu.commons.hbase.HzDataSource;
import org.openyu.commons.hbase.HzSession;
import org.openyu.commons.hbase.HzSessionFactory;
import org.openyu.commons.bao.hbase.HzTemplate;
import org.openyu.commons.bao.hbase.supporter.HzBaoSupporter;
import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class ApplicationContextHbaseZookeeperTest extends BaseTestSupporter {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(new String[] {
				"applicationContext-init.xml",//
				"META-INF/applicationContext-commons-core.xml",//
				"applicationContext-hbase-zookeeper.xml",//
		});
	}

	@Test
	public void hzDataSource() throws Exception {
		HzDataSource bean = (HzDataSource) applicationContext
				.getBean("hzDataSource");
		System.out.println(bean);
		assertNotNull(bean);
		//
		HConnection hconnection = bean.getHConnection();
		System.out.println(hconnection);
		assertNotNull(hconnection);
	}

	@Test
	public void hzSessionFactory() throws Exception {
		HzSessionFactory bean = (HzSessionFactory) applicationContext
				.getBean("hzSessionFactory");
		System.out.println(bean);
		assertNotNull(bean);
		//
		HzSession session = bean.openSession();
		System.out.println(session);
		assertNotNull(session);
	}

	@Test
	public void hzTemplate() throws Exception {
		HzTemplate bean = (HzTemplate) applicationContext.getBean("hzTemplate");
		System.out.println(bean);
		assertNotNull(bean);
		//
		HzSession session = bean.getSession();
		System.out.println(session);
		assertNotNull(session);
	}

	@Test
	public void hzBaoSupporter() {
		HzBaoSupporter bean = (HzBaoSupporter) applicationContext
				.getBean("hzBaoSupporter");
		System.out.println(bean);
		assertNotNull(bean);
	}

}
