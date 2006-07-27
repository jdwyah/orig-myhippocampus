package com.aavu.client;

/*
Auto-Completion Textbox for GWT
Copyright (C) 2006 Oliver Albers http://gwt.components.googlepages.com/

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA

*/

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.widget.autocompletion.CompletionItems;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;



public class AutoCompleteTextBoxVal extends TextBox
    implements KeyboardListener, ClickListener, ChangeListener, PopupListener {
   
  protected PopupPanel choicesPopup = new PopupPanel(true);
  protected ListBox choices = new ListBox();
  protected boolean popupAdded = false;
  protected boolean visible = false;
  protected CompletionItems items = null;
   
  /**
   * Default Constructor
   *
   */
  public AutoCompleteTextBoxVal()
  {
    super();
    this.addKeyboardListener(this);
    choices.addClickListener(this);
    choices.addChangeListener(this);
    choicesPopup.addPopupListener(this);
    this.setStyleName("AutoCompleteTextBoxVal");
       
    choicesPopup.add(choices);
    choicesPopup.addStyleName("AutoCompleteChoices");
       
    choices.setStyleName("list");
  }

  /**
   * Sets an "algorithm" returning completion items
   * You can define your own way how the textbox retrieves autocompletion items
   * by implementing the CompletionItems interface and setting the according object
   * @see SimpleAutoCompletionItem
   * @param items CompletionItem implementation
   */
  public void setCompletionItems(CompletionItems items)
  {
    this.items = items;
  }
   
  /**
   * Returns the used CompletionItems object
   * @return CompletionItems implementation
   */
  public CompletionItems getCompletionItems()
  {
    return this.items;
  }
   
  /**
   * Not used at all
   */
  public void onKeyDown(Widget arg0, char arg1, int arg2) {
  }

  /**
   * Not used at all
   */
  public void onKeyPress(Widget arg0, char arg1, int arg2) {
  }
  
  /**
   * Clear choices when popup is hidden
   */
  public void onPopupClosed(PopupPanel sender, boolean autoClosed){
	  System.out.println("in onPopupClosed()");
	  visible = false;
	  choices.clear();
  }

  /**
   * A key was released, start autocompletion
   */
  public void onKeyUp(Widget arg0, char arg1, int arg2) {
	  System.out.println("In onKeyUp() " + arg1);
    if(arg1 == KEY_DOWN)
    {
      int selectedIndex = choices.getSelectedIndex();
      selectedIndex++;
      if(selectedIndex >= choices.getItemCount())
      {
        selectedIndex = 0;
      }
      choices.setSelectedIndex(selectedIndex);
           
      return;
    }
       
    if(arg1 == KEY_UP)
    {
      int selectedIndex = choices.getSelectedIndex();
      selectedIndex--;
      if(selectedIndex < 0)
      {
        selectedIndex = choices.getItemCount();
      }
      choices.setSelectedIndex(selectedIndex);
           
      return;        
    }
       
    if(arg1 == KEY_ENTER)
    {
      complete();
      return;
    }
       
    if(arg1 == KEY_ESCAPE)
    {
      choices.clear();
      choicesPopup.hide();
      visible = false;
           
      return;
    }
       
    String text = this.getText();
    List matches = new ArrayList();
    if(text.length() > 0)
    {
//      matches = items.getCompletionItems(text);
    }
    choices.clear();
       
    if(matches.size() > 0)
    {
      for(int i = 0; i < matches.size(); i++)
      {
        choices.addItem((String) matches.get(i));
      }
      
      choices.setSelectedIndex(0);
      choices.setVisibleItemCount(matches.size() + 1);
               
      if(!popupAdded)
      {
        RootPanel.get().add(choicesPopup);
        popupAdded = true;
      }
        choicesPopup.show();
        visible = true;
        choicesPopup.setPopupPosition(this.getAbsoluteLeft(),
        this.getAbsoluteTop() + this.getOffsetHeight());
        //choicesPopup.setWidth(this.getOffsetWidth() + "px");
        choices.setWidth(this.getOffsetWidth() + "px");

    } else {
      visible = false;
      choicesPopup.hide();
    }
  }

  /**
   * A mouseclick in the list of items
   */
  public void onChange(Widget arg0) {
	  System.out.println("in onchange()");
    complete();
  }
 
  public void onClick(Widget arg0) {
	System.out.println("in onClick");
    complete();
  }
   
  // add selected item to textbox
  protected void complete()
  {
	System.out.println("in autoCompleteTextBox complete()  " + choices.getItemCount() + " "+ choices.getSelectedIndex());
    if(choices.getItemCount() > 0 && choices.getSelectedIndex() != -1)
    {
      this.setText(choices.getItemText(choices.getSelectedIndex()));
    }
       
    choices.clear();
    choicesPopup.hide();
  }
}
 
