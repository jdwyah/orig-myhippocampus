package com.aavu.client.gui.ocean.dhtmlIslands;

import com.aavu.client.Interactive;
import com.aavu.client.util.PsuedoRandom;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

public class ImageHolder {


	
	private final Type TYPE_30_PURPLE = new Type(30,30,Type.SPACING_30,Type.SPACING_30,"30px_Purple",4);
	private final Type TYPE_60_PURPLE = new Type(60,60,Type.SPACING_60,Type.SPACING_60,"60px_Purple",1);	
	private final Type TYPE_100_PURPLE =  new Type(100,100,Type.SPACING_100,Type.SPACING_100,"100px_Purple",2);
	private final Type TYPE_120_PURPLE =  new Type(120,120,Type.SPACING_120,Type.SPACING_120,"120px_Purple",3);

	private final Type TYPE_30_GREEN = new Type(30,30,Type.SPACING_30,Type.SPACING_30,"30px_Green",3);
	private final Type TYPE_60_GREEN = new Type(60,60,Type.SPACING_60,Type.SPACING_60,"60px_Green",4);	
	private final Type TYPE_120_GREEN =  new Type(120,120,Type.SPACING_120,Type.SPACING_120,"120px_Green",2);
	
	private final Type TYPE_30_ORANGE = new Type(30,30,Type.SPACING_30,Type.SPACING_30,"30px_Orange",3);
	private final Type TYPE_60_ORANGE = new Type(60,60,Type.SPACING_60,Type.SPACING_60,"60px_Orange",1);
	private final Type TYPE_120_ORANGE =  new Type(120,120,Type.SPACING_120,Type.SPACING_120,"120px_Orange",2);
	

//a nice idea, but we really can't scale these images
//	private static IslandImages islandImages;
//	
//	static {
//		islandImages = (IslandImages) GWT.create(IslandImages.class);
//	}

	/**
	 * Return the proper image 
	 *  
	 * @param size
	 * @param id
	 * @param random
	 * @param extension
	 * @return
	 */
	public Image getImage(AcreSize size,long id,PsuedoRandom random, String extension){
		
		Type type = null;
		if(size == AcreSize.SIZE_30){
			switch ((int)id%3) {
			case 0:
				type = TYPE_30_PURPLE;
				break;
			case 1:
				type = TYPE_30_ORANGE;
				break;
			case 2:
				type = TYPE_30_GREEN;
				break;			
			}
			
		}else if(size == AcreSize.SIZE_60){
			switch ((int)id%3) {
			case 0:
				type = TYPE_60_PURPLE;
				break;
			case 1:
				type = TYPE_60_ORANGE;
				break;
			case 2:
				type = TYPE_60_GREEN;
				break;			
			}
		}else if(size == AcreSize.SIZE_100){
			type = TYPE_100_PURPLE;	
			System.out.println("FAIIIIIIIIIIILL  NO 100s");
		}	
		else if(size == AcreSize.SIZE_120){
			switch ((int)id%3) {
			case 0:
				type = TYPE_120_PURPLE;
				break;
			case 1:
				type = TYPE_120_ORANGE;
				break;
			case 2:
				type = TYPE_120_GREEN;
				break;			
			}
		}	
		
		/*
		 * avoid 50*50 = 0 by adding in id
		 */		
		
//		Image i = islandImages.island_s().createImage();
//		i.setPixelSize(100, 100);
//		return i;//slandImages.island_s().createImage();
		
System.out.println(getImgLoc()+"type"+type.prefix+"_"+(1+random.nextInt(type.numImages))+"_"+extension+".png");		
		//return new Image(OceanDHTMLImpl.IMG_LOC+"type"+type.prefix+"_"+(1+(id+(x-5)*y)%type.numImages)+"_"+extension+".png");
		return new Image(getImgLoc()+"type"+type.prefix+"_"+(1+random.nextInt(type.numImages))+"_"+extension+".png");
	}

	
	//static final String IMG_LOC = "file://C:/workspace/RealHippo/src/main/webapp/img/simplicity/";	

	public static String getImgLoc(){
		return getImgLoc("simplicity/");
	}
	/**
	 * PEND LOW relative ../../../../webapp/ doesn't seem to work.
	 * 
	 * Moved these out of public to the basic web tier. This makes life easier for
	 * multiple modules, loading in other contexts etc, with the only downside being
	 * that hosted mode needs this file:// url
	 * 
	 * Note, only the images used for backgrounds or being scaled should be calling this.
	 * All other images should be in ImageBundles, which will get loaded correctly by the.
	 * 
	 * @param img_postfix
	 * @return
	 */
	public static String getImgLoc(String img_postfix){
		if(GWT.isScript()){
			return Interactive.getRelativeURL("img/"+img_postfix);
		}else{
			return "file://C:/workspace/RealHippo/src/main/webapp/img/"+img_postfix;			
		}
		
	}

	
}
