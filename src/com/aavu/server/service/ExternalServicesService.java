package com.aavu.server.service;

import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoException;
import com.aavu.server.domain.DeliciousPost;

public interface ExternalServicesService {

	List<? extends Subject> lookup(Class type, String matchString) throws HippoException;

	List<DeliciousPost> deliciousReq(String username, String password) throws HippoException;

	void addDeliciousTags(String username, String password)throws HippoException;
}
