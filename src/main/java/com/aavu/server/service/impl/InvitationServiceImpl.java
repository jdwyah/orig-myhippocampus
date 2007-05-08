package com.aavu.server.service.impl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoInfrastructureException;
import com.aavu.server.dao.MailingListDAO;
import com.aavu.server.domain.MailingListEntry;
import com.aavu.server.service.InvitationService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.MailingListCommand;

import freemarker.template.Template;

public class InvitationServiceImpl implements InvitationService {
	private static final Logger log = Logger.getLogger(InvitationServiceImpl.class);
	
	private FreeMarkerConfigurer configurer = null;
	private String from;
	private String invitationTemplate;

	private MailingListDAO	mailingListDAO;
	private JavaMailSender mailSender;
	private String masterkey;

	private UserService userService;
	
	
	
	public MailingListEntry getEntryForKey(String randomkey) {		
		return mailingListDAO.getEntryForKey(randomkey);
	}

	public boolean isKeyValid(String randomkey) {
		return (getEntryForKey(randomkey) != null)
		||
		randomkey.equals(masterkey);		
	}

	public void requestInvitation(MailingListCommand comm) {
		mailingListDAO.createEntry(comm);
	}

	public void saveSignedUpUser(String randomkey, User u) {
		//may be null for masterkey overrides of the system		
		MailingListEntry entry = getEntryForKey(randomkey);
		if(entry != null){
			entry.setSignedUpUser(u);
			mailingListDAO.save(entry);
		}
	}





	/**
	 * See http://opensource.atlassian.com/confluence/spring/display/DISC/Sending+FreeMarker-based+multipart+email+with+Spring
	 */
	public void createAndSendInvitation(final String email, final User inviter) throws HippoBusinessException, HippoInfrastructureException {

		if(inviter.getInvitations() < 1){
			throw new HippoBusinessException("No invites available for user.");
		}

		final MailingListEntry invitation = mailingListDAO.createEntry(email, inviter);

		userService.subtractInvitationFrom(inviter);

		sendInvite(invitation);


	}

	public void sendInvite(final MailingListEntry invitation) throws HippoInfrastructureException {
		//send mail
		try {
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setTo(invitation.getEmail());
					message.setFrom(from);
					message.setSubject("MyHippocampus Invitation");
					
					Map<String,Object> model = new HashMap<String, Object>();            	 
					model.put("inviter", invitation.getInviter());
					model.put("randomkey", invitation.getRandomkey());
					model.put("email", invitation.getEmail());
					
					Template textTemplate = configurer.getConfiguration().getTemplate(invitationTemplate);
					final StringWriter textWriter = new StringWriter();

					textTemplate.process(model, textWriter);

					message.setText(textWriter.toString(), true);
					
					
					
					log.info("Inviting: "+invitation.getEmail());
					log.debug("From: "+from);
					log.debug("Message: "+textWriter.toString());
					
				}
			};
			this.mailSender.send(preparator);

			invitation.setSentEmailOk(true);
			mailingListDAO.save(invitation);

		} catch (Exception e) {
			log.error(e);
			throw new HippoInfrastructureException(e);
		}
	}
	
	

	public void setConfigurer(FreeMarkerConfigurer configuration) {
		this.configurer = configuration;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setInvitationTemplate(String invitationTemplate) {
		this.invitationTemplate = invitationTemplate;
	}

	public void setMailingListDAO(MailingListDAO mailingListDAO) {
		this.mailingListDAO = mailingListDAO;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setMasterkey(String masterkey) {
		this.masterkey = masterkey;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public MailingListEntry getEntryById(Long id) {
		return mailingListDAO.getEntryById(id);
	}

	public List<MailingListEntry> getMailingList() {
		return mailingListDAO.getMailingList();
	}







}