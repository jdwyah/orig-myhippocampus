package com.aavu.client.domain.generated;

import java.io.Serializable;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.OccurrenceI;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.google.gwt.user.client.rpc.IsSerializable;

public class AbstractTopicOccurrenceConnector implements Serializable, IsSerializable {

	private long id;
	private Topic topic;
	private OccurrenceI occurrence;
	private int latitude;
	private int longitude;
	
	public AbstractTopicOccurrenceConnector(){}
	
	
	public AbstractTopicOccurrenceConnector(Topic topic, OccurrenceI occurrence, int latitude, int longitude) {
		super();
		this.topic = topic;
		this.occurrence = occurrence;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public int getLongitude() {
		return longitude;
	}
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	


	public OccurrenceI getOccurrence() {
		return occurrence;
	}


	public void setOccurrence(OccurrenceI occurrence) {
		this.occurrence = occurrence;
	}


	//@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;		
		result = PRIME * result + (int) (latitude * 1000);		
		result = PRIME * result + (int) (longitude * 1000);
		result = PRIME * result + ((topic == null) ? 0 : topic.hashCode());
		result = PRIME * result + ((occurrence == null) ? 0 : occurrence.hashCode());
		return result;
	}
	//@Override
	public boolean equals(Object obj) {
		
		
//		System.out.println(".equals");
//		System.out.println("Me "+topic+" "+type);
//		TopicTypeConnector him = (TopicTypeConnector) obj;
//		System.out.println("Him "+him.getTopic()+" "+him.getType());
//		
//		
//		System.out.println("2"+(!topic.equals(him.getTopic())));
//		System.out.println("3"+());
//		System.out.println("4"+(longitude != him.getLongitude()));
//		System.out.println("5"+(!type.equals(him.getType())));
		
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractTopicOccurrenceConnector))
			return false;
		final AbstractTopicOccurrenceConnector other = (AbstractTopicOccurrenceConnector) obj;
		if (latitude != other.latitude)
			return false;
		if (longitude != other.longitude)
			return false;
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic)){
			System.out.println("dinging on top");
			return false;
		}
		if (occurrence == null) {
			if (other.occurrence != null)
				return false;
		} else if (!occurrence.equals(other.occurrence)){
			System.out.println("dinging on type");
			return false;
		}
		
		System.out.println("TopicType .eq rtn true");
		return true;
	}
	
	
}
