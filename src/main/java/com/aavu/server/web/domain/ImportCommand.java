package com.aavu.server.web.domain;

public class ImportCommand {

	private String deliciousName;
	private String deliciousPass;

	private String googleName;
	private String googlePass;

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
