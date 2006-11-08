package com.aavu.server.service;

import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoException;

public interface SubjectService {

	List<? extends Subject> lookup(Class type, String matchString) throws HippoException;

	Document deliciousReq() throws IOException, DocumentException;
}
