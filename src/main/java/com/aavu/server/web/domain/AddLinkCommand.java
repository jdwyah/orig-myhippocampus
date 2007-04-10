package com.aavu.server.web.domain;

public class AddLinkCommand {
	
	/**
	 * why the odd names?
	 * 
	 * otherwise the FF plugin goes to addLink.html?description="foo",
	 * we init the command, but then on post the binder doubles us up, 
	 * once from the url description, once from the command. Better solution? 
	 * 
	 */
	private String command_url;
	private String command_notes;
	private String command_description;
	/**
	 * the hidden field
	 */
	private String command_tags;
	/**
	 * the visible field
	 */
	private String command_tags_field;
	
	public String getCommand_description() {
		return command_description;
	}
	public void setCommand_description(String description) {
		this.command_description = description;
	}
	public String getCommand_notes() {
		return command_notes;
	}
	public void setCommand_notes(String notes) {
		this.command_notes = notes;
	}
	public String getCommand_tags() {
		return command_tags;
	}
	public void setCommand_tags(String tags) {
		this.command_tags = tags;
	}
	public String getCommand_url() {
		return command_url;
	}
	public void setCommand_url(String url) {
		this.command_url = url;
	}
	public String getCommand_tags_field() {
		return command_tags_field;
	}
	public void setCommand_tags_field(String command_tags_field) {
		this.command_tags_field = command_tags_field;
	}
	public String toString(){
		return "descr: "+getCommand_description()+"\nnotes "+getCommand_notes()+"\ntags "+getCommand_tags()+"\nurl "+getCommand_url();
	}

}
