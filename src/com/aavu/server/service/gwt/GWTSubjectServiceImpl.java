package com.aavu.server.service.gwt;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.client.service.remote.GWTSubjectService;
import com.aavu.server.service.ExternalServicesService;

public class GWTSubjectServiceImpl extends org.gwtwidgets.server.spring.GWTSpringController implements GWTSubjectService {
	private static final Logger log = Logger.getLogger(GWTSubjectServiceImpl.class);
	
	public ExternalServicesService externalServicesService;

	public void setExternalServicesService(ExternalServicesService subjectService) {
		this.externalServicesService = subjectService;
	}



	public List<? extends Subject> lookup(Subject type, String matchString) throws HippoBusinessException {

		if(type == null){
			throw new HippoBusinessException("Lookup of Null type");
		}
		try{
			return externalServicesService.lookup(type.getClass(),matchString);
		}
		catch(Exception e){
			log.error("Lookup error "+e);
			e.printStackTrace();
			throw new HibernateException(e); 
		}
	}

	public void addDeliciousTags(String username, String password) throws HippoException {
		externalServicesService.addDeliciousTags(username,password);
	}
	
}

