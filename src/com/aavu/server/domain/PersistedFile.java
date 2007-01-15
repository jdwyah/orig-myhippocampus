package com.aavu.server.domain;

import java.io.Serializable;

public class PersistedFile implements Serializable {

	private String contentType;
	private byte[] content;
	private String filename;

	public PersistedFile(byte[] content, String contentType, String filename) {
		this.content = content;
		this.contentType = contentType;
		this.filename = filename;
	}

	public byte[] getContent() {
		return content;
	}

	public String getContentType() {
		return contentType;
	}

	public String getFilename() {
		return filename;
	}
	

}
