package com.aavu.client.gui.ext;

import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameListener;
import org.gwm.client.impl.DefaultGFrame;

import com.aavu.client.gui.timeline.CloseListener;
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

		System.out.println("PopupWindow.setting theme !!!");
		// This must be called before anything else.
		// NOTE: don't forget to add the css file to the html.
		System.out.println("Z");
		frame.setTheme("alphacube");
		System.out.println("A..");
		if (width != -1) {
			frame.setWidth(width);
			frame.setHeight(height);

			int w = (Window.getClientWidth() - width) / 2;
			int h = Window.getClientHeight() / 3;

			// System.out.println("W "+w+" H "+h+" "+Window.getClientWidth()+"
			// "+Window.getClientHeight());

			w = (w < 100) ? 100 : w;
			h = (h < 100) ? 100 : h;
			frame.setLocation(w, h);
		}
		System.out.println("B");
		frame.setMinimizable(false);
		frame.setMaximizable(true);
		frame.setDraggable(true);
		((DefaultGFrame) frame).setEffects(false);

		System.out.println("set vis");
		frame.setVisible(true);
		System.out.println("set vis 2");
		// This end up with windows where the title bar is hidden/undraggable
		// frame.showCenter(false);

		frame.setCaption(title);


		// frame.setDestroyOnClose();
	}

	public void setTitle(String title) {
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
		frame.minimize();
	}

	public void addInternalFrameListener(GFrameListener listener) {
		frame.addFrameListener(listener);
	}

}
