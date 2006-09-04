/*
 * Created on 2006/07/27 19:33:51 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

import com.google.gwt.user.client.Element;

public class QuoteToolButton extends ImageToolButton {
	private static final String QUOTESTART = "<blockquote style=\"border-left:2px solid #7D7D7D; padding: 5px;\">";
	private static final String QUOTEEND = "</blockquote>";
	
	public QuoteToolButton(String id, String imgUrl, String toolTip) {
		super(id, imgUrl, toolTip, null);
	}
	
	protected void onClick(){
		setQuoteText(this.getRichTextArea().getEditorFrame().getElement(), QUOTESTART, QUOTEEND);
	}
	
	private native void setQuoteText(Element e, String starQuoteHtml, String endQuoteHtml)/*-{
	    var win = e.contentWindow;
	    var p;
	    var range;
	    try {
	        if (document.all) {
	            range = win.document.selection.createRange();
	            var type = win.document.selection.type;
	            p = range.parentElement();
	        } else {
	            var sel = win.getSelection();
	            range = sel.getRangeAt(0);
	            p = range.commonAncestorContainer;
	        }
	    }catch (e) {
	        //alert("error: " + e.message);
	        return;
	    }
	
	    if (document.all && type != 'Control') {
	        try {
	            var txtrange = win.document.body.createTextRange();
	            txtrange.moveToElementText(p);
	            range.setEndPoint("StartToStart", txtrange);
	            range.setEndPoint("EndToEnd", txtrange);
	            range.select();
	        }
	        catch (e) {
	            return;
	        }
	    }else{
	        var range1 = win.document.createRange();
	        range1.selectNode(p);
	        sel.removeAllRanges();
	        sel.addRange(range1);
	        range = sel.getRangeAt(0);
	    }
	
	
	    if (document.all) {
	        var html = p.innerHTML;
	        html = starQuoteHtml + html + endQuoteHtml;
	        range.pasteHTML(html);
	        range.collapse(false);
	        range.select();
	    }else {
	        var blockquote = win.document.createElement('blockquote');
	        blockquote.style.borderLeft = "2px solid #7D7D7D";
	        blockquote.style.padding = "3px";
	
	        if (p.tagName && p.tagName.toLowerCase() == 'body') {
	            p.innerHTML = starQuoteHtml + p.innerHTML + endQuoteHtml;
	        }else {
	            var parent = p.parentNode;
	            parent.insertBefore(blockquote, p);
	            parent.removeChild(p);
	            blockquote.appendChild(p);
	        }
	        range.selectNode(blockquote);
	    } 
	}-*/;

}
