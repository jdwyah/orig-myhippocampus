package com.aavu.client.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WebLink extends URI implements Serializable, IsSerializable, ReallyCloneable {

	public WebLink() {
	}

	public WebLink(User user, String description, String url, String notes) {
		setUser(user);
		setTitle(description);
		setUri(url);
		setData(notes);
	}

	public String getDescription() {
		return getTitle();
	}

	public String getNotes() {
		return getData();
	}

	public void setDescription(String text) {
		setTitle(text);
	}

	public void setNotes(String text) {
		setData(text);
	}

	// @Override
	public Object clone() {
		return copyProps(new WebLink());
	}

	// @Override
	public String getDefaultName() {
		return "New Weblink";
	}

	public boolean mustHaveUniqueName() {
		return false;
	}

	// @Override
	public String getWindowTheme() {
		return "alphacube-green";
	}

	// @Override
	public String getTitlePromptText() {
		return "Link:";
	}
}
