package com.aavu.client.domain.subjects;

import java.util.List;

import com.aavu.client.domain.subjects.generated.AbstractSubject;

public abstract class Subject extends AbstractSubject {

	//List<SubjectInfo>
	public abstract List getInfos();

}
