package com.aavu.server.service.impl;

import java.io.IOException;

import com.aavu.server.service.TheGoogleService;
import com.aavu.server.service.gwt.BaseTestWithTransaction;
import com.google.gdata.util.ServiceException;

public class GoogleServicesImplTest extends BaseTestWithTransaction {

	private TheGoogleService googleService;

	public void testGetAllDocs() throws IOException, ServiceException {
		int rtn = googleService.importDocsForUser("jdwyah@myhippocampus.com", "");

		System.out.println("found " + rtn + " docs");
	}

	public void setGoogleService(TheGoogleService googleService) {
		this.googleService = googleService;
	}


}
