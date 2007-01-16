package com.aavu.client.gui.ext;

import org.gwtwidgets.client.wrap.Effect;
import org.gwtwidgets.client.wrap.EffectOption;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class GUIEffects {

	/**
	 * Utility to fade & remove a widget after a short time using 2 timers
	 * 
	 * @param w
	 * @param i
	 */
	public static void fadeAndRemove(final Widget w, int fadeInX,int removeInX){		
		Timer t = new Timer() {
		      public void run() {
		    	  Effect.fade(w);
		      }
		    };
		t.schedule(fadeInX);		
		removeInXMilSecs(w,removeInX);
	}
	

	public static void fadeAndRemove(Widget w, int removeInX) {
		Effect.fade(w);
		/*
		 * java.lang.NullPointerException: null
	at com.google.gwt.dev.shell.ie.ModuleSpaceIE6.invokeNative(ModuleSpaceIE6.java:377)
	at com.google.gwt.dev.shell.ie.ModuleSpaceIE6.invokeNativeVoid(ModuleSpaceIE6.java:283)
	at com.google.gwt.dev.shell.JavaScriptHost.invokeNativeVoid(JavaScriptHost.java:127)
	at org.gwtwidgets.client.wrap.Effect.fade(Effect.java:152)
	at org.gwtwidgets.client.wrap.Effect.fade(Effect.java:149)
	at com.aavu.client.gui.ext.GUIEffects.fadeAndRemove(GUIEffects.java:30)
		 */
//		Effect.fade(w,new EffectOption[] {
//				new EffectOption("duration",3.0),
//				new EffectOption("from",.8),
//				new EffectOption("to",0.0)});
		
		
		removeInXMilSecs(w, removeInX);
	}
	
	/**
	 * Utility to remove a widget after a short time,
	 * for instance after we Effect.fade()
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
	 * @param toMove
	 * @param options
	 * @param x
	 * @param y
	 */
	public static void move(Widget toMove, EffectOption[] options, int x, int y) {
		if(GWT.isScript()){			
			Effect.move(toMove, options);			
		}else{			
			Effect.moveBy(toMove, y,x);
		}
	}
	/**
	 * options are not safe in Hosted GWT
	 * @param toAppear
	 * @param duration
	 */
	public static void appear(Widget toAppear, int duration) {
		if(GWT.isScript()){			
			Effect.appear(toAppear, new EffectOption[] {
					new EffectOption("duration",duration)});			
		}else{			
			Effect.appear(toAppear);
		}
	}
	
}
