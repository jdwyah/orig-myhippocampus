/*
 * Copyright 2006 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.aavu.client.gui.ext.tabbars;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabListenerCollection;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

/**
 * A horizontal bar of folder-style tabs, most commonly used as part of a
 * {@link com.google.gwt.user.client.ui.TabPanel}.
 * 
 * <p>
 * <img class='gallery' src='TabBar.png'/>
 * </p>
 * 
 * <h3>CSS Style Rules</h3> * <ul class='css'> * <li>.gwt-TabBar { the tab bar itself }</li>
 * <li>.gwt-TabBar .gwt-TabBarFirst { the left edge of the bar }</li>
 * <li>.gwt-TabBar .gwt-TabBarRest { the right edge of the bar }</li>
 * <li>.gwt-TabBar .gwt-TabBarItem { unselected tabs }</li>
 * <li>.gwt-TabBar .gwt-TabBarItem-selected { additional style for selected tabs } </li>
 * </ul>
 * 
 * <p>
 * <h3>Example</h3> {@example com.google.gwt.examples.TabBarExample}
 * </p>
 */
public class VertableTabBar extends Composite implements SourcesTabEvents,
ClickListener, MouseListener {

	private CellPanel panel;
	private Widget selectedTab;
	private TabListenerCollection tabListeners;
	private Orientation alignment;
	private CellPanelWithMethods cellPanelWithMethods;
	/**
	 * Creates an empty tab bar.
	 * @param orient 
	 */
	public VertableTabBar(Orientation orient) {
		this.alignment = orient;
		
		if(orient == Orientation.HORIZONTAL){
			panel = new HorizontalPanel();
			((HorizontalPanel)panel).setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);
		}else{
			panel = new VerticalPanel();
			((VerticalPanel)panel).setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		}
		
		cellPanelWithMethods = new CellPanelWithMethods(panel);

		initWidget(panel);
		sinkEvents(Event.ONCLICK);
		setStyleName("H-VertTabBar");

		

		HTML first = new HTML("&nbsp;", true), rest = new HTML("&nbsp;", true);
		first.setStyleName("H-VertTabBarFirst");
		rest.setStyleName("H-VertTabBarRest");
		first.setHeight("100%");
		rest.setHeight("100%");

		panel.add(first);
		panel.add(rest);
		first.setHeight("100%");
		panel.setCellHeight(first, "100%");
		panel.setCellWidth(rest, "100%");
	}
	
	private class CellPanelWithMethods{

		private CellPanel panel;

		public CellPanelWithMethods(CellPanel panel) {
			this.panel = panel;
		}

		public Widget getWidget(int i) {
			if(alignment == Orientation.HORIZONTAL)
				return ((HorizontalPanel)panel).getWidget(i);
			else
				return ((VerticalPanel)panel).getWidget(i);
		}

		public int getWidgetCount() {
			if(alignment == Orientation.HORIZONTAL)
				return ((HorizontalPanel)panel).getWidgetCount();
			else
				return ((VerticalPanel)panel).getWidgetCount();
		}

		public void insert(Label item, int i) {
			if(alignment == Orientation.HORIZONTAL)
				((HorizontalPanel)panel).insert(item, i);
			else
				((VerticalPanel)panel).insert(item, i);
		}

		public int getWidgetIndex(Widget selectedTab) {
			if(alignment == Orientation.HORIZONTAL)
				return ((HorizontalPanel)panel).getWidgetIndex(selectedTab);
			else
				return ((VerticalPanel)panel).getWidgetIndex(selectedTab);
		}
		
	}
	
	
	
	/**
	 * Adds a new tab with the specified text.
	 * 
	 * @param text the new tab's text
	 */
	public void addTab(String text) {
		insertTab(text, getTabCount());
	}

	/**
	 * Adds a new tab with the specified text.
	 * 
	 * @param text the new tab's text
	 * @param asHTML <code>true</code> to treat the specified text as html
	 */
	public void addTab(String text, boolean asHTML) {
		insertTab(text, asHTML, getTabCount());
	}

	public void addTabListener(TabListener listener) {
		if (tabListeners == null)
			tabListeners = new TabListenerCollection();
		tabListeners.add(listener);
	}

	/**
	 * Gets the tab that is currently selected.
	 * 
	 * @return the selected tab
	 */
	public int getSelectedTab() {
		if (selectedTab == null)
			return -1;
		return cellPanelWithMethods.getWidgetIndex(selectedTab) - 1;
	}

	/**
	 * Gets the number of tabs present.
	 * 
	 * @return the tab count
	 */
	public int getTabCount() {	
		return cellPanelWithMethods.getWidgetCount() - 2;
	}

	/**
	 * Gets the specified tab's HTML.
	 * 
	 * @param index the index of the tab whose HTML is to be retrieved
	 * @return the tab's HTML
	 */
	public String getTabHTML(int index) {
		if (index >= getTabCount())
			return null;
		return ((HTML) cellPanelWithMethods.getWidget(index - 1)).getHTML();
	}

	/**
	 * Inserts a new tab at the specified index.
	 * 
	 * @param text the new tab's text
	 * @param asHTML <code>true</code> to treat the specified text as HTML
	 * @param beforeIndex the index before which this tab will be inserted
	 */
	public void insertTab(String text, boolean asHTML, int beforeIndex) {
		
		Label item;
		if (asHTML)
			item = new HTML(text);
		else
			item = new Label(text);
		insertTab(item, beforeIndex);	}

	public void insertTab(Label item, int beforeIndex) {
		if ((beforeIndex < 0) || (beforeIndex > getTabCount()))
			throw new IndexOutOfBoundsException();

		
		item.setWordWrap(false);
		item.addClickListener(this);
		item.addMouseListener(this);
		item.setStyleName("H-VertTabBarItem");
		cellPanelWithMethods.insert(item, beforeIndex + 1);
	}
	
	/**
	 * Inserts a new tab at the specified index.
	 * 
	 * @param text the new tab's text
	 * @param beforeIndex the index before which this tab will be inserted
	 */
	public void insertTab(String text, int beforeIndex) {
		insertTab(text, false, beforeIndex);
	}

	public void onClick(Widget sender) {
		for (int i = 1; i < cellPanelWithMethods.getWidgetCount() - 1; ++i) {
			if (cellPanelWithMethods.getWidget(i) == sender) {
				selectTab(i - 1);
				return;
			}
		}
	}

	/**
	 * Removes the tab at the specified index.
	 * 
	 * @param index the index of the tab to be removed
	 */
	public void removeTab(int index) {
		checkTabIndex(index);

		// (index + 1) to account for 'first' placeholder widget.
		Widget toRemove = cellPanelWithMethods.getWidget(index + 1);
		if (toRemove == selectedTab)
			selectedTab = null;
		panel.remove(toRemove);
	}

	public void removeTabListener(TabListener listener) {
		if (tabListeners != null)
			tabListeners.remove(listener);
	}

	/**
	 * Programmatically selects the specified tab.
	 * 
	 * @param index the index of the tab to be selected
	 * @return <code>true</code> if successful, <code>false</code> if the
	 *         change is denied by the {@link TabListener}.
	 */
	public boolean selectTab(int index) {
		checkTabIndex(index);

		if (tabListeners != null) {
			if (!tabListeners.fireBeforeTabSelected(this, index))
				return false;
		}

		setSelectionStyle(selectedTab, false);
		selectedTab = cellPanelWithMethods.getWidget(index + 1);
		setSelectionStyle(selectedTab, true);

		if (tabListeners != null)
			tabListeners.fireTabSelected(this, index);
		return true;
	}

	private void checkTabIndex(int index) {
		if ((index < 0) || (index >= getTabCount()))
			throw new IndexOutOfBoundsException();
	}

	private void setSelectionStyle(Widget item, boolean selected) {
		if (item != null) {
			if (selected)
				item.addStyleName("H-VertTabBarItem-selected");
			else
				item.removeStyleName("H-VertTabBarItem-selected");
		}
	}

	
	/**
	 * added to the labels in insertTab()
	 */
	public void onMouseEnter(Widget sender) {
		for (int i = 1; i < cellPanelWithMethods.getWidgetCount() - 1; ++i) {
			if (cellPanelWithMethods.getWidget(i) == sender) {
				selectTab(i - 1);
				return;
			}
		}
	}
	public void onMouseDown(Widget sender, int x, int y) {}
	public void onMouseLeave(Widget sender){}
	public void onMouseMove(Widget sender, int x, int y) {}
	public void onMouseUp(Widget sender, int x, int y) {}
}
