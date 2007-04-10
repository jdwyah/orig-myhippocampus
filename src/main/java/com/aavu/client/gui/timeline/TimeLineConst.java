package com.aavu.client.gui.timeline;

public class TimeLineConst {

	public static final TimeLineConst UPDATED = new TimeLineConst(0);
	public static final TimeLineConst CREATED = new TimeLineConst(1);
	public static final TimeLineConst META = new TimeLineConst(2);
	
	private int type;

	public TimeLineConst(int type) {
		super();
		this.type = type;
	}	
}
