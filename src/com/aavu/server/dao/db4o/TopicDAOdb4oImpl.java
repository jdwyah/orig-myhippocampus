package com.aavu.server.dao.db4o;

import java.util.List;

import javax.servlet.ServletContext;

import org.db4ospring.support.Db4oDaoSupport;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.aavu.client.domain.Topic;
import com.aavu.server.dao.TopicDAO;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class TopicDAOdb4oImpl extends Db4oDAO implements TopicDAO{


	public TopicDAOdb4oImpl(){


//		ObjectContainer db=null;				


//		db=Db4o.openFile(FILENAME);

//		db.close();
//		setObjectContainer(db);
	}

	public void save(Topic t){
		System.out.println("setting "+t.getTitle());



//		long id = getDb().ext().getID(t);
//
//		System.out.println("id in: "+id+" saved "+t.getId());

//		Topic ts = getDb().ext().getByID(t.getId());
//		System.out.println("ts "+getDb().ext().getID(ts)+" saved "+ts.getId());
//		ts = t;		
//		System.out.println("ts "+getDb().ext().getID(ts)+" saved "+ts.getId());
//		getDb().set(ts);

		//getDb().ext().
		System.out.println("pre bind"+t.getTitle());
		getDb().ext().bind(t, t.getId());
		System.out.println("post bind"+t.getTitle());
		getDb().set(t);


		closeDB();

		//db.set(t);
	}

	public List<Topic> getAllTopics(){


		ObjectSet result = getDb().get(Topic.class);

		List<Topic> res = result;
		for(Topic t : res){
			t.setId(getDb().ext().getID(t));
			System.out.println(getDb().ext().getID(t)+" "+t.getTitle());
		}
		closeDB();
		//ObjectSet result=db.get(Topic.class);
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
