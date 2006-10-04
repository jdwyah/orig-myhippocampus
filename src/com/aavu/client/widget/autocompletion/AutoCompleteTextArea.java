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

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class AutoCompleteTextArea extends TextArea 
    implements KeyboardListener, ChangeListener, //ClickListener, 
	       MatchesRequiring {

    private static final int MAX_ITEMS = 10;

    protected PopupPanel choicesPopup = new PopupPanel(true);
    protected ListBox choices = new ListBox();
    protected CompletionItems items = new SimpleAutoCompletionItems(new Completable[]{});
    protected boolean popupAdded = false;
    protected String typedText = "";
    protected boolean visible = false;
    protected Completable[] matches;

    protected int posy = -1;
	
    /**
     * Default Constructor
     *
     */
    public AutoCompleteTextArea()
    {
	super();
	this.addKeyboardListener(this);
	choices.addChangeListener(this);
	this.setStyleName("AutoCompleteTextArea");
		
	choicesPopup.add(choices);
	choicesPopup.addStyleName("AutoCompleteChoices");
		
	choices.setStyleName("list");
    }

    /**
     * Sets an "algorithm" returning completion items
     * You can define your own way how the textbox retrieves autocompletion items
     * by implementing the CompletionItems interface and setting the according object
     * @see SimpleAutoCompletionItem
     * @param items	CompletionItem implementation
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
	
    public void onKeyDown(Widget sender, char keyCode, int modifiers) {	
    }

    public void onKeyPress(Widget sender, char keyCode, int modifiers) {
    }

    public void onKeyUp(Widget sender, char keyCode, int modifiers) {
	if(keyCode == KEY_DOWN)
	    {
		int selectedIndex = choices.getSelectedIndex();
		selectedIndex++;
		if(selectedIndex > choices.getItemCount())
		    {
			selectedIndex = 0;
		    }
		choices.setSelectedIndex(selectedIndex);
			
		return;
	    }
		
	if(keyCode == KEY_UP)
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
		
	if(keyCode == KEY_ENTER)
	    {
		if(visible)
		    {
			complete();
		    }
			
		return;
	    }
		
	if(keyCode == KEY_ESCAPE)
	    {
		choices.clear();
		choicesPopup.hide();
		visible = false;
			
		return;
	    }
		
	String text = this.getText();
	matches = new Completable[]{};
		
	String[] words = text.split(" |\n|\r");
	text = words[words.length - 1];
		
	if(text.length() > 0)
	    {
		items.getCompletionItems(text, this);
	    } else {
		onMatch(text);
	    }
    }
		    

    public void setMatches(Completable[] matches) {
	this.matches = matches;
    }
    public void setMatches(String[] matches) {
    	Completable[] cr = new CompleteLunacy[matches.length];
    	for (int i = 0; i < matches.length; i++) {			
			cr[i] = new CompleteLunacy(matches[i]);
		}
    	this.matches = cr;
    }
    /**
     * sorry. wanted the flexibility to have matchers on objects that aren't
     * just a String[], but we can't make String implement Completable.
     * @author Jeff Dwyer
     *
     */
    private class CompleteLunacy implements Completable{
		private String string;
		public CompleteLunacy(String string) {
			this.string = string;
		}
		public String getCompleteStr() {
			return string;	
		}    	
    }
    
    // use for Asynchronous Match
    public void onMatch(String text) {
	typedText = text;
	if(matches.length > 0){
	    choices.clear();
	    
	    for(int i = 0; i < matches.length; i++) {
		choices.addItem(matches[i].getCompleteStr());
	    }
	    
	    // if there is only one match and it is what is in the
	    // text field anyways there is no need to show autocompletion
	    if(matches.length == 1 && matches[0].getCompleteStr().compareTo(text) == 0) {
		choicesPopup.hide();
	    } else {
		if(matches.length == 1){
		    //use a wrong count, so that the list gets rendered as 
		    //listbox and not as dropdownbox
		    choices.setVisibleItemCount(2);
		}
		//choices.setSelectedIndex(0);
		choices.setVisibleItemCount(matches.length + 1);
		
		if(!popupAdded)
		    {
			RootPanel.get().add(choicesPopup);
			popupAdded = true;
		    }
		choicesPopup.show();
		visible = true;
		int nposy = this.getAbsoluteTop() + this.getOffsetHeight();
		if(posy < 0 || nposy > posy)
		    {
			posy = nposy;
		    }
		choicesPopup.setPopupPosition(this.getAbsoluteLeft(), 
					      posy);
		//choicesPopup.setWidth(this.getOffsetWidth() + "px");
		choices.setWidth(this.getOffsetWidth() + "px");
	    }
	    
	} else {
	    choicesPopup.hide();
	    visible = false;
	}
    }
    
    public void onChange(Widget sender) {
	complete();
    }

    // add selected item to textarea
    protected void complete()
    {
	if(choices.getItemCount() > 0)
	    {
		String text = this.getText();
		text = text.substring(0, text.length() - typedText.length());
		text += choices.getItemText(choices.getSelectedIndex());
		this.setText(text);
		this.setFocus(true);
	    }
		
	choices.clear();
	choicesPopup.hide();
    }

}
