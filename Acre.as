import mx.utils.Delegate;
import flash.external.*;
import flash.geom.*;

//an Island contains several Acres (blocks, units..)
class Hippo.Acre {
	private var isleID:Number;
	private var landClip:MovieClip;						//contains: mapSqBtn
	private var shoreClip:MovieClip;
	private var shallowClip:MovieClip;
	private var pressInterval:Number;////
	private var clickedPoint:Point;
	static private var PRESSDELAY:Number = 150;////		//number of ms to wait until it turns into a drag.
	static private var DRAGDISTANCE:Number = 2;
	static public var sqSize:Number;
	static private var DEFAULTSIZE = 20;
	
	function Acre(p_isleID:Number, xx:Number, yy:Number, landAnchor, waterAnchor, action:String) {
		isleID = p_isleID;
		var rotation = Math.random()*360;
		var sqShape = (Math.random() > .5) ? 1 : 1;			//randomly "1" or "2"
		landClip = arrange("landSq"+sqShape, landAnchor.lands, xx, yy, rotation);
		landClip.gotoAndPlay(action);
		shoreClip = arrange("shoreSq"+sqShape, landAnchor.shores, xx, yy, rotation);
		shoreClip.gotoAndPlay(action);
		shallowClip = arrange("shallowSq", waterAnchor, xx, yy, rotation);
		shallowClip.gotoAndPlay(action);
		landClip.mapSqBtn.onPress = Delegate.create(this, function () {
			clickedPoint = new Point(_xmouse, _ymouse);
			startDrag(landClip._parent._parent._parent, false);
		});
		landClip.mapSqBtn.onRelease = Delegate.create(this, function () {
			stopDrag();
			if (Point.distance(clickedPoint, new Point(_xmouse, _ymouse)) < DRAGDISTANCE) {
				trace("EXT CALL: Clicked "+isleID);
				ExternalInterface.call("islandClicked", isleID);
			} else {
				var isleClip = landClip._parent._parent._parent;
				trace("EXT CALL: "+Math.round(isleClip._x)+","+Math.round(isleClip._y));
				ExternalInterface.call("isleMovedTo", isleID, Math.round(isleClip._x), Math.round(isleClip._y));
			}
		});
		//setPreClick();
	}
	
	private function arrange(iden:String, subAnchor:MovieClip, xx:Number, yy:Number, rotation:Number) {
		var depth = subAnchor.getNextHighestDepth();
		var theClip = subAnchor.attachMovie(iden, "sq"+depth, depth);
		theClip._x = xx * sqSize * Math.sqrt(3)/2;				//hex accounting
		var yDrop = xx * sqSize / 2;
		theClip._xscale = (sqSize/DEFAULTSIZE) * 100;
		theClip._y = yy * sqSize + yDrop;
		theClip._yscale = (sqSize/DEFAULTSIZE) * 100;
		theClip._rotation = rotation;
		return theClip;
	}
	
	private function setPreClick() {
		landClip.mapSqBtn.onPress = Delegate.create(this, function () {
			pressInterval = setInterval(Delegate.create(this, setPostClick), PRESSDELAY);
		});
		landClip.mapSqBtn.onRelease = Delegate.create(this, function () {
			clearInterval(pressInterval);
			ExternalInterface.call("islandClicked", isleID);
			trace("Clicked :: xmit "+isleID);
		});
	}
	
	private function setPostClick() {
		trace("pI: "+pressInterval);
		clearInterval(pressInterval);
		//trace("   hitTest: "+landClip.mapSqBtn.hitTest(_xmouse, _ymouse, true));
		//if (!landClip.mapSqBtn.hitTest(_xmouse, _ymouse, true)) {
		//	return;			//if we hit the island in passing, don't grab it.
		//}
		delete landClip.mapSqBtn.onPress;
		trace(" Dragging parent, : "+landClip._parent._parent._parent);
		startDrag(landClip._parent._parent._parent, false);
		landClip.mapSqBtn.onReleaseOutside = Delegate.create(this, function () {
			stopDrag();
			var isleClip = landClip._parent._parent._parent;
			ExternalInterface.call("isleMovedTo", isleID, isleClip._x, isleClip._y);
			setPreClick();
		});
		landClip.mapSqBtn.onRelease = Delegate.create(this, function () {
			stopDrag();
			var isleClip = landClip._parent._parent._parent;
			trace("Dropped at "+isleClip._x+","+isleClip._y);
			ExternalInterface.call("isleMovedTo", isleID, isleClip._x, isleClip._y);
			setPreClick();
		});
	}
	
	function redisplay() {
		
	}
	
	function remove() {
		trace("  Removing Acre @ x "+landClip._x);
		removeMovieClip(landClip);
		removeMovieClip(shoreClip);
		removeMovieClip(shallowClip);
	}
}