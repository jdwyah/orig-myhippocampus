package com.aavu.server.dao.db4o;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.server.dao.TopicDAO;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class TopicDAOdb4oImpl extends Db4oDAO implements TopicDAO{


	public void save(Topic t){

		getDb().ext().bind(t, t.getId());
		getDb().set(t);
		closeDB();
	}

	public List<Topic> getAllTopics(){


		ObjectSet result = getDb().get(Topic.class);

		List<Topic> res = result;
//		for(Topic t : res){
//			t.setId(getDb().ext().getID(t));
//			System.out.println(getDb().ext().getID(t)+" "+t.getTitle());
//		}
		closeDB();
		
		return res;

	}

	public List<Topic>  getTopicsStarting(String match) {
		
		Query q = getDb().query();
		q.constrain(Topic.class);
		q.descend("title").constrain(match).startsWith(false);
		
		List<Topic> rtn = q.execute();
		
		closeDB();
		
		return rtn;
		
	}

}
