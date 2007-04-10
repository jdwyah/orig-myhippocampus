package com.aavu.server.dao.hibernate;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;


public class HibernateTransactionalTest extends AbstractTransactionalDataSourceSpringContextTests{

	@Override
	protected String[] getConfigLocations() {
		
//		String path = "tomcat/webapps/HippoTest/WEB-INF/";
//		String pathc = path+"classes/";
//		
//		File f = new File(".");
//		System.out.println(f.getAbsolutePath());
//		
//		String pathh = "file:"+path;
//		
//		PropertyConfigurator.configure(pathc+"log4j.properties");
//		return new String[] {pathh+"applicationContext-hibernate.xml",pathh+"applicationContext.xml"};
		
		PropertyConfigurator.configure(getClass().getResource("/log4j.properties"));		
		return new String[] {"classpath*:/applicationContext-hibernate.xml","classpath*:/applicationContext.xml"};
	}

}
