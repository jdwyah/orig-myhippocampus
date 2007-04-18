package com.aavu.server.web.domain;

public class CreateUserRequestCommand {

	private String username;
	private String password;
	private String password2;
	private String email;
	private String randomkey;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRandomkey() {
		return randomkey;
	}
	public void setRandomkey(String randomkey) {
		this.randomkey = randomkey;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
