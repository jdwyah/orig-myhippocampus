package com.aavu.server.service.gwt;

import java.util.List;

import org.apache.log4j.Logger;
import org.gwtwidgets.server.rpc.GWTSpringController;
import org.hibernate.HibernateException;

import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.service.remote.GWTSubjectService;
import com.aavu.server.service.SubjectService;

public class GWTSubjectServiceImpl extends GWTSpringController implements GWTSubjectService {
	private static final Logger log = Logger.getLogger(GWTSubjectServiceImpl.class);
	
	public SubjectService subjectService;

	public void setSubjectService(SubjectService subjectService) {
		this.subjectService = subjectService;
	}



	public List<? extends Subject> lookup(Subject type, String matchString) throws HippoBusinessException {

		if(type == null){
			throw new HippoBusinessException("Lookup of Null type");
		}
		try{
			return subjectService.lookup(type.getClass(),matchString);
		}
		catch(Exception e){
			log.error("Lookup error "+e);
			e.printStackTrace();
			throw new HibernateException(e); 
		}
	}

}

