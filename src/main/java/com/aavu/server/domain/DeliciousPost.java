package com.aavu.server.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DeliciousPost {

	private String description;
	private String href;
	private String tag_string;
	private String extended;
	private Date date;
	private boolean shared;


	public DeliciousPost(String description, String href, String tags, String extended,
			String shareStr, Date date) {
		super();
		this.description = description;
		this.href = href;
		this.tag_string = tags;
		this.extended = extended;
		this.date = date;

		if (shareStr != null && shareStr.equals("no")) {
			this.shared = false;
		} else {
			this.shared = true;
		}

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

	/**
	 * A bit tricky. Couldn't find a suitable regex. See TestCase for details
	 * 
	 * @return
	 */
	public String[] getTags() {
		String[] tmp = {};

		List<String> results = new LinkedList<String>();

		StringBuffer b = new StringBuffer(tag_string);

		int start = 0;
		int end = -1;
		int adder = 1;

		while (end < b.length() - 1) {


			if (b.charAt(start) == '\"') {
				start++;// skip over the "
				end = b.indexOf("\"", start);
				adder = 2;// skip over the space after the "
			} else {
				end = b.indexOf(" ", start);
				adder = 1;
			}
			if (end == -1) {
				end = b.length();
			}

			results.add(b.substring(start, end));
			start = end + adder;
		}

		return results.toArray(tmp);
	}

	public Date getDate() {
		return date;
	}

	public boolean isShared() {
		return shared;
	}

}
