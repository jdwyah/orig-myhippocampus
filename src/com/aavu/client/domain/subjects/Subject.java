package com.aavu.client.domain.subjects;

import java.util.List;

import com.aavu.client.domain.subjects.generated.AbstractSubject;

public abstract class Subject extends AbstractSubject {

	//List<SubjectInfo>
	public abstract List getInfos();
	
	/**
	 * This should return the name of the Tag that we automatically 
	 * assign to things with this Subject. 
	 * 
	 * @return
	 */
	public abstract String getTagName();
}
