package com.aavu.client.domain;

import org.gwtwidgets.client.ui.EditableLabel;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.widget.edit.MetaTopicEditWidget;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaText extends Meta {

	//NOTE: BAD leads to wacky NoClassDefFoundError
	//private static final String TYPE = Manager.myConstants.meta_text();
	private static final String TYPE = "Text";
	private transient Topic topic;
	private transient HippoText mv;
	private transient EditableLabelExtension editable;
	private transient TopicCache topicCache;
	
	public MetaText(){
		this("");
	}

	public MetaText(String value){
		setTitle(value);
	}


	public Widget getEditorWidget(final Topic topic, final SaveNeededListener saveNeeded,Manager manager) {
		this.topic = topic;
		this.topicCache = manager.getTopicCache();
		
		HorizontalPanel hp = new HorizontalPanel();
		
		mv = (HippoText) topic.getSingleMetaValueFor(this);
		if(mv == null || mv.isEmpty()){
			mv = new HippoText(Manager.myConstants.editMe());
		}
				
		editable = new EditableLabelExtension(mv.getValue(),new ChangeListener(){
			
			/**
			 * onChange, set the value of MV, save it, then update the Topic.
			 * Set "Save Needed" although it's actually only needed the first time round.
			 */
			public void onChange(final Widget sender) {
				mv.setValue(editable.getText());			
				topicCache.save(mv, new StdAsyncCallback(Manager.myConstants.meta_text_async_save()){
					public void onSuccess(Object result) {
						super.onSuccess(result);	
						Topic[] res = (Topic[]) result;
						mv = (HippoText) res[0];										
						topic.addMetaValue(MetaText.this, mv);
						saveNeeded.onChange(sender);
					}					
				});							
			}			
		});
		
		hp.add(new Label(getTitle()));
		hp.add(editable);
		
		return hp;
	}	
	
	
	//@Override
	public String getType() {		
		return TYPE;
	}

	protected Object getValue() {
		return getTitle();
	}

	//@Override
	public boolean equals(Object other) {
		{
			if ( (this == other ) ) return true;
			if ( (other == null ) ) return false;
			if ( !(other instanceof MetaText) ) return false;
			MetaText castOther = ( MetaText ) other; 

			return ( (this.getTitle()==castOther.getTitle()) || ( this.getTitle()!=null && castOther.getTitle()!=null && this.getTitle().equals(castOther.getTitle()) ) );
		}
	}


}
