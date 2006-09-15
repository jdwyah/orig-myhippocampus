/*
 * Created on 30.06.2006
 */
package com.aavu.client.widget.RichText2;


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
public class Editor extends Composite/* implements HasHTML*/{

	private String width;
	private String height;
	private String skin;
	private String baseUrl;
	private Frame text;
	Element textElement;
	VerticalPanel panel;
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
	 * TODO if somebody edit->cancel->edit, we re-init everything. caused jscript error until we panel.clear(), but this should
	 * really not have to happen. Maybe even make these static.
	 */
	protected void onLoad(){
		super.onLoad();
		if(!inited ){

			Timer t = new Timer(){
				public void run(){
					initFrame();
				}
			};
			t.schedule(1000);
			inited = true;
		}


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

		toolbar.setStyleName("Toolbar");
		toolbar.setVisible(true);
		
		//
		//
		panel.clear();
		panel.add(toolbar);
		panel.add(text);
		
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
			}
			loadedOk  = true;
			
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
			
			Timer t = new Timer() {
				public void run() {
					initFrame();
				}
			};
			t.schedule(500);
		}

	}


	/**
	 * Add a ImageButton to the toolbar with a standard edit command.
	 * @param url The Imagename relative to the skin.
	 * @param tooltip The tooltip of the button.
	 * @param command The edit command
	 */
	private void addFormatImageButton(String url, String tooltip, final String command){
		Image image = new ImageButton( baseUrl + url, tooltip);
		image.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				format(textElement, command, null);
			}
		});
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
			Element old = textElement;
			initFrame();
			replaceNode(old, textElement);
			setHTML(html);
		}
		//toolbar.setVisible(editable);
		//edit.setVisible(!editable);
	}


	native static void setEditable(Element element,boolean b)/*-{
	    	    
        var doc = element.contentWindow.document;        
        
        element.contentWindow.focus();
        
        try {
        setTimeout(function(){
        	doc.designMode="On";
        	doc.body.contentEditable = true;
        	},1000);
        }catch(e){
        	alert("fail");
        }
                
    }-*/;


	native static void replaceNode(Element oldElement, Element newElement)/*-{
        oldElement.parentNode.replaceChild(newElement, oldElement);
    }-*/;


	native static void format(Element editor, String command, Object option)/*-{
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
//    	alert("error occured while adjust whether frame is loaded.\n" + e.message);
    	return false;
    }
}-*/;
}
