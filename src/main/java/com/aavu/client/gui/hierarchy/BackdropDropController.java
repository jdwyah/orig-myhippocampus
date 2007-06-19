package com.aavu.client.gui.hierarchy;

import com.allen_sauer.gwt.dragdrop.client.DragController;
import com.allen_sauer.gwt.dragdrop.client.DragEndEvent;
import com.allen_sauer.gwt.dragdrop.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dragdrop.client.drop.VetoDropException;
import com.allen_sauer.gwt.dragdrop.client.util.Location;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

public class BackdropDropController extends AbsolutePositionDropController {

	private HierarchyDisplay panel;

	public BackdropDropController(HierarchyDisplay panel) {
		super(panel);
		this.panel = panel;
	}

	public DragEndEvent onDrop(Widget reference, Widget draggable, DragController dragController) {
		DragEndEvent event = super.onDrop(reference, draggable, dragController);

		panel.dragFinished(draggable);

		System.out.println("on drop");
		
		return event;
	}


}
