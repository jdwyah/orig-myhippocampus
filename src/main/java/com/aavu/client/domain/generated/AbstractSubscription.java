package com.aavu.client.domain.generated;

import java.io.Serializable;

public class AbstractSubscription implements Serializable {

	private String description;
	private long id;
	private int maxTopics;
	private double price;

	public AbstractSubscription() {
	}

	public AbstractSubscription(long id, int maxTopics, String description, double price) {
		super();
		this.id = id;
		this.maxTopics = maxTopics;
		this.description = description;
		this.price = price;
	}

	// @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractSubscription))
			return false;

		final AbstractSubscription other = (AbstractSubscription) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (maxTopics != other.maxTopics)
			return false;
		return true;
	}

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	public int getMaxTopics() {
		return maxTopics;
	}

	public double getPrice() {
		return price;
	}

	// @Override
	public int hashCode() {
		final int PRIME = 31;
		long result = 1;
		result = PRIME * result + ((description == null) ? 0 : description.hashCode());
		result = PRIME * result + id;
		result = PRIME * result + maxTopics;
		return (int) result;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMaxTopics(int maxTopics) {
		this.maxTopics = maxTopics;
	}

	public void setPrice(double price) {
		this.price = price;
	}


}
