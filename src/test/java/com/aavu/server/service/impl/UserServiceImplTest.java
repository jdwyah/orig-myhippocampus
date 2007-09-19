package com.aavu.server.service.impl;

import java.util.Iterator;
import java.util.List;

import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.server.dao.SelectDAO;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.service.UserService;
import com.aavu.server.service.gwt.BaseTestWithTransaction;

public class UserServiceImplTest extends BaseTestWithTransaction {

	private static final String NAME = "userser";
	private static final String PASS = "sdfmasiuh23490234n";

	private static final String NAME2 = "uNER#$)-034";
	private static final String PASS2 = "435kln5908ew80&@34023.4--2342";

	private static final String EMAIL = "sdfbnsdui@nfesd.co";

	private UserService userService;
	private SelectDAO selectDAO;

	private PasswordEncoder passwordEncoder;
	private SaltSource saltSource;


	/**
	 * test create user and the initial user "demo" setup
	 * 
	 * @throws HippoBusinessException
	 */
	public void testCreateUserCreateUserRequestCommand() throws HippoException {

		User u = userService.createUser(NAME, PASS, EMAIL, false);

		assertEquals(NAME, u.getUsername());
		assertNotSame(PASS, u.getPassword());

		Object salt = saltSource.getSalt(new ServerSideUser(u));

		assertNotNull(salt);

		String passShouldBe = passwordEncoder.encodePassword(PASS, salt);

		assertEquals(passShouldBe, u.getPassword());
		assertEquals(EMAIL, u.getEmail());


		List<Topic> topics = selectDAO.getAllTopics(u);

		for (Iterator iterator = topics.iterator(); iterator.hasNext();) {
			Topic t = (Topic) iterator.next();
			System.out.println("t " + t + " " + t.getId());
		}

		assertEquals(17, topics.size());

	}

	/**
	 * make sure that we don't salt users w/ null
	 * 
	 * @throws HippoException
	 */
	public void testSalting() throws HippoException {

		User u = userService.createUser(NAME2, PASS2, EMAIL, false, null);

		assertEquals(NAME2.toLowerCase(), u.getUsername());
		assertNotSame(PASS2, u.getPassword());

		Object salt = saltSource.getSalt(new ServerSideUser(u));
		assertNull(salt);

		String passShouldBe = passwordEncoder.encodePassword(PASS2, salt);

		assertEquals(passShouldBe, u.getPassword());
		assertEquals(EMAIL, u.getEmail());

	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setSelectDAO(SelectDAO selectDAO) {
		this.selectDAO = selectDAO;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}


}
