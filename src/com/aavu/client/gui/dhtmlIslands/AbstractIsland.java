package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.domain.TagInfo;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;

public class AbstractIsland extends AbsolutePanel {

	protected static ImageHolder imgHolder = new ImageHolder();
	protected static final int GRID = 100;


	protected int scale = 1;


	protected IslandRepresentation repr;
	protected TagInfo tagStat;
	protected int top;
	protected int left;

	protected int my_spacing;
	protected ClickListener listener;

	
	protected int theSize;
	


	/**
	 * 
	 *
	 */
	protected void setTypeAndSpacing() {
		if(theSize >= 16){
			my_spacing = Type.SPACING_30;//NOTE not using 100's			
		}
		else if(theSize >= 4){
			my_spacing = Type.SPACING_30;
		}
		else{
			my_spacing = Type.SPACING_30;
		}
		my_spacing *= scale;
	}
	
	
	protected int gridToRelativeX(int gridValue,int spacing){		
		return (gridValue - repr.min_x)* spacing;		
	}
	protected int gridToRelativeY(int gridValue,int spacing){				
		return (gridValue - repr.min_y)* spacing;		
	}



	/**
	 * used[][] will be an array with value of the order in which we "found"
	 * this square. Idea is to have the big ones in the center, surrounded by med,
	 * then small. 
	 * 
	 * Would like to make it a bit smarter about laying out new little guys.
	 * 
	 * @param style
	 * @param panel 
	 */

	protected void doIslandType(int style) {
		int x;
		int count = 0;
		AcreSize acreSize = null;

		for (x = 0; x < GRID; x++) {
			for (int j = 0; j < GRID; j++) {
				int value = repr.get(x,j);
				if(value > -1){
					if(value > repr.bigs + repr.meds){
						acreSize = AcreSize.SIZE_30;												
					}else if(value > repr.bigs){
						acreSize = AcreSize.SIZE_60;												
					}
					else{
						acreSize = AcreSize.SIZE_100;												
					}
					Logger.debug("used "+x+" "+j+" "+value+" Acre: "+acreSize.getSize());
					count++;

					if(0 == style){
						addShadow(x,j,acreSize);
					}
					else if(1 == style){
						addAcre(x,j,acreSize);
					}
					else if(2 == style){
						addInner(x,j,acreSize);
					}
				}
			}
		}
	}


	/**
	 * takes values from -50 -> 50 (GRID/2)
	 * 
	 * gridToRelative using my_type || type? 
	 * 
	 */
	private void addAcre(int x, int y,AcreSize acreSize){

		int corrected_x = gridToRelativeX(x,my_spacing);
		int corrected_y = gridToRelativeY(y,my_spacing);		


		Logger.debug("x "+x+" cx "+corrected_x);
		Logger.debug("y "+y+" cy "+corrected_y);


		add(new Acre(listener,x,y,acreSize),corrected_x,corrected_y);

	}

	private void addShadow(int x, int y,AcreSize acreSize){

		int corrected_x = gridToRelativeX(x,my_spacing);
		int corrected_y = gridToRelativeY(y,my_spacing);		

		add(new Shadow(x,y,acreSize),corrected_x,corrected_y);
	}
	private void addInner(int x, int y,AcreSize acreSize){

		int corrected_x = gridToRelativeX(x,my_spacing);
		int corrected_y = gridToRelativeY(y,my_spacing);		

		add(new Inner(x,y,acreSize),corrected_x,corrected_y);
	}

	private class Level extends AbsolutePanel {		


		public Level(ClickListener listener, int x, int y,AcreSize acreSize,String extension,String style){

			Image isle = imgHolder.getImage(acreSize,tagStat.getTagId(),x,y,extension);//new Image(OceanDHTMLImpl.IMG_LOC+"type"+type.prefix+"_"+(1+(x*y)%type.numImages)+"_"+extension+".png");
			if(listener != null){
				isle.addClickListener(listener);
			}
			isle.setStyleName(style);
			isle.setPixelSize(acreSize.getSize()*scale, acreSize.getSize()*scale);
			add(isle,0,0);

			DOM.setStyleAttribute(getElement(), "width", acreSize.getSize()*scale+"px");
			DOM.setStyleAttribute(getElement(), "height", acreSize.getSize()*scale+"px");

		}
	}
	private class Acre extends Level {
		public Acre(ClickListener listener,int x, int y,AcreSize acreSize){
			super(listener,x,y,acreSize,"I","Isle");			
		}
	}
	private class Shadow extends Level {
		public Shadow(int x, int y,AcreSize acreSize){
			super(null,x,y,acreSize,"S","Overlay");			
		}
	}
	private class Inner extends Level {
		public Inner(int x, int y,AcreSize acreSize){
			super(null,x,y,acreSize,"Inner","Overlay");			
		}
	}


	public int getLeft() {
		return left;
	}
	public int getTop() {
		return top;
	}

}
