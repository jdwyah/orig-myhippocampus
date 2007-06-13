package com.aavu.server.dao;

import java.io.Serializable;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;

public interface EditDAO {

	void changeState(Topic t, boolean toIsland);

	void delete(Topic topic);

	void deleteOccurrence(Occurrence o);
	void evict(Serializable obj);

	MindTree save(MindTree tree);

	Occurrence save(Occurrence link);

	Topic save(Topic t) throws HippoBusinessException;

	Long saveSimple(Topic t);

	void saveTopicsLocation(long tagID, long topicID, double longitude, double latitude);


}
