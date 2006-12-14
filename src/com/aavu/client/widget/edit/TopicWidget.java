package com.aavu.client.widget.edit;


import org.gwtwidgets.client.util.WindowUtils;
import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicWidget extends FocusPanel implements ClickListener {

	public static Widget getSeeAlsoWidget(Topic topic2) {
		Association assoc = topic2.getSeeAlsoAssociation();		
		MetaSeeAlso see = (MetaSeeAlso) assoc.getFirstType();
		return see.getWidget(topic2);
	}
	
	private FlowPanel textPanel = new FlowPanel();

	private VerticalPanel panel = new VerticalPanel();

	public Topic topic;


	
	/**
	 * NOTE: this class is responsible for noticing possible clicks on links,
	 * then tickling the History object, since the <A> won't do this for us
	 * and we'll never getHistoryChanged events.
	 * @param manager 
	 * 
	 * 
	 * @param topic
	 */
	public TopicWidget(Manager manager, Topic topic){
			
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		panel.setWidth("100%");
		
		load(topic);

		add(panel);
		addClickListener(this);
	}	

	public void load(Topic topic){
		this.topic = topic;

		setup();

		Effect.highlight(panel);
	}

	public void setup(){

		Entry entry = topic.getLatestEntry();
		textPanel.clear();
		textPanel.add(new TextDisplay(entry.getData()));		

		panel.clear();
		if(entry.isEmpty()){
			panel.add(new Label(Manager.myConstants.topic_blank()));
		}
		panel.add(textPanel);		

	}




	/**
	 * Any click on this widget could be a click on a link,
	 * but we won't know right now. Wait until it has time to update
	 * the URL bar
	 */
	public void onClick(Widget sender) {

		String href = WindowUtils.getLocation().getHref();
		System.out.println("href_before: "+href);

		Timer t = new Timer(){
			public void run(){
				checkInASec();
			}
		};
		t.schedule(500);
	}

	/**
	 * Now that we've given the link time to write to the URL bar, 
	 * parse out the token and tickle the History manually.
	 */
	private void checkInASec() {		
		String href = WindowUtils.getLocation().getHref();
		System.out.println("href: "+href);
		int i = href.indexOf("#");
		if(i != -1){
			String token = href.substring(i+1);
			System.out.println("token "+token);
			History.newItem(token);
		}		
	}

	
}
