/*
 * This source code is under public domain.
 * You may use this source code as you wish, even distribute it
 * with different licenssing terms.
 * 
 * Contribution of bug fixes and new features would be appreciated.
 * 
 * Oliver Albers <oliveralbers@gmail.com>
 */ 

package com.aavu.client.widget.autocompletion;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestApplication implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
	final AutoCompleteTextBox box = new AutoCompleteTextBox();
	//	box.setCompletionItems(new SimpleAutoCompletionItems(
	//					     new String[]{ "\u0101n\u0101pana", "anatta", "apple", "ape", "anything", "else"}));

	SimpleAutoCompletionItems items = new SimpleAutoCompletionItems(
			new String[]{
					"a",
					"aa",
					"aaa",
					"aaaa",
					"ab",
					"abc",
					"abcd",
					"b",
					"bb",
					"bbb",
					"bbbb",
					"bc",
					"bcd"
			});
	
	//RemoteTopicAutoCompletionItems items = new RemoteTopicAutoCompletionItems("/completion");
	
	box.setCompletionItems(items);
      
	RootPanel.get("box").add(box);
	

	final AutoCompleteTextArea area = new AutoCompleteTextArea();

	area.setCompletionItems(items);
      
	RootPanel.get("area").add(area);
      
    }
}
