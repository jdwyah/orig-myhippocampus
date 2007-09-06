package com.aavu.server.dao;

import java.io.Serializable;
import java.util.List;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;

public interface EditDAO {

	void changeState(Topic t, boolean toIsland);

	void delete(Topic topic);

	void evict(Serializable obj);

	MindTree save(MindTree tree);

	Occurrence save(Occurrence link);

	Topic save(Topic t) throws HippoBusinessException;

	Long saveSimple(Topic t);

	void saveTopicsLocation(long tagID, long topicID, int latitude, int longitude, User currentUser)
			throws HippoBusinessException;

	void saveOccurrenceLocation(long topicID, long occurrenceID, int lat, int lng, User currentUser)
			throws HippoBusinessException;

	List<Topic> getDeleteList(long topicID);

	Topic merge(Topic t);



}
