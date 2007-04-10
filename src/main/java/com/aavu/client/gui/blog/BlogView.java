package com.aavu.client.gui.blog;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.gui.TopicPreviewLink;
import com.aavu.client.gui.explorer.FTICachingExplorerPanel;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * A way to see things that have been recently updated
 * 
 * @author Jeff Dwyer
 *
 */
public class BlogView extends FTICachingExplorerPanel {
	
	/**
	 * NOTE the - we want them in reverse chronological order.
	 */
	private static final Comparator dateComparator = new Comparator(){
		public int compare(Object o1, Object o2) {
			DatedTopicIdentifier o = (DatedTopicIdentifier)o1;
			DatedTopicIdentifier oo = (DatedTopicIdentifier) o2;			
			return -o.getCreated().compareTo(oo.getCreated());
		}};

	
	private static final SimpleDateFormat df = new SimpleDateFormat("EE, MMM dd");


	private static final int MAX_PER_PAGE = 14;


	private static final String DATE_STYLE = "H-Blog-Date";
	private VerticalPanel tagPanel = new VerticalPanel();
	private HorizontalPanel mainPanel = new HorizontalPanel();
	
	
	private SimplePanel previewPanel = new SimplePanel();
	private Manager manager;


	private GWTSortedMap sortedByDate = new GWTSortedMap(dateComparator);

	private NextPage pageNavigation;
	
	public BlogView(Manager manager, Map defaultMap, int height) {
		super(manager,defaultMap);	
		this.manager = manager;
	
		
		ScrollPanel scroll = new ScrollPanel(tagPanel);		
		scroll.setHeight(height-100+"px");
		
		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.add(scroll);
		
		pageNavigation = new NextPage(0);
		
		leftPanel.add(pageNavigation);
		mainPanel.add(leftPanel);
		mainPanel.add(previewPanel);
		System.out.println("INITTIING");
		initWidget(mainPanel);
		
	}

	public Widget getWidget() {
		return this;
	}

	/**
	 * on draw, setup the sorted list, then display starting at 0.
	 */
	public void draw(List ftis) {
		sortedByDate.clear();
		
		Date now = new Date();		
		for (Iterator iterator = ftis.iterator(); iterator.hasNext();) {
			sortedByDate.put((DatedTopicIdentifier) iterator.next(),null);
		}
		Date fin = new Date();
		System.out.println("BlogView sort from "+fin.getTime()+" to " + now.getTime());
		System.out.println("BlogView sort took "+(fin.getTime() - now.getTime()));
		display(0);
	}
	
	/**
	 * Requires the sortedByDate map to have been created. It will poke the NavigationTool to 
	 * upate the previous, next buttons.
	 * 
	 * @param start
	 */
	private void display(int start) {
	
		tagPanel.clear();
		
		int count = 0;
		for (Iterator iterator = sortedByDate.getKeyList().iterator(); iterator.hasNext();) {

			DatedTopicIdentifier fti = (DatedTopicIdentifier) iterator.next();		
			count++;
			
	
			if(count < start){
				continue;
			}
			if(count > start + MAX_PER_PAGE){
				break;
			}
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);
			
			TopicPreviewLink previewLink = new TopicPreviewLink(fti,100,null,previewPanel,manager);
			
			Label dateLabel = new Label(df.format(fti.getCreated()),false);
			dateLabel.addStyleName(DATE_STYLE);
			hp.add(dateLabel);
			hp.add(previewLink);
			
		
			tagPanel.add(hp);
			
		}
			
		pageNavigation.setStart(start,sortedByDate.size());

	}
	
	private class NextPage extends Composite implements ClickListener {
		private Label previous;
		private Label next;
		private int start;
		private Label showing;


		public NextPage(int count){
			
			HorizontalPanel mainP = new HorizontalPanel();
			
			next = new Label("Next Page");
			next.addClickListener(this);
			previous = new Label("Previous Page");
			previous.addClickListener(this);
			
			showing = new Label("");
			
			mainP.add(previous);
			mainP.add(showing);
			mainP.add(next);
			
			initWidget(mainP);
			previous.addStyleName("H-Blog-NextPage");
			next.addStyleName("H-Blog-NextPage");
		}

		
		public void setStart(int start,int max) {
			this.start = start;
			if(start == 0){
				previous.setVisible(false);
			}else{
				previous.setVisible(true);
			}
			if(start + MAX_PER_PAGE < max){
				next.setVisible(true);
				showing.setText(start +" - " + (start+MAX_PER_PAGE));
			}else{
				next.setVisible(false);
				showing.setText(start +" - " + max);
			}
			
		}


		public void onClick(Widget sender) {
			if(next == sender){
				display(start+MAX_PER_PAGE);
			}else if(previous == sender){
				display(start-MAX_PER_PAGE);
			}
		}
	}

	
	
}
