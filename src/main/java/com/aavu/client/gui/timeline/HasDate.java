package com.aavu.client.gui.timeline;

import java.util.Date;

import com.aavu.client.domain.commands.AbstractCommand;

public interface HasDate {

	Date getEndDate();

	Date getStartDate();

	void setStartDate(Date newD);

	AbstractCommand getDateSaveCommand();

	void setEndDate(Date newD);


}
