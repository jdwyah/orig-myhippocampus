package com.aavu.client.domain;

// Generated Jul 18, 2006 12:44:47 PM by Hibernate Tools 3.1.0.beta4

import java.io.Serializable;
import java.util.Date;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveDatesCommand;
import com.aavu.client.gui.timeline.HasDate;
import com.google.gwt.user.client.rpc.IsSerializable;



/**
 * NOTE: HippoDate stores startDate as Created & endDate as lastUpdated.
 */
public class HippoDate extends MetaValue implements IsSerializable, Serializable, HasDate {

	private transient static SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy");


	public HippoDate() {
		setPublicVisible(false);
	}


	public HippoDate(long id, String title, Date date, Date endDate) {
		setId(id);
		setTitle(title);
		setStartDate(date);
		setEndDate(endDate);
	}

	/**
	 * 
	 * @return
	 */
	public Date getEndDate() {
		return getLastUpdated();
	}

	public AbstractCommand getDateSaveCommand() {
		return new SaveDatesCommand(this, getStartDate(), getEndDate());
	}

	public Date getStartDate() {
		return getCreated();
	}

	public boolean mustHaveUniqueName() {
		return false;
	}

	public void setEndDate(Date date) {
		setLastUpdated(date);
	}

	public void setStartDate(Date date) {
		if (getTitle() == null || getTitle().equals("")) {
			setTitle(df.format(date));
		}
		setCreated(date);
	}



	// @Override
	public String getDefaultName() {
		return "(New Date Range)";
	}


	/**
	 * bc we use lastUpdated for our own nefarious purpose, make sure not to update it on saves
	 */
	// @Override
	public boolean usesLastUpdated() {
		return true;
	}

	// public void deltaStartDate(int dxSeconds) {
	// getStartDate().setTime(getStartDate().getTime() + dxSeconds);
	// }
	//
	// public void deltaEndDate(int dxSeconds) {
	// getEndDate().setTime(getEndDate().getTime() + dxSeconds);
	// }

}
