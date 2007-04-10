package com.aavu.server.dao;

import java.util.List;

import com.aavu.server.domain.MailingListEntry;
import com.aavu.server.web.domain.MailingListCommand;

public interface MailingListDAO {

	void createEntry(MailingListCommand comm);

	List<MailingListEntry> getMailingList();
	
}
