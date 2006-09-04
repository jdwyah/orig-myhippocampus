package com.aavu.client.widget.RichText;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ComponentTest implements EntryPoint {
	private RichTextArea rta;

	private DialogBox dialog;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		rta = new RichTextArea();
		// rta.setAuotoResizeHeight(true);
		rta.setSize("600px", "380px");
		rta
				.setHtml("<b>Hello world</b><br><br><font size='1'>from Richtextarea</font>");
		RootPanel.get("slot1").add(rta);

		Button btn = new Button("Show HTML of the above Rich text area.");
		btn.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				Window.alert(rta.getHtml());
			}

		});
		RootPanel.get("slot2").add(btn);

		//----------rich text area on dialog panel.
		VerticalPanel vp = new VerticalPanel();
		vp.add(new RichTextArea());
		Button close = new Button("close");
		close.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				dialog.hide();
			}

		});
		vp.add(close);
		dialog = new MyDialog(vp);
		dialog.setText("Dialog Test");
		DOM.setStyleAttribute(dialog.getElement(), "border", "1px solid blue");
		Button btn2 = new Button("Show dialog with RichTextArea");
		btn2.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				dialog.show();
			}

		});
		
		RootPanel.get().add(btn2);

	}

}
class MyDialog extends DialogBox {

	    public MyDialog(Widget w) {
	      // Set the dialog box's caption.
	      setText("RichTextArea on Dialog");
	      setWidget(w);
	    }
}
