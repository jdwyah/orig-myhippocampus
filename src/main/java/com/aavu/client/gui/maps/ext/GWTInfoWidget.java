package com.aavu.client.gui.maps.ext;

import java.util.Iterator;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * Wrapper for a HasClickListeners widget.
 * 
 * 
 * @author Jeff Dwyer
 *
 */
public class GWTInfoWidget extends Widget {

	public GWTInfoWidget(HasClickListeners listenerWidget)   {	                
		setElement(listenerWidget.getMainWidget().getElement());

		for (Iterator iter = listenerWidget.getPairs().iterator(); iter.hasNext();) {

			ListenerWidget element = (ListenerWidget) iter.next();
			setClickListener(element);						
		}

	}

	public void	setClickListener(ListenerWidget lw)  {
		setOnClickListener(this, lw.getWidget().getElement(), lw.getListener());
	}

	private native void setOnClickListener(Widget sender, Element a,
			ClickListener manager)/*-{
    			a.onclick = function() {
						manager.@com.google.gwt.user.client.ui.ClickListener::onClick(Lcom/google/gwt/user/client/ui/Widget;)(sender);
                };
        }-*/;
} 