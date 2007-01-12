package com.aavu.client.widget.tags;

import com.aavu.client.domain.Meta;
import com.aavu.client.service.local.TagLocalService;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class MetaChooser extends Composite {

		
	private TextBox metaName;
	private MetaListBox metaType;
	private Meta metaElement;
	
	public MetaChooser(TagLocalService tagLocalService){
		
		HorizontalPanel mainP = new HorizontalPanel();
		
		metaName = new TextBox();
		metaName.setText("");

		metaType = new MetaListBox();
		
		metaType.addItems(tagLocalService.getAllMetaTypes());

		mainP.add(metaName);
		mainP.add(metaType);
		
		initWidget(mainP);		
	}

	public void setMeta(Meta element) {
		this.metaElement = element;
		metaName.setText(element.getName());
		metaType.setMetaType(element.getType());
	}

	/**
	 * onSave() Get the type. If it's the same, return our old, saved meta but
	 * with the possibly new title.
	 *  
	 * TODO This may orphan created metas that then have their types changed, since in 
	 * that case the other metaElement will be orphaned.
	 * 
	 * @return
	 */
	public Meta getMeta() {
		
		Meta newMeta = metaType.getSelectedMeta();
		
		if(metaElement == null || metaElement.getType() != newMeta.getType()){
			metaElement = newMeta;
		}
		
		metaElement.setTitle(metaName.getText());
		return metaElement;
	}
	
	
	
}
