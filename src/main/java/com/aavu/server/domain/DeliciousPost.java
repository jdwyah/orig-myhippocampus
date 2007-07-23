package com.aavu.server.domain;

import java.util.Date;

public class DeliciousPost {

	private String description;
	private String href;
	private String tag_string;
	private String extended;
	private Date date;


	public DeliciousPost(String description, String href, String tags, String extended, Date date) {
		super();
		this.description = description;
		this.href = href;
		this.tag_string = tags;
		this.extended = extended;
		this.date = date;
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

	public String[] getTags() {
		return tag_string.split(" ");
	}

	public Date getDate() {
		return date;
	}

}
