package com.aavu.server.web.controllers;
import java.io.File;
import java.net.MalformedURLException;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class AbstractBaseTestClass extends AbstractDependencyInjectionSpringContextTests {
	protected static Logger log = Logger.getLogger(AbstractBaseTestClass.class);
	
	protected XmlWebApplicationContext ctx;
	
	/**
	 * Note! these Methods will look in the build/ directory for resources,
	 * thus they need to be built using the ant build script if changed.
	 * @throws MalformedURLException 
	 * 
	 */

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		createSecureContext();

		PropertyConfigurator.configure(getClass().getResource("/WEB-INF/classes/log4j.properties"));
		
		
		

		ctx = new XmlWebApplicationContext();		
		ctx.setConfigLocations(getConfigLocations());
		 
		MockServletContext m = new MockServletContext();//"PemsOrder/WebRoot/");
		try {
			System.out.println(m.getResource("."));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctx.setServletContext(m);				
		ctx.refresh();
		
	}
	
	/**
	 * These requests will have the context loaded correctly, so we'll be able to 
	 * get the template views correctly, complete with their springMacroContext
	 * 
	 * @return
	 */
	public MockHttpServletRequest getRequest(){
		return getRequest("GET");
	}
	public MockHttpServletRequest getRequest(String type) {
        MockHttpServletRequest req = new MockHttpServletRequest(ctx
                .getServletContext());
        req.setMethod(type);
        //req.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, ctx
         //       .getBean("localeResolver"));
        req.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                ctx);
        assertEquals("en", req.getLocale().getLanguage());
        return req;
    }
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
	protected void onTearDown() throws Exception {
		super.onTearDown();
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

	@Override
	protected String[] getConfigLocations() {
		String[] paths = {"classpath:/WEB-INF/applicationContext-acegi-security.xml",
				  "classpath:/WEB-INF/applicationContext-hibernate.xml",			
		          "classpath:/WEB-INF/applicationContext.xml",						  
				  "classpath:/WEB-INF/dispatcher-servlet.xml"};
		return paths;
	}
	
	
}