package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Map;

import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;



/**
 * Meta generated by hbm2java
 */

public abstract class Meta extends Topic implements IsSerializable,Serializable {


	public abstract Widget getEditorWidget(Topic top, Manager manager); 
	
	public abstract String getType();

	//@Override
	public void accept(TopicVisitor visitor) {
		visitor.visit(this);
	}

	public String getName(){
		return getTitle();
	}

}
