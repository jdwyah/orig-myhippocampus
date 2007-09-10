package com.aavu.server.web.domain;

public class ImportCommand {

	private String deliciousName;
	private String deliciousPass;

	private String googlePass;
	private String googleName;
	private String googleDomain;

	public String getDeliciousName() {
		return deliciousName;
	}

	public void setDeliciousName(String deliciousName) {
		this.deliciousName = deliciousName;
	}

	public String getDeliciousPass() {
		return deliciousPass;
	}

	public void setDeliciousPass(String deliciousPass) {
		this.deliciousPass = deliciousPass;
	}

	public String getGoogleDomain() {
		return googleDomain;
	}

	public void setGoogleDomain(String googleDomain) {
		this.googleDomain = googleDomain;
	}

	public String getGooglePass() {
		return googlePass;
	}

	public void setGooglePass(String googlePass) {
		this.googlePass = googlePass;
	}

	public String getGoogleName() {
		return googleName;
	}

	public void setGoogleName(String googleName) {
		this.googleName = googleName;
	}

}
