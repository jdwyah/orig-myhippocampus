package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoException;

public interface SubjectService {

	List<? extends Subject> lookup(Class type, String matchString) throws HippoException;

}
