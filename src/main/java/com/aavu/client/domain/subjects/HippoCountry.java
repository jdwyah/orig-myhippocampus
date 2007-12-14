package com.aavu.client.domain.subjects;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.strings.ConstHolder;

public class HippoCountry extends HippoSubject {

	public HippoCountry() {
	}

	public HippoCountry(String foreignID, String string) {
		setForeignID(foreignID);
		setName(string);
	}

	@Override
	public List<SubjectInfo> getInfos() {
		List<SubjectInfo> subjectInfo = new ArrayList<SubjectInfo>();
		subjectInfo.add(new SubjectInfo(ConstHolder.myConstants.country_name(), getName(), false));
		return subjectInfo;
	}

	@Override
	public String getTagName() {
		return ConstHolder.myConstants.country();
	}
}
