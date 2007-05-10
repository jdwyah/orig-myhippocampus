package com.aavu.server.service;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoInfrastructureException;
import com.aavu.server.domain.MailingListEntry;
import com.aavu.server.web.domain.MailingListCommand;

public interface InvitationService {

	void requestInvitation(MailingListCommand comm);

	void createAndSendInvitation(String email, User inviter) throws HippoBusinessException, HippoInfrastructureException;

	boolean isKeyValid(String randomkey);

	MailingListEntry getEntryForKey(String randomkey);

	void saveSignedUpUser(String randomkey, User u);
	void sendInvite(final MailingListEntry invitation) throws HippoInfrastructureException;

	MailingListEntry getEntryById(Long id);

	List<MailingListEntry> getMailingList();

	void setMailSender(JavaMailSender sender); 
}
