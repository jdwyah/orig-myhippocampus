/*
 * Created on 2006/07/27 23:45:12 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

/**
 * <p>when the cursor changed in the editor, the updateStatus method will
 * be called to tell the tool button that maybe states should be switched.</p>
 * <p>If tool button wanna switch state, for example, bold button on or off, button
 * sould implements this interface to get notified when tool button need update states</p>
 * 
 * @author David
 *
 */
public interface SwitchableToolButton {
	/**
	 * called when tool button need switch its states.
	 * whether the tool button swith its states should depend the newValue.
	 * @param newValue this value is from document.queryCommandState(ToolButton.getCommand())
	 */
	public void updateStates(Object newValue);
}
