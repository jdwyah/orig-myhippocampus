package com.aavu.client.domain.subjects;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.strings.ConstHolder;

public class AmazonBook extends Amazon {

	/**
	 * Remember, these properties are not persisted to the DB Not transient becaseu they need to go
	 * over the RPC wire.
	 */
	private String author;
	private String detailURL;
	private String manufacturer;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDetailURL() {
		return detailURL;
	}

	public void setDetailURL(String detailURL) {
		this.detailURL = detailURL;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public String toString() {
		return "Title: " + getName() + "\nAuthor: " + author + "\nManufacturer: " + manufacturer
				+ "\nASIN: " + getForeignID() + "\nURL: " + getDetailURL();
	}

	// @Override
	public List<SubjectInfo> getInfos() {
		List<SubjectInfo> subjectInfo = new ArrayList<SubjectInfo>();
		subjectInfo.add(new SubjectInfo(ConstHolder.myConstants.book_title(), getName(), false));
		subjectInfo.add(new SubjectInfo(ConstHolder.myConstants.book_author(), getAuthor(), true));
		subjectInfo.add(new SubjectInfo(ConstHolder.myConstants.book_publisher(),
				getManufacturer(), false));
		return subjectInfo;
	}

	// @Override
	public String getTagName() {
		return ConstHolder.myConstants.book();
	}
}
