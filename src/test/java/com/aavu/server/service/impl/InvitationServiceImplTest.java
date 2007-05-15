package com.aavu.server.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.AssertThrows;

import com.aavu.client.domain.Subscription;
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
	private static final int INV = 3;
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

		List<Subscription> allSubscriptions = userDAO.getAllSubscriptions();		
		assertEquals(4, allSubscriptions.size());
		
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
		
		allSubscriptions = userDAO.getAllSubscriptions();		
		assertEquals(4, allSubscriptions.size());
		
        assertEquals(0, sUser.getInvitations());
        
        
        //Trying to ferret out a problem where sending an invite 
        //gives users a new subscription, a dupe of the default.
        //With save-update this leads to new DB rows, w/o we get transient excpetion. 
        //
        //NOTE: Figured out what was wrong. Our 'Basic Subscription' was ID 0,
        //bizarely, sometimes Hibernate decided that this meant it was unsaved and 
        //would try to do an insert. The weird thing is that it seemed to be user dependent.
        //Fix was to bump all subscriptions in DB up 1 row, (1-4) instead of (0-3) so that there is no 0 row.
        User u2 = new User();
        u2.setUsername("u2");
        u2.setInvitations(INV);        
        u2.setPassword("foo");
        
        u2 = userDAO.save(u2);
        
        invitationService.createAndSendInvitation("jdwyah@gmail.com", u2);
		
    	sUser = userDAO.getUserForId(u2.getId());
        assertEquals(INV - 1, sUser.getInvitations());
        
        allSubscriptions = userDAO.getAllSubscriptions();		
		assertEquals(4, allSubscriptions.size());

		User u3 = new User();
		u3.setUsername("u3");
		u3.setInvitations(INV);        
		u3.setPassword("foo");
		u3.setSubscription(null);

		u3 = userDAO.save(u3);

		invitationService.createAndSendInvitation("jdwyah@gmail.com", u3);

		sUser = userDAO.getUserForId(u3.getId());
		assertEquals(INV - 1, sUser.getInvitations());

		allSubscriptions = userDAO.getAllSubscriptions();		
		assertEquals(4, allSubscriptions.size());
		
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
