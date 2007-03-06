package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.domain.TagInfo;
import com.aavu.client.util.MiddleSquarePseudoRandom;
import com.aavu.client.util.PsuedoRandom;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;

public class Level extends AbsolutePanel {		
		
		private Image isle;
		private int baseSize;
		private int scalar;
		private int y;
		private int x;
		
		public Level(ImageHolder imgHolder, TagInfo tagStat,ClickListener listener, int x, int y,AcreSize acreSize,String extension,String style){

			this.x = x;
			this.y = y;
			
//			if(tagStat.getTagName().equals("GWT")){
//				System.out.println(" tag "+tagStat.getTagId()+" "+x+" "+y);
//				PsuedoRandom rG = new MiddleSquarePseudoRandom(tagStat.getTagId()*x*y,4);
//				System.out.println("rg "+rG.nextInt(3)+" "+rG.nextInt(3)+" "+rG.nextInt(3));
//				
//			}
			
			PsuedoRandom randomGen = new MiddleSquarePseudoRandom(tagStat.getTagId()*x*y,4);
			
			isle = imgHolder.getImage(acreSize,tagStat.getTagId(),randomGen,extension);//new Image(OceanDHTMLImpl.IMG_LOC+"type"+type.prefix+"_"+(1+(x*y)%type.numImages)+"_"+extension+".png");
			if(listener != null){
				isle.addClickListener(listener);
			}
			isle.setStyleName(style);
			
			baseSize = acreSize.getSize();
			scalar = acreSize.getScalar();
			
			setToScale(1);
			
			add(isle,0,0);
			
			disableSelect(isle.getElement());
		}
		
		/**
		 * Only the 'firefox route' is required. Without it, dragging topics on 
		 * island is a mess because it tries to highligh the acre images.
		 * 
		 * @param target
		 */
		public native void disableSelect(Element target) /*-{
	   		if (typeof target.onselectstart!="undefined") //IE route
				target.onselectstart=function(){return false}
			else if (typeof target.style.MozUserSelect!="undefined") //Firefox route
				target.style.MozUserSelect="none"
			else //All other route (ie: Opera)
				target.onmousedown=function(){return false}
			//target.style.cursor = "default"			
	  	}-*/;
					

		/**
		 * PEND /4 
		 * 
		 * @param scale
		 */
		public void setToScale(double scale){
		
			isle.setPixelSize((int)(baseSize*scale/scalar), (int)(baseSize*scale/scalar));			
			
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
