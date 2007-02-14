package com.aavu.client.gui.dhtmlIslands;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.domain.TagInfo;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;

public class AbstractIsland extends AbsolutePanel {

	protected static ImageHolder imgHolder = new ImageHolder();
	protected static final int GRID = 30;


	protected double scale = 1;


	protected IslandRepresentation repr;
	protected TagInfo tagStat;
	protected int top;
	protected int left;

	protected int my_spacing;
	protected ClickListener listener;

	
	protected int theSize;
	
	protected Map levels = new HashMap();
	protected int img_size;


	/**
	 * 
	 *
	 */
	protected void setTypeAndSpacing() {
		if(theSize >= 16){
			my_spacing = Type.SPACING_30;//NOTE not using 100's
			img_size = Type.SIZE_30;
		}
		else if(theSize >= 4){
			my_spacing = Type.SPACING_30;
			img_size = Type.SIZE_30;
		}
		else{
			my_spacing = Type.SPACING_30;
			img_size = Type.SIZE_30;
		}
		img_size *= scale;
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
		
		for (int x = 0; x < GRID; x++) {
			for (int j = 0; j < GRID; j++) {
				doIslandType(style, x, j);
			}
		}
	}
	protected void doIslandType(int style,int x,int y) {
		AcreSize acreSize = null;

		int value = repr.get(x,y);
		if(value > -1){
			if(value > repr.bigs + repr.meds){
				acreSize = AcreSize.SIZE_30;												
			}else if(value > repr.bigs){
				acreSize = AcreSize.SIZE_60;												
			}
			else{
				acreSize = AcreSize.SIZE_100;												
			}
			Logger.debug("used "+x+" "+y+" "+value+" Acre: "+acreSize.getSize());
			//count++;

			if(0 == style){
				addShadow(x,y,acreSize);
			}
			else if(1 == style){
				addAcre(x,y,acreSize);
			}
			else if(2 == style){
				addInner(x,y,acreSize);
			}
		}
	}

	/**
	 * 
	 * 
	 * @param level
	 * @param corrected_x
	 * @param corrected_y
	 * @param doInsert this will determine whether we inert or add. insert if you want to be covered
	 */
	private void addLevel(Level level, int corrected_x, int corrected_y,boolean doInsert) {
		
		//TODO doesn't appear to have the affect we wanted
		if(doInsert){
			insert(level,getElement(),0);
			setWidgetPosition(level, corrected_x, corrected_y);
		}else{
			add(level,corrected_x,corrected_y);
		}
		
		levels.put(level,new PointLocation(corrected_x,corrected_y));
		
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


		addLevel(new Acre(listener,x,y,acreSize),corrected_x,corrected_y,true);

	}



	private void addShadow(int x, int y,AcreSize acreSize){

		int corrected_x = gridToRelativeX(x,my_spacing);
		int corrected_y = gridToRelativeY(y,my_spacing);		

		addLevel(new Shadow(x,y,acreSize),corrected_x,corrected_y,true);
	}
	private void addInner(int x, int y,AcreSize acreSize){

		int corrected_x = gridToRelativeX(x,my_spacing);
		int corrected_y = gridToRelativeY(y,my_spacing);		

		addLevel(new Inner(x,y,acreSize),corrected_x,corrected_y,false);
	}

	
	private class Acre extends Level {
		public Acre(ClickListener listener,int x, int y,AcreSize acreSize){
			super(imgHolder,tagStat,listener,x,y,acreSize,"I","Isle");			
		}
	}
	private class Shadow extends Level {
		public Shadow(int x, int y,AcreSize acreSize){
			super(imgHolder,tagStat,null,x,y,acreSize,"S","Overlay");			
		}
	}
	private class Inner extends Level {
		public Inner(int x, int y,AcreSize acreSize){
			super(imgHolder,tagStat,null,x,y,acreSize,"Inner","Overlay");			
		}
	}


	public int getLeft() {
		return left;
	}
	public int getTop() {
		return top;
	}

}
