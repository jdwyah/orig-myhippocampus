package com.aavu.client.gui.timeline.draggable;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.dhtmlIslands.RemembersPosition;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.gui.glossary.SimpleTopicDisplay;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.Widget;


public class TLOWrapper extends Composite implements RemembersPosition, SourcesMouseWheelEvents {
	
	private DateTimeFormat format = DateTimeFormat.getFormat("MMM, d yyyy");
	private class PreviewPopup extends PopupWindow {

		private static final int HEIGHT = 200;
		private static final int WIDTH = 400;

		public PreviewPopup(final Manager manager, Topic topic,int top,int left) {
			super(manager.newFrame(),topic.getTitle(),WIDTH,HEIGHT);
			SimpleTopicDisplay display = new SimpleTopicDisplay(topic,manager,this,WIDTH,HEIGHT);		
			frame.setLocation(top, left);
			setContent(display);
		}		
	}
	
	private int left;
	private TimeLineObj tlo;
	private int top;
	private Label label;
	private Image image;	

	public TLOWrapper(final Manager manager, final TimeLineObj tlo,int left, int top){
		this.tlo = tlo;
		this.left = left;
		this.top = top;

		HorizontalPanel panel = new HorizontalPanel();

		label = new Label(tlo.getTopic().getTopicTitle());

		image = ConstHolder.images.bullet_blue().createImage();
		panel.add(image);
		panel.add(label);

		label.addClickListener(new ClickListener(){

			public void onClick(final Widget sender) {
				manager.getTopicCache().getTopic(tlo.getTopic(), new StdAsyncCallback("Fetch Preview"){

					//@Override
					public void onSuccess(Object result) {							
						super.onSuccess(result);

						PreviewPopup p = new PreviewPopup(manager,(Topic) result,0,0);									
					}});					
			}});


		label.addMouseListener(new TooltipListener(format.format(tlo.getStart())));
		
		initWidget(panel);		
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
	
}
