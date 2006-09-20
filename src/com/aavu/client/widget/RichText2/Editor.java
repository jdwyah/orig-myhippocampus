/*
 * Created on 30.06.2006
 */
package com.aavu.client.widget.RichText2;


import org.gwtwidgets.client.util.Location;
import org.gwtwidgets.client.util.WindowUtils;
import org.gwtwidgets.client.wwrapper.WHyperlink;

import com.aavu.client.widget.RichText.RichTextArea;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;


/**
 * This is the main class of the rich text editor. A typical use is:
 * <code><pre>
 *      Editor editor = new Editor();
 *      editor.setTextPixelSize(400, 300);
 *      RootPanel.get().add(editor);
 *      editor.setHTML("My editor text.");
 * </pre></code>
 * The editor is using the follow stylenames:
 * <ul>
 * <li>Toolbar
 * <li>ImageButton
 * <li>ImageButton-over
 * <li>ImageButton-pressed
 * <li>Tooltip
 * </ul>
 * 
 * <!--webbot bot="Include" U-Include="../../../../../../_private/google-banner.html" TAG="BODY" -->
 * @author Volker Berlin
 */
public class Editor extends Composite /* implements HasHTML*/{

	private String width;
	private String height;
	private String skin;
	private String baseUrl;
	private Frame text;
	private Element textElement;
	private VerticalPanel panel;

	protected EditorWidget editorW;

	private HorizontalPanel toolbar = new HorizontalPanel();
	private boolean inited = false;
	private boolean loadedOk = false;


	/**
	 * Constructor of a Rich Text Editor
	 */
	public Editor(){
		panel = new VerticalPanel();
		initWidget(panel);
	}



	/**
	 * This method is called when the editor becomes attached to the browser's
	 * document. In this method all elements are created with the current skin.
	 * 
	 */
	protected void onLoad(){
		super.onLoad();		
		System.out.println("on load");
		if(!inited ){

			System.out.println("do init");

			initFrame();

			if(skin == null || skin.length() == 0){
				skin = "default";
			}
			baseUrl = "richtext/skins/" + skin + "/";
			setStylesheet(baseUrl);


			ListBox fontname = new ListBox();
			fontname.addItem("Arial");
			fontname.addItem("Courier New");
			fontname.addItem("Times New Roman");
			fontname.addChangeListener(new ChangeListener(){
				public void onChange(Widget sender) {
					String value = ((ListBox)sender).getItemText(((ListBox)sender).getSelectedIndex());
					format(textElement, "FontName", value);
				}
			});
			toolbar.add(fontname);

			ListBox fontsize = new ListBox();
			addItem(fontsize,"8","1");
			addItem(fontsize,"10","2");
			addItem(fontsize,"12","3");
			addItem(fontsize,"14","4");
			addItem(fontsize,"18","5");
			addItem(fontsize,"24","6");
			addItem(fontsize,"36","7");
			fontsize.addChangeListener(new ChangeListener(){
				public void onChange(Widget sender) {
					Element child = DOM.getChild( sender.getElement(), ((ListBox)sender).getSelectedIndex());
					String value = DOM.getAttribute(child, "value");
					format(textElement, "FontSize", value);
				}
			});
			toolbar.add(fontsize);

			addImages();

			toolbar.setStyleName("Toolbar");
			toolbar.setVisible(true);

			//
			//
			panel.clear();
			panel.add(toolbar);
			panel.add(text);

			inited = true;
		}

	}
	/**
	 * Default toolbar. 
	 * Over-ride with what you want.
	 *
	 */
	protected void addImages(){
		addFormatImageButton("bold.gif", "Bold", "Bold");
		addFormatImageButton("italic.gif", "Italic", "Italic");
		addFormatImageButton("underline.gif", "Underline", "Underline");

		addFormatImageButton("left.gif", "Left", "JustifyLeft");
		addFormatImageButton("center.gif", "Center", "JustifyCenter");
		addFormatImageButton("right.gif", "Right", "JustifyRight");

		addFormatImageButton("numbered.gif", "Numbered List", "InsertOrderedList");        
		addFormatImageButton("list.gif", "Bullet List", "InsertUnorderedList");
		addFormatImageButton("outdent.gif", "Outdent", "Outdent");        
		addFormatImageButton("indent.gif", "Indent", "Indent");        

		addFormatImageButton("hr.gif", "Horizontal Rule", "InsertHorizontalRule");

	}

	/**
	 * Sets the text's size, in pixels, not including decorations such as
	 * border, margin, padding and the toolbar.
	 * 
	 * @param width the text's new width, in pixels
	 * @param height the text's new height, in pixels
	 */
	public void setTextSize(String width, String height){
		this.width = width;
		this.height = height;
		if(text != null){
			text.setWidth(width);
			text.setHeight(height);
		}
	}



	/**
	 * Set the skin of the editor. If not set then the default skin is used.
	 * This method must be called before the editor is added to the parent.
	 * @param skin the name of the skin.
	 */
	void setSkin(String skin){
		this.skin = skin;
	}


	/**
	 * Create the IFrame that hold the editable text.
	 */

	private void initFrame(){


		System.out.println("111");
		if (isFrameLoaded(textElement)){
			//if frame is loaded, then set it to editor
			//setDesignMode(text.getElement(), true);
			System.out.println("2222");



			if(!loadedOk){
				System.out.println("setEdit");
				setEditable(true);

				System.out.println("attach events to textElement");
				attachEventsToFrame(textElement);
			}
			loadedOk  = true;


			//doOnInitSucess();


//			final RichTextArea rta = this;
//			attachEventsToFrame(rta, editor.getElement(), isAuotoResizeHeight());
////			Window.alert("initFrame, will set html to " + this.html);		  		
//			if(this.html != null){
//			setHtmlToFrame();
//			}else{
//			//setHTML will fire the onDisplay event, donot need fire again, so that
//			//this statement was written in the 'else'.
//			onDisplayChanged();
//			}
		}else{
			System.out.println("151");
			text = new Frame();
			text.setWidth(width);
			text.setHeight(height);			
			textElement = text.getElement();			
			editorW = new EditorWidget(textElement);
			
			
			//run the second half of the initialization above
			//
			Timer t = new Timer() {
				public void run() {
					initFrame();
				}
			};
			t.schedule(500);
		}

	}

	/**
	 * lifecycle call for extended classes
	 * over-ride if you need to know when we're setup
	 */
	protected void doOnInitSucess(){
		//do nothing
	}


	/**
	 * Add a ImageButton to the toolbar with a standard edit command.
	 * @param url The Imagename relative to the skin.
	 * @param tooltip The tooltip of the button.
	 * @param command The edit command
	 */	
	protected void addFormatImageButton(String url, String tooltip, final String command){
		addFormatImageButton(url, tooltip, command,new ClickListener(){
			public void onClick(Widget sender) {
				format(textElement, command, null);
			}
		});

	}
	protected void addFormatImageButton(String url, String tooltip, final String command,ClickListener cl){
		Image image = new ImageButton( baseUrl + url, tooltip);
		image.addClickListener(cl);
		toolbar.add(image);
	}

	/**
	 * Get the current HTML text of the editor.
	 * @see #setHTML(String)
	 */
	public String getHTML(){
		return getHTML(textElement);
	}


	native static String getHTML(Element element)/*-{
        var doc = element.contentWindow.document;
        return doc.documentElement.innerHTML;
    }-*/;


	/**
	 * Sets this editor's contents via HTML. This method can be call first after it was add to the parent.
	 * 
	 * @param html the editor's new HTML
	 */
	public void setHTML(final String html) {
		if (isFrameLoaded(textElement)){
			System.out.println("really set");
			setHTML(textElement, html);
		}else{			
			Timer t = new Timer() {
				public void run() {
					setHTML(html);
				}
			};
			t.schedule(500);
		}

	}


	native static void setHTML(Element element, String html)/*-{
        var doc = element.contentWindow.document;
        if (!html)
            html = '';
        doc.open(); 
        doc.write(html); 
        doc.close();
    }-*/;


	/**
	 * Enable or disable the edit mode. Depending on the mode the toolbar is visible or not.
	 * @param editable true means that is can be edit.
	 */
	public void setEditable(boolean editable){
		System.out.println("set edit "+editable);

		if(editable){
			setEditable(textElement,editable);
		}else{
			setEditable(textElement,editable);

			String html = getHTML();
//			Element old = textElement;
//			initFrame();
//			replaceNode(old, textElement);
//			setHTML(html);
		}
		//toolbar.setVisible(editable);
		//edit.setVisible(!editable);
	}


	native static void setEditable(Element element,boolean b)/*-{

        var doc = element.contentWindow.document;        

        element.contentWindow.focus();


        try {
        setTimeout(function(){
                //
                //crucial! running this code in IE kills the keyboard listener for some reason
                //
        		if(!doc.all){
        			doc.designMode="On";
        			doc.body.contentEditable = true;
        		}
        	},1000);
        }catch(e){
        	alert("fail");
        }

    }-*/;


	native static void replaceNode(Element oldElement, Element newElement)/*-{
        oldElement.parentNode.replaceChild(newElement, oldElement);
    }-*/;


	native static void format(Element editor, String command, String option)/*-{
        editor.contentWindow.focus();
        editor.contentWindow.document.execCommand(command, false, option);
    }-*/;	

	/**
	 * Add the style sheet from the skin to the page. 
	 * @param baseUrl The baseURL of the skin.
	 */
	native void setStylesheet(String baseUrl)/*-{
        var css = $doc.createElement('link');
        css.rel = 'stylesheet';
        css.type = 'text/css';
        css.href = baseUrl + 'skin.css';
        $doc.documentElement.firstChild.appendChild(css);
    }-*/;


	/**
	 * A hack because currently the ListBox does not support the attribut VALUE.
	 * @param list
	 * @param display
	 * @param value
	 */
	private static void addItem(ListBox list, String display, String value) {
		Element option = DOM.createElement("OPTION");
		DOM.setInnerText(option, display);
		DOM.setAttribute(option, "value", value);
		DOM.appendChild(list.getElement(), option);
	}


	private native boolean isFrameLoaded(Element editor) /*-{
	try{		
    	if(editor != null && editor.contentWindow != null 
    		&& editor.contentWindow.document != null  
    		&& editor.contentWindow.document.body != null){

    		return true;
    	}else{
    		return false;
    	}
    }catch(e){
    	alert("error occured while adjust whether frame is loaded.\n" + e.message);
    	return false;
    }
}-*/;

	private native void attachEventsToFrame(Element elem)/*-{
    var d = elem.contentWindow.document;

    var handleEvent = function(){
    		alert("foo!");
        };

    var _eventPatch = function(editor_id) {
		var win, e;
		alert("1");
		try {
			// Try selected instance first

			alert("win: "+win);

			win = elem.contentWindow;
			alert("win: "+win);

			if (win && win.event) {
				e = win.event;

				if (!e.target)
					e.target = e.srcElement;

				handleEvent(e);
				return;
			}


			for (var i=0; i<$doc.frames.length; i++) {

 	                    if ($doc.frames[i].event) {
 	                                var event = $doc.frames[i].event;

 	                                if (!event.target)
 	                                        event.target = event.srcElement;

 	                                handleEvent(event);
 	                                return;
						}
			}


		} catch (ex) {
			alert(ex);
			// Ignore error if iframe is pointing to external URL
		}
	};

    function addEvent(o, n, h) {
		if (o.attachEvent)
			o.attachEvent("on" + n, h);
		else
			o.addEventListener(n, h, false);
	};


	if (document.all) {	
		addEvent(d, "keypress", handleEvent);		
	} else {
		addEvent(d, "keypress", handleEvent);
	}





//    if(document.all){
//        d.attachEvent("onkeydown", f);
//        d.attachEvent("onkeypress", f);
//        d.attachEvent("onmousedown", f);
//        d.attachEvent("onmouseup", f);
//    }else{
//        d.addEventListener("keydown", f, false);
//        d.addEventListener("keypress", f, false);
//        d.addEventListener("mousedown", f, false);
//        d.addEventListener("mouseup", f, false);
//    }


}-*/;

	public Object getSelectionRange(){
		return nativeGetSelectionRange(textElement);
	}

	private native Object nativeGetSelectionRange(Element e)/*-{
		try{
			if (document.all) {
		        var sel = e.contentWindow.document.selection;
				return sel.createRange();
			} else {
		        var sel = e.contentWindow.getSelection();
				e.contentWindow.focus();
				if (typeof sel != "undefined") {
					try {
						return sel.getRangeAt(0);
					} catch(e) {
						return e.contentWindow.document.createRange();
					}
				} else {
					return e.contentWindow.document.createRange();
				}
			}
		}catch(e){
			alert("error occured while get the selection. Please contact with vendor.\n" + e.message);
		}
	}-*/;


	private class EditorWW extends Widget{
		public EditorWW(Element textElement) {
			setElement(textElement);
		}		
	}

	protected class EditorWidget extends FocusPanel {//implements SourcesKeyboardEvents {

		//private KeyboardListenerCollection keyboardListeners;

		public EditorWidget(Element textElement) {
			super(new EditorWW(textElement));
			System.out.println("made it");
			//setElement(textElement);
			//sinkEvents(Event.KEYEVENTS | Event.FOCUSEVENTS); 
		}

//		public void addKeyboardListener(KeyboardListener listener) {
//		if (keyboardListeners == null)
//		keyboardListeners = new KeyboardListenerCollection();
//		keyboardListeners.add(listener);
//		}

//		public void removeKeyboardListener(KeyboardListener listener) {
//		if (keyboardListeners != null)
//		keyboardListeners.remove(listener);
//		}	
//		public void onBrowserEvent(Event event) {
//		System.out.println("EditorWidget browser event "+event);
//		switch (DOM.eventGetType(event)) {		    
//		case Event.ONKEYDOWN:
//		case Event.ONKEYUP:
//		case Event.ONKEYPRESS:
//		if (keyboardListeners != null)
//		keyboardListeners.fireKeyboardEvent(this, event);
//		break;
//		}			
//		}
	}


}
