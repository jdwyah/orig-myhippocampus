/*
 * Created on 2006/07/26 17:37:12 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;


public class ToolButtonList {

	public static ToolButton getButtonById(String id) {
		if (id.equals("bold")) {
			return (new ImageToolButton("bold", RichTextArea.IMGFOLDER+"bold.gif",
					"Bold", "bold"));
		} else if (id.equals("italic")) {
			return (new ImageToolButton("italic",
					RichTextArea.IMGFOLDER+"italic.gif", "Italic", "italic"));
		} else if (id.equals("underline")) {
			return (new ImageToolButton("underline",
					RichTextArea.IMGFOLDER+"underline.gif", "UnderLine", "underline"));
		} else if (id.equals("strike")) {
			return (new ImageToolButton("strike",
					RichTextArea.IMGFOLDER+"strike.gif", "Strike", "strikethrough"));
		} else if (id.equals("numbered-list")) {
			return (new ImageToolButton("numbered-list",
					RichTextArea.IMGFOLDER+"orderedlist.gif", "Numered list",
					"insertorderedlist"));
		} else if (id.equals("bulleted-list")) {
			return (new ImageToolButton("bulleted-list",
					RichTextArea.IMGFOLDER+"unorderedlist.gif", "Bulleted list",
					"insertunorderedlist"));
		} else if (id.equals("indent-more")) {
			return (new ImageToolButton("indent-more",
					RichTextArea.IMGFOLDER+"indent.gif", "Indent more", "indent"));
		} else if (id.equals("indent-less")) {
			return (new ImageToolButton("indent-less",
					RichTextArea.IMGFOLDER+"outdent.gif", "Indent less", "outdent"));
		} else if (id.equals("align-left")) {
			return (new ImageToolButton("align-left",
					RichTextArea.IMGFOLDER+"alignleft.gif", "Align left",
					"justifyleft"));
		} else if (id.equals("align-right")) {
			return (new ImageToolButton("align-right",
					RichTextArea.IMGFOLDER+"alignright.gif", "Align right",
					"justifyright"));
		} else if (id.equals("remove-formatting")) {
			return (new ImageToolButton("remove-formatting",
					RichTextArea.IMGFOLDER+"removeformat.gif", "Remove formatting",
					"removeformat"));
		} else if (id.equals("color")) {
			return (new ColorDropDownToolButton("color",
					RichTextArea.IMGFOLDER+"color.gif", "Font color", "ForeColor"));
		} else if (id.equals("background")) {
			return (new ColorDropDownToolButton("background",
					RichTextArea.IMGFOLDER+"backgroundcolor.gif", "Background color",
					"BackColor"));
		} else if (id.equals("quote")) {
			return (new QuoteToolButton("quote", RichTextArea.IMGFOLDER+"quote.gif",
					"Quote"));
		} else if (id.equals("link")) {
			return (new LinkToolButton("link", RichTextArea.IMGFOLDER+"link.gif",
					"Link"));
		} else if (id.equals("font")) {
			return (new FontToolButton("font", RichTextArea.IMGFOLDER+"font.gif",
					"Font", "fontname"));
		} else if (id.equals("font-size")) {
			return (new FontSizeToolButton("font-size",
					RichTextArea.IMGFOLDER+"fontsize.gif", "Font Size", "fontsize"));
		}else{
			return null;
		}
	}
}
