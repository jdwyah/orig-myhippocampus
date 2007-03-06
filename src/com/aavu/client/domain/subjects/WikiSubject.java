package com.aavu.client.domain.subjects;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.strings.ConstHolder;

public class WikiSubject extends Subject {

	private String summary;
	private String displayURL;
	
	public WikiSubject(){}
	public WikiSubject(String name, String foreignID, String summary, String displayURL){
		setName(name);
		setForeignID(foreignID);
		this.summary = summary;
		this.displayURL = displayURL;
	}
	

	//@Override
	public List getInfos() {
		List subjectInfo = new ArrayList();
		subjectInfo.add(new SubjectInfo(ConstHolder.myConstants.wiki_summary(),summary,false));
		subjectInfo.add(new SubjectInfo(ConstHolder.myConstants.wiki_url(),displayURL,false));	
		return subjectInfo;
	}

	
	//@Override
	public String getTagName() {
		return ConstHolder.myConstants.wiki();
	}
}
