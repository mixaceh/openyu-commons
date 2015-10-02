import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

import org.openyu.commons.junit.supporter.BaseTestSupporter;

public class Hbm2DdlTest extends BaseTestSupporter {

	@Test
	public void schemaExport() {
		Configuration config = new Configuration()
				.configure("hibernate.cfg.xml");
		SchemaExport schemaExport = new SchemaExport(config);
		//
		schemaExport.execute(true, true, false, true);

	}
}
