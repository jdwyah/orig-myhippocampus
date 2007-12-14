package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveDatesCommand;
import com.aavu.client.domain.generated.AbstractOccurrence;
import com.aavu.client.gui.timeline.HasDate;

public class Occurrence extends AbstractOccurrence implements Serializable, ReallyCloneable,
		HasDate {

	public Occurrence() {

	}

	/**
	 * This is a leaked concern of CGLIB
	 */
	// @Override
	public Object clone() {
		return copyPropsIntoParam(new Occurrence());
	}

	/**
	 * copy properties of _this_ into the parameter
	 * 
	 * @param o
	 * @return
	 */
	public Occurrence copyPropsIntoParam(Occurrence o) {
		super.copyPropsIntoParam(o);
		o.setData(getData());
		return o;
	}

	public Occurrence copyPropsButNotIDIntoParam(Occurrence o) {
		System.out.println("Occ copy rpops");
		super.copyPropsButNotIDIntoParam(o);
		o.setData(getData());
		return o;
	}

	public Set<Topic> getTopicsAsTopics() {

		Set<Topic> rtn = new HashSet<Topic>();
		for (TopicOccurrenceConnector toc : getTopics()) {
			rtn.add(toc.getTopic());
		}
		return rtn;
	}

	public Date getEndDate() {
		return null;
	}

	public AbstractCommand getDateSaveCommand() {
		return new SaveDatesCommand(this, getStartDate());
	}

	public Date getStartDate() {
		return getCreated();
	}

	public void setStartDate(Date startDate) {
		setCreated(startDate);
	}

	/**
	 * no-op. This shouldn't be used since it is lastUpdated
	 */
	public void setEndDate(Date newD) {
		// no-op
	}



}
