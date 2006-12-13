package com.aavu.client.widget.edit;


import java.util.Iterator;
import java.util.Set;

import org.gwtwidgets.client.util.WindowUtils;
import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.URI;
import com.aavu.client.service.Manager;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.util.SimpleDateFormatGWT;
import com.aavu.client.widget.ExternalLink;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.ReferencePanel;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicWidget extends FocusPanel implements ClickListener {

	private static SimpleDateFormatGWT df; 
	
	public static Widget getSeeAlsoWidget(Topic topic2) {
		Association assoc = topic2.getSeeAlsoAssociation();		
		MetaSeeAlso see = (MetaSeeAlso) assoc.getFirstType();
		return see.getWidget(topic2);
	}
	

	private FlowPanel topicTitlePanel = new FlowPanel();
	private Label titleLabel;

	private FlowPanel textPanel = new FlowPanel();

	private VerticalPanel panel = new VerticalPanel();

	public Topic topic;

	private ReferencePanel referencesPanel;
	private Manager manager;

	
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
		this.manager = manager;
		
		panel.setSpacing(4);	
		titleLabel = new Label(manager.myConstants.title());	

		topicTitlePanel.add(titleLabel);
	
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		panel.setWidth("100%");
		panel.add(topicTitlePanel);

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

		textPanel.clear();
		textPanel.add(new TextDisplay(topic.getLatestEntry().getData()));		

		topicTitlePanel.clear();
		topicTitlePanel.add(titleLabel);
		topicTitlePanel.add(new Label(topic.getTitle()));
		topicTitlePanel.add(new Label(manager.myConstants.topic_updated()+formatDate(topic.getLastUpdated())));


		panel.clear();
		panel.add(topicTitlePanel);

		panel.add(doSubject(topic));
		panel.add(doTags(topic));
		panel.add(getSeeAlsoWidget(topic));
		
		panel.add(new LinkDisplayWidget(topic));
		
		panel.add(textPanel);
		
		referencesPanel = new ReferencePanel(manager,topic);
		panel.add(referencesPanel);
		referencesPanel.load();

	}

	private Widget doSubject(Topic topic2) {
		HorizontalPanel horizP = new HorizontalPanel();

		horizP.add(new HeaderLabel(Manager.myConstants.subject()));
		if(topic2.getSubject() == null){
			horizP.add(new Label(Manager.myConstants.subject_none()));
		}else{
			horizP.add(new Label(topic2.getSubject().getName()));
		}
		return horizP;
	}
	

	private Widget doTags(Topic t){
		VerticalPanel panel = new VerticalPanel();	
		for (Iterator iter = topic.getTags().iterator(); iter.hasNext();) {
			Tag element = (Tag) iter.next();
			panel.add(new TopicLink(element));
			displayMetas(element,t,panel);
		}		
		return panel;
	}

	private void displayMetas(Tag tag,Topic top,VerticalPanel tagPanel) {
		System.out.println("TopicWidg display metas: "+tag.getTitle());
		System.out.println("metas: "+tag.getMetas().size());
		Set metas = tag.getMetas();
		for (Iterator iter = metas.iterator(); iter.hasNext();) {
			Meta element = (Meta) iter.next();
			tagPanel.add(element.getWidget(top));
		}
	}


	public String formatDate(java.util.Date date){		
		if(df == null){
			df = new SimpleDateFormatGWT("yyyy-MM-dd HH:mm:ss");
		}
		if(date != null){
			return df.format(date);
		}
		return null;
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
