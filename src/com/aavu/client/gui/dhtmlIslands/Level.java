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
		
		public Level(ImageHolder imgHolder, TagInfo tagStat,ClickListener listener, int x, int y,AcreSize acreSize,String extension,String style){

			isle = imgHolder.getImage(acreSize,tagStat.getTagId(),x,y,extension);//new Image(OceanDHTMLImpl.IMG_LOC+"type"+type.prefix+"_"+(1+(x*y)%type.numImages)+"_"+extension+".png");
			if(listener != null){
				isle.addClickListener(listener);
			}
			isle.setStyleName(style);
			
			baseSize = acreSize.getSize();
			
			setToScale(1);
			
		
		}
		
		public void setToScale(double scale){
			isle.setPixelSize((int)(baseSize*scale), (int)(baseSize*scale));
			add(isle,0,0);
			
			DOM.setStyleAttribute(getElement(), "width", baseSize*scale+"px");
			DOM.setStyleAttribute(getElement(), "height", baseSize*scale+"px");
		}
		
	}
