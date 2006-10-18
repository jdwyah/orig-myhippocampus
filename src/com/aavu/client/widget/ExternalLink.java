package com.aavu.client.widget;

import com.aavu.client.domain.Occurrence;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

public class ExternalLink extends Widget implements HasHTML, SourcesClickEvents {

	private Element anchorElem;
	private ClickListenerCollection fClickListeners;
	private String target;

	/**
	 * Creates an empty hyperlink.
	 */
	public ExternalLink() {
		setElement(DOM.createDiv());
		DOM.appendChild(getElement(), anchorElem = DOM.createAnchor());
		sinkEvents(Event.ONCLICK);
		setStyleName("gwt-Hyperlink");
	}

	public ExternalLink(Occurrence occ) {
		this();		
		setText(occ.getData());
		setTarget(occ.getTitle());
	}






	public void addClickListener(ClickListener listener) {
		if (fClickListeners == null)
			fClickListeners = new ClickListenerCollection();
		fClickListeners.add(listener);
	}

	public String getHTML() {
		return DOM.getInnerHTML(anchorElem);
	}

	/**
	 * Gets the target referenced by this hyperlink.
	 * 
	 * @return the target history token
	 * @see #setTargetHistoryToken
	 */
	public String getTarget() {
		return target;
	}

	public String getText() {
		return DOM.getInnerText(anchorElem);
	}

//	public void onBrowserEvent(Event event) {
//		if (DOM.eventGetType(event) == Event.ONCLICK) {
//			if (fClickListeners != null)
//				fClickListeners.fireClick(this);
//			Window.open(getTarget(), getText(), "");
//			DOM.eventPreventDefault(event);
//		}
//	}

	public void removeClickListener(ClickListener listener) {
		if (fClickListeners != null)
			fClickListeners.remove(listener);
	}

	public void setHTML(String html) {
		DOM.setInnerHTML(anchorElem, html);
	}

	/**
	 * Sets the history token referenced by this hyperlink. This is the history
	 * token that will be passed to {@link History#newItem} when this link is
	 * clicked.
	 * 
	 * @param target the new target history token
	 */
	public void setTarget(String target) {
		this.target = target;
		DOM.setAttribute(anchorElem, "href", target);
	}

	public void setText(String text) {
		DOM.setInnerHTML(anchorElem, text);
	}

}
