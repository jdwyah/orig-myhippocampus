package com.aavu.client.gui.explorer;

import java.util.List;

import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ExplorerGlossary extends Composite implements ExplorerPanel {
	
	private Glossary myGlossary;
	private Manager manager;

	public ExplorerGlossary(Manager manager, Orientation orient) {
		this.manager = manager;
		myGlossary = new Glossary(manager,orient);
		initWidget(myGlossary);
	}

	

	//@Override
	protected void draw(List ftis) {
		System.out.println("explorer glossary load "+ftis.size());
		myGlossary.load(ftis);
	}



	public void loadAll() {
		myGlossary.doAdHoc();
	}



	public void load(List topics) {
		// TODO Auto-generated method stub
		
	}
	public Widget getWidget(){
		return this;
	}

}
