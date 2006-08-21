package com.aavu.server.dao.db4o;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.db4ospring.support.Db4oDaoSupport;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TagDAO;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class TagDAOdb4oImpl extends Db4oDaoSupport implements TagDAO {
	private static final Logger log = Logger.getLogger(TagDAOdb4oImpl.class);

	public List<Tag> getAllTags(User user) {
		List<Tag> rtn = getDb4oTemplate().get(Tag.class);

		for(Tag tag : rtn){
			tag.getMetas();
		}

		return rtn;
	}

	/**
	 * Get Tag, or null
	 * 
	 */
	public Tag getTag(final User user,final String tagName) {

		Tag rtn = (Tag) Db4oUtil.getUniqueResOrNull(getDb4oTemplate().query(new Predicate<Tag>() {
			public boolean match(Tag tag) {			
				return 
				tag.isPublicVisible() || 
				(user.equals(tag.getUser())
						&&
						tag.getName().toLowerCase().startsWith(tagName.toLowerCase()));

			}
		}));

		return rtn;
	}

	public void removeTag(User user,Tag selectedTag) {

		if(selectedTag.getId() == 0){
			log.error("Deleting unsaved tag: "+selectedTag.getName());
			throw new RuntimeException("Deleting ");
		}else{
			getDb4oTemplate().bind(selectedTag, selectedTag.getId());
			getDb4oTemplate().delete(selectedTag);
		}

	}

	public List<Tag> getTagsStarting(User user,String match) {
		Query q = getDb4oTemplate().query();
		q.constrain(Tag.class);
		q.descend("name").constrain(match).startsWith(false);

		List<Tag> rtn = q.execute();


		return rtn;
	}

	public void save(Tag tag) {

		System.out.println("SAVE Tag: " + tag.getId()+" "+tag.getName());
		System.out.println("metas: ");
		for (Object meta : tag.getMetas()){
			Meta metaCast = (Meta)meta;
			System.out.println(metaCast+" "+metaCast.getName()+" "+metaCast.getId());
		}

		//now save metas
		//Not done using the cascade above because that won't do the binding correctly.
		//could replace with deleting all, then re-adding...
		//cascade w/ bind? Stateless is an unfortunate bedfellow
		//
		for (Iterator iter = tag.getMetas().iterator(); iter.hasNext();) {
			Meta m = (Meta) iter.next();
			if(m.getId() == 0){
				getDb4oTemplate().set(m);
				long id = getDb4oTemplate().getID(m);
				System.out.println("meta save was 0, now "+id);				
				m.setId(id);	
			}else{
				getDb4oTemplate().bind(m, m.getId());	
			}
			getDb4oTemplate().set(m);
		}
		
		
		//Db4o.configure().objectClass("com.aavu.client.domain.Tag").cascadeOnUpdate(true);
		//Db4o.configure().objectClass("com.aavu.client.domain.Tag").updateDepth(1);

		//log.debug("save tag: "+tag.getId()+" "+tag+" "+tag.getMetas());
		if(tag.getId() == 0){

			getDb4oTemplate().set(tag);

			long id = getDb4oTemplate().getID(tag);
			System.out.println("tag save was 0, now "+id);				
			tag.setId(id);						
		}else{		
			getDb4oTemplate().bind(tag, tag.getId());			
		}
		getDb4oTemplate().set(tag);

		
	


	}

}
