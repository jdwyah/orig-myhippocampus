package com.aavu.client.gui.ext;

import org.gwm.client.impl.DefaultGInternalFrame;

import com.aavu.client.gui.MainMap;

public class DefaultGInternalFrameHippoExt extends DefaultGInternalFrame {

	private LocationSetter map;

	public DefaultGInternalFrameHippoExt(String caption,LocationSetter map) {
        super(caption);
        this.map = map;
    }

	public void setLocation(int top, int left) {
		
		if (map != null) {
			if(top > 0){			
				map.setLocation(this, left,top);
				this.top = top;
				this.left = left;
			}
		}
		
	}
}
