package com.aavu.client.gui.explorer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwm.client.GInternalFrame;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.AddButton;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TagChooser extends Composite {

	
	private VerticalPanel mainP;
	private Manager manager;
	private VerticalPanel tagP;
	private Explorer explorer;

	public TagChooser(final Map defaultMap, Manager manager, Explorer explorer){
		this.explorer = explorer;
		this.manager = manager;
		
		mainP = new VerticalPanel();
		
		mainP.add(new HeaderLabel(ConstHolder.myConstants.chooser_showing()));
		
		AddButton addEditButton = new AddButton(ConstHolder.myConstants.chooser_add());
		
		addEditButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				addEditClick(defaultMap.keySet());				
			}});		
		
		mainP.add(addEditButton);
		
		
		tagP = new VerticalPanel();
		showTags(defaultMap.keySet());
	
		mainP.add(tagP);
		
	
		
		initWidget(mainP);
	}


	//Set<TagStat>
	public void showTags(Set tags) {
		tagP.clear();
		if(tags.isEmpty()){
			tagP.add(new Label(ConstHolder.myConstants.chooser_all()));
		}else{
			for (Iterator iter = tags.iterator(); iter.hasNext();) {
				TopicIdentifier tag = (TopicIdentifier) iter.next();			
				tagP.add(new TopicLink(tag));
			}
		}
	}
	
	private void loadAll() {
		showTags(new HashSet());
		explorer.loadAll();
	}
	
	
	//Set<TagStat>
	public void loadTags(Set tags) {
		showTags(tags);
		explorer.load(tags);
	}
	
	protected void addEditClick(Set tags) {
		TagSelectPopup tp = new TagSelectPopup(manager.newFrame(),tags,this);		
	}
	
	private class TagSelectPopup extends PopupWindow implements ClickListener {

		private static final int HEIGHT = 400;
		private static final int WIDTH = 500;
		protected static final int NUM_COLS = 5;
		
		private VerticalPanel tagSelectP;
		private List pickers = new ArrayList();
		private TagChooser chooser;

		public TagSelectPopup(GInternalFrame frame, final Set tags, final TagChooser chooser) {
			super(frame, ConstHolder.myConstants.chooser_tagSelectTitle(), WIDTH, HEIGHT);
			this.chooser = chooser;
			
			manager.getTagCache().getTagStats(new StdAsyncCallback(ConstHolder.myConstants.chooser_lookup_async()){				
				public void onSuccess(Object result) {
					super.onSuccess(result);
					
					TagStat[] stats = (TagStat[]) result;
					
					System.out.println("stats "+stats.length+" "+((int)Math.ceil((double)stats.length / NUM_COLS))+" "+NUM_COLS);
					
					Grid grid = new Grid((int)Math.ceil((double)stats.length / NUM_COLS), NUM_COLS);
										
					int row = 0;
					int col = 0;
					for (int i = 0; i < stats.length; i++) {
						TagStat stat = stats[i];
						
						Picker p = new Picker(stat);						
						for (Iterator iter = tags.iterator(); iter.hasNext();) {
							TopicIdentifier t = (TopicIdentifier) iter.next();
							if(t.getTopicID() == stat.getTagId()){
								p.setChecked(true);
							}
						}
						
						grid.setWidget(row, col, p);
						pickers.add(p);
						
						col++;
						
						if(col == NUM_COLS){
							row++;
							col=0;
						}
						
					}					
					tagSelectP.insert(grid, 0);
					
				}});
			tagSelectP = new VerticalPanel();
			
		
						
			ScrollPanel sp = new ScrollPanel(tagSelectP);
			sp.setHeight(HEIGHT-40+"px");
			
			
			
			Button selectB = new Button(ConstHolder.myConstants.chooser_lookup_select());
			selectB.addClickListener(this);
			
			Button showAllB = new Button(ConstHolder.myConstants.chooser_show_all());
			showAllB.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					TagSelectPopup.this.chooser.loadAll();
					close();
				}});						
			
			HorizontalPanel bottomP = new HorizontalPanel();
			bottomP.add(selectB);
			bottomP.add(showAllB);
			
			
			VerticalPanel mainP = new VerticalPanel();
			mainP.add(sp);
			mainP.add(bottomP);
			
			setContent(mainP);			
		}

		public void onClick(Widget sender) {
			Set tags = new HashSet();
			for (Iterator iter = pickers.iterator(); iter.hasNext();) {
				Picker p = (Picker) iter.next();
				if(p.isChecked()){
					System.out.println("adding checked ");
					tags.add(p.getIdentifier());
				}				
			}
			chooser.loadTags(tags);
			close();
		}
		
	}
	
	private class Picker extends Label implements ClickListener {

		private static final String CHECKED_STYLE = "H-TagPicker-Checked";
		private static final String HOVER_STYLE = "H-TagPicker-Hover";
		
		public static final double MAX = 3;
		public static final double TOPIC_SKY = 20;
		
		private TagStat stat;
		private boolean checked;

		public Picker(final TagStat stat) {
			super(stat.getTagName()+"("+stat.getNumberOfTopics()+")");
			this.stat = stat;		
			addStyleName("H-TagPicker");
			
			//not doing anything. why?
//			double fontSize = MAX * stat.getNumberOfTopics() / TOPIC_SKY;
//			fontSize = fontSize < 1 ? 1 : fontSize;
//			System.out.println("stat "+stat.getNumberOfTopics()+" "+fontSize);
//			DOM.setStyleAttribute(getElement(), "font-size", fontSize+"em");
			
			addClickListener(this);
			addMouseListener(new MouseListenerAdapter(){
				//@Override
				public void onMouseEnter(Widget sender) {
					addStyleName(HOVER_STYLE);
				}
				//@Override
				public void onMouseLeave(Widget sender) {
					removeStyleName(HOVER_STYLE);	
				}});
		}

		
		public void setChecked(boolean b) {
			checked = b;
			if(isChecked()){
				addStyleName(CHECKED_STYLE);
			}else{				
				removeStyleName(CHECKED_STYLE);				
			}
		}

		public boolean isChecked() {
			return checked;
		}

		public TopicIdentifier getIdentifier() {
			return stat.getTopicIdentifier();
		}


		public void onClick(Widget sender) {
			setChecked(!isChecked());			
		}
		
	}

}
