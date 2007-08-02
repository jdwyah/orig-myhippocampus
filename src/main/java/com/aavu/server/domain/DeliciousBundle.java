package com.aavu.server.domain;


public class DeliciousBundle {

	private String name;
	private String tag_string;

	public DeliciousBundle(String name, String tags) {
		super();
		this.name = name;
		this.tag_string = tags;
	}

	public String getName() {
		return name;
	}


	public String getTag_string() {
		return tag_string;
	}


	public String[] getTags() {
		return tag_string.split(" ");
	}

}
