package com.aavu.client.widget.tags;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MetaTopicListWidget extends Composite {
	private static final String ADD_IMAGE = "images/plus-sign.jpg";
	private static final String MINUS_IMAGE = "images/minus-sign.jpg";

	private Label label;

	private VerticalPanel listP;

	/**
	 * This widget has a topic autocomplete for each thing
	 *
	 */
	public MetaTopicListWidget(){
		HorizontalPanel widget = new HorizontalPanel();
		label = new Label();

		listP = new VerticalPanel();



		listP.add(new CompleteRow(true));

		widget.add(listP);

		setWidget(widget);
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
	
	public void setName(String name) {
		label.setText(name);
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


			completeRow.add(label);
			completeRow.add(completer);
			completeRow.add(addAnother);

			//don't show remove '-' for first one
			if(!isFirst){
				completeRow.add(remove);
			}

			setWidget(completeRow);
		}
	
		public String getTopicName(){
			return completer.getText();
		}
	}

}
