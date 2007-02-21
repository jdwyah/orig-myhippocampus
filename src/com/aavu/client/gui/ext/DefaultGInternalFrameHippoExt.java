package com.aavu.client.gui.ext;

import org.gwm.client.impl.DefaultGInternalFrame;

import com.aavu.client.gui.MainMap;

public class DefaultGInternalFrameHippoExt extends DefaultGInternalFrame {

	private MainMap map;

	public DefaultGInternalFrameHippoExt(String caption,MainMap map) {
        super(caption);
        this.map = map;
    }

	public void setLocation(int top, int left) {
		
		
//		System.out.println("set location "+top+" "+left);		
//		if(top > 0){
//			try {
//				throw new Exception();
//			} catch (Exception e) {
//			
//				e.printStackTrace();
//			}
//		}
		
		if (map != null) {
			if(top > 0){			
				map.setLocation(this, left,top);
				this.top = top;
				this.left = left;
			}
		}
		
	}
}
