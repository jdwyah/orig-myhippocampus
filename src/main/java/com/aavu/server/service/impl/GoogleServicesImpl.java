package com.aavu.server.service.impl;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.aavu.server.service.TheGoogleService;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.Category;
import com.google.gdata.data.Link;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.util.ServiceException;

public class GoogleServicesImpl implements TheGoogleService {


	private static final Logger log = Logger.getLogger(GoogleServicesImpl.class);

	private DocsService service;

	public GoogleServicesImpl() {
		service = new DocsService("Document List Demo");



	}

	public void getAllDocs() throws IOException, ServiceException {
		service.setUserCredentials("jdwyah@gmail.com", "hippo2000");



		URL documentListFeedUrl = new URL("http://docs.google.com/feeds/documents/private/full");
		DocumentListFeed feed = service.getFeed(documentListFeedUrl, DocumentListFeed.class);

		for (DocumentListEntry entry : feed.getEntries()) {
			printDocumentEntry(entry);
		}

	}

	public void printDocumentEntry(DocumentListEntry doc) {
		String shortId = doc.getId().substring(doc.getId().lastIndexOf('/') + 1);


		log.info(" -- Document(" + shortId + "/" + doc.getTitle().getPlainText() + ")");

		for (Category c : doc.getCategories()) {
			log.info("cat: " + c);
		}
		for (Link l : doc.getLinks()) {
			log.info("link: " + l.getHref() + " " + l.getTitle());
		}

		log.info("go there " + doc.getDocumentLink().getHref() + " "
				+ doc.getDocumentLink().getTitle());
		log.info("title: " + doc.getTitle().getPlainText());
		log.info("summary: " + doc.getSummary());
		log.info("updated: " + doc.getUpdated());
		log.info("published: " + doc.getPublished());



	}
}
