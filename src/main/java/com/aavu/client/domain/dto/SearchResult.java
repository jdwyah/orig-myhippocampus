package com.aavu.client.domain.dto;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchResult implements IsSerializable {


	private String text;

	private float score;
	private boolean publicVisible;

	private Topic result;
	private List clickLinks;
	private User user;

	private boolean selfLink = false;


	public SearchResult() {
	}

	public SearchResult(Topic result, final List _clickLinks, float score,
			final String highlightText) {

		this.result = result;
		this.clickLinks = _clickLinks;
		this.score = score;
		this.text = highlightText;
		this.publicVisible = result.isPublicVisible();
		this.user = result.getUser();

		if (null == clickLinks) {
			selfLink = true;
			clickLinks = new ArrayList();
			clickLinks.add(result.getIdentifier());
		}
	}



	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}



	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public boolean isPublicVisible() {
		return publicVisible;
	}


	public void setPublicVisible(boolean publicVisible) {
		this.publicVisible = publicVisible;
	}


	public boolean isSelfLink() {
		return selfLink;
	}

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Topic getResult() {
		return result;
	}

	public void setResult(Topic result) {
		this.result = result;
	}

	public List getClickLinks() {
		return clickLinks;
	}

	public void setClickLinks(List clickLinks) {
		this.clickLinks = clickLinks;
	}


	public String toString() {
		return getResult() + " " + getScore() + " " + " " + getText();
	}



}
