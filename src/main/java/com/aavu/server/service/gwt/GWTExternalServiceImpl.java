package com.aavu.server.service.gwt;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Required;

import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.client.service.remote.GWTExternalService;
import com.aavu.server.service.DeliciousService;
import com.aavu.server.service.ExternalServicesService;
import com.aavu.server.util.gwt.GWTSpringControllerReplacement;

public class GWTExternalServiceImpl extends GWTSpringControllerReplacement implements
		GWTExternalService {
	private static final Logger log = Logger.getLogger(GWTExternalServiceImpl.class);

	public ExternalServicesService externalServicesService;
	public DeliciousService deliciousService;

	@Required
	public void setExternalServicesService(ExternalServicesService subjectService) {
		this.externalServicesService = subjectService;
	}


	@Required
	public void setDeliciousService(DeliciousService deliciousService) {
		this.deliciousService = deliciousService;
	}



	public List<? extends Subject> lookup(Subject type, String matchString)
			throws HippoBusinessException {

		if (type == null) {
			throw new HippoBusinessException("Lookup of Null type");
		}
		try {
			return externalServicesService.lookup(type.getClass(), matchString);
		} catch (Exception e) {
			log.error("Lookup error " + e);
			e.printStackTrace();
			throw new HibernateException(e);
		}
	}

	public void addDeliciousTags(String username, String password) throws HippoException {
		deliciousService.newLinksForUser(username, password);
	}

}
