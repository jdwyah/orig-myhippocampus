package com.aavu.server.web.domain;

import com.aavu.client.domain.User;

public class UserPageBean {
	
	private User user;
	
	private int numberOfTopics;
	private int numberOfIslands;
	private int numberOfLinks;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getNumberOfIslands() {
		return numberOfIslands;
	}
	public void setNumberOfIslands(int numberOfIslands) {
		this.numberOfIslands = numberOfIslands;
	}
	public int getNumberOfLinks() {
		return numberOfLinks;
	}
	public void setNumberOfLinks(int numberOfLinks) {
		this.numberOfLinks = numberOfLinks;
	}
	public int getNumberOfTopics() {
		return numberOfTopics;
	}
	public void setNumberOfTopics(int numberOfTopics) {
		this.numberOfTopics = numberOfTopics;
	}
	
	@Override
	public String toString() {
		return "Islands: "+getNumberOfIslands()+" Links: "+getNumberOfLinks()+" Topics: "+getNumberOfTopics();		
	}
	
	
	
	
}
