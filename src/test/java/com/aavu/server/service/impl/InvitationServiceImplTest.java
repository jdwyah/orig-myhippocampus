package com.aavu.server.service.impl;

import java.io.InputStream;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.tools.ant.taskdefs.Ear;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.AssertThrows;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoInfrastructureException;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.service.InvitationService;
import com.aavu.server.service.UserService;
import com.aavu.server.service.gwt.BaseTestWithTransaction;

public class InvitationServiceImplTest extends BaseTestWithTransaction {

	private class MockJavaMailSender implements JavaMailSender {

		public MimeMessage createMimeMessage() {
			return null;
		}

		public MimeMessage createMimeMessage(InputStream arg0) throws MailException {
			// TODO Auto-generated method stub
			return null;
		}

		public void send(MimeMessage arg0) throws MailException {
			// TODO Auto-generated method stub
			
		}

		public void send(MimeMessage[] arg0) throws MailException {
			// TODO Auto-generated method stub
			
		}

		public void send(MimeMessagePreparator arg0) throws MailException {
			// TODO Auto-generated method stub
			
		}

		public void send(MimeMessagePreparator[] arg0) throws MailException {
			// TODO Auto-generated method stub
			
		}

		public void send(SimpleMailMessage arg0) throws MailException {
			// TODO Auto-generated method stub
			
		}

		public void send(SimpleMailMessage[] arg0) throws MailException {
			// TODO Auto-generated method stub
			
		}
		
	}
	private InvitationService invitationService;
	private User u;
	private UserDAO userDAO;

	
	private UserService userService;


	@Override
	protected void onSetUpInTransaction() throws Exception {	
		super.onSetUpInTransaction();		
		u = userService.getCurrentUser();	
		
		
		System.out.println("invitationService"+invitationService+" "+invitationService.getClass());
		
		System.out.println(invitationService instanceof InvitationServiceImpl);
		
//		InvitationServiceImpl impl = (InvitationServiceImpl) invitationService;
		
		invitationService.setMailSender(new MockJavaMailSender());
	}


	public void setInvitationService(InvitationService invitationService) {
		this.invitationService = invitationService;
	}


	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	public void testCreateAndSendInvitation() throws HippoBusinessException, HippoInfrastructureException {

		new AssertThrows(HippoBusinessException.class) {
            public void test() throws HippoBusinessException, HippoInfrastructureException {
            	invitationService.createAndSendInvitation("jdwyah@gmail.com", u);
            }
        }.runTest();
		

        
        userService.addInvitationsTo(u, 4);
        u = userService.getCurrentUser();
        
        User sUser = userDAO.getUserForId(u.getId());
        
        assertEquals(4, sUser.getInvitations());
        
		invitationService.createAndSendInvitation("jdwyah@gmail.com", sUser);
		invitationService.createAndSendInvitation("jdwyah@gmail.com", sUser);
		invitationService.createAndSendInvitation("jdwyah@gmail.com", sUser);
		invitationService.createAndSendInvitation("jdwyah@gmail.com", sUser);
		
		sUser = userDAO.getUserForId(u.getId());
		
        assertEquals(0, sUser.getInvitations());
        
        
		
	}
//	public void testGetEntryForKey() {
//		fail("Not yet implemented");
//	}
//
//	public void testIsKeyValid() {
//		fail("Not yet implemented");
//	}
//
//	public void testRequestInvitation() {
//		fail("Not yet implemented");
//	}
//
//	public void testSaveSignedUpUser() {
//		fail("Not yet implemented");
//	}
//
//	public void testSendInvite() {
//		fail("Not yet implemented");
//	}


}
