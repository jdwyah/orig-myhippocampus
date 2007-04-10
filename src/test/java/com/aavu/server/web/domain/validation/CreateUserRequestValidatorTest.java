package com.aavu.server.web.domain.validation;



import junit.framework.TestCase;

import org.springframework.validation.BindException;

import com.aavu.server.web.domain.CreateUserRequestCommand;

public class CreateUserRequestValidatorTest extends TestCase {

	private static final String OK_U1 = "username";
	private static final String OK_U2 = "j459823sknd__--33dm";
	private static final String OK_P1 = "password";
	private static final String OK_P2 = "diffpassword55kjfd89.$$$!09384@";
	
	
	private CreateUserRequestValidator validator;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	
		validator = new CreateUserRequestValidator();
		validator.setUserService(new MockUserService());
	}

	public void testValid() {
		CreateUserRequestCommand c = new CreateUserRequestCommand();
		c.setUsername(OK_U1);
		c.setPassword(OK_P1);
		c.setPassword2(OK_P1);
		
		BindException errors = new BindException(c, "");		
		validator.validate(c, errors);
		assertFalse(errors.hasErrors());
		
		
		c.setUsername(OK_U2);
		c.setPassword(OK_P2);
		c.setPassword2(OK_P2);
		errors = new BindException(c, "");		
		validator.validate(c, errors);
		assertFalse(errors.hasErrors());
	}
	public void testInvalid() {
		CreateUserRequestCommand c = new CreateUserRequestCommand();
		
		//password !=
		c.setUsername(OK_U1);
		c.setPassword(OK_P1);
		c.setPassword2(OK_P2);		
		BindException errors = new BindException(c, "");		
		validator.validate(c, errors);
		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors("password2"));
		
		//can't have username == pass
		c.setUsername(OK_U1);
		c.setPassword(OK_U1);
		c.setPassword2(OK_U1);
		errors = new BindException(c, "");		
		validator.validate(c, errors);
		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors("username"));
		
		//password ok from here on out
		c.setPassword(OK_P2);
		c.setPassword2(OK_P2);
		
		
		
		c.setUsername("okbut.nodots");
		errors = new BindException(c, "");		
		validator.validate(c, errors);
		assertTrue(errors.hasErrors());		
		assertTrue(errors.hasFieldErrors("username"));
		
		
		c.setUsername("okbut nospaces ");
		errors = new BindException(c, "");		
		validator.validate(c, errors);
		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors("username"));
		
		c.setUsername("no@%^@");
		errors = new BindException(c, "");		
		validator.validate(c, errors);
		assertTrue(errors.hasFieldErrors("username"));
		
		
		//too short
		c.setUsername("no");
		errors = new BindException(c, "");		
		validator.validate(c, errors);
		assertTrue(errors.hasFieldErrors("username"));
		
		
		//sanity check 
		c.setUsername("no3");
		errors = new BindException(c, "");		
		validator.validate(c, errors);
		assertFalse(errors.hasErrors());
	}

}
