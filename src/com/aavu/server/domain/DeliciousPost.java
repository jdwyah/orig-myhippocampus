package com.aavu.server.domain;

public class DeliciousPost {
	
	private String description;
	private String href;
	private String tag_string;
	private String extended;

	public DeliciousPost(String description, String href, String tags, String extended) {
		super();
		this.description = description;
		this.href = href;
		this.tag_string = tags;
		this.extended = extended;
	}
	
	public String getDescription() {
		return description;
	}

	public String getHref() {
		return href;
	}

	public String getTag_string() {
		return tag_string;
	}
	
	public String getExtended() {
		return extended;
	}

	public String[] getTags(){
		return tag_string.split(" ");
	}

}
