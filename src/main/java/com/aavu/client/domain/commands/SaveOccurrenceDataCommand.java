package com.aavu.client.domain.commands;

import java.io.Serializable;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.URI;
import com.aavu.client.exception.HippoBusinessException;

public class SaveOccurrenceDataCommand extends AbstractCommand implements Serializable {

	private String title;
	private String uri;

	public SaveOccurrenceDataCommand() {
	};

	public SaveOccurrenceDataCommand(Occurrence occ, String title, String data) {
		this(occ, title, data, null);
	}

	public SaveOccurrenceDataCommand(Occurrence occ, String title, String data, String uri) {
		super(occ);
		setData(data);
		this.title = title;
		this.uri = uri;
	}

	// @Override
	public void executeCommand() throws HippoBusinessException {
		Occurrence occ = (Occurrence) getTopic(0);

		occ.setData(getData());

		occ.setTitle(title);

		if (uri != null) {
			if (occ instanceof URI) {
				URI uocc = (URI) occ;
				uocc.setUri(uri);
			} else {
				throw new HippoBusinessException("Trying to save URI to non URI occurrence. "
						+ getTopicID(0) + " " + uri);
			}
		}
	}

	// @Override
	public String toString() {
		return "SaveOccurrenceDataCommand ID " + getTopicID(0) + " " + title + " " + getData()
				+ " " + uri;
	}



}
