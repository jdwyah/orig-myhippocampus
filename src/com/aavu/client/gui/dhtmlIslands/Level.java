package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.TagStat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;

public class Level extends AbsolutePanel {		
		
		private Image isle;
		private int baseSize;
		private int y;
		private int x;
		
		public Level(ImageHolder imgHolder, TagInfo tagStat,ClickListener listener, int x, int y,AcreSize acreSize,String extension,String style){

			this.x = x;
			this.y = y;
			
			isle = imgHolder.getImage(acreSize,tagStat.getTagId(),x,y,extension);//new Image(OceanDHTMLImpl.IMG_LOC+"type"+type.prefix+"_"+(1+(x*y)%type.numImages)+"_"+extension+".png");
			if(listener != null){
				isle.addClickListener(listener);
			}
			isle.setStyleName(style);
			
			baseSize = acreSize.getSize();
			
			setToScale(1);
			
			add(isle,0,0);
		}
		
		public void setToScale(double scale){
		
			isle.setPixelSize((int)(baseSize*scale), (int)(baseSize*scale));			
			
			DOM.setStyleAttribute(getElement(), "width", baseSize*scale+"px");
			DOM.setStyleAttribute(getElement(), "height", baseSize*scale+"px");
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
		
	}
