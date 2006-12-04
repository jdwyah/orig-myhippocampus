package com.aavu.server.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.server.dao.MailingListDAO;
import com.aavu.server.domain.MailingListEntry;
import com.aavu.server.web.domain.MailingListCommand;

public class MailingListDAOHibernateImpl extends HibernateDaoSupport implements MailingListDAO {

	public void createEntry(MailingListCommand comm) {
		MailingListEntry e = new MailingListEntry(comm);
		getHibernateTemplate().save(e);
	}

	public List<MailingListEntry> getMailingList() {		
		return getHibernateTemplate().find("from MailingListEntry");		
	}

}
