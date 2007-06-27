package com.aavu.client.domain;

import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.InitDAO;
import com.aavu.server.dao.hibernate.HibernateTransactionalTest;

public class Updater extends HibernateTransactionalTest {
//public class Updater extends BaseTestNoTransaction {
	
	private InitDAO initDAO;


	public void setInitDAO(InitDAO initDAO) {
		this.initDAO = initDAO;
	}


	public void testView() throws HippoBusinessException {
		
		
		initDAO.displayRootInfo();
		
	}

	public void testrun() throws HippoBusinessException {
		
		

		initDAO.convertToAllTopics();
		
		
	}
	

	public void testrunRemovetags() throws HippoBusinessException {
		
		
		initDAO.upgradeRemoveTags();
		
		
	}
	

}
