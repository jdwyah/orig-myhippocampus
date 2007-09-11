package com.aavu.server.dao.hibernate;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;


public abstract class HibernateTransactionalTest extends
		AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {

		// String path = "tomcat/webapps/HippoTest/WEB-INF/";
		// String pathc = path+"classes/";
		//		
		// File f = new File(".");
		// System.out.println(f.getAbsolutePath());
		//		
		// String pathh = "file:"+path;
		//		
		// PropertyConfigurator.configure(pathc+"log4j.properties");
		// return new String[]
		// {pathh+"applicationContext-hibernate.xml",pathh+"applicationContext.xml"};



		PropertyConfigurator.configure(getClass().getResource("/log4j.properties"));

		String path = "src/main/webapp/WEB-INF/";
		String pathh = "file:" + path;
		return new String[] { pathh + "applicationContext-acegi-security.xml",
				pathh + "applicationContext-hibernate.xml", pathh + "applicationContext.xml" };

		// this finds them in resources/
		// return new String[]
		// {"classpath*:/applicationContext-hibernate.xml","classpath*:/applicationContext.xml"};
	}

}
