package com.aavu.server.dao.db4o;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.db4ospring.support.Db4oDaoSupport;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.server.dao.TopicDAO;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class TopicDAOdb4oImpl extends Db4oDaoSupport implements TopicDAO{
	

	public void save(Topic t){


		if(t.getId() == 0){
			getDb4oTemplate().set(t);
			long id = getDb4oTemplate().getID(t);
			System.out.println("was 0, now "+id);				
			t.setId(id);							
		}else{
			getDb4oTemplate().bind(t, t.getId());			
		}

		getDb4oTemplate().delete(t.getLastUpdated());
		t.setLastUpdated(new Date());
		getDb4oTemplate().set(t);

	}

	public List<Topic> getAllTopics(){

		System.out.println("ABOUT TO GET ALL TOPICS");
		
		ObjectSet result = getDb4oTemplate().get(Topic.class);
		
		System.out.println("FINISHED GET ALL TOPICS");

		List<Topic> res = result;

		//System.out.println("res: "+res);
		System.out.println("number of topics: "+res.size());
		
		ObjectSet r2 = getDb4oTemplate().get(Object.class);
		System.out.println("number of objs "+r2.size());

//		for(Topic t : res){
//		t.setId(getDb().ext().getID(t));
//		System.out.println(getDb().ext().getID(t)+" "+t.getTitle());
//		t.setSeeAlso(new ArrayList());
//		getDb().set(t);
//		}


		return res;

	}

	public List<Topic>  getTopicsStarting(String match) {

		Query q = getDb4oTemplate().query();
		q.constrain(Topic.class);
		q.descend("title").constrain(match).startsWith(false);

		List<Topic> rtn = q.execute();	

		return rtn;

	}

	public Topic getForName(String title) {
		Query q = getDb4oTemplate().query();
		q.constrain(Topic.class);
		q.descend("title").constrain(title);

		List<Topic> rtn = q.execute();

		if(rtn != null & rtn.size() == 1){
			return rtn.get(0);
		}else{
			System.out.println("multiple returns returning null");
			for(Topic t : rtn){
				System.out.println("t "+t.getTitle()+" "+t.getId());
				System.out.println("t "+t.getText()+" ");
			}
			//PEND MED ambiguous what >1 would mean
			return null;
		}
	}


	public List<Topic> getBlogTopics(int start, int numberPerScreen) {
		Query q = getDb4oTemplate().query();
		q.constrain(Topic.class);
		q.descend("lastUpdated").orderDescending();

		List<Topic> rtn = q.execute();

		

		return rtn;
	}

	public void tester() {
		List<Tag> list = getDb4oTemplate().get(Tag.class);
		for(Tag t : list){
			System.out.println("name: "+t.getName()+" "+t.getId());
			System.out.println("meta: "+t.getMetas());

			if(t.getMetas() != null)
				for (Iterator iter = t.getMetas().iterator(); iter.hasNext();) {
					Meta element = (Meta) iter.next();
					System.out.println("meta "+element.getName()+" "+element.getType()+" "+element.getId());
				}

//			t.setMetas(new ArrayList());
//			if(t.getName() == null){
//			getDb().delete(t);				
//			}
//			saveT(t);
			//getDb().set(t);

		}
		List<Topic> list2 = getDb4oTemplate().get(Topic.class);
		for(Topic t : list2){

//			t.setLastUpdated(new Date());
//			getDb().set(t);
		}
	
	}
//	public void saveT(Tag tag) {

//	System.out.println("Tag: " + tag.getName());
//	System.out.println("metas: ");
//	for (Object meta : tag.getMetas()){
//	Meta metaCast = (Meta)meta;
//	System.out.println(metaCast);
//	}

//	Db4o.configure().objectClass("com.aavu.client.domain.Tag").cascadeOnUpdate(true);
//	Db4o.configure().objectClass("com.aavu.client.domain.Tag").updateDepth(1);

//	if(tag.getId() == 0){
//	getDb().set(tag);
//	long id = getDb().ext().getID(tag);
//	System.out.println("tag save was 0, now "+id);				
//	tag.setId(id);
//	getDb().set(tag);			
//	}else{
//	getDb().ext().bind(tag, tag.getId());
//	getDb().set(tag);
//	}
//	closeDB();

//	}

}
