package com.aavu.server.service.impl;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.server.dao.SelectDAO;
import com.aavu.server.service.UserService;
import com.aavu.server.service.gwt.BaseTestWithTransaction;
import com.aavu.server.util.CryptUtils;

public class UserServiceImplTest extends BaseTestWithTransaction {

	private static final String NAME = "userser";
	private static final String PASS = "sdfmasiuh23490234n";
	private static final String EMAIL = "sdfbnsdui@nfesd.co";

	private UserService userService;
	private SelectDAO selectDAO;

	/**
	 * test create user and the initial user "demo" setup
	 * 
	 * @throws HippoBusinessException
	 */
	public void testCreateUserCreateUserRequestCommand() throws HippoException {

		User u = userService.createUser(NAME, PASS, EMAIL, false);

		assertEquals(NAME, u.getUsername());
		assertNotSame(PASS, u.getPassword());
		assertEquals(CryptUtils.hashString(PASS), u.getPassword());
		assertEquals(EMAIL, u.getEmail());


		List<Topic> topics = selectDAO.getAllTopics(u);

		for (Iterator iterator = topics.iterator(); iterator.hasNext();) {
			Topic t = (Topic) iterator.next();
			System.out.println("t " + t + " " + t.getId());
		}

		assertEquals(6, topics.size());

	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setSelectDAO(SelectDAO selectDAO) {
		this.selectDAO = selectDAO;
	}


}
