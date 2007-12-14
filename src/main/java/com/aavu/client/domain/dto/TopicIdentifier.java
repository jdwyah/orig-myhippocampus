package com.aavu.client.domain.dto;


import java.io.Serializable;

public class TopicIdentifier implements HippoIdentifier, Serializable, Comparable<TopicIdentifier> {

	private long topicID;
	private String topicTitle;
	private boolean publicVisible;


	public TopicIdentifier() {
	}

	public TopicIdentifier(long topicID, String topicTitle, boolean publicVisible) {
		super();
		this.topicID = topicID;
		this.topicTitle = topicTitle;
		this.publicVisible = publicVisible;
	}


	public long getTopicID() {
		return topicID;
	}

	public void setTopicID(long topicID) {
		this.topicID = topicID;
	}

	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	public boolean isPublicVisible() {
		return publicVisible;
	}

	public void setPublicVisible(boolean publicVisible) {
		this.publicVisible = publicVisible;
	}

	public String toString() {
		return getTopicID() + " " + getTopicTitle();
	}

	// @Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (int) (topicID ^ (topicID >>> 32));
		result = PRIME * result + ((topicTitle == null) ? 0 : topicTitle.hashCode());
		return result;
	}

	// @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TopicIdentifier))
			return false;
		final TopicIdentifier other = (TopicIdentifier) obj;
		if (topicID != other.topicID)
			return false;
		if (topicTitle == null) {
			if (other.topicTitle != null)
				return false;
		} else if (!topicTitle.equals(other.topicTitle))
			return false;
		return true;
	}

	/**
	 * no compartToIgnoreCase in GWT String impl
	 */
	public int compareTo(TopicIdentifier o) {
		if (o == null) {
			return 1;
		}
		return getTopicTitle().toLowerCase().compareTo(o.getTopicTitle().toLowerCase());

	}
}
