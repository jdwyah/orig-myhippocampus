package com.aavu.client.domain.dto;

import java.io.Serializable;

import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;

public class LinkAndUser implements Serializable {
	private WebLink weblink;
	private User user;

	public LinkAndUser() {
	}

	public LinkAndUser(WebLink weblink, User user) {
		super();
		this.weblink = weblink;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public WebLink getWeblink() {
		return weblink;
	}

	public void setWeblink(WebLink weblink) {
		this.weblink = weblink;
	}

}
