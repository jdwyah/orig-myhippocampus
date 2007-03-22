package com.aavu.client.gui.explorer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwm.client.GInternalFrame;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.AddButton;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
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
		
		
		tagP = new VerticalPanel();
		showTags(defaultMap.keySet());
	
		mainP.add(tagP);
		
		AddButton addEditButton = new AddButton(ConstHolder.myConstants.chooser_add());
		
		addEditButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				addEditClick(defaultMap.keySet());				
			}});		
		
		mainP.add(addEditButton);
		
		Button showAllB = new Button(ConstHolder.myConstants.chooser_show_all());
		showAllB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				loadAll();
			}			});
		
		mainP.add(showAllB);
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
		explorer.setAllMode(true);
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
		
		private VerticalPanel mainP;
		private List pickers = new ArrayList();
		private TagChooser chooser;

		public TagSelectPopup(GInternalFrame frame, final Set tags, TagChooser chooser) {
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
					mainP.insert(grid, 0);
					
				}});
			mainP = new VerticalPanel();
			
			Button selectB = new Button(ConstHolder.myConstants.chooser_lookup_select());
			selectB.addClickListener(this);
			mainP.add(selectB);
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
	
	private class Picker extends CheckBox {

		private TagStat stat;

		public Picker(TagStat stat) {
			super(stat.getTagName());
			this.stat = stat;		
		}

		public TopicIdentifier getIdentifier() {
			return stat.getTopicIdentifier();
		}
		
	}

}
