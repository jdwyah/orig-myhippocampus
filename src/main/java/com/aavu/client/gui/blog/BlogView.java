package com.aavu.client.gui.blog;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.gui.TopicPreviewLink;
import com.aavu.client.gui.explorer.ExplorerPanel;
import com.aavu.client.gui.ext.MultiDivPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * A way to see things that have been recently updated
 * 
 * @author Jeff Dwyer
 *
 */
public class BlogView extends Composite implements ExplorerPanel  {
	
	/**
	 * NOTE the - we want them in reverse chronological order.
	 */
	private static final Comparator dateComparator = new Comparator(){
		public int compare(Object o1, Object o2) {
			DatedTopicIdentifier o = (DatedTopicIdentifier)o1;
			DatedTopicIdentifier oo = (DatedTopicIdentifier) o2;			
			return -o.getLastUpdated().compareTo(oo.getLastUpdated());
		}};

	private static final DateTimeFormat df = DateTimeFormat.getFormat("EE, MMM dd");
	
	private static final int MAX_PER_PAGE = 10;


	private static final String DATE_STYLE = "H-Blog-Date";
	private MultiDivPanel tagPanel = new MultiDivPanel();
	private HorizontalPanel mainPanel = new HorizontalPanel();
	
	
	private ScrollPanel previewPanel = new ScrollPanel();


	private GWTSortedMap sortedByDate = new GWTSortedMap(dateComparator);

	private Navigator pageNavigation;


	private int loadAllStart = 0;

	private boolean allMode;

	private Manager manager;
	
	public BlogView(Manager manager, int width,int height) {
		this.manager = manager;
		
		ScrollPanel scroll = new ScrollPanel(tagPanel);		
		scroll.setHeight(height-100+"px");
		
		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.add(scroll);
		
		pageNavigation = new Navigator(0);
		
		leftPanel.add(pageNavigation);
		mainPanel.add(leftPanel);
		
		previewPanel.setWidth(width-200+"px");
		previewPanel.setHeight(height-100+"px");
		
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
		
		pageNavigation.setNoNumbers(false);
		
		prepForDisplay(ftis);
		display(0);
	}
	
	private void prepForDisplay(List ftis){
		sortedByDate.clear();
		
		Date now = new Date();		
		for (Iterator iterator = ftis.iterator(); iterator.hasNext();) {
			sortedByDate.put((DatedTopicIdentifier) iterator.next(),null);
		}
		Date fin = new Date();
		System.out.println("BlogView sort from "+fin.getTime()+" to " + now.getTime());
		System.out.println("BlogView sort took "+(fin.getTime() - now.getTime()));		
	}
	

	public void loadAll() {
		allMode = true;
		
		loadAllStart  = 0;
		
		pageNavigation.setNoNumbers(true);
		
		doLoadAll();
	}
	public void load(List tags){
		
	}
	private void doLoadAll(){
		manager.getTopicCache().getAllTopicIdentifiers(loadAllStart, MAX_PER_PAGE, new StdAsyncCallback("Fetch Blog"){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				
				List ftis = (List) result;				
				System.out.println("LOAD all return ");
				
				//NOTE! sorting is redundant...
				prepForDisplay(ftis);
				
				display(loadAllStart);
			}			
		});		
	}
	
	private void display(int start){
		//System.out.println("Display "+start+" "+loadAllStart+" "+allMode);
		
		if(allMode){				
			if(start != loadAllStart){

				loadAllStart = start;
				doLoadAll();
			}else{
				draw(0);
			}
		
		}else{
			draw(start);

		}
		pageNavigation.setStart(start,sortedByDate.size());

	}
	
	/**
	 * Requires the sortedByDate map to have been created. It will poke the NavigationTool to 
	 * upate the previous, next buttons.
	 * 
	 * @param start
	 */
	private void draw(int start) {
				
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
			
			
			
			MultiDivPanel hp = new MultiDivPanel();			
			hp.addStyleName("H-BlogView");
			
			TopicPreviewLink previewLink = new TopicPreviewLink(fti,100,null,previewPanel,manager);
			//previewLink.addStyleName("H-BlogLink");
			
			Label dateLabel = new Label(df.format(fti.getLastUpdated()),false);
			dateLabel.addStyleName(DATE_STYLE);
					
			hp.add(dateLabel);
			hp.add(previewLink);
					
			if(count % 2 == 0){
				hp.addStyleName("H-BlogView");			
			}else{
				hp.addStyleName("H-BlogViewOdd");				
			}
			
			tagPanel.add(hp);
			
		}
		
	}
	
	private class Navigator extends Composite implements ClickListener {
		private Label previous;
		private Label next;
		private int start;
		private Label showing;

		
		public Navigator(int count){
			
			HorizontalPanel mainP = new HorizontalPanel();
			
			next = new Label(ConstHolder.myConstants.blog_next());
			next.addClickListener(this);
			previous = new Label(ConstHolder.myConstants.blog_previous());
			previous.addClickListener(this);
			
			showing = new Label("");
			showing.setWidth("90px");
			
			mainP.add(previous);
			mainP.add(showing);
			mainP.add(next);
			
			initWidget(mainP);
			previous.addStyleName("H-Blog-NextPage");
			next.addStyleName("H-Blog-NextPage");
		}



		public void setNoNumbers(boolean noNumbers) {
			showing.setVisible(!noNumbers);
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
				showing.setText(start +" - " + (start+MAX_PER_PAGE)+" of "+max);
			}else{
				next.setVisible(false);
				showing.setText(start +" - " + max+" of "+max);
			}
			

			//hackish if we're in allMode, show next no matter what
			if(!showing.isVisible()){
				next.setVisible(true);
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
