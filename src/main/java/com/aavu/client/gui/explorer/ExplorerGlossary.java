package com.aavu.client.gui.explorer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aavu.client.domain.Tag;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.service.Manager;

public class ExplorerGlossary extends FTICachingExplorerPanel {
	
	private Glossary myGlossary;

	public ExplorerGlossary(Manager manager, Map defaultMap, Orientation orient) {
		super(manager, defaultMap);
		
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

}
