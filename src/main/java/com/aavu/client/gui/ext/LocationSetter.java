package com.aavu.client.gui.ext;

/**
 * Used in conjuction with DefaultGInternalFrameHippoExt
 * to get around some assumptions made by gwm DefaultGInternalFrame
 * that we're always using a Desktop.
 * 
 * @author Jeff Dwyer
 *
 */
public interface LocationSetter {

	void setLocation(DefaultGInternalFrameHippoExt ext, int left, int top);

}
