package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTSubjectService extends RemoteService {

	//List <Subject>
	List lookup(Subject type, String matchString) throws HippoBusinessException;

}
