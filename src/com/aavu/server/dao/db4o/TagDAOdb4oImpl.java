package com.aavu.server.dao.db4o;

import java.util.List;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.server.dao.TagDAO;
import com.db4o.Db4o;
import com.db4o.query.Query;

public class TagDAOdb4oImpl extends Db4oDAO implements TagDAO {

	public void addTag(Tag tag) {
		getDb().set(tag);
		closeDB();
	}

	public List<Tag> getAllTags() {
		List<Tag> rtn = getDb().get(Tag.class);
		closeDB();
		return rtn;
	}

	public Tag getTag(String tagName) {

		Query q = getDb().query();
		q.constrain(Tag.class);
		q.descend("name").constrain(tagName);
		
		Tag rtn = null;
		
		List<Tag> o = q.execute();

		if(o.size() > 0){
			rtn = (Tag) o.get(0);
		}
		
		
		closeDB();
		return rtn;
	}

	public void removeTag(String itemText) {
		//TODO
	}

	public List<Tag> getTagsStarting(String match) {
		Query q = getDb().query();
		q.constrain(Tag.class);
		q.descend("name").constrain(match).startsWith(false);
		
		List<Tag> rtn = q.execute();
		
		closeDB();
		
		return rtn;
	}

	public void save(Tag selectedTag) {
	
		System.out.println("Tag: " + selectedTag.getName());
		System.out.println("metas: ");
		for (Object meta : selectedTag.getMetas()){
			Meta metaCast = (Meta)meta;
			System.out.println(metaCast);
		}

		Db4o.configure().objectClass("com.aavu.client.domain.Tag").cascadeOnUpdate(true);
		Db4o.configure().objectClass("com.aavu.client.domain.Tag").updateDepth(1);
		
		getDb().ext().bind(selectedTag, selectedTag.getId());
		getDb().set(selectedTag);
		closeDB();
		
	}

}
