package com.aavu.client.service.local;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.MetaTopicList;
import com.aavu.client.domain.MetaURL;

public class TagLocalService {

	
	//public List<Meta> getAllMetaTypes() {	
	public List getAllMetaTypes() {
		
		List rtn = new ArrayList();
		
		rtn.add(new MetaText());
		rtn.add(new MetaDate());
		rtn.add(new MetaURL());
		rtn.add(new MetaTopicList());
		
		//Class l = MetaDate.class;		
		//MetaDate date = (MetaDate) l.getConstructors()[0].newInstance(null);
		
		return rtn;
	}
	
}
