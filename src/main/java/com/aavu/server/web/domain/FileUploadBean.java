package com.aavu.server.web.domain;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadBean {

    private Integer topicID;
    private MultipartFile file;

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }
    
	public Integer getTopicID() {
		return topicID;
	}

	public void setTopicID(Integer topicID) {
		this.topicID = topicID;
	}
	
}
