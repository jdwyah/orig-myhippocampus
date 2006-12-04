package com.aavu.server.domain;

import com.aavu.server.domain.generated.AbstractMailingListEntry;
import com.aavu.server.web.domain.MailingListCommand;

public class MailingListEntry extends AbstractMailingListEntry {

	/**
	 * Change the command into a DB serializable class. 
	 * 
	 * @param comm
	 */
	public MailingListEntry(MailingListCommand comm) {
		setEmail(comm.getEmail());
		setUserAgent(comm.getUserAgent());
		setHost(comm.getHost());
		setReferer(comm.getReferer());
	}
	
	

}
