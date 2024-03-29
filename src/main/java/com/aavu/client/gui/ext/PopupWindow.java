package com.aavu.client.gui.ext;

import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameListener;
import org.gwm.client.impl.DefaultGFrame;

import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupWindow implements CloseListener {

	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;


	protected GInternalFrame frame;

	public PopupWindow(GInternalFrame frame, String title) {
		this(frame, title, false, WIDTH, HEIGHT);
	}

	public PopupWindow(GInternalFrame frame, String title, boolean noHeightSet) {
		this(frame, title, false, -1, -1);
	}

	public PopupWindow(GInternalFrame frame, String title, int width, int height) {
		this(frame, title, false, width, height);
	}

	public PopupWindow(GInternalFrame frame, String title, boolean modal, int width, int height) {

		this.frame = frame;


		// This must be called before anything else.
		// NOTE: don't forget to add the css file to the html.

		frame.setTheme("alphacube");

		if (width != -1) {
			frame.setWidth(width);
			frame.setHeight(height);

			int left = Window.getClientWidth() / 2 - width / 2;
			int top = Window.getClientHeight() / 2 - height / 2;

			// System.out.println("\n\nPopupWindow W " + left + " H " + top + " " +
			// Window.getClientWidth() + " "
			// + Window.getClientHeight());

			left = (left < 100) ? 100 : left;
			top = (top < 100) ? 100 : top;
			// System.out.println("PopupWindow.SET LOCATION " + left + " " + top);
			frame.setLocation(top, left);
		}

		frame.setMinimizable(false);
		frame.setMaximizable(true);
		frame.setDraggable(true);
		((DefaultGFrame) frame).setEffects(false);


		frame.setVisible(true);

		// This end up with windows where the title bar is hidden/undraggable
		// frame.showCenter(false);

		frame.setCaption(title);


		// frame.setDestroyOnClose();
	}

	public void setCaption(String title) {
		frame.setCaption(title);
	}

	protected void setContent(Widget w) {
		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
		hp.addStyleName("H-FullSize");
		hp.add(w);

		setCenteredContent(hp);
		// SimplePanel outerPanel = new SimplePanel();
		// outerPanel.setStyleName("H-FullDiv");
		// outerPanel.add(w);
		// setCenteredContent(outerPanel);
	}

	protected void setContentWithProblematicSize(Widget w, int width, int height) {
		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
		hp.addStyleName("H-FullSize");
		hp.add(w);
		hp.setPixelSize(width, height);
		setCenteredContent(hp);
		// SimplePanel outerPanel = new SimplePanel();
		// outerPanel.setStyleName("H-FullDiv");
		// outerPanel.add(w);
		// setCenteredContent(outerPanel);
	}

	protected void setCenteredContent(Widget w) {
		frame.setContent(w);
	}

	public void close() {
		// try {
		// throw new Exception();
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		System.out.println("PopupWindow close()");
		try {
			frame.close();
		} catch (Exception e) {
			System.out.println("CAUGHT frame.destroy() exception in PopupWindow.close()");
		}
	}

	public void hide() {
		System.out.println("PopupWindow hide()");
		try {
			frame.minimize();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("PopupWindow hide on hidden");
		}
	}

	public void addInternalFrameListener(GFrameListener listener) {
		frame.addFrameListener(listener);
	}

	public void show() {
		frame.restore();
		frame.setVisible(true);
	}

}
