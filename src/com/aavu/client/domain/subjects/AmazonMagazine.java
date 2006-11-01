package com.aavu.client.domain.subjects;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.service.Manager;

public class AmazonMagazine extends Amazon {
	
//	@Override
	public List getInfos() {
		List subjectInfo = new ArrayList();
		subjectInfo.add(new SubjectInfo(Manager.myConstants.book_title(),getName(),false));
		return subjectInfo;
	}
}
