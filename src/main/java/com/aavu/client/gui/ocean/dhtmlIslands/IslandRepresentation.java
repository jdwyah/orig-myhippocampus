package com.aavu.client.gui.ocean.dhtmlIslands;

import org.gwtwidgets.client.util.Location;

import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.User;
import com.aavu.client.util.MiddleSquarePseudoRandom;
import com.aavu.client.util.PsuedoRandom;

/**
 * Class that holds the "used[][]" so that we can share it between Closeup
 * and regular islands representations.
 * 
 * @author Jeff Dwyer
 *
 */
public class IslandRepresentation {

	int max_x = 0;
	int min_x = Integer.MAX_VALUE;
	int max_y = 0;
	int min_y = Integer.MAX_VALUE;

	private PsuedoRandom pseudoRandomGen;


	private int gridSize;
	int[][] used;

	private TagInfo tagStat;

	int bigs = 0;
	int meds = 0;
	int smalls = 0;
	private int theSize;



	public IslandRepresentation(int gridSize, TagInfo stat, User user) {

		this.tagStat = stat;
		this.gridSize = gridSize;

		used = new int[gridSize][gridSize];

		long seed = user.getId()+tagStat.getTagId();
		pseudoRandomGen = new MiddleSquarePseudoRandom(seed,4);

		theSize = tagStat.getNumberOfTopics()+1;

		doGrow();	

	}

	private void clearUseArray() {
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				used[i][j] = -1;
			}
		}
	}



	public PointLocation growByOne() {
		theSize++;			
		return doGrow();					
	}

	/**
	 * Creat an int[][] of the island's "used" graph. This is the framework upon
	 * which we'll draw the island.
	 * 
	 * TODO will crash if things go beyond bounds of tag GRID
	 * 
	 * @param i
	 */
	private PointLocation doGrow() {

		clearUseArray();
		pseudoRandomGen.reInit();	

		/*
		 * calculate here
		 */
//		bigs = theSize /16;
//		meds = (theSize %16)/4;
//		smalls = theSize %4;

		/*
		 * don't use 100's for now
		 */		
		bigs = 0;//theSize /16;
		meds = theSize / 4;
		smalls = theSize %4;

		bigs = 0;
		meds = 0;
		smalls = theSize;


		//System.out.println("grow "+theSize+" "+bigs+" "+meds+" "+smalls);
		int x = gridSize/2;			
		int y = gridSize/2;

		for (int j = 1; j < bigs + meds + smalls + 1; j++) {

			//x = gridSize/2;			
			//y = gridSize/2;

			//TODO take this out.. only to prevent loops if Von Neuman PRG explodes
			int c = 0;
			while(-1 != used[x][y] && c < 200){

				if(tagStat.getTagName().equals("Person"))
					System.out.println("check "+x+" "+y+" c "+c+" used "+used[x][y]);
				c++;

				int dx = pseudoRandomGen.nextInt(3) - 1;
				int dy = pseudoRandomGen.nextInt(3) - 1;
				x += dx;
				y += dy;
				//System.out.println("sw: "+sw);
			}
			//System.out.println("FOUND: "+x+" "+y+" "+j);
			used[x][y] = j;

			//update BOUNDS
			if(x < min_x){
				min_x = x;
			}
			if(x > max_x){
				max_x = x;
			}
			if(y < min_y){
				min_y = y;
			}
			if(y > max_y){
				max_y = y;
			}

		}
		return new PointLocation(x,y);
	}


	public TagInfo getStat() {
		return tagStat;
	}

	public int get(int x, int j) {
		return used[x][j];
	}

	public PsuedoRandom getPseudoRandomGen() {
		return pseudoRandomGen;
	}

}
