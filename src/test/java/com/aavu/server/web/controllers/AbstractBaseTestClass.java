package com.aavu.server.web.controllers;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import junit.framework.TestCase;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public abstract class AbstractBaseTestClass extends TestCase {
	protected static Logger log = Logger.getLogger(AbstractBaseTestClass.class);
	
	//protected XmlWebApplicationContext ctx;
	
	protected FileSystemXmlApplicationContext ctx;
	
	/**
	 * Note! these Methods will look in the build/ directory for resources,
	 * thus they need to be built using the ant build script if changed.
	 * @throws MalformedURLException 
	 * 
	 */

	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		createSecureContext();

		PropertyConfigurator.configure(getClass().getResource("/log4j.properties"));
		
	
	    //HandlerMapping handlerMapping = (HandlerMapping) ctx.getBean("urlMapping");
//
//
//		ctx = new XmlWebApplicationContext();		
//		ctx.setConfigLocations(getMyLocations());
//		
//		
//		ClassPathXmlApplicationContext ctx2 = new ClassPathXmlApplicationContext(getMyLocations());
//		
//		System.out.println("cp "+ctx2.getBeanDefinitionCount());
		
		System.out.println("\n\n\n-----------------------\n\n");
//		FileSystemXmlApplicationContext ctx3 = new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/applicationContext.xml");
//		  FileSystemXmlApplicationContext ctx3 = new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/applicationContext.xml");
		
		ctx = new FileSystemXmlApplicationContext(getMyLocations());
		System.out.println("cp3 "+ctx.getBeanDefinitionCount());
		
		 
//		MockServletContext m = new MockServletContext();//"PemsOrder/WebRoot/");
//		try {
//			System.out.println(m.getResource("."));
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ctx.setServletContext(m);				
//		ctx.refresh();
//		
//		System.out.println("ctx 2222 "+ctx.getBeanDefinitionCount());
//		for(String beanName : ctx.getBeanDefinitionNames()){
//			System.out.println("Bean Name "+beanName);
//		}
	}
	
	/**
	 * These requests will have the context loaded correctly, so we'll be able to 
	 * get the template views correctly, complete with their springMacroContext
	 * 
	 * @return
	 */
//	public MockHttpServletRequest getRequest(){
//		return getRequest("GET");
//	}
//	public MockHttpServletRequest getRequest(String type) {
//        MockHttpServletRequest req = new MockHttpServletRequest(ctx
//                .getServletContext());
//        req.setMethod(type);
//        //req.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, ctx
//         //       .getBean("localeResolver"));
//        req.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE,
//                ctx);
//        assertEquals("en", req.getLocale().getLanguage());
//        return req;
//    }
	public View getViewFromMv(ModelAndView mv, Locale locale) throws Exception {
        if (mv.getView() != null)
            return mv.getView();
        assertNotNull(mv.getViewName());
        return getViewResolver().resolveViewName(mv.getViewName(),locale);
    }
	public ViewResolver getViewResolver() {
        return (ViewResolver) ctx.getBean("viewResolver");
    }
	
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		destroySecureContext();
	}

	/**
	 * Creates an Acegi SecureContext and stores it on the ContextHolder
	 */
	private void createSecureContext() {

		TestingAuthenticationToken auth = new TestingAuthenticationToken(getUName(), getPass(), new GrantedAuthority[] {
				new GrantedAuthorityImpl("ROLE_TELLER"), new GrantedAuthorityImpl("ROLE_PERMISSION_LIST") });
		
		SecurityContext secureContext = new SecurityContextImpl();
		secureContext.setAuthentication(auth);
		SecurityContextHolder.setContext(secureContext);

	}
	protected String getUName(){
		return "test-with-data";		
	}
	protected String getPass(){
		return "test-with-data";		
	}

	/**
	 * Removed the Acegi SecureContext from the ContextHolder
	 */
	private static void destroySecureContext() {
		SecurityContextHolder.setContext(new SecurityContextImpl());
	}
	
	private String[] getMyLocations() {
		
		File f = new File("src/main/webapp/WEB-INF/applicationContext.xml");
		
		System.out.println(f);
		System.out.println(f.getAbsolutePath());		
		System.out.println(f.exists());
		
		URL l = getClass().getResource("WEB-INF/applicationContext.xml");
		System.out.println("l "+l);
		
//		String[] paths = {"classpath*:applicationContext-acegi-security.xml",
//				  "classpath*:applicationContext-hibernate.xml",			
//		          "classpath*:applicationContext.xml",						  
//				  "classpath*:dispatcher-servlet.xml"};
		
		String[] paths = {"src/main/webapp/WEB-INF/applicationContext-acegi-security.xml",
				  "src/main/webapp/WEB-INF/applicationContext-hibernate.xml",			
		          "src/main/webapp/WEB-INF/applicationContext.xml",						  
				  "src/main/webapp/WEB-INF/dispatcher-servlet.xml"};
		return paths;
	}
	
	
}