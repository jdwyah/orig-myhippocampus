package com.aavu.client.service.local;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.domain.Meta;

public class TagLocalService {

	
	//public List<Meta> getAllMetaTypes() {	
	public List getAllMetaTypes() {
		
		List rtn = new ArrayList();
		
		rtn.add(new Meta("Date"));
		rtn.add(new Meta("Text"));
		rtn.add(new Meta("Topic List"));
		
		//Class l = MetaDate.class;		
		//MetaDate date = (MetaDate) l.getConstructors()[0].newInstance(null);
		
		return rtn;
	}
	
}
