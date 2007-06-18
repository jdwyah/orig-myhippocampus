package com.aavu.server.dao;

import com.aavu.client.exception.HippoBusinessException;

public interface InitDAO {

	void doInit();

	void upgradeRemoveTags() throws HippoBusinessException;
	
	void displayRootInfo() throws HippoBusinessException;
	
}
