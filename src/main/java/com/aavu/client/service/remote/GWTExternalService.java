package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTExternalService extends RemoteService {


	List<? extends Subject> lookup(Subject type, String matchString) throws HippoBusinessException;

	void addDeliciousTags(String username, String password) throws HippoException;
}
