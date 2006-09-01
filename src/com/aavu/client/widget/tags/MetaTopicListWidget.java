package com.aavu.client.widget.tags;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.MetaTopicList;
import com.aavu.client.domain.Topic;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * must use it's save listener if you want it to update the map
 * 
 * @author Jeff Dwyer
 *
 */
public class MetaTopicListWidget extends SaveListener {
	private static final String ADD_IMAGE = "images/plus-sign.jpg";
	private static final String MINUS_IMAGE = "images/minus-sign.jpg";

	private VerticalPanel listP;
	private MetaTopicList metaTopicList;

	/**
	 * This widget has a topic autocomplete for each thing
	 * @param list 
	 * @param mmp 
	 *
	 */
	public MetaTopicListWidget(MetaTopicList list,boolean isEdit){

		this.metaTopicList = list;
		
		HorizontalPanel widget = new HorizontalPanel();
		
		listP = new VerticalPanel();

		load(list,isEdit);

		widget.add(listP);

		initWidget(widget);
	}


	/**
	 * load the list of seealso topics that the topic list generates for us.
	 * It does the hard work of doing the metaValue -> topic lookup
	 * 
	 * @param list
	 * @param isEdit 
	 */
	private void load(MetaTopicList list, boolean isEdit) {
		
		List l = list.getVals();
		
		System.out.println("load: "+l);
		System.out.println("size "+l.size());
		
		if(l.isEmpty()){
			if(isEdit){
				listP.add(new CompleteRow(true));
			}
			else{
				listP.add(new Label("None"));
			}
			return;
		}
		
		boolean firstRow = true;
		
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Topic s = (Topic) iter.next();
			
			
			if(isEdit){
				CompleteRow cr = new CompleteRow(firstRow);
				cr.setText(s.getTitle());
				listP.add(cr);
			}
			else{
				listP.add(new Hyperlink(s.getTitle(),""));
			}
			
			firstRow = false;	
		}
		
	}


	/**
	 * return all of the names
	 * @return
	 */
	public List getTopicNames(){		
		List rtn = new ArrayList();		
		for(int i=0; i < listP.getWidgetCount(); i++){
			CompleteRow c = (CompleteRow)listP.getWidget(i);			
			rtn.add(c.getTopicName());
		}		
		return rtn;		
	}
		
	
	/**
	 * One row in the list
	 * 
	 * @author Jeff Dwyer
	 *
	 */
	private class CompleteRow extends Composite{
		private TopicCompleter completer; 
		public CompleteRow(boolean isFirst){

			HorizontalPanel completeRow = new HorizontalPanel();

			completer = new TopicCompleter();

			Image addAnother = new Image(ADD_IMAGE);		
			addAnother.addClickListener(new ClickListener(){

				public void onClick(Widget sender) {
					listP.add(new CompleteRow(false));
				}});
			Image remove = new Image(MINUS_IMAGE);		
			remove.addClickListener(new ClickListener(){

				public void onClick(Widget sender) {
					listP.remove(CompleteRow.this);
				}});


			completeRow.add(new Label(metaTopicList.getName()));
			completeRow.add(completer);
			completeRow.add(addAnother);

			//don't show remove '-' for first one
			if(!isFirst){
				completeRow.add(remove);
			}

			initWidget(completeRow);
		}
	
		public void setText(String text){
			completer.setText(text);
		}
		
		public String getTopicName(){
			return completer.getText();
		}
	}

	public void saveNowEvent() {
		System.out.println("save now!");		
		metaTopicList.setVals(getTopicNames());	
	}

}
