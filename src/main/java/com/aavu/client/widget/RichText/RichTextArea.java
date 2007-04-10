/*
 * Created on 2006/07/26 10:17:37 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RichTextArea extends Composite {
	public static final String IMGFOLDER = "richtext/img/";
	private List toolButtons = new ArrayList();
	private VerticalPanel panel;
	private Frame editor = new Frame();
	private boolean auotoResizeHeight = false;
	private String html = null;
	private RichTextAreaConfig config;
	private FlowPanel lastToolBar; 
	private boolean inited = false;
	
	public RichTextArea(){
		this(SimpleRichTextAreaConfig.DEFAULT);
	}
    public RichTextArea(RichTextAreaConfig config){
    	this.config = config;
    	
        panel = new VerticalPanel();
        String[][] toolBars = this.config.getToolBars();

        for(int i=0; i<toolBars.length; i++){
            FlowPanel toolBarPanel = new FlowPanel();
            lastToolBar = toolBarPanel;
            DOM.setStyleAttribute(toolBarPanel.getElement(), "backgroundColor", config.getToolBarBackgroundColor());
            DOM.setStyleAttribute(toolBarPanel.getElement(), "padding", "1px 2px 1px 2px");
            DOM.setStyleAttribute(toolBarPanel.getElement(), "height", "25px");
            DOM.setStyleAttribute(toolBarPanel.getElement(), "width", "100%");
            DOM.setStyleAttribute(toolBarPanel.getElement(), "verticalAlign", "middle");
            for(int j=0; j<toolBars[0].length; j++){
            	if (toolBars[i][j].equals("-")){
            		Image separator = new Image(IMGFOLDER + "separator.gif");
            		DOM.setStyleAttribute(separator.getElement(), "display", "");
            		DOM.setStyleAttribute(separator.getElement(), "cssFloat", "left");
            		DOM.setStyleAttribute(separator.getElement(), "styleFloat", "left");
            		toolBarPanel.add(separator);
            		continue;
            	}
            	ToolButton btn = ToolButtonList.getButtonById(toolBars[i][j]);
            	if(btn == null){
            		//try to get it from user custom buttons
            		btn = config.getCustomToolButton(toolBars[i][j]);
            	}
            	if(btn != null){
            		btn.setRichTextArea(this);
            		toolButtons.add(btn);
            		toolBarPanel.add(btn);
            	}else{
            		//throw (new NullPointerException("Button " + toolBars[i][j] + " not foundl"));
            	}
            }
            panel.add(toolBarPanel);
        }
        editor.setWidth("100%");
        DOM.setStyleAttribute(editor.getElement(), "border", "1px solid #7D7D7D");
        panel.add(editor);

        initWidget(panel);

    }


	protected void onLoad() {
		super.onLoad();
		if(!inited){
			if(isAuotoResizeHeight()){
				DOM.setAttribute(editor.getElement(), "scrolling", "no");
			}else{
				DOM.setAttribute(editor.getElement(), "scrolling", "auto");
			}
			Timer t = new Timer(){
				public void run(){
					initFrame();
				}
			};
			t.schedule(1000);
			inited = true;
		}
	}
	void addWidgetToLastToolBar(Widget w){
		lastToolBar.add(w);
	}
	private void initFrame(){
		if (isFrameLoaded(editor.getElement())){
			//if frame is loaded, then set it to editor
	    	setDesignMode(editor.getElement(), true);
	    	final RichTextArea rta = this;
	  		attachEventsToFrame(rta, editor.getElement(), isAuotoResizeHeight());
//Window.alert("initFrame, will set html to " + this.html);		  		
	  		if(this.html != null){
	  			setHtmlToFrame();
//	  			nativeSetHtml(editor.getElement(), this.html);
//	  			onDisplayChanged();
	  		}else{
		  		//setHTML will fire the onDisplay event, donot need fire again, so that
		  		//this statement was written in the 'else'.
		  		onDisplayChanged();
	  		}
		}else{
			Timer t = new Timer() {
			      public void run() {
			    	  initFrame();
			      }
			};
			t.schedule(500);
		}
		
	}
	/**
	 * return the html value of this control
	 * @return
	 */
	public String getHtml(){
		if(editor.isAttached()){
			return nativeGetHtml(editor.getElement());
		}else{
			return html;
		}
	}
	/**
	 * set the html value of this control 
	 * @param html
	 */
	public void setHtml(String html){
		this.html = html;
		if(inited){
			//if not inited, the initFrame method will set the HTMLs.
			setHtmlToFrame();
		}
	}
	private void setHtmlToFrame(){
		if(this.html == null){
			return;
		}
		if (isFrameLoaded(editor.getElement())){
			nativeSetHtml(editor.getElement(), this.html);
			//after successfule setHtml, change the html to null, avoid set again.
			this.html = null;
			//maybe this action effect the height of the frame, fire the onDisplayChanged event.
			onDisplayChanged();
		}else{
			Timer t = new Timer() {
			      public void run() {
			    	  setHtmlToFrame();
			      }
			};
			t.schedule(500);
		}
	}
	/**
	 * set the width of the edit area
	 */
	public void setWidth(String width) {
		super.setWidth(width);
	}
	
	/**
	 * set the height of the edit area (height of the toolbar(s) is <b>not</b> included)
	 */
	public void setHeight(String height) {
		editor.setHeight(height);
	}
	/**
	 * <b>not supported.</b> please use setSize(String width, String height) instead.
	 */
	public void setPixelSize(int width, int height) {
		//throw new NoSuchMethodError("setPixelSize(int, int) is unsupported in RichTextArea, use SetSize(String, String) instead.");
		Window.alert("setPixelSize(int, int) is unsupported in RichTextArea, use SetSize(String, String) instead.");
		throw new NullPointerException();
	}
	/**
	 * set the height and width of the edit area(height of toolbar(s) is <b>not</b> included)
	 */
	public void setSize(String width, String height) {
		this.setWidth(width);
		editor.setHeight(height);
	}
	/**
	 * is the editor is auto adjust it's height.
	 * if true, the height of the edit area will be adjusted automatic when editing, like Google NoteBook 
	 * @return
	 */
	public boolean isAuotoResizeHeight() {
		return auotoResizeHeight;
	}
	/**
	 * set wheter using auto adjust the height of the edit area
	 * @param auotoResizeHeight boolean default false
	 */
	public void setAuotoResizeHeight(boolean auotoResizeHeight) {
		this.auotoResizeHeight = auotoResizeHeight;
	}
	public RichTextAreaConfig getConfig(){
		return config;
	}
	
	/**
	 * return the editor frame.
	 * @return
	 */
	public Frame getEditorFrame(){
		return editor;
	}
	private native void setDesignMode(Element e, boolean on)/*-{
		if(on){
	    	e.contentWindow.document.designMode = "on";
   	        e.contentWindow.focus();
	    }else{
	    	e.contentWindow.document.designMode = "off";
	    }
	}-*/;
	private native void attachEventsToFrame(RichTextArea rta, Element e, boolean isAutoHeight)/*-{
        var d = e.contentWindow.document;
        
        var f = function(){
        		rta.@com.aavu.client.widget.RichText.RichTextArea::updateToolButtonStates()();
            };
	    if(document.all){
	        d.attachEvent("onkeydown", f);
	        d.attachEvent("onkeypress", f);
	        d.attachEvent("onmousedown", f);
	        d.attachEvent("onmouseup", f);
	    }else{
	        d.addEventListener("keydown", f, false);
	        d.addEventListener("keypress", f, false);
	        d.addEventListener("mousedown", f, false);
	        d.addEventListener("mouseup", f, false);
	    }
	    
	    if(isAutoHeight){
	    	var resizeF = function(){
	    		rta.@com.aavu.client.widget.RichText.RichTextArea::onDisplayChanged()();
	    	};
		    if(document.all){
		        d.attachEvent("onkeyup", resizeF);
		    }else{
		        d.addEventListener("keyup", resizeF, false);
		    }
		}
	    
	}-*/;
	private native Object queryCommandValue(Element e, String cmd)/*-{
		try{
			var v = e.contentWindow.document.queryCommandValue(cmd);
			return (v==null?null:v.toString());
		}catch(err){
			//alert("query '" + cmd + "' error.  " + err.message);
			return null;
		}
	}-*/;
	public void setSelectionRange(Object range){
		nativeSetSelectionRange(editor.getElement(), range);
	}
	private native void nativeSetSelectionRange(Element e, Object range) /*-{
	    if(range == null){
	        return;
	    }
	    if(document.all){
	        range.select();
	    }else{
			var sel = e.contentWindow.getSelection();
			sel.removeAllRanges();
			sel.addRange(range);
	    }
	}-*/;
	public Object getSelectionRange(){
		return nativeGetSelectionRange(editor.getElement());
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
	public void execCommand(String command, String value){
		nativeExecCommand(editor.getElement(), command, value);
		//maybe this action effect the height of the frame, fire the onDisplayChanged event.
		onDisplayChanged();
	}
    private native void nativeExecCommand(Element editor, String command, String value)/*-{
		try{
		    editor.contentWindow.focus();
		    if(command.toLowerCase() == "backcolor"){
		        if(!document.all){
		        	//mozilla based brower.
		            try{
		                editor.contentWindow.document.execCommand("styleWithCSS", false, true);
		            }catch(exception){
		                editor.contentWindow.document.execCommand("useCSS", false, false);
		            }
		            command = "hilitecolor";
		        }
		    }
		    editor.contentWindow.document.execCommand(command, false, value);
		}catch(e){
			//alert("error occured while execute command '" + command + "' value='" + value + "'\n" + e.message);
		}
	}-*/;
	
	public void addLink(){
		nativeAddLink(this, editor.getElement(), "Please enter a URL:");
		//maybe this action effect the height of the frame, fire the onDisplayChanged event.
		onDisplayChanged();
	}
	private native void nativeAddLink(RichTextArea rta, Element editor, String promptMsg) /*-{
		try{
			var range = rta.@com.aavu.client.widget.RichText.RichTextArea::getSelectionRange()();
			var url = window.prompt(promptMsg, "http://");
			editor.contentWindow.focus();
			if(url != null && url!=""){
				rta.@com.aavu.client.widget.RichText.RichTextArea::setSelectionRange(Ljava/lang/Object;)(range);
				var selectedText;
				if(document.all){
					selectedText = range.text;
				}else{
					selectedText = range.toString();
				}
				if(selectedText == ""){
					//no selection.
					var html = "<a href=\"" + url + "\">" + url + "</a>";
					rta.@com.aavu.client.widget.RichText.RichTextArea::insertHTML(Ljava/lang/String;)(html);
				}else{
					editor.contentWindow.document.execCommand("createlink", false, url);
				}
			}
		}catch(e){
			//alert("error occured while create link, please contact with vendor.\n" + e.message);
		}
	}-*/;
	
	public void insertHTML(String html){
		nativeInsertHTML(editor.getElement(), html);
		//maybe this action effect the height of the frame, fire the onDisplayChanged event.
		onDisplayChanged();
	}
	private native void nativeInsertHTML(Element editor, String html) /*-{
		try{
			editor.contentWindow.focus();
			if(document.all){
				//ie: use the pasteHTML method of the textRange object, for it didnot supply the inserthml command
				var insertRange = editor.contentWindow.document.selection.createRange();
				insertRange.select();
				insertRange.pasteHTML(html);
				insertRange.collapse(true);
			}else{
				editor.contentWindow.document.execCommand("inserthtml", false, html);
			}
		}catch(e){
			//alert("error occured while insert html, please contact with vendor. \n" + e.message);
		}
	}-*/;
	
	public void updateToolButtonStates(){
		for(Iterator it = toolButtons.iterator(); it.hasNext();){
			Object o = it.next();
			if(o instanceof SwitchableToolButton){
				
				Object value = null;
				if(o instanceof CommandToolButton){
					String cmd = ((CommandToolButton) o).getCommand();
					if(cmd != null){
						value = queryCommandValue(editor.getElement(), cmd);
					}
				}
				((SwitchableToolButton) o).updateStates(value);
			}
		}
	}
	/**
	 * fired when the editing content changed and maybe effect the height or the frame.
	 *
	 */
    public void onDisplayChanged(){
    	if(isAuotoResizeHeight()){
    		nativeAdjustFrameSize(editor.getElement());
    	}
    }
    private native void nativeAdjustFrameSize(Element editor)/*-{
    	try{
		    if (editor.contentDocument && editor.contentDocument.body.offsetHeight) {
		        // W3C DOM (and Mozilla) syntax
		        editor.height = editor.contentDocument.body.offsetHeight+16;
		    } else if (editor.Document && editor.Document.body.scrollHeight) {
		        // IE DOM syntax
		        editor.height = editor.Document.body.scrollHeight + 5;
		    }
	    }catch(e){
	    	//alert("error occured while adjust frame size. \n" + e.message);
	    }
	    try{
	    	editor.contentWindow.document.body.style.margin="0px";
	    }catch(e){
	    	//alert("error occured while change frame body margin. \n" + e.message);
	    }
    }-*/;
    
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
//	    	alert("error occured while adjust whether frame is loaded.\n" + e.message);
	    	return false;
	    }
    }-*/;
    private native void nativeSetHtml(Element editor, String html) /*-{
		editor.contentWindow.document.body.innerHTML = html;
    }-*/; 
    private native String nativeGetHtml(Element editor) /*-{
    	return editor.contentWindow.document.body.innerHTML;
    }-*/; 
}