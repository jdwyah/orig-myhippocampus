package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.EntryEditWindow;
import com.aavu.client.gui.ext.GUIEffects;
import com.aavu.client.gui.ext.ModablePopupPanel;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.aavu.client.widget.RichText.RichTextToolbar;
import com.aavu.client.widget.RichText2.KeyCodeEventListener;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SpecialTextbox extends Composite implements KeyCodeEventListener, ClickListener, SourcesChangeEvents, KeyboardListener {

	/**
	 * no I'm not kidding. 92 is "|" on IE & FF, but ctrl-Pipe is 28 on IE. I dunno.
	 * 
	 */
	private static final int KEY_PIPE_IE = 28;
	private static final int KEY_PIPE_FF = 92;	//124; \92?  |124. ctrl \ 28?

	private RichTextArea textArea;
	private PopupPanel completePopup;
	private TopicCompleter completer;
	private Button selectButton;

	private JavaScriptObject range;

	private TopicCache topicCache;

	private ChangeListenerCollection listeners;
	private String text;

	public SpecialTextbox(TopicCache topicC){
		super();
		this.topicCache = topicC;

		textArea = new RichTextArea(); //new HippoEditor(this);
		
		textArea.setSize((EntryEditWindow.WIDTH - 120)+"px", "400px");
		textArea.addKeyboardListener(this);

		RichTextToolbar toolbar = new RichTextToolbar(textArea);
				
		//textArea.addChangeListener(this);		
		//textArea.setKeyEventlistener(this);		

		VerticalPanel mainPanel = new VerticalPanel();		

		completer = new TopicCompleter(topicC);
		completer.addKeyboardListener(new KeyboardListenerAdapter(){
			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				if(keyCode == KEY_ESCAPE){
					hideLinker();
				}
			}});

		selectButton = new Button("Link");
		selectButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				link();
			}});

		Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				hideLinker();
			}});

		

		completePopup = new ModablePopupPanel(true);		
		completePopup.setPopupPosition(300,300);
		completePopup.addStyleName("H-PopLinkWindow");

		HorizontalPanel completePanel = new HorizontalPanel();
		completePanel.add(completer);
		completePanel.add(selectButton);	
		completePanel.add(cancelButton);
		completePopup.add(completePanel);		

		mainPanel.add(toolbar);
		mainPanel.add(textArea);



		//mainPanel.add(completePanel);
		initWidget(mainPanel);
		
		
	}



	public void keyCodeEvent(int i,boolean ctrl) {
		if(ctrl && (i == KEY_PIPE_FF || i == KEY_PIPE_IE)){
			openLinkDialog();			
		}
//		if(listeners != null){
//			listeners.fireChange(this);
//		}
	}


	/**
	 * PEND MED Necessary? otherwise we get a content.body.blah not an object 
	 * @param text
	 */
	public void setText(final String text) {
		this.text = text;
		DeferredCommand.addCommand(new Command(){
			public void execute(){
				textArea.setHTML(text);	
			}
		});
	}

	public String getText() {
		return textArea.getHTML();

	}

	/**
	 * The "link" button in the popup was clicked
	 * 
	 * Go ahead and make the link.
	 */
	private void link(){

		String linkTo = completer.getText();

		topicCache.getTopicIdentForNameOrCreateNew(linkTo, new StdAsyncCallback(ConstHolder.myConstants.topic_lookupAsync()){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				
				TopicIdentifier topic = (TopicIdentifier) result;
				
//				textArea.setSelectionRange(range);
//
//				textArea.makeLink(topic.getTopicID());

				hideLinker();

			}});

	}

	private void hideLinker() {
		//must "hide()" too, because it's modal.
		GUIEffects.dropOut(completePopup);							
		completePopup.hide();

		textArea.setFocus(true);
	};
	
	
	/**
	 * this is the listener that we pass to HippoEditor so that it knows what to 
	 * do when the "Link" Button is clicked.
	 * 
	 */
	public void onClick(Widget sender) {
		openLinkDialog();
	}




	private void openLinkDialog() {
		System.out.println("LINK");

		//range = textArea.getExpandedSelection();//getSelectionRange();

		completePopup.show();			
		completePopup.setVisible(true);				
		GUIEffects.appear(completePopup, 1);

		DeferredCommand.addCommand(new Command(){
			public void execute() {			
				completer.setFocus(true);		
			}});

	}



	/**
	 * NOTE see above, textArea listeners seem broken
	 */
	public void addChangeListener(ChangeListener listener) {	

		textArea.addKeyboardListener(this);
		if(listeners == null){
			listeners = new ChangeListenerCollection();
		}
		listeners.add(listener);
	}


	public void removeChangeListener(ChangeListener listener) {
		
		textArea.removeKeyboardListener(this);
		if(listeners != null){
			listeners.remove(listener);
		}		
	}




	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		//System.out.println("DOWN KEY_CTRL "+KEY_CTRL+" mod "+modifiers+" "+keyCode+" "+KEY_PIPE_FF+" "+KEY_PIPE_IE);
	}
	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		//System.out.println("PRESS KEY_CTRL "+KEY_CTRL+" mod "+modifiers+" "+keyCode+" "+KEY_PIPE_FF+" "+KEY_PIPE_IE);
	}
	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		
		if(!textArea.getHTML().equals(text)){		
			listeners.fireChange(sender);
		}
		
//		System.out.println("UP KEY_CTRL "+KEY_CTRL+" mod "+modifiers+" "+keyCode+" "+KEY_PIPE_FF+" "+KEY_PIPE_IE);
//		
//		if((KEY_CTRL == modifiers) && (keyCode == KEY_PIPE_FF || keyCode == KEY_PIPE_IE)){
//			openLinkDialog();			
//		}
////		System.out.println("list "+listeners);
////		if(listeners != null){
////			listeners.fireChange(this);
////		}		
	}

}
