package com.aavu.server.domain;

import com.aavu.server.domain.generated.AbstractMailingListEntry;
import com.aavu.server.util.RandomUtils;
import com.aavu.server.web.domain.MailingListCommand;

public class MailingListEntry extends AbstractMailingListEntry {

	private static final int KEY_LENGTH = 20;

	public MailingListEntry(){
		setRandomkey(RandomUtils.randomstring(KEY_LENGTH));
	};
	
	/**
	 * Change the command into a DB serializable class. 
	 * 
	 * @param comm
	 */
	public MailingListEntry(MailingListCommand comm) {
		this();
		setEmail(comm.getEmail());
		setUserAgent(comm.getUserAgent());
		setHost(comm.getHost());
		setReferer(comm.getReferer());		
	}
	
	

}
