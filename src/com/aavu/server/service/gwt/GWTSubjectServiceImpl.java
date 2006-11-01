package com.aavu.server.service.gwt;

import java.util.List;

import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.service.remote.GWTSubjectService;
import com.aavu.server.service.SubjectService;

public class GWTSubjectServiceImpl extends GWTSpringController implements GWTSubjectService {

	public SubjectService subjectService;
	
	public void setSubjectService(SubjectService subjectService) {
		this.subjectService = subjectService;
	}



	public List<? extends Subject> lookup(Subject type, String matchString) throws HippoBusinessException {
		if(type == null){
			throw new HippoBusinessException("Lookup of Null type");
		}
		return subjectService.lookup(type.getClass(),matchString);
	}

}
