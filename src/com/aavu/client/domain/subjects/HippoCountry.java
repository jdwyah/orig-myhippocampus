package com.aavu.client.domain.subjects;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.service.Manager;

public class HippoCountry extends HippoSubject {
	
	public HippoCountry(){}
	
	public HippoCountry(String foreignID,String string) {
		setForeignID(foreignID);
		setName(string);
	}
	
	//@Override
	public List getInfos() {
		List subjectInfo = new ArrayList();
		subjectInfo.add(new SubjectInfo(Manager.myConstants.country_name(),getName(),false));
		return subjectInfo;
	}
	
	//@Override
	public String getTagName() {
		return Manager.myConstants.country();
	}
}
