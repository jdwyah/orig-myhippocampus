package com.aavu.server.dao.db4o;

import java.util.ArrayList;
import java.util.List;

import org.db4ospring.support.Db4oDaoSupport;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.server.dao.TagDAO;
import com.db4o.Db4o;
import com.db4o.query.Query;

public class TagDAOdb4oImpl extends Db4oDaoSupport implements TagDAO {

	public void addTag(Tag tag) {
	
		System.out.println("UNUSED OLD CODE!!!!");
//		getDb().set(tag);
//		closeDB();
	}

	public List<Tag> getAllTags() {
		List<Tag> rtn = getDb4oTemplate().get(Tag.class);
		
		for(Tag tag : rtn){
			tag.getMetas();
		}
		
		return rtn;
	}

	public Tag getTag(String tagName) {

		
		
		Query q = getDb4oTemplate().query();
		q.constrain(Tag.class);
		q.descend("name").constrain(tagName);
		
		Tag rtn = null;
		
		List<Tag> o = q.execute();

		if(o.size() > 0){
			rtn = (Tag) o.get(0);
		}
		
		getDb4oTemplate().query();
		
		return rtn;
	}

	public void removeTag(String itemText) {
		//TODO
	}

	public List<Tag> getTagsStarting(String match) {
		Query q = getDb4oTemplate().query();
		q.constrain(Tag.class);
		q.descend("name").constrain(match).startsWith(false);
		
		List<Tag> rtn = q.execute();
		
		
		return rtn;
	}

	public void save(Tag tag) {
	
		System.out.println("Tag: " + tag.getName());
		System.out.println("metas: ");
		for (Object meta : tag.getMetas()){
			Meta metaCast = (Meta)meta;
			System.out.println(metaCast+" "+metaCast.getName()+" "+metaCast.getId());
		}

		Db4o.configure().objectClass("com.aavu.client.domain.Tag").cascadeOnUpdate(true);
		Db4o.configure().objectClass("com.aavu.client.domain.Tag").updateDepth(1);
		
		if(tag.getId() == 0){
			getDb4oTemplate().set(tag);
			long id = getDb4oTemplate().getID(tag);
			System.out.println("tag save was 0, now "+id);				
			tag.setId(id);
			getDb4oTemplate().set(tag);			
		}else{
			getDb4oTemplate().bind(tag, tag.getId());
			getDb4oTemplate().set(tag);
		}
		
		
	}

}
