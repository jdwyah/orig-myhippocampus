package com.aavu.client.domain.dto;

import java.util.Date;

public class DatedTopicIdentifier extends TopicIdentifier {
	
	private Date created;
	private Date lastUpdated;
	
	public DatedTopicIdentifier(){}
	
	public DatedTopicIdentifier(long topicID, String topicTitle) {
		super(topicID,topicTitle);
	}

	public DatedTopicIdentifier(long topicID, String topicTitle,Date created, Date lastUpdated) {
		super(topicID,topicTitle);
		this.created = created;
		this.lastUpdated = lastUpdated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	//@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());		
		result = PRIME * result + ((created == null) ? 0 : created.hashCode());
		return result;
	}

	//@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof DatedTopicIdentifier))
			return false;
		final DatedTopicIdentifier other = (DatedTopicIdentifier) obj;
		if (lastUpdated == null) {
			if (other.lastUpdated != null)
				return false;
		} else if (!lastUpdated.equals(other.lastUpdated))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		
		return true;
	}
	
}
