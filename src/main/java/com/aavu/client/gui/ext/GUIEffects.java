package com.aavu.client.gui.ext;

import com.aavu.client.gui.glossary.SimpleTopicDisplay;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public class GUIEffects {

	private static GUIEffects singleton = new GUIEffects();


	/**
	 * Utility to fade & remove a widget after a short time using 2 timers
	 * 
	 * @param w
	 * @param i
	 */
	public static void fadeAndRemove(final Widget w, int fadeInX, int removeInX) {
		Timer t = new Timer() {
			public void run() {
				fade(w);
			}
		};
		t.schedule(fadeInX);
		removeInXMilSecs(w, removeInX);
	}


	public static void fadeAndRemove(Widget w, int removeInX) {
		fade(w);

		/*
		 * java.lang.NullPointerException: null at
		 * com.google.gwt.dev.shell.ie.ModuleSpaceIE6.invokeNative(ModuleSpaceIE6.java:377) at
		 * com.google.gwt.dev.shell.ie.ModuleSpaceIE6.invokeNativeVoid(ModuleSpaceIE6.java:283) at
		 * com.google.gwt.dev.shell.JavaScriptHost.invokeNativeVoid(JavaScriptHost.java:127) at
		 * org.gwtwidgets.client.wrap.Effect.fade(Effect.java:152) at
		 * org.gwtwidgets.client.wrap.Effect.fade(Effect.java:149) at
		 * com.aavu.client.gui.ext.GUIEffects.fadeAndRemove(GUIEffects.java:30)
		 */
		// Effect.fade(w,new EffectOption[] {
		// new EffectOption("duration",3.0),
		// new EffectOption("from",.8),
		// new EffectOption("to",0.0)});

		removeInXMilSecs(w, removeInX);
	}

	public static void fade(Widget w) {
		opacity(w, 1.0, 0.0, 500);
	}

	/**
	 * Utility to remove a widget after a short time, for instance after we Effect.fade()
	 * 
	 * @param w
	 * @param i
	 */
	public static void removeInXMilSecs(final Widget w, int i) {
		Timer t = new Timer() {
			public void run() {
				w.removeFromParent();
			}
		};
		t.schedule(i);
	}


	/**
	 * options are not safe in Hosted GWT
	 * 
	 * @param toMove
	 * @param i
	 * @param x
	 * @param cloud_move_sec
	 */
	public static void move(Widget toMove, int x, int y, int duration) {

		int steps = duration / MoveTimer.FREQ;

		MoveTimer mover = singleton.new MoveTimer(toMove, x, y, steps);

		mover.schedule(100);

	}


	/**
	 * options are not safe in Hosted GWT
	 * 
	 * @param toAppear
	 * @param duration
	 */
	public static void appear(Widget toAppear, int duration) {
		opacity(toAppear, .1, 1.0, duration);
	}

	public static void opacity(Widget widget, double from, double to, int duration) {

		int steps = duration / OpacityTimer.FREQ;

		OpacityTimer opacity = singleton.new OpacityTimer(widget, from, to, steps);

		opacity.schedule(100);

	}

	private class OpacityTimer extends Timer {
		public static final int FREQ = 100;

		private Element element;
		private double delta;

		private double cur;
		private double from;
		private double to;

		private int steps;

		private int curStep;

		public OpacityTimer(Widget widget, double from, double to, int steps) {
			this.element = widget.getElement();
			this.delta = (to - from) / steps;
			this.from = from;
			this.to = to;
			this.steps = steps;
			cur = from;
			curStep = 0;
		}

		public void run() {

			// TODO strBuff
			String ieStr = "alpha(opacity = " + (int) (cur * 100) + ")";
			// System.out.println("cur "+cur+" "+from+" "+to+" "+delta+" "+ieStr);
			DOM.setStyleAttribute(element, "filter", ieStr);
			DOM.setStyleAttribute(element, "-moz-opacity", cur + "");
			DOM.setStyleAttribute(element, "opacity", cur + "");
			DOM.setStyleAttribute(element, "-khtml-opacity", cur + "");

			cur += delta;
			curStep++;
			if (curStep > steps) {
				cancel();
			} else {
				schedule(FREQ);
			}
		}
	}

	private class MoveTimer extends Timer {
		public static final int FREQ = 100;

		private Element element;

		private int curX;
		private int curY;
		private int curStep = 0;

		private int dx;
		private int dy;

		private int steps;

		public MoveTimer(Widget widget, int x, int y, int steps) {
			this.element = widget.getElement();
			this.dx = x / steps;
			this.dy = y / steps;
			this.steps = steps;
			this.curX = DOM.getIntStyleAttribute(element, "left");
			this.curY = DOM.getIntStyleAttribute(element, "top");
		}

		public void run() {

			DOM.setIntStyleAttribute(element, "left", curX);
			DOM.setIntStyleAttribute(element, "top", curY);

			curX += dx;
			curY += dy;
			curStep++;

			if (curStep > steps) {
				cancel();
			} else {
				schedule(FREQ);
			}

		}
	}


	public static native void close() /*-{
		$wnd.close();
	}-*/;


	public static void highlight(SimpleTopicDisplay display) {
		// Effect.highlight(display);
	}


}
