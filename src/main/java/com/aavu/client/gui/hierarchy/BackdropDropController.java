package com.aavu.client.gui.hierarchy;

import com.allen_sauer.gwt.dragdrop.client.DragController;
import com.allen_sauer.gwt.dragdrop.client.DragEndEvent;
import com.allen_sauer.gwt.dragdrop.client.drop.AbsolutePositionDropController;
import com.google.gwt.user.client.ui.Widget;

public class BackdropDropController extends AbsolutePositionDropController {

	private HierarchyDisplay panel;

	public BackdropDropController(HierarchyDisplay panel) {
		super(panel);
		this.panel = panel;
	}

	// @Override
	public DragEndEvent onDrop(Widget reference, Widget draggable, DragController dragController) {
		DragEndEvent event = super.onDrop(reference, draggable, dragController);

		panel.dragFinished(draggable);

		System.out.println("BackdropControler on drop");

		return event;
	}

	// // @Override
	// public void drop(Widget widget, int left, int top) {
	// super.drop(widget, left, top);
	// System.out.println("drop");
	// }
	//
	// // @Override
	// public void onMove(Widget reference, Widget draggable, DragController dragController) {
	// super.onMove(reference, draggable, dragController);
	// System.out.println("on move");
	// }
	//
	// // @Override
	// public void onPreviewDrop(Widget reference, Widget draggable, DragController dragController)
	// throws VetoDropException {
	// super.onPreviewDrop(reference, draggable, dragController);
	// System.out.println("ON PREVIEW DROP");
	// }


}
