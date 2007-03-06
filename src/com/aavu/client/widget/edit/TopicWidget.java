package com.aavu.client.widget.edit;


import org.gwtwidgets.client.util.WindowUtils;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicWidget extends FocusPanel implements ClickListener {
	
	protected static final String HOVER_STYLE = "H-editableText-hover";

	protected FlowPanel textPanel = new FlowPanel();

	private VerticalPanel panel = new VerticalPanel();

	public Topic topic;

	//protected String data;

//	private Timer removeHighlight;


	
	/**
	 * NOTE: this class is responsible for noticing possible clicks on links,
	 * then tickling the History object, since the <A> won't do this for us
	 * and we'll never getHistoryChanged events.
	 * @param manager 
	 * 
	 * 
	 * @param testTopic
	 */
	public TopicWidget(){
			
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		
		add(panel);
		addClickListener(this);
				
		addStyleName("H-editableText");
		
		/*
		 * This is essentially Effect.highlight, but more reliable.
		 * we were getting stuck on various shades of yellow
		 */
//		removeHighlight = new Timer(){
//			public void run() {
//				removeStyleName("editableLabel-label-hover");		
//			}};
		addMouseListener(new MouseListenerAdapter(){
			public void onMouseEnter(Widget sender) {
				addStyleName(HOVER_STYLE);
				
				//removeHighlight.schedule(500);
			}
			public void onMouseLeave(Widget sender) {
				removeStyleName(HOVER_STYLE);
			}	
			
		});
	}	

	public void load(Topic topic){
		this.topic = topic;

		setup();
		
	}

	public void setup(){

		Entry entry = topic.getLatestEntry();
		
		setText(entry);
		
		panel.clear();

		panel.add(textPanel);
		
		/*
		 * shouldn't need the else, but if they enter just a <BR> then there's 
		 * no place to click to edit.
		 */
		System.out.println("ENTRY is empty = "+entry.isEmpty());
		if(entry.isEmpty()){
			panel.add(new Label(ConstHolder.myConstants.topic_blank()));
		}else{
			panel.add(new Label(ConstHolder.myConstants.topic_edit()));
		}

		
	}

	public void setText(Entry entry){		
		System.out.println("SUPER SET");
		textPanel.clear();		
		textPanel.add(new TextDisplay(entry.getData()));		
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
