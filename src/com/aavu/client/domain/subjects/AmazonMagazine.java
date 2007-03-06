package com.aavu.client.domain.subjects;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;

public class AmazonMagazine extends Amazon {
	
//	@Override
	public List getInfos() {
		List subjectInfo = new ArrayList();
		subjectInfo.add(new SubjectInfo(ConstHolder.myConstants.book_title(),getName(),false));
		return subjectInfo;
	}

	//@Override
	public String getTagName() {
		return ConstHolder.myConstants.magazine();
	}
}
