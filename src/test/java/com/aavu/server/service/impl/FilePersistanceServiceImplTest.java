package com.aavu.server.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.S3File;
import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoException;
import com.aavu.server.dao.EditDAO;
import com.aavu.server.dao.SelectDAO;
import com.aavu.server.domain.PersistedFile;
import com.aavu.server.service.FilePersistanceService;
import com.aavu.server.service.UserService;
import com.aavu.server.service.gwt.BaseTestWithTransaction;
import com.aavu.server.util.IOUtils;

public class FilePersistanceServiceImplTest extends BaseTestWithTransaction {

	private static final Logger log = Logger.getLogger(FilePersistanceServiceImplTest.class);

	private static final int TOPIC_ID = 0;
	private static final String FILE_NAME = "TEST_DATA";
	private static final String TOPIC_TITLE = "TEST_TITLE";

	public FilePersistanceService fileService;
	public UserService userService;
	public SelectDAO selectDAO;
	public EditDAO editDAO;

	@Required
	public void setEditDAO(EditDAO editDAO) {
		this.editDAO = editDAO;
	}

	@Required
	public void setFileService(FilePersistanceService fileService) {
		this.fileService = fileService;
	}

	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Required
	public void setSelectDAO(SelectDAO selectDAO) {
		this.selectDAO = selectDAO;
	}



	public void testSaveFileStringIntByteArrayStringUser() throws HippoException {
		byte[] orig_bytes = new byte[] { 0, 54, 22, 34 };

		String key = fileService.saveFile("text/plain", TOPIC_ID, orig_bytes, FILE_NAME,
				userService.getCurrentUser());

		PersistedFile file = fileService.getFile(userService.getCurrentUser(), key);

		byte[] cnt = file.getContent();
		for (int i = 0; i < cnt.length; i++) {
			assertEquals(orig_bytes[i], cnt[i]);
		}

		fileService.deleteFile(userService.getCurrentUser(), key);

	}

	public void testSaveFileStringIntInputStreamStringUser() throws HippoException, IOException {
		Topic t = new RealTopic(userService.getCurrentUser(), TOPIC_TITLE);

		t = editDAO.save(t);

		// File f = new File("build.xml");
		// System.out.println(f.getAbsolutePath());

		InputStream inputStream = new FileInputStream("build.xml");
		int available = inputStream.available();

		S3File s3 = null;
		try {
			PersistedFile fileToSave = new PersistedFile(IOUtils.getStreamAsByteArray(inputStream),
					"text/plain", "build.xml");

			fileService.saveFileToTopic(fileToSave, t, userService.getCurrentUser());

			t = selectDAO.getForName(userService.getCurrentUser(), TOPIC_TITLE);

			assertEquals(1, t.getOccurenceObjs().size());

			Occurrence o = (Occurrence) t.getOccurenceObjs().iterator().next();
			assertTrue(o instanceof S3File);
			s3 = (S3File) o;


			log.debug("URI " + s3.getUri());

			assertTrue(s3.getUri().startsWith(
					userService.getCurrentUser().getUsername() + "/" + t.getId() + "/"));

			PersistedFile file = fileService.getFile(userService.getCurrentUser(), s3.getUri());

			assertEquals("text/plain", file.getContentType());
			assertNotNull(file.getContent());

			log.debug("Checking saved length: " + available);
			assertEquals(available, file.getContent().length);

		} finally {
			if (s3 != null) {
				fileService.deleteFile(userService.getCurrentUser(), s3.getUri());
			} else {
				fail("s3 was null");
			}
		}
	}

	/**
	 * tested implicitly above
	 */
	public void testGetFile() {

	}

}
