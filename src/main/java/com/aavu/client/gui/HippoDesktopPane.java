package com.aavu.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.gwm.client.GDesktopPane;
import org.gwm.client.GFrame;
import org.gwm.client.GInternalFrame;
import org.gwm.client.impl.DefaultGFrame;
import org.gwm.client.impl.DefaultGInternalFrame;
import org.gwm.client.impl.SelectBoxManagerImpl;
import org.gwm.client.impl.SelectBoxManagerImplIE6;
import org.gwm.client.util.Gwm;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;


public abstract class  HippoDesktopPane extends Composite implements WindowResizeListener, GDesktopPane {

//	private AbsolutePanel frameContainer;

	//private FlexTable desktopWidget;
	//private IconBar buttonBar;

	private List frames;

	private GInternalFrame activeFrame;

	private String theme = Gwm.getDefaultTheme();

	public HippoDesktopPane() {
		initialize();
		theme = Gwm.getDefaultTheme();
		
		setupListeners();
		
	}

	private void initialize() {
		this.frames = new ArrayList();
	}

	private void setupListeners() {

	}

	public abstract LocationSettingWidget getFrame();
	public abstract void addButton(GFrame frame);

	public abstract void removeButton(GFrame frame);

	public void iconify(GFrame frame) {
		frame.setVisible(false);
		addButton(frame);
	}

	public void deIconify(GFrame frame) {
		removeButton(frame);
		frame.restore();
	}

	/**
	 * Add a GFrame to this GDesktopPane.
	 * 
	 * @param internalFrame
	 */
	public void addFrame(GInternalFrame internalFrame) {
		internalFrame.setDesktopPane(this);
		int spos = (frames.size() + 1) * 30;
		int left = getFrame().getAbsoluteLeft() + spos;
		int top = getFrame().getAbsoluteTop() + spos;
		SelectBoxManagerImpl selectBoxManager = ((DefaultGFrame) internalFrame)
		.getSelectBoxManager();
		if (selectBoxManager instanceof SelectBoxManagerImplIE6) {
			getFrame().add(selectBoxManager.getBlockerWidget(), left, top);
		}
		getFrame().add((Widget)internalFrame);
		internalFrame.setLocation(left, top);
		
		//NOTE needed to add this to get the windwos to pop ontop of the ocean
		DOM.setStyleAttribute(((DefaultGInternalFrame)internalFrame).getElement(), "position","absolute");
		
		frames.add(internalFrame);
		internalFrame.setTheme(theme);
	}


	public void removeFrame(GInternalFrame internalFrame) {
		getFrame().remove((Widget)internalFrame);
		SelectBoxManagerImpl selectBoxManager = ((DefaultGFrame) internalFrame)
		.getSelectBoxManager();
		if (selectBoxManager instanceof SelectBoxManagerImplIE6) {
			getFrame().remove(selectBoxManager.getBlockerWidget());
		}
		frames.remove(internalFrame);
		removeButton(internalFrame);

	}

	/**
	 * Closes all GInternalFrames contained in this GDesktopPane.
	 */
	public void closeAllFrames() {
		for (int i = 0; i < frames.size(); i++) {
			((GFrame) frames.get(i)).attemptClose();
		}
	}

	public List getAllFrames() {
		return frames;
	}

	public abstract void onWindowResized(int width, int height);


	public void addWidget(Widget widget, int left, int top) {
		getFrame().remove(widget);
		getFrame().add(widget);
		getFrame().setWidgetPosition(widget, left, top);
	}

	public void setWidgetPosition(Widget widget, int left, int top) {
		getFrame().setWidgetPosition(widget, left, top);
	}

	public Widget getFramesContainer() {
		return getFrame().getWidget();
	}

	public void setActivateFrame(GInternalFrame internalFrame) {
		activeFrame = internalFrame;
	}

	public GInternalFrame getActiveFrame() {
		return activeFrame;
	}

	public void setTheme(String theme) {
		this.theme = theme;
		for (int x = 0; x < frames.size(); x++) {
			GInternalFrame theFrame = (GInternalFrame) frames.get(x);
			theFrame.setTheme(theme);
		}
		//getFrame()setStyleName("gwm-"+ theme + "-GDesktopPane-FrameContainer");

//		desktopWidget.getFlexCellFormatter().setStyleName(1, 0,
//				"gwm-"+ theme + "-GDesktopPane-TaskBar");
		setStyleName("gwm-"+ theme + "-GDesktopPane");
	}

}
