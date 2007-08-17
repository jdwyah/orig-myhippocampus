package com.aavu.server.service;

import java.io.IOException;

import com.google.gdata.util.ServiceException;

public interface TheGoogleService {

	void getAllDocs() throws IOException, ServiceException;

}
