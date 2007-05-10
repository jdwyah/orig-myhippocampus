package com.aavu.server.web.domain.validation;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoInfrastructureException;
import com.aavu.server.domain.MailingListEntry;
import com.aavu.server.service.InvitationService;
import com.aavu.server.web.domain.MailingListCommand;

/**
 * always returns unique == true
 * 
 * @author Jeff Dwyer
 *
 */
public class MockInvitationService implements InvitationService {

	private String badKey;
	private String goodKey;

	public MockInvitationService(String good_key, String bad_key) {
		this.goodKey = good_key;
		this.badKey = bad_key;
	}

	public MailingListEntry getEntryForKey(String randomkey) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isKeyValid(String randomkey) {				
		return randomkey.equals(goodKey);		
	}

	public void requestInvitation(MailingListCommand comm) {
		// TODO Auto-generated method stub
		
	}

	public void saveSignedUpUser(String randomkey, User u) {
		// TODO Auto-generated method stub
		
	}

	public void createAndSendInvitation(String email, User inviter) throws HippoBusinessException, HippoInfrastructureException {
		// TODO Auto-generated method stub
		
	}

	public MailingListEntry getEntryById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<MailingListEntry> getMailingList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendInvite(MailingListEntry invitation) throws HippoInfrastructureException {
		// TODO Auto-generated method stub
		
	}

	public void setMailSender(JavaMailSender sender) {
		// TODO Auto-generated method stub
		
	}

}
