package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.User;
import com.aavu.server.domain.MailingListEntry;
import com.aavu.server.web.domain.MailingListCommand;

public interface MailingListDAO {

	void createEntry(MailingListCommand comm);
	MailingListEntry createEntry(String email, User inviter);

	List<MailingListEntry> getMailingList();
	void save(MailingListEntry invitation);	
	MailingListEntry getEntryForKey(String randomkey);
	MailingListEntry getEntryById(Long id);
	
}
