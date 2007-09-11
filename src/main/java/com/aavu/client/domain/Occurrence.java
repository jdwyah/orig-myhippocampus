package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aavu.client.domain.generated.AbstractOccurrence;
import com.aavu.client.gui.timeline.HasDate;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Occurrence extends AbstractOccurrence implements Serializable, IsSerializable,
		ReallyCloneable, HasDate {

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

	public Set getTopicsAsTopics() {

		Set rtn = new HashSet();
		for (Iterator iter = getTopics().iterator(); iter.hasNext();) {
			TopicOccurrenceConnector toc = (TopicOccurrenceConnector) iter.next();
			rtn.add(toc.getTopic());
		}
		return rtn;
	}

	public Date getEndDate() {
		return null;
	}

	public Date getStartDate() {
		return getCreated();
	}



}
