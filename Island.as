import mx.transitions.Tween;
import mx.transitions.easing.*;
import flash.geom.Point;
import Hippo.*;

class Hippo.Island {
	private var id:Number;
	private var tag:String;
	//private var size:Number;				//does island need to know how big it is?
	private var seed:Number;				//for randomness
	private var landClip:MovieClip;
	private var waterClip:MovieClip;
	private var depth:Number;				//mc placement and coords for display()
	private var linkA:Array;				//a,b,c,d,e,f clockwise around hex ring (b at top)
	private var libertyA:Array;				//unordered list of available liberties (.xco, .yco, .letter)
	private var orderA:Array;				//array of Points
	private var acreA:Array;				//Acres corresponding to index of orderA
	private var maxRadius:Number;
	static private var sqSize:Number;		//defined by WorldSize
	//first four are hardcoded, subsequent are dynamic
	static private var initCoordA = [{xx:.56,yy:.5},{xx:.23,yy:.24},{xx:.13,yy:.63},{xx:.71,yy:.21}];
	static private var EDGEMARGIN = 15;
	static private var TEXTSIZE = 16;
	static private var NORMALWIDTH = 550;	//the "regular" stage size.  scale tag's font size accordingly.
	
	function Island(userID:Number, p_id:Number, p_tag:String, lAnc:MovieClip, wAnc:MovieClip, p_xx:Number, p_yy:Number) {
		this.id = p_id;
		this.seed = Math.round(Math.abs(Math.sin(userID+id)*10000));
		this.depth = lAnc.getNextHighestDepth();
		this.landClip = lAnc.attachMovie("island","is"+depth,(depth+2));
		this.depth = wAnc.getNextHighestDepth();
		this.waterClip = wAnc.attachMovie("island_water","is"+depth,(depth+2));
		
		this.linkA = new Array();
		this.libertyA = new Array();
		this.orderA = new Array();
		this.acreA = new Array();
		this.maxRadius = 1;						//keep low to force init of new rows
		rename(p_tag);
		resizeTag();
	}
	
	function display(islandCount, xx, yy) {
		trace("Placing Clip at depth"+islandCount);
		if (xx != undefined && yy != undefined) {
			trace("  moving clip to ["+xx+","+yy+"]");
			landClip._x = xx;
			landClip._y = yy;
			waterClip._x = xx;
			waterClip._y = yy;
		} else if (islandCount < initCoordA.length) {
			//first four hardcoded
			landClip._x = initCoordA[islandCount].xx * Stage.width;
			landClip._y = initCoordA[islandCount].yy * Stage.height;
			waterClip._x = initCoordA[islandCount].xx * Stage.width;
			waterClip._y = initCoordA[islandCount].yy * Stage.height;
		} else {
			landClip._x = (seed % 1000)/1000 * (Stage.width - EDGEMARGIN*2) + EDGEMARGIN;
			landClip._y = (seed % 873)/873 * (Stage.height - EDGEMARGIN*2) + EDGEMARGIN;
			waterClip._x = (seed % 1000)/1000 * (Stage.width - EDGEMARGIN*2) + EDGEMARGIN;
			waterClip._y = (seed % 873)/873 * (Stage.height - EDGEMARGIN*2) + EDGEMARGIN;
		}
	}
	
	function grow() {
		//trace(" Attempting to grow-- ");
		if (linkA["x0"]["y0"] == undefined) {
			linkA["x0"] = new Array();
			linkA["x1"] = new Array();
			linkA["x-1"] = new Array();
			linkA["x0"]["y0"] = {a:false, b:false, c:false, d:false, e:false, f:false};
			checkLiberties(0, 0);
			orderA.push(new Point(0, 0));
			acreA.push(new Acre(id, 0, 0, landClip.landAnchor, waterClip.waterAnchor, "emerge"));
		} else {
			var libObj = libertyA.splice(seed % libertyA.length,1);
			addLand(libObj[0].xx, libObj[0].yy, libObj[0].letter);
		}
	}
	
	function rename(newName:String) {
		tag = newName;
		landClip.tagText.text = tag;
	}
	
	function resizeTag() {
		var txFmt = new TextFormat();
		txFmt.size = Math.ceil(TEXTSIZE * NORMALWIDTH / Stage.width);
		landClip.tagText.setTextFormat(txFmt);
	}
	
	function shrink() {
		trace("Shrink called, oA.l: "+orderA.length);
		if (orderA.length < 1) {
			return;
		} else if (orderA.length == 1) {
			rename("");
		}
		var lastOrder = orderA.pop();
		var lastAcre = acreA.pop();
		reverseLiberties(lastOrder.x, lastOrder.y);
		lastAcre.remove();
	}

	function nudge(dist, angle) {
		var xDist = Math.cos(angle)*dist;
		var yDist = Math.sin(angle)*dist;
		var lxTween = new Tween(landClip, "_x", Strong.easeInOut, landClip._x, landClip._x + xDist, 1, true);
		var lyTween = new Tween(landClip, "_y", Strong.easeInOut, landClip._y, landClip._y + yDist, 1, true);
		var wxTween = new Tween(waterClip, "_x", Strong.easeInOut, waterClip._x, waterClip._x + xDist, 1, true);
		var wyTween = new Tween(waterClip, "_y", Strong.easeInOut, waterClip._y, waterClip._y + yDist, 1, true);
		lxTween.start();
		lyTween.start();
		wxTween.start();
		wyTween.start();
	}

	private function addLand(xx, yy, letter) {
		switch (letter) {
			case "a":
				xx--; break;
			case "b":
				yy--; break;
			case "c":
				xx++; yy--; break;
			case "d":
				xx++; break;
			case "e":
				yy++; break;
			case "f":
				xx--; yy++; break;
		}
		//if we've expanded, initialize the new arrays
		var newRadius = Math.max(Math.abs(xx), Math.abs(yy));
		if (maxRadius < newRadius) {
			//trace(" new Radius of "+id+" :: "+newRadius);
			maxRadius = newRadius;
			if (linkA["x"+newRadius] == undefined) {
				linkA["x"+newRadius] = new Array();
				linkA["x"+(-newRadius)] = new Array();
			}
			//do things on radius increase?
		}
		
		var aa = (typeof(linkA["x"+(xx-1)]["y" + yy  ]) == "object");
		var bb = (typeof(linkA["x" + xx  ]["y"+(yy-1)]) == "object");
		var cc = (typeof(linkA["x"+(xx+1)]["y"+(yy-1)]) == "object");
		var dd = (typeof(linkA["x"+(xx+1)]["y" + yy]) == "object");
		var ee = (typeof(linkA["x" + xx  ]["y"+(yy+1)]) == "object");
		var ff = (typeof(linkA["x"+(xx-1)]["y"+(yy+1)]) == "object");
		linkA["x"+xx]["y"+yy] = {a:aa, b:bb, c:cc, d:dd, e:ee, f:ff};
		checkLiberties(xx, yy, letter);
		
		var newAcre = new Acre(id, xx, yy, landClip.landAnchor, waterClip.waterAnchor, "set");
		acreA.push(newAcre);
		orderA.push(new Point(xx, yy));
		//trace(" Added land to "+id+" at ("+xx+","+yy+") :: oA.l: "+orderA.length);
	}
		
	private function checkLiberties(xx, yy, letter) {
		var obj = linkA["x"+xx]["y"+yy];
		
		//opposite letters are the usual, so don't mind those
		if (obj.a && letter != "d") { removeLiberty((xx-1), yy,   "d"); }
		if (obj.b && letter != "e") { removeLiberty( xx   ,(yy-1),"e"); }
		if (obj.c && letter != "f") { removeLiberty((xx+1),(yy-1),"f"); }
		if (obj.d && letter != "a") { removeLiberty((xx+1), yy,   "a"); }
		if (obj.e && letter != "b") { removeLiberty( xx   ,(yy+1),"b"); }
		if (obj.f && letter != "c") { removeLiberty((xx-1),(yy+1),"c"); }
		
		if (!obj.a) {obj.a = addLiberty(xx, yy, "a");}
		if (!obj.b) {obj.b = addLiberty(xx, yy, "b");}
		if (!obj.c) {obj.c = addLiberty(xx, yy, "c");}
		if (!obj.d) {obj.d = addLiberty(xx, yy, "d");}
		if (!obj.e) {obj.e = addLiberty(xx, yy, "e");}
		if (!obj.f) {obj.f = addLiberty(xx, yy, "f");}
	}
	
	private function addLiberty(xx, yy, letter):Boolean {
		//trace("  +lib: {["+xx+","+yy+"]::"+letter);
		libertyA.push({xx:xx, yy:yy, letter:letter});
		return true;
	}
	
	private function removeLiberty(xx, yy, letter):Boolean {
		for (var lib = 0; lib < libertyA.length; lib++) {
			if (xx == libertyA[lib].xx) {
				if (yy == libertyA[lib].yy) {
					if (letter == libertyA[lib].letter) {
						//trace("  -lib: {["+xx+","+yy+"]::"+letter);
						libertyA.splice(lib,1);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//used with shrink()
	private function reverseLiberties(xx:Number, yy:Number) {
		//delink land pieces, or remove the liberty if it's water
		if (!disconnect((xx-1),  yy  , "d")) { removeLiberty(xx, yy, "a"); }
		if (!disconnect( xx  , (yy-1), "e")) { removeLiberty(xx, yy, "b"); }
		if (!disconnect((xx+1),(yy-1), "f")) { removeLiberty(xx, yy, "c"); }
		if (!disconnect((xx+1),  yy  , "a")) { removeLiberty(xx, yy, "d"); }
		if (!disconnect( xx  , (yy+1), "b")) { removeLiberty(xx, yy, "e"); }
		if (!disconnect((xx-1),(yy+1), "c")) { removeLiberty(xx, yy, "f"); }
		//alter links
		linkA["x"+xx]["y"+yy] = undefined;
	}
	
	//returns true if the target was land.
	private function disconnect(tgtX:Number, tgtY:Number, srcLetter:String):Boolean {
		if (typeof(linkA["x"+tgtX]["y"+tgtY]) == "object") {
			//it's a piece of land; set its link and add a Liberty
			linkA["x"+tgtX]["y"+tgtY][srcLetter] = false;
			return true;
		} else {
			return false;
		}
	}
	
	function setSqSize(p_sqSize:Number) {
		Acre.sqSize = p_sqSize;
		for (var a = 0; a < acreA.length; a++) {
			acreA[a].redisplay();
		}
	}
	
	function getX() {
		return landClip._x;
	}
	
	function getY() {
		return landClip._y;
	}
	
	function get radius() {
		return maxRadius;
	}
}