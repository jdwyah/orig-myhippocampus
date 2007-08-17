package com.aavu.server.service;

import java.io.IOException;

import com.google.gdata.util.ServiceException;

public interface TheGoogleService {

	int getDocsForUser(String username, String password) throws IOException, ServiceException;



}
