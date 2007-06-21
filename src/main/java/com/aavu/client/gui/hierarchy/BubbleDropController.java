package com.aavu.client.gui.hierarchy;

import com.allen_sauer.gwt.dragdrop.client.DragController;
import com.allen_sauer.gwt.dragdrop.client.DragEndEvent;
import com.allen_sauer.gwt.dragdrop.client.drop.SimpleDropController;
import com.allen_sauer.gwt.dragdrop.client.drop.VetoDropException;
import com.google.gwt.user.client.ui.Widget;

public class BubbleDropController extends SimpleDropController {

	private static final String STYLE_ENGAGE = "dragdrop-engage";
	
	private TopicBubble bubble;

	public BubbleDropController(TopicBubble bubble) {
	    super(bubble);
	    this.bubble = bubble;
	  }

	  public DragEndEvent onDrop(Widget reference, Widget draggable, DragController dragController) {
	    DragEndEvent event = super.onDrop(reference, draggable, dragController);
	  
	    System.out.println("Bubble Drop Controller onDrop");
	    
	    //getDropTarget().
	    
	    //dragController.get
	    
	    //bin.eatWidget(draggable);
	    
	    bubble.receivedDrop(draggable);
	    
	    //currentDragController = null;
	    return makeDragEndEvent(reference, draggable, dragController);
	    
	    //return event;
	  }

	//@Override
	public Widget getDropTarget() {
		return bubble.getDropTarget();
	}

	public void onEnter(Widget reference, Widget draggable, DragController dragController) {
	    super.onEnter(reference, draggable, dragController);
	  
	  }

	  public void onLeave(Widget draggable, DragController dragController) {
	    super.onLeave(draggable, dragController);

	  }

	  public void onPreviewDrop(Widget reference, Widget draggable, DragController dragController) throws VetoDropException {
	    super.onPreviewDrop(reference, draggable, dragController);
	    
//	    if (!bin.isWidgetEater()) {
//	      throw new VetoDropException();
//	    }
	    
	  }
	}
