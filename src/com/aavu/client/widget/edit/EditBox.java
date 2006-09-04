package com.aavu.client.widget.edit;


import java.util.Iterator;

import com.aavu.client.widget.RichText.CommandToolButton;
import com.aavu.client.widget.RichText.SwitchableToolButton;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditBox extends Composite {

	private Frame editor = new Frame();
	private VerticalPanel panel;
	private boolean inited = false;
	private boolean auotoResizeHeight = true;
	private String html;

	public EditBox(){

		panel = new VerticalPanel();
		panel.add(editor);
		initWidget(panel);



	}


	protected void onLoad() {
		super.onLoad();
		if(!inited){
			System.out.println("111");
			if(isAuotoResizeHeight()){
				DOM.setAttribute(editor.getElement(), "scrolling", "no");
			}else{
				DOM.setAttribute(editor.getElement(), "scrolling", "auto");
			}
			Timer t = new Timer(){
				public void run(){
					System.out.println("222");
					initFrame();
				}
			};
			t.schedule(1000);
			inited = true;
		}
	}
	private void initFrame(){
		System.out.println("333");
		if (isFrameLoaded(editor.getElement())){
			//if frame is loaded, then set it to editor
			setDesignMode(editor.getElement(), true);
			final EditBox rta = this;
			attachEventsToFrame(rta, editor.getElement(), isAuotoResizeHeight());
			Window.alert("initFrame, will set html to " + this.html);		  		
			if(this.html != null){
				setHtmlToFrame();
//				nativeSetHtml(editor.getElement(), this.html);
//				onDisplayChanged();
			}else{
				//setHTML will fire the onDisplay event, donot need fire again, so that
				//this statement was written in the 'else'.
				onDisplayChanged();
			}
		}else{
			Timer t = new Timer() {
				public void run() {
					System.out.println("initFrame() 2nd");
					initFrame();
				}
			};
			t.schedule(500);
		}

	}
	public void setHTML(String html){
		this.html = html;
		if(inited){
			//if not inited, the initFrame method will set the HTMLs.
			setHtmlToFrame();
		}
	}
	public String getHTML(){
		if(editor.isAttached()){
			return nativeGetHtml(editor.getElement());
		}else{
			return html;
		}
	}
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
	
	public void updateToolButtonStates(){
		
		System.out.println("ffff");
		//value = queryCommandValue(editor.getElement(), cmd);
		
//		for(Iterator it = toolButtons.iterator(); it.hasNext();){
//			Object o = it.next();
//			if(o instanceof SwitchableToolButton){
//				
//				Object value = null;
//				if(o instanceof CommandToolButton){
//					String cmd = ((CommandToolButton) o).getCommand();
//					if(cmd != null){
//						value = queryCommandValue(editor.getElement(), cmd);
//					}
//				}
//				((SwitchableToolButton) o).updateStates(value);
//			}
//		}
	}
	private native Object queryCommandValue(Element e, String cmd)/*-{
	try{
		var v = e.contentWindow.document.queryCommandValue(cmd);
		return (v==null?null:v.toString());
	}catch(err){
		//alert("query '" + cmd + "' error.  " + err.message);
		return null;
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
	private native void setDesignMode(Element e, boolean on)/*-{
	if(on){
    	e.contentWindow.document.designMode = "on";
	        e.contentWindow.focus();
    }else{
    	e.contentWindow.document.designMode = "off";
    }
}-*/;
	private native void attachEventsToFrame(EditBox rta, Element e, boolean isAutoHeight)/*-{
    var d = e.contentWindow.document;

    var f = function(){
    		rta.@com.aavu.client.widget.edit.EditBox::updateToolButtonStates()();
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
    		rta.@com.aavu.client.widget.edit.EditBox::onDisplayChanged()();
    	};
	    if(document.all){
	        d.attachEvent("onkeyup", resizeF);
	    }else{
	        d.addEventListener("keyup", resizeF, false);
	    }
	}

}-*/;

	public boolean isAuotoResizeHeight() {
		return auotoResizeHeight;
	}


	public void setAuotoResizeHeight(boolean auotoResizeHeight) {
		this.auotoResizeHeight = auotoResizeHeight;
	} 

}
