package com.aavu.client.widget.tags;

import com.aavu.client.domain.Meta;
import com.aavu.client.service.local.TagLocalService;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class MetaChooser extends Composite {

		
	private TextBox metaName;
	private MetaListBox metaType;
	
	public MetaChooser(TagLocalService tagLocalService){
		
		HorizontalPanel mainP = new HorizontalPanel();
		
		metaName = new TextBox();
		metaName.setText("");

		metaType = new MetaListBox();
		
		metaType.addItems(tagLocalService.getAllMetaTypes());

		mainP.add(metaName);
		mainP.add(metaType);
		
		setWidget(mainP);		
	}

	public void setMeta(Meta element) {
		metaName.setText(element.getName());
		metaType.setMetaType(element.getType());
	}

	public Meta getMeta() {
		Meta meta = metaType.getSelectedMeta();
		meta.setName(metaName.getText());
		return meta;
	}
	
	
	
}
