package com.aavu.client.widget.datepickertimeline;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.dhtmlIslands.RemembersPosition;
import com.aavu.client.gui.ext.JSUtil;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.Widget;



public class DraggableTimeLineObj extends FocusPanel implements RemembersPosition, SourcesMouseWheelEvents, SourcesMouseEvents  {

	private DateTimeFormat format = DateTimeFormat.getFormat("MMM, d yyyy");
	

	private int left;
	private TimeLineObj tlo;
	private int top;
	private Label label;
	private Image image;


	private MouseListenerCollection listeners;


	//private FocusPanel panel;

	public DraggableTimeLineObj(final Manager manager, final TimeLineObj tlo,int left, int top){
		super();
		this.tlo = tlo;
		this.left = left;
		this.top = top;

//		panel = new FocusPanel();

		label = new Label(tlo.getTopic().getTopicTitle());

		image = ConstHolder.images.bullet_blue().createImage();
		
		
//		panel.add(image);
	//	panel.add(label);
		

//		label.addClickListener(new ClickListener(){
//
//			public void onClick(final Widget sender) {
//				manager.getTopicCache().getTopic(tlo.getTopic(), new StdAsyncCallback("Fetch Preview"){
//
//					//@Override
//					public void onSuccess(Object result) {							
//						super.onSuccess(result);
//
//						PreviewPopup p = new PreviewPopup(manager,(Topic) result,0,0);									
//					}});					
//			}});


		label.addMouseListener(new TooltipListener(format.format(tlo.getStart())));

		
		
		setWidget(image);

		//setPixelSize(40, 40);
		
		JSUtil.disableSelect(getElement());
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public Widget getWidget() {		
		return this;
	}

	public void addMouseWheelListener(MouseWheelListener listener) {
		label.addMouseWheelListener(listener);
		image.addMouseWheelListener(listener);
	}

	public void removeMouseWheelListener(MouseWheelListener listener) {
		image.removeMouseWheelListener(listener);
		label.removeMouseWheelListener(listener);
	}

	public void setTop(int top) {
		this.top = top;
	}


}

