package com.aavu.server.web.controllers;

import java.util.Map;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class IndexControllerTest extends AbstractBaseTestClass {


	
	private IndexController control;

	@Override
	protected void onSetUp() throws Exception {	
		super.onSetUp();		
		
		control = (IndexController) ctx.getBean("indexController");
		
	}

	private MockHttpServletRequest getGetRequest(){
		log.debug("\ncalling getGetRequest !!!!");
		MockHttpServletRequest req = getRequest();	
		
		return req;
	}
	
	public void testBasic() throws Exception{
		
		MockHttpServletRequest req = getGetRequest();		
		
		String toEncrypt = "fofoasfd sadfsdf";
		
		req.addParameter("string", toEncrypt);

		ModelAndView mav = control.handleRequest(req,new MockHttpServletResponse());
		Map m = mav.getModel();		

		assertEquals("Should have view","secure/userPage",mav.getViewName());
		
		
		String rtn = (String) m.get("string");
		
	
		
		System.out.println("rtn "+rtn);
				
		
	}
	
	public void testTemplate() throws Exception{
		
		
		MockHttpServletRequest req = getGetRequest();
		//req.addParameter("string", toEncrypt);
		
		
		ModelAndView mav = control.handleRequest(req,new MockHttpServletResponse());		
		View view = getViewFromMv(mav,req.getLocale());		

		MockHttpServletRequest req2 = getRequest();
		MockHttpServletResponse response2 = new MockHttpServletResponse();

		assertEquals("Should have success","simpleString",mav.getViewName());

		view.render(mav.getModel(), req2, response2);

		//String myVersion = des.encrypt(toEncrypt);
		
		//assertEquals(myVersion, response2.getContentAsString());
		
		
		System.out.println(response2.getContentAsString());
		
	}
	
}
