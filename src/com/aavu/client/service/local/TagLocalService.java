package com.aavu.client.service.local;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.MetaText;

public class TagLocalService {

	
	//public List<Meta> getAllMetaTypes() {	
	public List getAllMetaTypes() {
		
		List rtn = new ArrayList();
		
		rtn.add(new MetaDate());
		rtn.add(new MetaText());
		
		//Class l = MetaDate.class;		
		//MetaDate date = (MetaDate) l.getConstructors()[0].newInstance(null);
		
		return rtn;
	}
	
}
