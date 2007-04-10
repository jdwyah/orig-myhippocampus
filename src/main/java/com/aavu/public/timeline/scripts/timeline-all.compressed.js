Timeline.create=function(_1,_2,_3,_4){
return new Timeline._Impl(_1,_2,_3,_4);
};
Timeline.HORIZONTAL=0;
Timeline.VERTICAL=1;
Timeline._defaultTheme=null;
Timeline.createBandInfo=function(_5){
var _6=("theme" in _5)?_5.theme:Timeline.getDefaultTheme();
var _7=("eventSource" in _5)?_5.eventSource:null;
var _8=new Timeline.LinearEther({centersOn:("date" in _5)?_5.date:new Date(),interval:Timeline.DateTime.gregorianUnitLengths[_5.intervalUnit],pixelsPerInterval:_5.intervalPixels});
var _9=new Timeline.GregorianEtherPainter({unit:_5.intervalUnit,theme:_6});
var _a=new Timeline.StaticTrackBasedLayout({eventSource:_7,ether:_8,showText:("showEventText" in _5)?_5.showEventText:true,theme:_6});
var _b={showText:("showEventText" in _5)?_5.showEventText:true,layout:_a,theme:_6};
if("trackHeight" in _5){
_b.trackHeight=_5.trackHeight;
}
if("trackGap" in _5){
_b.trackGap=_5.trackGap;
}
var _c=new Timeline.DurationEventPainter(_b);
return {width:_5.width,eventSource:_7,timeZone:("timeZone" in _5)?_5.timeZone:0,ether:_8,etherPainter:_9,eventPainter:_c};
};
Timeline.createHotZoneBandInfo=function(_d){
var _e=("theme" in _d)?_d.theme:Timeline.getDefaultTheme();
var _f=("eventSource" in _d)?_d.eventSource:null;
var _10=new Timeline.HotZoneEther({centersOn:("date" in _d)?_d.date:new Date(),interval:Timeline.DateTime.gregorianUnitLengths[_d.intervalUnit],pixelsPerInterval:_d.intervalPixels,zones:_d.zones});
var _11=new Timeline.HotZoneGregorianEtherPainter({unit:_d.intervalUnit,zones:_d.zones,theme:_e});
var _12=new Timeline.StaticTrackBasedLayout({eventSource:_f,ether:_10,theme:_e});
var _13={showText:("showEventText" in _d)?_d.showEventText:true,layout:_12,theme:_e};
if("trackHeight" in _d){
_13.trackHeight=_d.trackHeight;
}
if("trackGap" in _d){
_13.trackGap=_d.trackGap;
}
var _14=new Timeline.DurationEventPainter(_13);
return {width:_d.width,eventSource:_f,timeZone:("timeZone" in _d)?_d.timeZone:0,ether:_10,etherPainter:_11,eventPainter:_14};
};
Timeline.getDefaultTheme=function(){
if(Timeline._defaultTheme==null){
Timeline._defaultTheme=Timeline.ClassicTheme.create(Timeline.Platform.getDefaultLocale());
}
return Timeline._defaultTheme;
};
Timeline.setDefaultTheme=function(_15){
Timeline._defaultTheme=_15;
};
Timeline.loadXML=function(url,f){
var _18=function(_19,_1a,_1b){
alert("Failed to load data xml from "+url+"\n"+_19);
};
var _1c=function(_1d){
var xml=_1d.responseXML;
if(!xml.documentElement&&_1d.responseStream){
xml.load(_1d.responseStream);
}
f(xml,url);
};
Timeline.XmlHttp.get(url,_18,_1c);
};
Timeline.loadJSON=function(url,f){
var _21=function(_22,_23,_24){
alert("Failed to load json data from "+url+"\n"+_22);
};
var _25=function(_26){
f(eval("("+_26.responseText+")"),url);
};
Timeline.XmlHttp.get(url,_21,_25);
};
Timeline._Impl=function(_27,_28,_29,_2a){
this._containerDiv=_27;
this._bandInfos=_28;
this._orientation=_29==null?Timeline.HORIZONTAL:_29;
this._unit=(_2a!=null)?_2a:Timeline.NativeDateUnit;
this._initialize();
};
Timeline._Impl.prototype.getBandCount=function(){
return this._bands.length;
};
Timeline._Impl.prototype.getBand=function(_2b){
return this._bands[_2b];
};
Timeline._Impl.prototype.layout=function(){
this._distributeWidths();
};
Timeline._Impl.prototype.paint=function(){
for(var i=0;i<this._bands.length;i++){
this._bands[i].paint();
}
};
Timeline._Impl.prototype.getDocument=function(){
return this._containerDiv.ownerDocument;
};
Timeline._Impl.prototype.addDiv=function(div){
this._containerDiv.appendChild(div);
};
Timeline._Impl.prototype.removeDiv=function(div){
this._containerDiv.removeChild(div);
};
Timeline._Impl.prototype.isHorizontal=function(){
return this._orientation==Timeline.HORIZONTAL;
};
Timeline._Impl.prototype.isVertical=function(){
return this._orientation==Timeline.VERTICAL;
};
Timeline._Impl.prototype.getPixelLength=function(){
return this._orientation==Timeline.HORIZONTAL?this._containerDiv.offsetWidth:this._containerDiv.offsetHeight;
};
Timeline._Impl.prototype.getPixelWidth=function(){
return this._orientation==Timeline.VERTICAL?this._containerDiv.offsetWidth:this._containerDiv.offsetHeight;
};
Timeline._Impl.prototype.getUnit=function(){
return this._unit;
};
Timeline._Impl.prototype.loadXML=function(url,f){
var tl=this;
var _32=function(_33,_34,_35){
alert("Failed to load data xml from "+url+"\n"+_33);
tl.hideLoadingMessage();
};
var _36=function(_37){
try{
var xml=_37.responseXML;
if(!xml.documentElement&&_37.responseStream){
xml.load(_37.responseStream);
}
f(xml,url);
}
finally{
tl.hideLoadingMessage();
}
};
this.showLoadingMessage();
window.setTimeout(function(){
Timeline.XmlHttp.get(url,_32,_36);
},0);
};
Timeline._Impl.prototype.loadJSON=function(url,f){
var tl=this;
var _3c=function(_3d,_3e,_3f){
alert("Failed to load json data from "+url+"\n"+_3d);
tl.hideLoadingMessage();
};
var _40=function(_41){
try{
f(eval("("+_41.responseText+")"),url);
}
finally{
tl.hideLoadingMessage();
}
};
this.showLoadingMessage();
window.setTimeout(function(){
Timeline.XmlHttp.get(url,_3c,_40);
},0);
};
Timeline._Impl.prototype._initialize=function(){
var _42=this._containerDiv;
var doc=_42.ownerDocument;
_42.className=_42.className.split(" ").concat("timeline-container").join(" ");
while(_42.firstChild){
_42.removeChild(_42.firstChild);
}
var _44=Timeline.Graphics.createTranslucentImage(doc,Timeline.urlPrefix+(this.isHorizontal()?"images/copyright-vertical.png":"images/copyright.png"));
_44.className="timeline-copyright";
_44.title="Timeline (c) SIMILE - http://simile.mit.edu/timeline/";
Timeline.DOM.registerEvent(_44,"click",function(){
window.location="http://simile.mit.edu/timeline/";
});
_42.appendChild(_44);
this._bands=[];
for(var i=0;i<this._bandInfos.length;i++){
var _46=new Timeline._Band(this,this._bandInfos[i],i);
this._bands.push(_46);
}
this._distributeWidths();
for(var i=0;i<this._bandInfos.length;i++){
var _48=this._bandInfos[i];
if("syncWith" in _48){
this._bands[i].setSyncWithBand(this._bands[_48.syncWith],("highlight" in _48)?_48.highlight:false);
}
}
var _49=Timeline.Graphics.createMessageBubble(doc);
_49.containerDiv.className="timeline-message-container";
_42.appendChild(_49.containerDiv);
_49.contentDiv.className="timeline-message";
_49.contentDiv.innerHTML="<img src='"+Timeline.urlPrefix+"images/progress-running.gif' /> Loading...";
this.showLoadingMessage=function(){
_49.containerDiv.style.display="block";
};
this.hideLoadingMessage=function(){
_49.containerDiv.style.display="none";
};
};
Timeline._Impl.prototype._distributeWidths=function(){
var _4a=this.getPixelLength();
var _4b=this.getPixelWidth();
var _4c=0;
for(var i=0;i<this._bands.length;i++){
var _4e=this._bands[i];
var _4f=this._bandInfos[i];
var _50=_4f.width;
var x=_50.indexOf("%");
if(x>0){
var _52=parseInt(_50.substr(0,x));
var _53=_52*_4b/100;
}else{
var _54=parseInt(_50);
}
_4e.setBandShiftAndWidth(_4c,_54);
_4e.setViewLength(_4a);
_4c+=_54;
}
};
Timeline._Band=function(_55,_56,_57){
this._timeline=_55;
this._bandInfo=_56;
this._index=_57;
this._locale=("locale" in _56)?_56.locale:Timeline.Platform.getDefaultLocale();
this._timeZone=("timeZone" in _56)?_56.timeZone:0;
this._labeller=("labeller" in _56)?_56.labeller:_55.getUnit().createLabeller(this._locale,this._timeZone);
this._dragging=false;
this._changing=false;
this._originalScrollSpeed=5;
this._scrollSpeed=this._originalScrollSpeed;
this._onScrollListeners=[];
var b=this;
this._syncWithBand=null;
this._syncWithBandHandler=function(_59){
b._onHighlightBandScroll();
};
this._selectorListener=function(_5a){
b._onHighlightBandScroll();
};
var _5b=this._timeline.getDocument().createElement("div");
_5b.className="timeline-band-input";
this._timeline.addDiv(_5b);
this._keyboardInput=document.createElement("input");
this._keyboardInput.type="text";
_5b.appendChild(this._keyboardInput);
Timeline.DOM.registerEventWithObject(this._keyboardInput,"keydown",this,this._onKeyDown);
Timeline.DOM.registerEventWithObject(this._keyboardInput,"keyup",this,this._onKeyUp);
this._div=this._timeline.getDocument().createElement("div");
this._div.className="timeline-band";
this._timeline.addDiv(this._div);
Timeline.DOM.registerEventWithObject(this._div,"mousedown",this,this._onMouseDown);
Timeline.DOM.registerEventWithObject(this._div,"mousemove",this,this._onMouseMove);
Timeline.DOM.registerEventWithObject(this._div,"mouseup",this,this._onMouseUp);
Timeline.DOM.registerEventWithObject(this._div,"mouseout",this,this._onMouseOut);
Timeline.DOM.registerEventWithObject(this._div,"dblclick",this,this._onDblClick);
this._innerDiv=this._timeline.getDocument().createElement("div");
this._innerDiv.className="timeline-band-inner";
this._div.appendChild(this._innerDiv);
this._ether=_56.ether;
_56.ether.initialize(_55);
this._etherPainter=_56.etherPainter;
_56.etherPainter.initialize(this,_55);
this._eventSource=_56.eventSource;
if(this._eventSource){
this._eventSource.addListener({onAddMany:function(){
b._onAddMany();
},onClear:function(){
b._onClear();
}});
}
this._eventPainter=_56.eventPainter;
_56.eventPainter.initialize(this,_55);
this._decorators=("decorators" in _56)?_56.decorators:[];
for(var i=0;i<this._decorators.length;i++){
this._decorators[i].initialize(this,_55);
}
this._bubble=null;
};
Timeline._Band.SCROLL_MULTIPLES=5;
Timeline._Band.prototype.addOnScrollListener=function(_5d){
this._onScrollListeners.push(_5d);
};
Timeline._Band.prototype.removeOnScrollListener=function(_5e){
for(var i=0;i<this._onScrollListeners.length;i++){
if(this._onScrollListeners[i]==_5e){
this._onScrollListeners.splice(i,1);
break;
}
}
};
Timeline._Band.prototype.setSyncWithBand=function(_60,_61){
if(this._syncWithBand){
this._syncWithBand.removeOnScrollListener(this._syncWithBandHandler);
}
this._syncWithBand=_60;
this._syncWithBand.addOnScrollListener(this._syncWithBandHandler);
this._highlight=_61;
this._positionHighlight();
};
Timeline._Band.prototype.getLocale=function(){
return this._locale;
};
Timeline._Band.prototype.getTimeZone=function(){
return this._timeZone;
};
Timeline._Band.prototype.getLabeller=function(){
return this._labeller;
};
Timeline._Band.prototype.getIndex=function(){
return this._index;
};
Timeline._Band.prototype.getEther=function(){
return this._ether;
};
Timeline._Band.prototype.getEtherPainter=function(){
return this._etherPainter;
};
Timeline._Band.prototype.getEventSource=function(){
return this._eventSource;
};
Timeline._Band.prototype.getEventPainter=function(){
return this._eventPainter;
};
Timeline._Band.prototype.layout=function(){
this.paint();
};
Timeline._Band.prototype.paint=function(){
this._etherPainter.paint();
this._paintDecorators();
this._paintEvents();
};
Timeline._Band.prototype.softLayout=function(){
this.softPaint();
};
Timeline._Band.prototype.softPaint=function(){
this._etherPainter.softPaint();
this._softPaintDecorators();
this._softPaintEvents();
};
Timeline._Band.prototype.setBandShiftAndWidth=function(_62,_63){
if(this._timeline.isHorizontal()){
this._div.style.top=_62+"px";
this._div.style.height=_63+"px";
}else{
this._div.style.left=_62+"px";
this._div.style.width=_63+"px";
}
};
Timeline._Band.prototype.getViewWidth=function(){
if(this._timeline.isHorizontal()){
return this._div.offsetHeight;
}else{
return this._div.offsetWidth;
}
};
Timeline._Band.prototype.setViewLength=function(_64){
this._viewLength=_64;
this._recenterDiv();
this._onChanging();
};
Timeline._Band.prototype.getViewLength=function(){
return this._viewLength;
};
Timeline._Band.prototype.getTotalViewLength=function(){
return Timeline._Band.SCROLL_MULTIPLES*this._viewLength;
};
Timeline._Band.prototype.getViewOffset=function(){
return this._viewOffset;
};
Timeline._Band.prototype.getMinDate=function(){
return this._ether.pixelOffsetToDate(this._viewOffset);
};
Timeline._Band.prototype.getMaxDate=function(){
return this._ether.pixelOffsetToDate(this._viewOffset+Timeline._Band.SCROLL_MULTIPLES*this._viewLength);
};
Timeline._Band.prototype.getMinVisibleDate=function(){
return this._ether.pixelOffsetToDate(0);
};
Timeline._Band.prototype.getMaxVisibleDate=function(){
return this._ether.pixelOffsetToDate(this._viewLength);
};
Timeline._Band.prototype.getCenterVisibleDate=function(){
return this._ether.pixelOffsetToDate(this._viewLength/2);
};
Timeline._Band.prototype.setMinVisibleDate=function(_65){
if(!this._changing){
this._moveEther(Math.round(-this._ether.dateToPixelOffset(_65)));
}
};
Timeline._Band.prototype.setMaxVisibleDate=function(_66){
if(!this._changing){
this._moveEther(Math.round(this._viewLength-this._ether.dateToPixelOffset(_66)));
}
};
Timeline._Band.prototype.setCenterVisibleDate=function(_67){
if(!this._changing){
this._moveEther(Math.round(this._viewLength/2-this._ether.dateToPixelOffset(_67)));
}
};
Timeline._Band.prototype.dateToPixelOffset=function(_68){
return this._ether.dateToPixelOffset(_68)-this._viewOffset;
};
Timeline._Band.prototype.pixelOffsetToDate=function(_69){
return this._ether.pixelOffsetToDate(_69+this._viewOffset);
};
Timeline._Band.prototype.createLayerDiv=function(_6a){
var div=this._timeline.getDocument().createElement("div");
div.className="timeline-band-layer";
div.style.zIndex=_6a;
this._innerDiv.appendChild(div);
var _6c=this._timeline.getDocument().createElement("div");
_6c.className="timeline-band-layer-inner";
if(Timeline.Platform.browser.isIE){
_6c.style.cursor="move";
}else{
_6c.style.cursor="-moz-grab";
}
div.appendChild(_6c);
return _6c;
};
Timeline._Band.prototype.removeLayerDiv=function(div){
this._innerDiv.removeChild(div.parentNode);
};
Timeline._Band.prototype.closeBubble=function(){
if(this._bubble!=null){
this._bubble.close();
this._bubble=null;
}
};
Timeline._Band.prototype.openBubbleForPoint=function(_6e,_6f,_70,_71){
this.closeBubble();
this._bubble=Timeline.Graphics.createBubbleForPoint(this._timeline.getDocument(),_6e,_6f,_70,_71);
return this._bubble.content;
};
Timeline._Band.prototype._onMouseDown=function(_72,evt,_74){
this.closeBubble();
this._dragging=true;
this._dragX=evt.clientX;
this._dragY=evt.clientY;
};
Timeline._Band.prototype._onMouseMove=function(_75,evt,_77){
if(this._dragging){
var _78=evt.clientX-this._dragX;
var _79=evt.clientY-this._dragY;
this._dragX=evt.clientX;
this._dragY=evt.clientY;
this._moveEther(this._timeline.isHorizontal()?_78:_79);
this._positionHighlight();
}
};
Timeline._Band.prototype._onMouseUp=function(_7a,evt,_7c){
this._dragging=false;
this._keyboardInput.focus();
};
Timeline._Band.prototype._onMouseOut=function(_7d,evt,_7f){
var _80=Timeline.DOM.getEventRelativeCoordinates(evt,_7d);
_80.x+=this._viewOffset;
if(_80.x<0||_80.x>_7d.offsetWidth||_80.y<0||_80.y>_7d.offsetHeight){
this._dragging=false;
}
};
Timeline._Band.prototype._onDblClick=function(_81,evt,_83){
var _84=Timeline.DOM.getEventRelativeCoordinates(evt,_81);
var _85=_84.x-(this._viewLength/2-this._viewOffset);
this._autoScroll(-_85);
};
Timeline._Band.prototype._onKeyDown=function(_86,evt,_88){
if(!this._dragging){
switch(evt.keyCode){
case 27:
break;
case 37:
case 38:
this._scrollSpeed=Math.min(50,Math.abs(this._scrollSpeed*1.05));
this._moveEther(this._scrollSpeed);
break;
case 39:
case 40:
this._scrollSpeed=-Math.min(50,Math.abs(this._scrollSpeed*1.05));
this._moveEther(this._scrollSpeed);
break;
default:
return;
}
this.closeBubble();
Timeline.DOM.cancelEvent(evt);
return false;
}
};
Timeline._Band.prototype._onKeyUp=function(_89,evt,_8b){
if(!this._dragging){
this._scrollSpeed=this._originalScrollSpeed;
switch(evt.keyCode){
case 35:
this.setCenterVisibleDate(this._eventSource.getLatestDate());
break;
case 36:
this.setCenterVisibleDate(this._eventSource.getEarliestDate());
break;
case 33:
this._autoScroll(this._timeline.getPixelLength());
break;
case 34:
this._autoScroll(-this._timeline.getPixelLength());
break;
default:
return;
}
this.closeBubble();
Timeline.DOM.cancelEvent(evt);
return false;
}
};
Timeline._Band.prototype._autoScroll=function(_8c){
var b=this;
var a=Timeline.Graphics.createAnimation(function(abs,_90){
b._moveEther(_90);
},0,_8c,3000);
a.run();
};
Timeline._Band.prototype._moveEther=function(_91){
this.closeBubble();
this._viewOffset+=_91;
this._ether.shiftPixels(-_91);
if(this._timeline.isHorizontal()){
this._div.style.left=this._viewOffset+"px";
}else{
this._div.style.top=this._viewOffset+"px";
}
if(this._viewOffset>-this._viewLength*0.5||this._viewOffset<-this._viewLength*(Timeline._Band.SCROLL_MULTIPLES-1.5)){
this._recenterDiv();
}else{
this.softLayout();
}
this._onChanging();
};
Timeline._Band.prototype._onChanging=function(){
this._changing=true;
this._fireOnScroll();
this._setSyncWithBandDate();
this._changing=false;
};
Timeline._Band.prototype._fireOnScroll=function(){
for(var i=0;i<this._onScrollListeners.length;i++){
this._onScrollListeners[i](this);
}
};
Timeline._Band.prototype._setSyncWithBandDate=function(){
if(this._syncWithBand){
var _93=this._ether.pixelOffsetToDate(this.getViewLength()/2);
this._syncWithBand.setCenterVisibleDate(_93);
}
};
Timeline._Band.prototype._onHighlightBandScroll=function(){
if(this._syncWithBand){
var _94=this._syncWithBand.getCenterVisibleDate();
var _95=this._ether.dateToPixelOffset(_94);
this._moveEther(Math.round(this._viewLength/2-_95));
if(this._highlight){
this._etherPainter.setHighlight(this._syncWithBand.getMinVisibleDate(),this._syncWithBand.getMaxVisibleDate());
}
}
};
Timeline._Band.prototype._onAddMany=function(){
this._paintEvents();
};
Timeline._Band.prototype._onClear=function(){
this._paintEvents();
};
Timeline._Band.prototype._positionHighlight=function(){
if(this._syncWithBand){
var _96=this._syncWithBand.getMinVisibleDate();
var _97=this._syncWithBand.getMaxVisibleDate();
if(this._highlight){
this._etherPainter.setHighlight(_96,_97);
}
}
};
Timeline._Band.prototype._recenterDiv=function(){
this._viewOffset=-this._viewLength*(Timeline._Band.SCROLL_MULTIPLES-1)/2;
if(this._timeline.isHorizontal()){
this._div.style.left=this._viewOffset+"px";
this._div.style.width=(Timeline._Band.SCROLL_MULTIPLES*this._viewLength)+"px";
}else{
this._div.style.top=this._viewOffset+"px";
this._div.style.height=(Timeline._Band.SCROLL_MULTIPLES*this._viewLength)+"px";
}
this.layout();
};
Timeline._Band.prototype._paintEvents=function(){
this._eventPainter.paint();
};
Timeline._Band.prototype._softPaintEvents=function(){
this._eventPainter.softPaint();
};
Timeline._Band.prototype._paintDecorators=function(){
for(var i=0;i<this._decorators.length;i++){
this._decorators[i].paint();
}
};
Timeline._Band.prototype._softPaintDecorators=function(){
for(var i=0;i<this._decorators.length;i++){
this._decorators[i].softPaint();
}
};
Timeline.Platform.os={isMac:false,isWin:false,isWin32:false,isUnix:false};
Timeline.Platform.browser={isIE:false,isNetscape:false,isMozilla:false,isFirefox:false,isOpera:false,isSafari:false,majorVersion:0,minorVersion:0};
(function(){
var an=navigator.appName.toLowerCase();
var ua=navigator.userAgent.toLowerCase();
Timeline.Platform.os.isMac=(ua.indexOf("mac")!=-1);
Timeline.Platform.os.isWin=(ua.indexOf("win")!=-1);
Timeline.Platform.os.isWin32=Timeline.Platform.isWin&&(ua.indexOf("95")!=-1||ua.indexOf("98")!=-1||ua.indexOf("nt")!=-1||ua.indexOf("win32")!=-1||ua.indexOf("32bit")!=-1);
Timeline.Platform.os.isUnix=(ua.indexOf("x11")!=-1);
Timeline.Platform.browser.isIE=(an.indexOf("microsoft")!=-1);
Timeline.Platform.browser.isNetscape=(an.indexOf("netscape")!=-1);
Timeline.Platform.browser.isMozilla=(ua.indexOf("mozilla")!=-1);
Timeline.Platform.browser.isFirefox=(ua.indexOf("firefox")!=-1);
Timeline.Platform.browser.isOpera=(an.indexOf("opera")!=-1);
var _9c=function(s){
var a=s.split(".");
Timeline.Platform.browser.majorVersion=parseInt(a[0]);
Timeline.Platform.browser.minorVersion=parseInt(a[1]);
};
var _9f=function(s,sub,_a2){
var i=s.indexOf(sub,_a2);
return i>=0?i:s.length;
};
if(Timeline.Platform.browser.isMozilla){
var _a4=ua.indexOf("mozilla/");
if(_a4>=0){
_9c(ua.substring(_a4+8,_9f(ua," ",_a4)));
}
}
if(Timeline.Platform.browser.isIE){
var _a5=ua.indexOf("msie ");
if(_a5>=0){
_9c(ua.substring(_a5+5,_9f(ua,";",_a5)));
}
}
if(Timeline.Platform.browser.isNetscape){
var _a6=ua.indexOf("rv:");
if(_a6>=0){
_9c(ua.substring(_a6+3,_9f(ua,")",_a6)));
}
}
if(Timeline.Platform.browser.isFirefox){
var _a7=ua.indexOf("firefox/");
if(_a7>=0){
_9c(ua.substring(_a7+8,_9f(ua," ",_a7)));
}
}
})();
Timeline.Platform.getDefaultLocale=function(){
return Timeline.Platform.clientLocale;
};
Timeline.Debug=new Object();
Timeline.Debug.log=function(msg){
};
Timeline.Debug.exception=function(e){
alert("Caught exception: "+(Timeline.Platform.isIE?e.message:e));
};
Timeline.XmlHttp=new Object();
Timeline.XmlHttp._onReadyStateChange=function(_aa,_ab,_ac){
switch(_aa.readyState){
case 4:
try{
if(_aa.status==0||_aa.status==200){
if(_ac){
_ac(_aa);
}
}else{
if(_ab){
_ab(_aa.statusText,_aa.status,_aa);
}
}
}
catch(e){
Timeline.Debug.exception(e);
}
break;
}
};
Timeline.XmlHttp._createRequest=function(){
if(Timeline.Platform.browser.isIE){
var _ad=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
for(var i=0;i<_ad.length;i++){
try{
var _af=_ad[i];
var f=function(){
return new ActiveXObject(_af);
};
var o=f();
Timeline.XmlHttp._createRequest=f;
return o;
}
catch(e){
}
}
throw new Error("Failed to create an XMLHttpRequest object");
}else{
try{
var f=function(){
return new XMLHttpRequest();
};
var o=f();
Timeline.XmlHttp._createRequest=f;
return o;
}
catch(e){
throw new Error("Failed to create an XMLHttpRequest object");
}
}
};
Timeline.XmlHttp.get=function(url,_b5,_b6){
var _b7=Timeline.XmlHttp._createRequest();
_b7.open("GET",url,true);
_b7.onreadystatechange=function(){
Timeline.XmlHttp._onReadyStateChange(_b7,_b5,_b6);
};
_b7.send(null);
};
Timeline.XmlHttp.post=function(url,_b9,_ba,_bb){
var _bc=Timeline.XmlHttp._createRequest();
_bc.open("POST",url,true);
_bc.onreadystatechange=function(){
Timeline.XmlHttp._onReadyStateChange(_bc,_ba,_bb);
};
_bc.send(_b9);
};
Timeline.XmlHttp._forceXML=function(_bd){
try{
_bd.overrideMimeType("text/xml");
}
catch(e){
_bd.setrequestheader("Content-Type","text/xml");
}
};
Timeline.DOM=new Object();
Timeline.DOM.registerEventWithObject=function(_be,_bf,obj,_c1){
Timeline.DOM.registerEvent(_be,_bf,function(_c2,evt,_c4){
return _c1.call(obj,_c2,evt,_c4);
});
};
Timeline.DOM.registerEvent=function(_c5,_c6,_c7){
var _c8=function(evt){
evt=(evt)?evt:((event)?event:null);
if(evt){
var _ca=(evt.target)?evt.target:((evt.srcElement)?evt.srcElement:null);
if(_ca){
_ca=(_ca.nodeType==1||_ca.nodeType==9)?_ca:_ca.parentNode;
}
return _c7(_c5,evt,_ca);
}
return true;
};
if(Timeline.Platform.browser.isIE){
_c5.attachEvent("on"+_c6,_c8);
}else{
_c5.addEventListener(_c6,_c8,false);
}
};
Timeline.DOM.getPageCoordinates=function(_cb){
var _cc=0;
var top=0;
if(_cb.nodeType!=1){
_cb=_cb.parentNode;
}
while(_cb!=null){
_cc+=_cb.offsetLeft;
top+=_cb.offsetTop;
_cb=_cb.offsetParent;
}
return {left:_cc,top:top};
};
Timeline.DOM.getEventRelativeCoordinates=function(evt,_cf){
if(Timeline.Platform.browser.isIE){
return {x:evt.offsetX,y:evt.offsetY};
}else{
var _d0=Timeline.DOM.getPageCoordinates(_cf);
return {x:evt.pageX-_d0.left,y:evt.pageY-_d0.top};
}
};
Timeline.DOM.cancelEvent=function(evt){
evt.returnValue=false;
evt.cancelBubble=true;
if("preventDefault" in evt){
evt.preventDefault();
}
};
Timeline.Graphics=new Object();
Timeline.Graphics.pngIsTranslucent=(!Timeline.Platform.browser.isIE)||(Timeline.Platform.browser.majorVersion>6);
Timeline.Graphics.createTranslucentImage=function(doc,url,_d4){
var _d5;
if(Timeline.Graphics.pngIsTranslucent){
_d5=doc.createElement("img");
_d5.setAttribute("src",url);
}else{
_d5=doc.createElement("img");
_d5.style.display="inline";
_d5.style.width="1px";
_d5.style.height="1px";
_d5.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+url+"', sizingMethod='image')";
}
_d5.style.verticalAlign=(_d4!=null)?_d4:"middle";
return _d5;
};
Timeline.Graphics.setOpacity=function(_d6,_d7){
if(Timeline.Platform.browser.isIE){
_d6.style.filter="progid:DXImageTransform.Microsoft.Alpha(Style=0,Opacity="+_d7+")";
}else{
var o=(_d7/100).toString();
_d6.style.opacity=o;
_d6.style.MozOpacity=o;
}
};
Timeline.Graphics._bubbleMargins={top:33,bottom:42,left:33,right:40};
Timeline.Graphics._arrowOffsets={top:0,bottom:9,left:1,right:8};
Timeline.Graphics._bubblePadding=15;
Timeline.Graphics._bubblePointOffset=6;
Timeline.Graphics._halfArrowWidth=18;
Timeline.Graphics.createBubbleForPoint=function(doc,_da,_db,_dc,_dd){
var _de={_closed:false,_doc:doc,close:function(){
if(!this._closed){
this._doc.body.removeChild(this._div);
this._doc=null;
this._div=null;
this._content=null;
this._closed=true;
}
}};
var _df=doc.body.offsetWidth;
var _e0=doc.body.offsetHeight;
var _e1=Timeline.Graphics._bubbleMargins;
var _e2=_e1.left+_dc+_e1.right;
var _e3=_e1.top+_dd+_e1.bottom;
var _e4=Timeline.Graphics.pngIsTranslucent;
var _e5=Timeline.urlPrefix;
var _e6=function(_e7,url,_e9,_ea){
_e7.style.position="absolute";
_e7.style.width=_e9+"px";
_e7.style.height=_ea+"px";
if(_e4){
_e7.style.background="url("+url+")";
}else{
_e7.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+url+"', sizingMethod='crop')";
}
};
var div=doc.createElement("div");
div.style.width=_e2+"px";
div.style.height=_e3+"px";
div.style.position="absolute";
div.style.zIndex=1000;
_de._div=div;
var _ec=doc.createElement("div");
_ec.style.width="100%";
_ec.style.height="100%";
_ec.style.position="relative";
div.appendChild(_ec);
var _ed=function(url,_ef,top,_f1,_f2){
var _f3=doc.createElement("div");
_f3.style.left=_ef+"px";
_f3.style.top=top+"px";
_e6(_f3,url,_f1,_f2);
_ec.appendChild(_f3);
};
_ed(_e5+"images/bubble-top-left.png",0,0,_e1.left,_e1.top);
_ed(_e5+"images/bubble-top.png",_e1.left,0,_dc,_e1.top);
_ed(_e5+"images/bubble-top-right.png",_e1.left+_dc,0,_e1.right,_e1.top);
_ed(_e5+"images/bubble-left.png",0,_e1.top,_e1.left,_dd);
_ed(_e5+"images/bubble-right.png",_e1.left+_dc,_e1.top,_e1.right,_dd);
_ed(_e5+"images/bubble-bottom-left.png",0,_e1.top+_dd,_e1.left,_e1.bottom);
_ed(_e5+"images/bubble-bottom.png",_e1.left,_e1.top+_dd,_dc,_e1.bottom);
_ed(_e5+"images/bubble-bottom-right.png",_e1.left+_dc,_e1.top+_dd,_e1.right,_e1.bottom);
var _f4=doc.createElement("div");
_f4.style.left=(_e2-_e1.right+Timeline.Graphics._bubblePadding-16-2)+"px";
_f4.style.top=(_e1.top-Timeline.Graphics._bubblePadding+1)+"px";
_f4.style.cursor="pointer";
_e6(_f4,_e5+"images/close-button.png",16,16);
Timeline.DOM.registerEventWithObject(_f4,"click",_de,_de.close);
_ec.appendChild(_f4);
var _f5=doc.createElement("div");
_f5.style.position="absolute";
_f5.style.left=_e1.left+"px";
_f5.style.top=_e1.top+"px";
_f5.style.width=_dc+"px";
_f5.style.height=_dd+"px";
_f5.style.overflow="auto";
_f5.style.background="white";
_ec.appendChild(_f5);
_de.content=_f5;
(function(){
if(_da-Timeline.Graphics._halfArrowWidth-Timeline.Graphics._bubblePadding>0&&_da+Timeline.Graphics._halfArrowWidth+Timeline.Graphics._bubblePadding<_df){
var _f6=_da-Math.round(_dc/2)-_e1.left;
_f6=_da<(_df/2)?Math.max(_f6,-(_e1.left-Timeline.Graphics._bubblePadding)):Math.min(_f6,_df+(_e1.right-Timeline.Graphics._bubblePadding)-_e2);
if(_db-Timeline.Graphics._bubblePointOffset-_e3>0){
var _f7=doc.createElement("div");
_f7.style.left=(_da-Timeline.Graphics._halfArrowWidth-_f6)+"px";
_f7.style.top=(_e1.top+_dd)+"px";
_e6(_f7,_e5+"images/bubble-bottom-arrow.png",37,_e1.bottom);
_ec.appendChild(_f7);
div.style.left=_f6+"px";
div.style.top=(_db-Timeline.Graphics._bubblePointOffset-_e3+Timeline.Graphics._arrowOffsets.bottom)+"px";
return;
}else{
if(_db+Timeline.Graphics._bubblePointOffset+_e3<_e0){
var _f8=doc.createElement("div");
_f8.style.left=(_da-Timeline.Graphics._halfArrowWidth-_f6)+"px";
_f8.style.top="0px";
_e6(_f8,_e5+"images/bubble-top-arrow.png",37,_e1.top);
_ec.appendChild(_f8);
div.style.left=_f6+"px";
div.style.top=(_db+Timeline.Graphics._bubblePointOffset-Timeline.Graphics._arrowOffsets.top)+"px";
return;
}
}
}
var top=_db-Math.round(_dd/2)-_e1.top;
top=_db<(_e0/2)?Math.max(top,-(_e1.top-Timeline.Graphics._bubblePadding)):Math.min(top,_e0+(_e1.bottom-Timeline.Graphics._bubblePadding)-_e3);
if(_da-Timeline.Graphics._bubblePointOffset-_e2>0){
var _fa=doc.createElement("div");
_fa.style.left=(_e1.left+_dc)+"px";
_fa.style.top=(_db-Timeline.Graphics._halfArrowWidth-top)+"px";
_e6(_fa,_e5+"images/bubble-right-arrow.png",_e1.right,37);
_ec.appendChild(_fa);
div.style.left=(_da-Timeline.Graphics._bubblePointOffset-_e2+Timeline.Graphics._arrowOffsets.right)+"px";
div.style.top=top+"px";
}else{
var _fb=doc.createElement("div");
_fb.style.left="0px";
_fb.style.top=(_db-Timeline.Graphics._halfArrowWidth-top)+"px";
_e6(_fb,_e5+"images/bubble-left-arrow.png",_e1.left,37);
_ec.appendChild(_fb);
div.style.left=(_da+Timeline.Graphics._bubblePointOffset-Timeline.Graphics._arrowOffsets.left)+"px";
div.style.top=top+"px";
}
})();
doc.body.appendChild(div);
return _de;
};
Timeline.Graphics.createMessageBubble=function(doc){
var _fd=doc.createElement("div");
if(Timeline.Graphics.pngIsTranslucent){
var _fe=doc.createElement("div");
_fe.style.height="33px";
_fe.style.background="url("+Timeline.urlPrefix+"images/message-top-left.png) top left no-repeat";
_fe.style.paddingLeft="44px";
_fd.appendChild(_fe);
var _ff=doc.createElement("div");
_ff.style.height="33px";
_ff.style.background="url("+Timeline.urlPrefix+"images/message-top-right.png) top right no-repeat";
_fe.appendChild(_ff);
var _100=doc.createElement("div");
_100.style.background="url("+Timeline.urlPrefix+"images/message-left.png) top left repeat-y";
_100.style.paddingLeft="44px";
_fd.appendChild(_100);
var _101=doc.createElement("div");
_101.style.background="url("+Timeline.urlPrefix+"images/message-right.png) top right repeat-y";
_101.style.paddingRight="44px";
_100.appendChild(_101);
var _102=doc.createElement("div");
_101.appendChild(_102);
var _103=doc.createElement("div");
_103.style.height="55px";
_103.style.background="url("+Timeline.urlPrefix+"images/message-bottom-left.png) bottom left no-repeat";
_103.style.paddingLeft="44px";
_fd.appendChild(_103);
var _104=doc.createElement("div");
_104.style.height="55px";
_104.style.background="url("+Timeline.urlPrefix+"images/message-bottom-right.png) bottom right no-repeat";
_103.appendChild(_104);
}else{
_fd.style.border="2px solid #7777AA";
_fd.style.padding="20px";
_fd.style.background="white";
Timeline.Graphics.setOpacity(_fd,90);
var _105=doc.createElement("div");
_fd.appendChild(_105);
}
return {containerDiv:_fd,contentDiv:_105};
};
Timeline.Graphics.createAnimation=function(f,from,to,_109){
return new Timeline.Graphics._Animation(f,from,to,_109);
};
Timeline.Graphics._Animation=function(f,from,to,_10d){
this.f=f;
this.from=from;
this.to=to;
this.current=from;
this.duration=_10d;
this.start=new Date().getTime();
this.timePassed=0;
};
Timeline.Graphics._Animation.prototype.run=function(){
var a=this;
window.setTimeout(function(){
a.step();
},100);
};
Timeline.Graphics._Animation.prototype.step=function(){
this.timePassed+=100;
var _10f=this.timePassed/this.duration;
var _110=-Math.cos(_10f*Math.PI)/2+0.5;
var _111=_110*(this.to-this.from)+this.from;
try{
this.f(_111,_111-this.current);
}
catch(e){
}
this.current=_111;
if(this.timePassed<this.duration){
this.run();
}
};
Timeline.DateTime=new Object();
Timeline.DateTime.MILLISECOND=0;
Timeline.DateTime.SECOND=1;
Timeline.DateTime.MINUTE=2;
Timeline.DateTime.HOUR=3;
Timeline.DateTime.DAY=4;
Timeline.DateTime.WEEK=5;
Timeline.DateTime.MONTH=6;
Timeline.DateTime.YEAR=7;
Timeline.DateTime.DECADE=8;
Timeline.DateTime.CENTURY=9;
Timeline.DateTime.MILLENNIUM=10;
Timeline.DateTime.EPOCH=-1;
Timeline.DateTime.ERA=-2;
Timeline.DateTime.gregorianUnitLengths=[];
(function(){
var d=Timeline.DateTime;
var a=d.gregorianUnitLengths;
a[d.MILLISECOND]=1;
a[d.SECOND]=1000;
a[d.MINUTE]=a[d.SECOND]*60;
a[d.HOUR]=a[d.MINUTE]*60;
a[d.DAY]=a[d.HOUR]*24;
a[d.WEEK]=a[d.DAY]*7;
a[d.MONTH]=a[d.DAY]*31;
a[d.YEAR]=a[d.DAY]*365;
a[d.DECADE]=a[d.YEAR]*10;
a[d.CENTURY]=a[d.YEAR]*100;
a[d.MILLENNIUM]=a[d.YEAR]*1000;
})();
Timeline.DateTime.parseGregorianDateTime=function(o){
if(o==null){
return null;
}else{
if(o instanceof Date){
return o;
}
}
var s=o.toString();
if(s.length>0&&s.length<8){
var _116=s.indexOf(" ");
if(_116>0){
var year=parseInt(s.substr(0,_116));
var _118=s.substr(_116+1);
if(_118.toLowerCase()=="bc"){
year=1-year;
}
}else{
var year=parseInt(s);
}
var d=new Date(0);
d.setUTCFullYear(year);
return d;
}
try{
return new Date(Date.parse(s));
}
catch(e){
return null;
}
};
Timeline.DateTime.setIso8601Date=function(_11b,_11c){
var _11d="^([0-9]{4})((-?([0-9]{2})(-?([0-9]{2}))?)|"+"(-?([0-9]{3}))|(-?W([0-9]{2})(-?([1-7]))?))?$";
var d=_11c.match(new RegExp(_11d));
if(!d){
throw new Error("Invalid date string: "+_11c);
}
var year=d[1];
var _120=d[4];
var date=d[6];
var _122=d[8];
var week=d[10];
var _124=(d[12])?d[12]:1;
_11b.setUTCFullYear(year);
if(_122){
_11b.setUTCMonth(0);
_11b.setUTCDate(Number(_122));
}else{
if(week){
_11b.setUTCMonth(0);
_11b.setUTCDate(1);
var gd=_11b.getUTCDay();
var day=(gd)?gd:7;
var _127=Number(_124)+(7*Number(week));
if(day<=4){
_11b.setUTCDate(_127+1-day);
}else{
_11b.setUTCDate(_127+8-day);
}
}else{
if(_120){
_11b.setUTCDate(1);
_11b.setUTCMonth(_120-1);
}
if(date){
_11b.setUTCDate(date);
}
}
}
return _11b;
};
Timeline.DateTime.setIso8601Time=function(_128,_129){
var _12a="Z|(([-+])([0-9]{2})(:?([0-9]{2}))?)$";
var d=_129.match(new RegExp(_12a));
var _12c=0;
if(d){
if(d[0]!="Z"){
_12c=(Number(d[3])*60)+Number(d[5]);
_12c*=((d[2]=="-")?1:-1);
}
_129=_129.substr(0,_129.length-d[0].length);
}
var _12d="^([0-9]{2})(:?([0-9]{2})(:?([0-9]{2})(.([0-9]+))?)?)?$";
var d=_129.match(new RegExp(_12d));
if(!d){
dojo.debug("invalid time string: "+_129);
return false;
}
var _12f=d[1];
var mins=Number((d[3])?d[3]:0);
var secs=(d[5])?d[5]:0;
var ms=d[7]?(Number("0."+d[7])*1000):0;
_128.setUTCHours(_12f);
_128.setUTCMinutes(mins);
_128.setUTCSeconds(secs);
_128.setUTCMilliseconds(ms);
return _128;
};
Timeline.DateTime.setIso8601=function(_133,_134){
var _135=(_134.indexOf("T")==-1)?_134.split(" "):_134.split("T");
Timeline.DateTime.setIso8601Date(_133,_135[0]);
if(_135.length==2){
Timeline.DateTime.setIso8601Time(_133,_135[1]);
}
return _133;
};
Timeline.DateTime.parseIso8601DateTime=function(_136){
try{
return Timeline.DateTime.setIso8601(new Date(0),_136);
}
catch(e){
return null;
}
};
Timeline.DateTime.roundDownToInterval=function(date,_138,_139,_13a,_13b){
var _13c=_139*Timeline.DateTime.gregorianUnitLengths[Timeline.DateTime.HOUR];
var _13d=new Date(date.getTime()+_13c);
var _13e=function(d){
d.setUTCMilliseconds(0);
d.setUTCSeconds(0);
d.setUTCMinutes(0);
d.setUTCHours(0);
};
var _140=function(d){
_13e(d);
d.setUTCDate(1);
d.setUTCMonth(0);
};
switch(_138){
case Timeline.DateTime.MILLISECOND:
var x=_13d.getUTCMilliseconds();
_13d.setUTCMilliseconds(x-(x%_13a));
break;
case Timeline.DateTime.SECOND:
_13d.setUTCMilliseconds(0);
var x=_13d.getUTCSeconds();
_13d.setUTCSeconds(x-(x%_13a));
break;
case Timeline.DateTime.MINUTE:
_13d.setUTCMilliseconds(0);
_13d.setUTCSeconds(0);
var x=_13d.getUTCMinutes();
_13d.setTime(_13d.getTime()-(x%_13a)*Timeline.DateTime.gregorianUnitLengths[Timeline.DateTime.MINUTE]);
break;
case Timeline.DateTime.HOUR:
_13d.setUTCMilliseconds(0);
_13d.setUTCSeconds(0);
_13d.setUTCMinutes(0);
var x=_13d.getUTCHours();
_13d.setUTCHours(x-(x%_13a));
break;
case Timeline.DateTime.DAY:
_13e(_13d);
break;
case Timeline.DateTime.WEEK:
_13e(_13d);
var d=(_13d.getUTCDay()+7-_13b)%7;
_13d.setTime(_13d.getTime()-d*Timeline.DateTime.gregorianUnitLengths[Timeline.DateTime.DAY]);
break;
case Timeline.DateTime.MONTH:
_13e(_13d);
_13d.setUTCDate(1);
var x=_13d.getUTCMonth();
_13d.setUTCMonth(x-(x%_13a));
break;
case Timeline.DateTime.YEAR:
_140(_13d);
var x=_13d.getUTCFullYear();
_13d.setUTCFullYear(x-(x%_13a));
break;
case Timeline.DateTime.DECADE:
_140(_13d);
_13d.setUTCFullYear(Math.floor(_13d.getUTCFullYear()/10)*10);
break;
case Timeline.DateTime.CENTURY:
_140(_13d);
_13d.setUTCFullYear(Math.floor(_13d.getUTCFullYear()/100)*100);
break;
case Timeline.DateTime.MILLENNIUM:
_140(_13d);
_13d.setUTCFullYear(Math.floor(_13d.getUTCFullYear()/1000)*1000);
break;
}
date.setTime(_13d.getTime()-_13c);
};
Timeline.DateTime.roundUpToInterval=function(date,_14a,_14b,_14c,_14d){
var _14e=date.getTime();
Timeline.DateTime.roundDownToInterval(date,_14a,_14b,_14c,_14d);
if(date.getTime()<_14e){
date.setTime(date.getTime()+Timeline.DateTime.gregorianUnitLengths[_14a]*_14c);
}
};
Timeline.DateTime.incrementByInterval=function(date,_150){
switch(_150){
case Timeline.DateTime.MILLISECOND:
date.setTime(date.getTime()+1);
break;
case Timeline.DateTime.SECOND:
date.setTime(date.getTime()+1000);
break;
case Timeline.DateTime.MINUTE:
date.setTime(date.getTime()+Timeline.DateTime.gregorianUnitLengths[Timeline.DateTime.MINUTE]);
break;
case Timeline.DateTime.HOUR:
date.setTime(date.getTime()+Timeline.DateTime.gregorianUnitLengths[Timeline.DateTime.HOUR]);
break;
case Timeline.DateTime.DAY:
date.setUTCDate(date.getUTCDate()+1);
break;
case Timeline.DateTime.WEEK:
date.setUTCDate(date.getUTCDate()+7);
break;
case Timeline.DateTime.MONTH:
date.setUTCMonth(date.getUTCMonth()+1);
break;
case Timeline.DateTime.YEAR:
date.setUTCFullYear(date.getUTCFullYear()+1);
break;
case Timeline.DateTime.DECADE:
date.setUTCFullYear(date.getUTCFullYear()+10);
break;
case Timeline.DateTime.CENTURY:
date.setUTCFullYear(date.getUTCFullYear()+100);
break;
case Timeline.DateTime.MILLENNIUM:
date.setUTCFullYear(date.getUTCFullYear()+1000);
break;
}
};
Timeline.DateTime.removeTimeZoneOffset=function(date,_152){
return new Date(date.getTime()+_152*Timeline.DateTime.gregorianUnitLengths[Timeline.DateTime.HOUR]);
};
Timeline.SortedArray=function(_153,_154){
this._a=(_154 instanceof Array)?_154:[];
this._compare=_153;
};
Timeline.SortedArray.prototype.add=function(elmt){
var sa=this;
var _157=this.find(function(_158){
return sa._compare(_158,elmt);
});
if(_157<this._a.length){
this._a.splice(_157,0,elmt);
}else{
this._a.push(elmt);
}
};
Timeline.SortedArray.prototype.remove=function(elmt){
var sa=this;
var _15b=this.find(function(_15c){
return sa._compare(_15c,elmt);
});
while(_15b<this._a.length&&this._compare(this._a[_15b],elmt)==0){
if(this._a[_15b]==elmt){
this._a.splice(_15b,1);
return true;
}else{
_15b++;
}
}
return false;
};
Timeline.SortedArray.prototype.removeAll=function(){
this._a=[];
};
Timeline.SortedArray.prototype.elementAt=function(_15d){
return this._a[_15d];
};
Timeline.SortedArray.prototype.length=function(){
return this._a.length;
};
Timeline.SortedArray.prototype.find=function(_15e){
var a=0;
var b=this._a.length;
while(a<b){
var mid=Math.floor((a+b)/2);
var c=_15e(this._a[mid]);
if(mid==a){
return c<0?a+1:a;
}else{
if(c<0){
a=mid;
}else{
b=mid;
}
}
}
return a;
};
Timeline.SortedArray.prototype.getFirst=function(){
return (this._a.length>0)?this._a[0]:null;
};
Timeline.SortedArray.prototype.getLast=function(){
return (this._a.length>0)?this._a[this._a.length-1]:null;
};
Timeline.EventIndex=function(unit){
var _164=this;
this._unit=(unit!=null)?unit:Timeline.NativeDateUnit;
this._events=new Timeline.SortedArray(function(_165,_166){
return _164._unit.compare(_165.getStart(),_166.getStart());
});
this._indexed=true;
};
Timeline.EventIndex.prototype.getUnit=function(){
return this._unit;
};
Timeline.EventIndex.prototype.add=function(evt){
this._events.add(evt);
this._indexed=false;
};
Timeline.EventIndex.prototype.removeAll=function(){
this._events.removeAll();
this._indexed=false;
};
Timeline.EventIndex.prototype.getCount=function(){
return this._events.length();
};
Timeline.EventIndex.prototype.getIterator=function(_168,_169){
if(!this._indexed){
this._index();
}
return new Timeline.EventIndex._Iterator(this._events,_168,_169,this._unit);
};
Timeline.EventIndex.prototype.getAllIterator=function(){
return new Timeline.EventIndex._AllIterator(this._events);
};
Timeline.EventIndex.prototype.getEarliestDate=function(){
var evt=this._events.getFirst();
return (evt==null)?null:evt.getStart();
};
Timeline.EventIndex.prototype.getLatestDate=function(){
var evt=this._events.getLast();
if(evt==null){
return null;
}
if(!this._indexed){
this._index();
}
var _16c=evt._earliestOverlapIndex;
var date=this._events.elementAt(_16c).getEnd();
for(var i=_16c+1;i<this._events.length();i++){
date=this._unit.later(date,this._events.elementAt(i).getEnd());
}
return date;
};
Timeline.EventIndex.prototype._index=function(){
var l=this._events.length();
for(var i=0;i<l;i++){
var evt=this._events.elementAt(i);
evt._earliestOverlapIndex=i;
}
var _172=1;
for(var i=0;i<l;i++){
var evt=this._events.elementAt(i);
var end=evt.getEnd();
_172=Math.max(_172,i+1);
while(_172<l){
var evt2=this._events.elementAt(_172);
var _177=evt2.getStart();
if(this._unit.compare(_177,end)<0){
evt2._earliestOverlapIndex=i;
_172++;
}else{
break;
}
}
}
this._indexed=true;
};
Timeline.EventIndex._Iterator=function(_178,_179,_17a,unit){
this._events=_178;
this._startDate=_179;
this._endDate=_17a;
this._unit=unit;
this._currentIndex=_178.find(function(evt){
return unit.compare(evt.getStart(),_179);
});
if(this._currentIndex-1>=0){
this._currentIndex=this._events.elementAt(this._currentIndex-1)._earliestOverlapIndex;
}
this._currentIndex--;
this._maxIndex=_178.find(function(evt){
return unit.compare(evt.getStart(),_17a);
});
this._hasNext=false;
this._next=null;
this._findNext();
};
Timeline.EventIndex._Iterator.prototype={hasNext:function(){
return this._hasNext;
},next:function(){
if(this._hasNext){
var next=this._next;
this._findNext();
return next;
}else{
return null;
}
},_findNext:function(){
var unit=this._unit;
while((++this._currentIndex)<this._maxIndex){
var evt=this._events.elementAt(this._currentIndex);
if(unit.compare(evt.getStart(),this._endDate)<0&&unit.compare(evt.getEnd(),this._startDate)>0){
this._next=evt;
this._hasNext=true;
return;
}
}
this._next=null;
this._hasNext=false;
}};
Timeline.EventIndex._AllIterator=function(_181){
this._events=_181;
this._index=0;
};
Timeline.EventIndex._AllIterator.prototype={hasNext:function(){
return this._index<this._events.length();
},next:function(){
return this._index<this._events.length()?this._events.elementAt(this._index++):null;
}};
Timeline.NativeDateUnit=new Object();
Timeline.NativeDateUnit.createLabeller=function(_182,_183){
return new Timeline.GregorianDateLabeller(_182,_183);
};
Timeline.NativeDateUnit.makeDefaultValue=function(){
return new Date();
};
Timeline.NativeDateUnit.cloneValue=function(v){
return new Date(v.getTime());
};
Timeline.NativeDateUnit.getParser=function(_185){
if(typeof _185=="string"){
_185=_185.toLowerCase();
}
return (_185=="iso8601"||_185=="iso 8601")?Timeline.DateTime.parseIso8601DateTime:Timeline.DateTime.parseGregorianDateTime;
};
Timeline.NativeDateUnit.parseFromObject=function(o){
return Timeline.DateTime.parseGregorianDateTime(o);
};
Timeline.NativeDateUnit.toNumber=function(v){
return v.getTime();
};
Timeline.NativeDateUnit.fromNumber=function(n){
return new Date(n);
};
Timeline.NativeDateUnit.compare=function(v1,v2){
var n1,n2;
if(typeof v1=="object"){
n1=v1.getTime();
}else{
n1=Number(v1);
}
if(typeof v2=="object"){
n2=v2.getTime();
}else{
n2=Number(v2);
}
return n1-n2;
};
Timeline.NativeDateUnit.earlier=function(v1,v2){
return Timeline.NativeDateUnit.compare(v1,v2)<0?v1:v2;
};
Timeline.NativeDateUnit.later=function(v1,v2){
return Timeline.NativeDateUnit.compare(v1,v2)>0?v1:v2;
};
Timeline.NativeDateUnit.change=function(v,n){
return new Date(v.getTime()+n);
};
Timeline.ClassicTheme=new Object();
Timeline.ClassicTheme.implementations=[];
Timeline.ClassicTheme.create=function(_192){
if(_192==null){
_192=Timeline.Platform.getDefaultLocale();
}
var f=Timeline.ClassicTheme.implementations[_192];
if(f==null){
f=Timeline.ClassicTheme._Impl;
}
return new f();
};
Timeline.ClassicTheme._Impl=function(){
this.firstDayOfWeek=0;
this.ether={backgroundColors:["#EEE","#DDD","#CCC","#AAA"],highlightColor:"white",highlightOpacity:50,interval:{line:{show:true,color:"#aaa",opacity:25},weekend:{color:"#FFFFE0",opacity:30},marker:{hAlign:"Bottom",hBottomStyler:function(elmt){
elmt.className="timeline-ether-marker-bottom";
},hBottomEmphasizedStyler:function(elmt){
elmt.className="timeline-ether-marker-bottom-emphasized";
},hTopStyler:function(elmt){
elmt.className="timeline-ether-marker-top";
},hTopEmphasizedStyler:function(elmt){
elmt.className="timeline-ether-marker-top-emphasized";
},vAlign:"Right",vRightStyler:function(elmt){
elmt.className="timeline-ether-marker-right";
},vRightEmphasizedStyler:function(elmt){
elmt.className="timeline-ether-marker-right-emphasized";
},vLeftStyler:function(elmt){
elmt.className="timeline-ether-marker-left";
},vLeftEmphasizedStyler:function(elmt){
elmt.className="timeline-ether-marker-left-emphasized";
}}}};
this.event={track:{offset:0.5,height:1.5,gap:0.5},instant:{icon:Timeline.urlPrefix+"images/dull-blue-circle.png",lineColor:"#58A0DC",impreciseColor:"#58A0DC",impreciseOpacity:20,showLineForNoText:true},duration:{color:"#58A0DC",opacity:100,impreciseColor:"#58A0DC",impreciseOpacity:20},label:{insideColor:"white",outsideColor:"black",width:200},highlightColors:["#FFFF00","#FFC000","#FF0000","#0000FF"],bubble:{width:250,height:125,titleStyler:function(elmt){
elmt.className="timeline-event-bubble-title";
},bodyStyler:function(elmt){
elmt.className="timeline-event-bubble-body";
},imageStyler:function(elmt){
elmt.className="timeline-event-bubble-image";
},timeStyler:function(elmt){
elmt.className="timeline-event-bubble-time";
}}};
};
Timeline.LinearEther=function(_1a0){
this._params=_1a0;
this._interval=_1a0.interval;
this._pixelsPerInterval=_1a0.pixelsPerInterval;
};
Timeline.LinearEther.prototype.initialize=function(_1a1){
this._timeline=_1a1;
this._unit=_1a1.getUnit();
if("startsOn" in this._params){
this._start=this._unit.parseFromObject(this._params.startsOn);
}else{
if("endsOn" in this._params){
this._start=this._unit.parseFromObject(this._params.endsOn);
this.shiftPixels(-this._timeline.getPixelLength());
}else{
if("centersOn" in this._params){
this._start=this._unit.parseFromObject(this._params.centersOn);
this.shiftPixels(-this._timeline.getPixelLength()/2);
}else{
this._start=this._unit.makeDefaultValue();
this.shiftPixels(-this._timeline.getPixelLength()/2);
}
}
}
};
Timeline.LinearEther.prototype.setDate=function(date){
this._start=this._unit.cloneValue(date);
};
Timeline.LinearEther.prototype.shiftPixels=function(_1a3){
var _1a4=this._interval*_1a3/this._pixelsPerInterval;
this._start=this._unit.change(this._start,_1a4);
};
Timeline.LinearEther.prototype.dateToPixelOffset=function(date){
var _1a6=this._unit.compare(date,this._start);
return this._pixelsPerInterval*_1a6/this._interval;
};
Timeline.LinearEther.prototype.pixelOffsetToDate=function(_1a7){
var _1a8=_1a7*this._interval/this._pixelsPerInterval;
return this._unit.change(this._start,_1a8);
};
Timeline.HotZoneEther=function(_1a9){
this._params=_1a9;
this._interval=_1a9.interval;
this._pixelsPerInterval=_1a9.pixelsPerInterval;
};
Timeline.HotZoneEther.prototype.initialize=function(_1aa){
this._timeline=_1aa;
this._unit=_1aa.getUnit();
this._zones=[{startTime:Number.NEGATIVE_INFINITY,endTime:Number.POSITIVE_INFINITY,magnify:1}];
var _1ab=this._params;
for(var i=0;i<_1ab.zones.length;i++){
var zone=_1ab.zones[i];
var _1ae=this._unit.parseFromObject(zone.start);
var _1af=this._unit.parseFromObject(zone.end);
for(var j=0;j<this._zones.length&&this._unit.compare(_1af,_1ae)>0;j++){
var _1b1=this._zones[j];
if(this._unit.compare(_1ae,_1b1.endTime)<0){
if(this._unit.compare(_1ae,_1b1.startTime)>0){
this._zones.splice(j,0,{startTime:_1b1.startTime,endTime:_1ae,magnify:_1b1.magnify});
j++;
_1b1.startTime=_1ae;
}
if(this._unit.compare(_1af,_1b1.endTime)<0){
this._zones.splice(j,0,{startTime:_1ae,endTime:_1af,magnify:zone.magnify*_1b1.magnify});
j++;
_1b1.startTime=_1af;
_1ae=_1af;
}else{
_1b1.magnify*=zone.magnify;
_1ae=_1b1.endTime;
}
}
}
}
if("startsOn" in this._params){
this._start=this._unit.parseFromObject(this._params.startsOn);
}else{
if("endsOn" in this._params){
this._start=this._unit.parseFromObject(this._params.endsOn);
this.shiftPixels(-this._timeline.getPixelLength());
}else{
if("centersOn" in this._params){
this._start=this._unit.parseFromObject(this._params.centersOn);
this.shiftPixels(-this._timeline.getPixelLength()/2);
}else{
this._start=this._unit.makeDefaultValue();
this.shiftPixels(-this._timeline.getPixelLength()/2);
}
}
}
};
Timeline.HotZoneEther.prototype.setDate=function(date){
this._start=this._unit.cloneValue(date);
};
Timeline.HotZoneEther.prototype.shiftPixels=function(_1b3){
this._start=this.pixelOffsetToDate(_1b3);
};
Timeline.HotZoneEther.prototype.dateToPixelOffset=function(date){
return this._dateDiffToPixelOffset(this._start,date);
};
Timeline.HotZoneEther.prototype.pixelOffsetToDate=function(_1b5){
return this._pixelOffsetToDate(_1b5,this._start);
};
Timeline.HotZoneEther.prototype._dateDiffToPixelOffset=function(_1b6,_1b7){
var _1b8=this._getScale();
var _1b9=_1b6;
var _1ba=_1b7;
var _1bb=0;
if(this._unit.compare(_1b9,_1ba)<0){
var z=0;
while(z<this._zones.length){
if(this._unit.compare(_1b9,this._zones[z].endTime)<0){
break;
}
z++;
}
while(this._unit.compare(_1b9,_1ba)<0){
var zone=this._zones[z];
var _1be=this._unit.earlier(_1ba,zone.endTime);
_1bb+=(this._unit.compare(_1be,_1b9)/(_1b8/zone.magnify));
_1b9=_1be;
z++;
}
}else{
var z=this._zones.length-1;
while(z>=0){
if(this._unit.compare(_1b9,this._zones[z].startTime)>0){
break;
}
z--;
}
while(this._unit.compare(_1b9,_1ba)>0){
var zone=this._zones[z];
var _1c1=this._unit.later(_1ba,zone.startTime);
_1bb+=(this._unit.compare(_1c1,_1b9)/(_1b8/zone.magnify));
_1b9=_1c1;
z--;
}
}
return _1bb;
};
Timeline.HotZoneEther.prototype._pixelOffsetToDate=function(_1c2,_1c3){
var _1c4=this._getScale();
var time=_1c3;
if(_1c2>0){
var z=0;
while(z<this._zones.length){
if(this._unit.compare(time,this._zones[z].endTime)<0){
break;
}
z++;
}
while(_1c2>0){
var zone=this._zones[z];
var _1c8=_1c4/zone.magnify;
if(zone.endTime==Number.POSITIVE_INFINITY){
time=this._unit.change(time,_1c2*_1c8);
_1c2=0;
}else{
var _1c9=this._unit.compare(zone.endTime,time)/_1c8;
if(_1c9>_1c2){
time=this._unit.change(time,_1c2*_1c8);
_1c2=0;
}else{
time=zone.endTime;
_1c2-=_1c9;
}
}
z++;
}
}else{
var z=this._zones.length-1;
while(z>=0){
if(this._unit.compare(time,this._zones[z].startTime)>0){
break;
}
z--;
}
_1c2=-_1c2;
while(_1c2>0){
var zone=this._zones[z];
var _1cc=_1c4/zone.magnify;
if(zone.startTime==Number.NEGATIVE_INFINITY){
time=this._unit.change(time,-_1c2*_1cc);
_1c2=0;
}else{
var _1cd=this._unit.compare(time,zone.startTime)/_1cc;
if(_1cd>_1c2){
time=this._unit.change(time,-_1c2*_1cc);
_1c2=0;
}else{
time=zone.startTime;
_1c2-=_1cd;
}
}
z--;
}
}
return time;
};
Timeline.HotZoneEther.prototype._getScale=function(){
return this._interval/this._pixelsPerInterval;
};
Timeline.GregorianEtherPainter=function(_1ce){
this._params=_1ce;
this._theme=_1ce.theme;
this._unit=_1ce.unit;
};
Timeline.GregorianEtherPainter.prototype.initialize=function(band,_1d0){
this._band=band;
this._timeline=_1d0;
this._backgroundLayer=band.createLayerDiv(0);
this._backgroundLayer.setAttribute("name","ether-background");
this._backgroundLayer.style.background=this._theme.ether.backgroundColors[band.getIndex()];
this._markerLayer=null;
this._lineLayer=null;
var _1d1=("align" in this._params)?this._params.align:this._theme.ether.interval.marker[_1d0.isHorizontal()?"hAlign":"vAlign"];
var _1d2=("showLine" in this._params)?this._params.showLine:this._theme.ether.interval.line.show;
this._intervalMarkerLayout=new Timeline.EtherIntervalMarkerLayout(this._timeline,this._band,this._theme,_1d1,_1d2);
this._highlight=new Timeline.EtherHighlight(this._timeline,this._band,this._theme,this._backgroundLayer);
};
Timeline.GregorianEtherPainter.prototype.setHighlight=function(_1d3,_1d4){
this._highlight.position(_1d3,_1d4);
};
Timeline.GregorianEtherPainter.prototype.paint=function(){
if(this._markerLayer){
this._band.removeLayerDiv(this._markerLayer);
}
this._markerLayer=this._band.createLayerDiv(100);
this._markerLayer.setAttribute("name","ether-markers");
this._markerLayer.style.display="none";
if(this._lineLayer){
this._band.removeLayerDiv(this._lineLayer);
}
this._lineLayer=this._band.createLayerDiv(1);
this._lineLayer.setAttribute("name","ether-lines");
this._lineLayer.style.display="none";
var _1d5=this._band.getMinDate();
var _1d6=this._band.getMaxDate();
var _1d7=this._band.getTimeZone();
var _1d8=this._band.getLabeller();
Timeline.DateTime.roundDownToInterval(_1d5,this._unit,_1d7,1,this._theme.firstDayOfWeek);
var p=this;
var _1da=function(date){
Timeline.DateTime.incrementByInterval(date,p._unit);
};
while(_1d5.getTime()<_1d6.getTime()){
this._intervalMarkerLayout.createIntervalMarker(_1d5,_1d8,this._unit,this._markerLayer,this._lineLayer);
_1da(_1d5);
}
this._markerLayer.style.display="block";
this._lineLayer.style.display="block";
};
Timeline.GregorianEtherPainter.prototype.softPaint=function(){
};
Timeline.HotZoneGregorianEtherPainter=function(_1dc){
this._params=_1dc;
this._theme=_1dc.theme;
this._zones=[{startTime:Number.NEGATIVE_INFINITY,endTime:Number.POSITIVE_INFINITY,unit:_1dc.unit,multiple:1}];
for(var i=0;i<_1dc.zones.length;i++){
var zone=_1dc.zones[i];
var _1df=Timeline.DateTime.parseGregorianDateTime(zone.start).getTime();
var _1e0=Timeline.DateTime.parseGregorianDateTime(zone.end).getTime();
for(var j=0;j<this._zones.length&&_1e0>_1df;j++){
var _1e2=this._zones[j];
if(_1df<_1e2.endTime){
if(_1df>_1e2.startTime){
this._zones.splice(j,0,{startTime:_1e2.startTime,endTime:_1df,unit:_1e2.unit,multiple:_1e2.multiple});
j++;
_1e2.startTime=_1df;
}
if(_1e0<_1e2.endTime){
this._zones.splice(j,0,{startTime:_1df,endTime:_1e0,unit:zone.unit,multiple:(zone.multiple)?zone.multiple:1});
j++;
_1e2.startTime=_1e0;
_1df=_1e0;
}else{
_1e2.multiple=zone.multiple;
_1e2.unit=zone.unit;
_1df=_1e2.endTime;
}
}
}
}
};
Timeline.HotZoneGregorianEtherPainter.prototype.initialize=function(band,_1e4){
this._band=band;
this._timeline=_1e4;
this._backgroundLayer=band.createLayerDiv(0);
this._backgroundLayer.setAttribute("name","ether-background");
this._backgroundLayer.style.background=this._theme.ether.backgroundColors[band.getIndex()];
this._markerLayer=null;
this._lineLayer=null;
var _1e5=("align" in this._params)?this._params.align:this._theme.ether.interval.marker[_1e4.isHorizontal()?"hAlign":"vAlign"];
var _1e6=("showLine" in this._params)?this._params.showLine:this._theme.ether.interval.line.show;
this._intervalMarkerLayout=new Timeline.EtherIntervalMarkerLayout(this._timeline,this._band,this._theme,_1e5,_1e6);
this._highlight=new Timeline.EtherHighlight(this._timeline,this._band,this._theme,this._backgroundLayer);
};
Timeline.HotZoneGregorianEtherPainter.prototype.setHighlight=function(_1e7,_1e8){
this._highlight.position(_1e7,_1e8);
};
Timeline.HotZoneGregorianEtherPainter.prototype.paint=function(){
if(this._markerLayer){
this._band.removeLayerDiv(this._markerLayer);
}
this._markerLayer=this._band.createLayerDiv(100);
this._markerLayer.setAttribute("name","ether-markers");
this._markerLayer.style.display="none";
if(this._lineLayer){
this._band.removeLayerDiv(this._lineLayer);
}
this._lineLayer=this._band.createLayerDiv(1);
this._lineLayer.setAttribute("name","ether-lines");
this._lineLayer.style.display="none";
var _1e9=this._band.getMinDate();
var _1ea=this._band.getMaxDate();
var _1eb=this._band.getTimeZone();
var _1ec=this._band.getLabeller();
var p=this;
var _1ee=function(date,zone){
for(var i=0;i<zone.multiple;i++){
Timeline.DateTime.incrementByInterval(date,zone.unit);
}
};
var _1f2=0;
while(_1f2<this._zones.length){
if(_1e9.getTime()<this._zones[_1f2].endTime){
break;
}
_1f2++;
}
var zEnd=this._zones.length-1;
while(zEnd>=0){
if(_1ea.getTime()>this._zones[zEnd].startTime){
break;
}
zEnd--;
}
for(var z=_1f2;z<=zEnd;z++){
var zone=this._zones[z];
var _1f6=new Date(Math.max(_1e9.getTime(),zone.startTime));
var _1f7=new Date(Math.min(_1ea.getTime(),zone.endTime));
Timeline.DateTime.roundDownToInterval(_1f6,zone.unit,_1eb,zone.multiple,this._theme.firstDayOfWeek);
Timeline.DateTime.roundUpToInterval(_1f7,zone.unit,_1eb,zone.multiple,this._theme.firstDayOfWeek);
while(_1f6.getTime()<_1f7.getTime()){
this._intervalMarkerLayout.createIntervalMarker(_1f6,_1ec,zone.unit,this._markerLayer,this._lineLayer);
_1ee(_1f6,zone);
}
}
this._markerLayer.style.display="block";
this._lineLayer.style.display="block";
};
Timeline.HotZoneGregorianEtherPainter.prototype.softPaint=function(){
};
Timeline.YearCountEtherPainter=function(_1f8){
this._params=_1f8;
this._theme=_1f8.theme;
this._startDate=Timeline.DateTime.parseGregorianDateTime(_1f8.startDate);
this._multiple=("multiple" in _1f8)?_1f8.multiple:1;
};
Timeline.YearCountEtherPainter.prototype.initialize=function(band,_1fa){
this._band=band;
this._timeline=_1fa;
this._backgroundLayer=band.createLayerDiv(0);
this._backgroundLayer.setAttribute("name","ether-background");
this._backgroundLayer.style.background=this._theme.ether.backgroundColors[band.getIndex()];
this._markerLayer=null;
this._lineLayer=null;
var _1fb=("align" in this._params)?this._params.align:this._theme.ether.interval.marker[_1fa.isHorizontal()?"hAlign":"vAlign"];
var _1fc=("showLine" in this._params)?this._params.showLine:this._theme.ether.interval.line.show;
this._intervalMarkerLayout=new Timeline.EtherIntervalMarkerLayout(this._timeline,this._band,this._theme,_1fb,_1fc);
this._highlight=new Timeline.EtherHighlight(this._timeline,this._band,this._theme,this._backgroundLayer);
};
Timeline.YearCountEtherPainter.prototype.setHighlight=function(_1fd,_1fe){
this._highlight.position(_1fd,_1fe);
};
Timeline.YearCountEtherPainter.prototype.paint=function(){
if(this._markerLayer){
this._band.removeLayerDiv(this._markerLayer);
}
this._markerLayer=this._band.createLayerDiv(100);
this._markerLayer.setAttribute("name","ether-markers");
this._markerLayer.style.display="none";
if(this._lineLayer){
this._band.removeLayerDiv(this._lineLayer);
}
this._lineLayer=this._band.createLayerDiv(1);
this._lineLayer.setAttribute("name","ether-lines");
this._lineLayer.style.display="none";
var _1ff=new Date(this._startDate.getTime());
var _200=this._band.getMaxDate();
var _201=this._band.getMinDate().getUTCFullYear()-this._startDate.getUTCFullYear();
_1ff.setUTCFullYear(this._band.getMinDate().getUTCFullYear()-_201%this._multiple);
var p=this;
var _203=function(date){
for(var i=0;i<p._multiple;i++){
Timeline.DateTime.incrementByInterval(date,Timeline.DateTime.YEAR);
}
};
var _206={labelInterval:function(date,_208){
var diff=date.getUTCFullYear()-p._startDate.getUTCFullYear();
return {text:diff,emphasized:diff==0};
}};
while(_1ff.getTime()<_200.getTime()){
this._intervalMarkerLayout.createIntervalMarker(_1ff,_206,Timeline.DateTime.YEAR,this._markerLayer,this._lineLayer);
_203(_1ff);
}
this._markerLayer.style.display="block";
this._lineLayer.style.display="block";
};
Timeline.YearCountEtherPainter.prototype.softPaint=function(){
};
Timeline.QuarterlyEtherPainter=function(_20a){
this._params=_20a;
this._theme=_20a.theme;
this._startDate=Timeline.DateTime.parseGregorianDateTime(_20a.startDate);
};
Timeline.QuarterlyEtherPainter.prototype.initialize=function(band,_20c){
this._band=band;
this._timeline=_20c;
this._backgroundLayer=band.createLayerDiv(0);
this._backgroundLayer.setAttribute("name","ether-background");
this._backgroundLayer.style.background=this._theme.ether.backgroundColors[band.getIndex()];
this._markerLayer=null;
this._lineLayer=null;
var _20d=("align" in this._params)?this._params.align:this._theme.ether.interval.marker[_20c.isHorizontal()?"hAlign":"vAlign"];
var _20e=("showLine" in this._params)?this._params.showLine:this._theme.ether.interval.line.show;
this._intervalMarkerLayout=new Timeline.EtherIntervalMarkerLayout(this._timeline,this._band,this._theme,_20d,_20e);
this._highlight=new Timeline.EtherHighlight(this._timeline,this._band,this._theme,this._backgroundLayer);
};
Timeline.QuarterlyEtherPainter.prototype.setHighlight=function(_20f,_210){
this._highlight.position(_20f,_210);
};
Timeline.QuarterlyEtherPainter.prototype.paint=function(){
if(this._markerLayer){
this._band.removeLayerDiv(this._markerLayer);
}
this._markerLayer=this._band.createLayerDiv(100);
this._markerLayer.setAttribute("name","ether-markers");
this._markerLayer.style.display="none";
if(this._lineLayer){
this._band.removeLayerDiv(this._lineLayer);
}
this._lineLayer=this._band.createLayerDiv(1);
this._lineLayer.setAttribute("name","ether-lines");
this._lineLayer.style.display="none";
var _211=new Date(0);
var _212=this._band.getMaxDate();
_211.setUTCFullYear(Math.max(this._startDate.getUTCFullYear(),this._band.getMinDate().getUTCFullYear()));
_211.setUTCMonth(this._startDate.getUTCMonth());
var p=this;
var _214=function(date){
date.setUTCMonth(date.getUTCMonth()+3);
};
var _216={labelInterval:function(date,_218){
var _219=(4+(date.getUTCMonth()-p._startDate.getUTCMonth())/3)%4;
if(_219!=0){
return {text:"Q"+(_219+1),emphasized:false};
}else{
return {text:"Y"+(date.getUTCFullYear()-p._startDate.getUTCFullYear()+1),emphasized:true};
}
}};
while(_211.getTime()<_212.getTime()){
this._intervalMarkerLayout.createIntervalMarker(_211,_216,Timeline.DateTime.YEAR,this._markerLayer,this._lineLayer);
_214(_211);
}
this._markerLayer.style.display="block";
this._lineLayer.style.display="block";
};
Timeline.QuarterlyEtherPainter.prototype.softPaint=function(){
};
Timeline.EtherIntervalMarkerLayout=function(_21a,band,_21c,_21d,_21e){
var _21f=_21a.isHorizontal();
if(_21f){
if(_21d=="Top"){
this.positionDiv=function(div,_221){
div.style.left=_221+"px";
div.style.top="0px";
};
}else{
this.positionDiv=function(div,_223){
div.style.left=_223+"px";
div.style.bottom="0px";
};
}
}else{
if(_21d=="Left"){
this.positionDiv=function(div,_225){
div.style.top=_225+"px";
div.style.left="0px";
};
}else{
this.positionDiv=function(div,_227){
div.style.top=_227+"px";
div.style.right="0px";
};
}
}
var _228=_21c.ether.interval.marker;
var _229=_21c.ether.interval.line;
var _22a=_21c.ether.interval.weekend;
var _22b=(_21f?"h":"v")+_21d;
var _22c=_228[_22b+"Styler"];
var _22d=_228[_22b+"EmphasizedStyler"];
var day=Timeline.DateTime.gregorianUnitLengths[Timeline.DateTime.DAY];
this.createIntervalMarker=function(date,_230,unit,_232,_233){
var _234=Math.round(band.dateToPixelOffset(date));
if(_21e&&unit!=Timeline.DateTime.WEEK){
var _235=_21a.getDocument().createElement("div");
_235.style.position="absolute";
if(_229.opacity<100){
Timeline.Graphics.setOpacity(_235,_229.opacity);
}
if(_21f){
_235.style.borderLeft="1px solid "+_229.color;
_235.style.left=_234+"px";
_235.style.width="1px";
_235.style.top="0px";
_235.style.height="100%";
}else{
_235.style.borderTop="1px solid "+_229.color;
_235.style.top=_234+"px";
_235.style.height="1px";
_235.style.left="0px";
_235.style.width="100%";
}
_233.appendChild(_235);
}
if(unit==Timeline.DateTime.WEEK){
var _236=_21c.firstDayOfWeek;
var _237=new Date(date.getTime()+(6-_236-7)*day);
var _238=new Date(_237.getTime()+2*day);
var _239=Math.round(band.dateToPixelOffset(_237));
var _23a=Math.round(band.dateToPixelOffset(_238));
var _23b=Math.max(1,_23a-_239);
var _23c=_21a.getDocument().createElement("div");
_23c.style.position="absolute";
_23c.style.background=_22a.color;
if(_22a.opacity<100){
Timeline.Graphics.setOpacity(_23c,_22a.opacity);
}
if(_21f){
_23c.style.left=_239+"px";
_23c.style.width=_23b+"px";
_23c.style.top="0px";
_23c.style.height="100%";
}else{
_23c.style.top=_239+"px";
_23c.style.height=_23b+"px";
_23c.style.left="0px";
_23c.style.width="100%";
}
_233.appendChild(_23c);
}
var _23d=_230.labelInterval(date,unit);
var div=_21a.getDocument().createElement("div");
div.innerHTML=_23d.text;
div.style.position="absolute";
(_23d.emphasized?_22d:_22c)(div);
this.positionDiv(div,_234);
_232.appendChild(div);
return div;
};
};
Timeline.EtherHighlight=function(_23f,band,_241,_242){
var _243=_23f.isHorizontal();
this._highlightDiv=null;
this._createHighlightDiv=function(){
if(this._highlightDiv==null){
this._highlightDiv=_23f.getDocument().createElement("div");
this._highlightDiv.setAttribute("name","ether-highlight");
this._highlightDiv.style.position="absolute";
this._highlightDiv.style.background=_241.ether.highlightColor;
var _244=_241.ether.highlightOpacity;
if(_244<100){
Timeline.Graphics.setOpacity(this._highlightDiv,_244);
}
_242.appendChild(this._highlightDiv);
}
};
this.position=function(_245,_246){
this._createHighlightDiv();
var _247=Math.round(band.dateToPixelOffset(_245));
var _248=Math.round(band.dateToPixelOffset(_246));
var _249=Math.max(_248-_247,3);
if(_243){
this._highlightDiv.style.left=_247+"px";
this._highlightDiv.style.width=_249+"px";
this._highlightDiv.style.top="2px";
this._highlightDiv.style.height=(band.getViewWidth()-4)+"px";
}else{
this._highlightDiv.style.top=_247+"px";
this._highlightDiv.style.height=_249+"px";
this._highlightDiv.style.left="2px";
this._highlightDiv.style.width=(band.getViewWidth()-4)+"px";
}
};
};
Timeline.GregorianDateLabeller=function(_24a,_24b){
this._locale=_24a;
this._timeZone=_24b;
};
Timeline.GregorianDateLabeller.monthNames=[];
Timeline.GregorianDateLabeller.dayNames=[];
Timeline.GregorianDateLabeller.labelIntervalFunctions=[];
Timeline.GregorianDateLabeller.getMonthName=function(_24c,_24d){
return Timeline.GregorianDateLabeller.monthNames[_24d][_24c];
};
Timeline.GregorianDateLabeller.prototype.labelInterval=function(date,_24f){
var f=Timeline.GregorianDateLabeller.labelIntervalFunctions[this._locale];
if(f==null){
f=Timeline.GregorianDateLabeller.prototype.defaultLabelInterval;
}
return f.call(this,date,_24f);
};
Timeline.GregorianDateLabeller.prototype.labelPrecise=function(date){
return Timeline.DateTime.removeTimeZoneOffset(date,this._timeZone).toUTCString();
};
Timeline.GregorianDateLabeller.prototype.defaultLabelInterval=function(date,_253){
var text;
var _255=false;
date=Timeline.DateTime.removeTimeZoneOffset(date,this._timeZone);
switch(_253){
case Timeline.DateTime.MILLISECOND:
text=date.getUTCMilliseconds();
break;
case Timeline.DateTime.SECOND:
text=date.getUTCSeconds();
break;
case Timeline.DateTime.MINUTE:
var m=date.getUTCMinutes();
if(m==0){
text=date.getUTCHours()+":00";
_255=true;
}else{
text=m;
}
break;
case Timeline.DateTime.HOUR:
text=date.getUTCHours()+"hr";
break;
case Timeline.DateTime.DAY:
text=Timeline.GregorianDateLabeller.getMonthName(date.getUTCMonth(),this._locale)+" "+date.getUTCDate();
break;
case Timeline.DateTime.WEEK:
text=Timeline.GregorianDateLabeller.getMonthName(date.getUTCMonth(),this._locale)+" "+date.getUTCDate();
break;
case Timeline.DateTime.MONTH:
var m=date.getUTCMonth();
if(m==0){
text=this.labelInterval(date,Timeline.DateTime.YEAR).text;
_255=true;
}else{
text=Timeline.GregorianDateLabeller.getMonthName(m,this._locale);
}
break;
case Timeline.DateTime.YEAR:
case Timeline.DateTime.DECADE:
case Timeline.DateTime.CENTURY:
case Timeline.DateTime.MILLENNIUM:
var y=date.getUTCFullYear();
if(y>0){
text=date.getUTCFullYear();
}else{
text=(1-y)+"BC";
}
_255=(_253==Timeline.DateTime.DECADE&&y%100==0)||(_253==Timeline.DateTime.CENTURY&&y%1000==0);
break;
default:
text=date.toUTCString();
}
return {text:text,emphasized:_255};
};
Timeline.DefaultEventSource=function(_259){
this._events=(_259 instanceof Object)?_259:new Timeline.EventIndex();
this._listeners=[];
};
Timeline.DefaultEventSource.prototype.addListener=function(_25a){
this._listeners.push(_25a);
};
Timeline.DefaultEventSource.prototype.removeListener=function(_25b){
for(var i=0;i<this._listeners.length;i++){
if(this._listeners[i]==_25b){
this._listeners.splice(i,1);
break;
}
}
};
Timeline.DefaultEventSource.prototype.loadXML=function(xml,url){
var base=this._getBaseURL(url);
var _260=xml.documentElement.getAttribute("date-time-format");
var _261=this._events.getUnit().getParser(_260);
var node=xml.documentElement.firstChild;
var _263=false;
while(node!=null){
if(node.nodeType==1){
var _264="";
if(node.firstChild!=null&&node.firstChild.nodeType==3){
_264=node.firstChild.nodeValue;
}
var evt=new Timeline.DefaultEventSource.Event(_261(node.getAttribute("start")),_261(node.getAttribute("end")),_261(node.getAttribute("latestStart")),_261(node.getAttribute("earliestEnd")),node.getAttribute("isDuration")!="true",node.getAttribute("title"),_264,this._resolveRelativeURL(node.getAttribute("image"),base),this._resolveRelativeURL(node.getAttribute("link"),base),this._resolveRelativeURL(node.getAttribute("icon"),base),node.getAttribute("color"),node.getAttribute("textColor"));
evt._node=node;
evt.getProperty=function(name){
return this._node.getAttribute(name);
};
this._events.add(evt);
_263=true;
}
node=node.nextSibling;
}
if(_263){
this._fire("onAddMany",[]);
}
};
Timeline.DefaultEventSource.prototype.loadJSON=function(data,url){
var base=this._getBaseURL(url);
var _26a=false;
if(data&&data.events){
var _26b=("dateTimeFormat" in data)?data.dateTimeFormat:null;
var _26c=this._events.getUnit().getParser(_26b);
for(var i=0;i<data.events.length;i++){
var _26e=data.events[i];
var evt=new Timeline.DefaultEventSource.Event(_26c(_26e.start),_26c(_26e.end),_26c(_26e.latestStart),_26c(_26e.earliestEnd),_26e.isDuration||false,_26e.title,_26e.description,this._resolveRelativeURL(_26e.image,base),this._resolveRelativeURL(_26e.link,base),this._resolveRelativeURL(_26e.icon,base),_26e.color,_26e.textColor);
evt._obj=_26e;
evt.getProperty=function(name){
return this._obj[name];
};
this._events.add(evt);
_26a=true;
}
}
if(_26a){
this._fire("onAddMany",[]);
}
};
Timeline.DefaultEventSource.prototype.loadSPARQL=function(xml,url){
var base=this._getBaseURL(url);
var _274="iso8601";
var _275=this._events.getUnit().getParser(_274);
if(xml==null){
return null;
}
var node=xml.documentElement.firstChild;
while(node!=null&&(node.nodeType!=1||node.nodeName!="results")){
node=node.nextSibling;
}
if(node!=null){
node=node.firstChild;
}
var _277=false;
while(node!=null){
if(node.nodeType==1){
var _278={};
var _279=node.firstChild;
while(_279!=null){
if(_279.nodeType==1&&_279.firstChild!=null&&_279.firstChild.nodeType==1&&_279.firstChild.firstChild!=null&&_279.firstChild.firstChild.nodeType==3){
_278[_279.getAttribute("name")]=_279.firstChild.firstChild.nodeValue;
}
_279=_279.nextSibling;
}
if(_278["start"]==null&&_278["date"]!=null){
_278["start"]=_278["date"];
}
var evt=new Timeline.DefaultEventSource.Event(_275(_278["start"]),_275(_278["end"]),_275(_278["latestStart"]),_275(_278["earliestEnd"]),_278["isDuration"]!="true",_278["title"],_278["description"],this._resolveRelativeURL(_278["image"],base),this._resolveRelativeURL(_278["link"],base),this._resolveRelativeURL(_278["icon"],base),_278["color"],_278["textColor"]);
evt._bindings=_278;
evt.getProperty=function(name){
return this._bindings[name];
};
this._events.add(evt);
_277=true;
}
node=node.nextSibling;
}
if(_277){
this._fire("onAddMany",[]);
}
};
Timeline.DefaultEventSource.prototype.add=function(evt){
this._events.add(evt);
this._fire("onAddOne",[evt]);
};
Timeline.DefaultEventSource.prototype.clear=function(){
this._events.removeAll();
this._fire("onClear",[]);
};
Timeline.DefaultEventSource.prototype.getEventIterator=function(_27d,_27e){
return this._events.getIterator(_27d,_27e);
};
Timeline.DefaultEventSource.prototype.getAllEventIterator=function(){
return this._events.getAllIterator();
};
Timeline.DefaultEventSource.prototype.getCount=function(){
return this._events.getCount();
};
Timeline.DefaultEventSource.prototype.getEarliestDate=function(){
return this._events.getEarliestDate();
};
Timeline.DefaultEventSource.prototype.getLatestDate=function(){
return this._events.getLatestDate();
};
Timeline.DefaultEventSource.prototype._fire=function(_27f,args){
for(var i=0;i<this._listeners.length;i++){
var _282=this._listeners[i];
if(_27f in _282){
try{
_282[_27f].apply(_282,args);
}
catch(e){
Timeline.Debug.exception(e);
}
}
}
};
Timeline.DefaultEventSource.prototype._getBaseURL=function(url){
if(url.indexOf("://")<0){
var url2=this._getBaseURL(document.location.href);
if(url.substr(0,1)=="/"){
url=url2.substr(0,url2.indexOf("/",url2.indexOf("://")+3))+url;
}else{
url=url2+url;
}
}
var i=url.lastIndexOf("/");
if(i<0){
return "";
}else{
return url.substr(0,i+1);
}
};
Timeline.DefaultEventSource.prototype._resolveRelativeURL=function(url,base){
if(url==null||url==""){
return url;
}else{
if(url.indexOf("://")>0){
return url;
}else{
if(url.substr(0,1)=="/"){
return base.substr(0,base.indexOf("/",base.indexOf("://")+3))+url;
}else{
return base+url;
}
}
}
};
Timeline.DefaultEventSource.Event=function(_288,end,_28a,_28b,_28c,text,_28e,_28f,link,icon,_292,_293){
this._id="e"+Math.floor(Math.random()*1000000);
this._instant=_28c||(end==null);
this._start=_288;
this._end=(end!=null)?end:_288;
this._latestStart=(_28a!=null)?_28a:(_28c?this._end:this._start);
this._earliestEnd=(_28b!=null)?_28b:(_28c?this._start:this._end);
this._text=text;
this._description=_28e;
this._image=(_28f!=null&&_28f!="")?_28f:null;
this._link=(link!=null&&link!="")?link:null;
this._icon=(icon!=null&&icon!="")?icon:null;
this._color=(_292!=null&&_292!="")?_292:null;
this._textColor=(_293!=null&&_293!="")?_293:null;
};
Timeline.DefaultEventSource.Event.prototype={getID:function(){
return this._id;
},isInstant:function(){
return this._instant;
},isImprecise:function(){
return this._start!=this._latestStart||this._end!=this._earliestEnd;
},getStart:function(){
return this._start;
},getEnd:function(){
return this._end;
},getLatestStart:function(){
return this._latestStart;
},getEarliestEnd:function(){
return this._earliestEnd;
},getText:function(){
return this._text;
},getDescription:function(){
return this._description;
},getImage:function(){
return this._image;
},getLink:function(){
return this._link;
},getIcon:function(){
return this._icon;
},getColor:function(){
return this._color;
},getTextColor:function(){
return this._textColor;
},getProperty:function(name){
return null;
},fillDescription:function(elmt){
elmt.innerHTML=this._description;
},fillTime:function(elmt,_297){
if(this._instant){
if(this.isImprecise()){
elmt.appendChild(elmt.ownerDocument.createTextNode(_297.labelPrecise(this._start)));
elmt.appendChild(elmt.ownerDocument.createElement("br"));
elmt.appendChild(elmt.ownerDocument.createTextNode(_297.labelPrecise(this._end)));
}else{
elmt.appendChild(elmt.ownerDocument.createTextNode(_297.labelPrecise(this._start)));
}
}else{
if(this.isImprecise()){
elmt.appendChild(elmt.ownerDocument.createTextNode(_297.labelPrecise(this._start)+" ~ "+_297.labelPrecise(this._latestStart)));
elmt.appendChild(elmt.ownerDocument.createElement("br"));
elmt.appendChild(elmt.ownerDocument.createTextNode(_297.labelPrecise(this._earliestEnd)+" ~ "+_297.labelPrecise(this._end)));
}else{
elmt.appendChild(elmt.ownerDocument.createTextNode(_297.labelPrecise(this._start)));
elmt.appendChild(elmt.ownerDocument.createElement("br"));
elmt.appendChild(elmt.ownerDocument.createTextNode(_297.labelPrecise(this._end)));
}
}
},fillInfoBubble:function(elmt,_299,_29a){
var doc=elmt.ownerDocument;
var _29c=this.getText();
var link=this.getLink();
var _29e=this.getImage();
if(_29e!=null){
var img=doc.createElement("img");
img.src=_29e;
_299.event.bubble.imageStyler(img);
elmt.appendChild(img);
}
var _2a0=doc.createElement("div");
var _2a1=doc.createTextNode(_29c);
if(link!=null){
var a=doc.createElement("a");
a.href=link;
a.appendChild(_2a1);
_2a0.appendChild(a);
}else{
_2a0.appendChild(_2a1);
}
_299.event.bubble.titleStyler(_2a0);
elmt.appendChild(_2a0);
var _2a3=doc.createElement("div");
this.fillDescription(_2a3);
_299.event.bubble.bodyStyler(_2a3);
elmt.appendChild(_2a3);
var _2a4=doc.createElement("div");
this.fillTime(_2a4,_29a);
_299.event.bubble.timeStyler(_2a4);
elmt.appendChild(_2a4);
}};
Timeline.StaticTrackBasedLayout=function(_2a5){
this._eventSource=_2a5.eventSource;
this._ether=_2a5.ether;
this._theme=_2a5.theme;
this._showText=("showText" in _2a5)?_2a5.showText:true;
this._laidout=false;
var _2a6=this;
if(this._eventSource!=null){
this._eventSource.addListener({onAddMany:function(){
_2a6._laidout=false;
}});
}
};
Timeline.StaticTrackBasedLayout.prototype.initialize=function(_2a7){
this._timeline=_2a7;
};
Timeline.StaticTrackBasedLayout.prototype.getTrack=function(evt){
if(!this._laidout){
this._tracks=[];
this._layout();
this._laidout=true;
}
return this._tracks[evt.getID()];
};
Timeline.StaticTrackBasedLayout.prototype.getTrackCount=function(){
if(!this._laidout){
this._tracks=[];
this._layout();
this._laidout=true;
}
return this._trackCount;
};
Timeline.StaticTrackBasedLayout.prototype._layout=function(){
if(this._eventSource==null){
return;
}
var _2a9=[Number.NEGATIVE_INFINITY];
var _2aa=this;
var _2ab=this._showText;
var _2ac=this._theme;
var _2ad=_2ac.event;
var _2ae=function(evt,_2b0,_2b1,_2b2){
var _2b3=_2b0-1;
if(evt.isImprecise()){
_2b3=_2b1;
}
if(_2ab){
_2b3=Math.max(_2b3,_2b0+_2ad.label.width);
}
return _2b3;
};
var _2b4=function(evt,_2b6,_2b7,_2b8){
if(evt.isImprecise()){
var _2b9=evt.getStart();
var _2ba=evt.getEnd();
var _2bb=Math.round(_2aa._ether.dateToPixelOffset(_2b9));
var _2bc=Math.round(_2aa._ether.dateToPixelOffset(_2ba));
}else{
var _2bd=_2b6;
var _2be=_2b7;
}
var _2bf=_2be;
var _2c0=Math.max(_2be-_2bd,1);
if(_2ab){
if(_2c0<_2ad.label.width){
_2bf=_2be+_2ad.label.width;
}
}
return _2bf;
};
var _2c1=function(evt){
var _2c3=evt.getStart();
var _2c4=evt.getEnd();
var _2c5=Math.round(_2aa._ether.dateToPixelOffset(_2c3));
var _2c6=Math.round(_2aa._ether.dateToPixelOffset(_2c4));
var _2c7=0;
for(;_2c7<_2a9.length;_2c7++){
if(_2a9[_2c7]<_2c5){
break;
}
}
if(_2c7>=_2a9.length){
_2a9.push(Number.NEGATIVE_INFINITY);
}
var _2c8=(_2ad.track.offset+_2c7*(_2ad.track.height+_2ad.track.gap))+"em";
_2aa._tracks[evt.getID()]=_2c7;
if(evt.isInstant()){
_2a9[_2c7]=_2ae(evt,_2c5,_2c6,_2c8);
}else{
_2a9[_2c7]=_2b4(evt,_2c5,_2c6,_2c8);
}
};
var _2c9=this._eventSource.getAllEventIterator();
while(_2c9.hasNext()){
var evt=_2c9.next();
_2c1(evt);
}
this._trackCount=_2a9.length;
};
Timeline.DurationEventPainter=function(_2cb){
this._params=_2cb;
this._theme=_2cb.theme;
this._layout=_2cb.layout;
this._showText=_2cb.showText;
this._showLineForNoText=("showLineForNoText" in _2cb)?_2cb.showLineForNoText:_2cb.theme.event.instant.showLineForNoText;
this._filterMatcher=null;
this._highlightMatcher=null;
};
Timeline.DurationEventPainter.prototype.initialize=function(band,_2cd){
this._band=band;
this._timeline=_2cd;
this._layout.initialize(band,_2cd);
this._eventLayer=null;
this._highlightLayer=null;
};
Timeline.DurationEventPainter.prototype.getLayout=function(){
return this._layout;
};
Timeline.DurationEventPainter.prototype.setLayout=function(_2ce){
this._layout=_2ce;
};
Timeline.DurationEventPainter.prototype.getFilterMatcher=function(){
return this._filterMatcher;
};
Timeline.DurationEventPainter.prototype.setFilterMatcher=function(_2cf){
this._filterMatcher=_2cf;
};
Timeline.DurationEventPainter.prototype.getHighlightMatcher=function(){
return this._highlightMatcher;
};
Timeline.DurationEventPainter.prototype.setHighlightMatcher=function(_2d0){
this._highlightMatcher=_2d0;
};
Timeline.DurationEventPainter.prototype.paint=function(){
var _2d1=this._band.getEventSource();
if(_2d1==null){
return;
}
if(this._highlightLayer!=null){
this._band.removeLayerDiv(this._highlightLayer);
}
this._highlightLayer=this._band.createLayerDiv(105);
this._highlightLayer.setAttribute("name","event-highlights");
this._highlightLayer.style.display="none";
if(this._eventLayer!=null){
this._band.removeLayerDiv(this._eventLayer);
}
this._eventLayer=this._band.createLayerDiv(110);
this._eventLayer.setAttribute("name","events");
this._eventLayer.style.display="none";
var _2d2=this._band.getMinDate();
var _2d3=this._band.getMaxDate();
var doc=this._timeline.getDocument();
var p=this;
var _2d6=this._eventLayer;
var _2d7=this._highlightLayer;
var _2d8=this._showText;
var _2d9=this._params.theme;
var _2da=_2d9.event;
var _2db=_2da.track.offset;
var _2dc=("trackHeight" in this._params)?this._params.trackHeight:_2da.track.height;
var _2dd=("trackGap" in this._params)?this._params.trackGap:_2da.track.gap;
var _2de=function(evt,div){
var icon=evt.getIcon();
var img=Timeline.Graphics.createTranslucentImage(doc,icon!=null?icon:_2da.instant.icon);
div.appendChild(img);
div.style.cursor="pointer";
Timeline.DOM.registerEvent(div,"mousedown",function(elmt,_2e4,_2e5){
p._onClickInstantEvent(img,_2e4,evt);
});
};
var _2e6=function(_2e7,_2e8,_2e9,_2ea,_2eb){
if(_2e7>=0){
var _2ec=_2da.highlightColors[Math.min(_2e7,_2da.highlightColors.length-1)];
var div=doc.createElement("div");
div.style.position="absolute";
div.style.overflow="hidden";
div.style.left=(_2e8-3)+"px";
div.style.width=(_2e9+6)+"px";
div.style.top=_2ea+"em";
div.style.height=_2eb+"em";
div.style.background=_2ec;
_2d7.appendChild(div);
}
};
var _2ee=function(evt,_2f0,_2f1,_2f2,_2f3,_2f4,_2f5){
if(evt.isImprecise()){
var _2f6=Math.max(_2f1-_2f0,1);
var _2f7=doc.createElement("div");
_2f7.style.position="absolute";
_2f7.style.overflow="hidden";
_2f7.style.top=_2f2;
_2f7.style.height=_2dc+"em";
_2f7.style.left=_2f0+"px";
_2f7.style.width=_2f6+"px";
_2f7.style.background=_2da.instant.impreciseColor;
if(_2da.instant.impreciseOpacity<100){
Timeline.Graphics.setOpacity(_2f7,_2da.instant.impreciseOpacity);
}
_2d6.appendChild(_2f7);
}
var div=doc.createElement("div");
div.style.position="absolute";
div.style.overflow="hidden";
_2d6.appendChild(div);
var _2f9=evt.getTextColor();
var _2fa=evt.getColor();
var _2fb=-8;
var _2fc=16;
if(_2d8){
div.style.width=_2da.label.width+"px";
div.style.color=_2f9!=null?_2f9:_2da.label.outsideColor;
_2de(evt,div);
div.appendChild(doc.createTextNode(evt.getText()));
}else{
if(p._showLineForNoText){
div.style.width="1px";
div.style.borderLeft="1px solid "+(_2fa!=null?_2fa:_2da.instant.lineColor);
_2fb=0;
_2fc=1;
}else{
_2de(evt,div);
}
}
div.style.top=_2f2;
div.style.height=_2dc+"em";
div.style.left=(_2f0+_2fb)+"px";
_2e6(_2f3,(_2f0+_2fb),_2fc,_2f4,_2f5);
};
var _2fd=function(evt,_2ff,_300,_301,_302,_303,_304){
var _305=function(elmt){
elmt.style.cursor="pointer";
Timeline.DOM.registerEvent(elmt,"mousedown",function(elmt,_308,_309){
p._onClickDurationEvent(_308,evt,_309);
});
};
var _30a=Math.max(_300-_2ff,1);
if(evt.isImprecise()){
var div=doc.createElement("div");
div.style.position="absolute";
div.style.overflow="hidden";
div.style.top=_301;
div.style.height=_2dc+"em";
div.style.left=_2ff+"px";
div.style.width=_30a+"px";
div.style.background=_2da.duration.impreciseColor;
if(_2da.duration.impreciseOpacity<100){
Timeline.Graphics.setOpacity(div,_2da.duration.impreciseOpacity);
}
_2d6.appendChild(div);
var _30c=evt.getLatestStart();
var _30d=evt.getEarliestEnd();
var _30e=Math.round(p._band.dateToPixelOffset(_30c));
var _30f=Math.round(p._band.dateToPixelOffset(_30d));
}else{
var _310=_2ff;
var _311=_300;
}
var _312=evt.getTextColor();
var _313=true;
if(_310<=_311){
_30a=Math.max(_311-_310,1);
_313=!(_30a>_2da.label.width);
div=doc.createElement("div");
div.style.position="absolute";
div.style.overflow="hidden";
div.style.top=_301;
div.style.height=_2dc+"em";
div.style.left=_310+"px";
div.style.width=_30a+"px";
var _314=evt.getColor();
div.style.background=_314!=null?_314:_2da.duration.color;
if(_2da.duration.opacity<100){
Timeline.Graphics.setOpacity(div,_2da.duration.opacity);
}
_2d6.appendChild(div);
}else{
var temp=_310;
_310=_311;
_311=temp;
}
if(div==null){
console.log(evt);
}
_305(div);
if(_2d8){
var _316=doc.createElement("div");
_316.style.position="absolute";
_316.style.top=_301;
_316.style.height=_2dc+"em";
_316.style.left=((_30a>_2da.label.width)?_310:_311)+"px";
_316.style.width=_2da.label.width+"px";
_316.style.color=_312!=null?_312:(_313?_2da.label.outsideColor:_2da.label.insideColor);
_316.style.overflow="hidden";
_316.appendChild(doc.createTextNode(evt.getText()));
_2d6.appendChild(_316);
_305(_316);
}
_2e6(_302,_2ff,_300-_2ff,_303,_304);
};
var _317=function(evt,_319){
var _31a=evt.getStart();
var _31b=evt.getEnd();
var _31c=Math.round(p._band.dateToPixelOffset(_31a));
var _31d=Math.round(p._band.dateToPixelOffset(_31b));
var _31e=(_2db+p._layout.getTrack(evt)*(_2dc+_2dd));
if(evt.isInstant()){
_2ee(evt,_31c,_31d,_31e+"em",_319,_31e-_2dd,_2dc+2*_2dd);
}else{
_2fd(evt,_31c,_31d,_31e+"em",_319,_31e-_2dd,_2dc+2*_2dd);
}
};
var _31f=(this._filterMatcher!=null)?this._filterMatcher:function(evt){
return true;
};
var _321=(this._highlightMatcher!=null)?this._highlightMatcher:function(evt){
return -1;
};
var _323=_2d1.getEventIterator(_2d2,_2d3);
while(_323.hasNext()){
var evt=_323.next();
if(_31f(evt)){
_317(evt,_321(evt));
}
}
this._highlightLayer.style.display="block";
this._eventLayer.style.display="block";
};
Timeline.DurationEventPainter.prototype.softPaint=function(){
};
Timeline.DurationEventPainter.prototype._onClickInstantEvent=function(icon,_326,evt){
_326.cancelBubble=true;
var c=Timeline.DOM.getPageCoordinates(icon);
this._showBubble(c.left+Math.ceil(icon.offsetWidth/2),c.top+Math.ceil(icon.offsetHeight/2),evt);
};
Timeline.DurationEventPainter.prototype._onClickDurationEvent=function(_329,evt,_32b){
_329.cancelBubble=true;
if("pageX" in _329){
var x=_329.pageX;
var y=_329.pageY;
}else{
var c=Timeline.DOM.getPageCoordinates(_32b);
var x=_329.offsetX+c.left;
var y=_329.offsetY+c.top;
}
this._showBubble(x,y,evt);
};
Timeline.DurationEventPainter.prototype._showBubble=function(x,y,evt){
var div=this._band.openBubbleForPoint(x,y,this._theme.event.bubble.width,this._theme.event.bubble.height);
evt.fillInfoBubble(div,this._theme,this._band.getLabeller());
};
Timeline.SpanHighlightDecorator=function(_335){
this._startDate=Timeline.DateTime.parseGregorianDateTime(_335.startDate);
this._endDate=Timeline.DateTime.parseGregorianDateTime(_335.endDate);
this._startLabel=_335.startLabel;
this._endLabel=_335.endLabel;
this._color=_335.color;
this._opacity=("opacity" in _335)?_335.opacity:100;
};
Timeline.SpanHighlightDecorator.prototype.initialize=function(band,_337){
this._band=band;
this._timeline=_337;
this._layerDiv=null;
};
Timeline.SpanHighlightDecorator.prototype.paint=function(){
if(this._layerDiv!=null){
this._band.removeLayerDiv(this._layerDiv);
}
this._layerDiv=this._band.createLayerDiv(10);
this._layerDiv.setAttribute("name","span-highlight-decorator");
this._layerDiv.style.display="none";
var _338=this._band.getMinDate();
var _339=this._band.getMaxDate();
if(this._startDate.getTime()<_339.getTime()&&this._endDate.getTime()>_338.getTime()){
_338=new Date(Math.max(_338.getTime(),this._startDate.getTime()));
_339=new Date(Math.min(_339.getTime(),this._endDate.getTime()));
var _33a=this._band.dateToPixelOffset(_338);
var _33b=this._band.dateToPixelOffset(_339);
var doc=this._timeline.getDocument();
var _33d=function(){
var _33e=doc.createElement("table");
_33e.insertRow(0).insertCell(0);
return _33e;
};
var div=doc.createElement("div");
div.style.position="absolute";
div.style.overflow="hidden";
div.style.background=this._color;
if(this._opacity<100){
Timeline.Graphics.setOpacity(div,this._opacity);
}
this._layerDiv.appendChild(div);
var _340=_33d();
_340.style.position="absolute";
_340.style.overflow="hidden";
_340.style.fontSize="300%";
_340.style.fontWeight="bold";
_340.style.color=this._color;
_340.rows[0].cells[0].innerHTML=this._startLabel;
this._layerDiv.appendChild(_340);
var _341=_33d();
_341.style.position="absolute";
_341.style.overflow="hidden";
_341.style.fontSize="300%";
_341.style.fontWeight="bold";
_341.style.color=this._color;
_341.rows[0].cells[0].innerHTML=this._endLabel;
this._layerDiv.appendChild(_341);
if(this._timeline.isHorizontal()){
div.style.left=_33a+"px";
div.style.width=(_33b-_33a)+"px";
div.style.top="0px";
div.style.height="100%";
_340.style.right=(this._band.getTotalViewLength()-_33a)+"px";
_340.style.width=(this._startLabel.length)+"em";
_340.style.top="0px";
_340.style.height="100%";
_340.style.textAlign="right";
_341.style.left=_33b+"px";
_341.style.width=(this._endLabel.length)+"em";
_341.style.top="0px";
_341.style.height="100%";
}else{
div.style.top=_33a+"px";
div.style.height=(_33b-_33a)+"px";
div.style.left="0px";
div.style.width="100%";
_340.style.bottom=_33a+"px";
_340.style.height="1.5px";
_340.style.left="0px";
_340.style.width="100%";
_341.style.top=_33b+"px";
_341.style.height="1.5px";
_341.style.left="0px";
_341.style.width="100%";
}
}
this._layerDiv.style.display="block";
};
Timeline.SpanHighlightDecorator.prototype.softPaint=function(){
};
Timeline.PointHighlightDecorator=function(_342){
this._date=Timeline.DateTime.parseGregorianDateTime(_342.date);
this._width=("width" in _342)?_342.width:10;
this._color=_342.color;
this._opacity=("opacity" in _342)?_342.opacity:100;
};
Timeline.PointHighlightDecorator.prototype.initialize=function(band,_344){
this._band=band;
this._timeline=_344;
this._layerDiv=null;
};
Timeline.PointHighlightDecorator.prototype.paint=function(){
if(this._layerDiv!=null){
this._band.removeLayerDiv(this._layerDiv);
}
this._layerDiv=this._band.createLayerDiv(10);
this._layerDiv.setAttribute("name","span-highlight-decorator");
this._layerDiv.style.display="none";
var _345=this._band.getMinDate();
var _346=this._band.getMaxDate();
if(this._date.getTime()<_346.getTime()&&this._date.getTime()>_345.getTime()){
var _347=this._band.dateToPixelOffset(this._date);
var _348=_347-Math.round(this._width/2);
var doc=this._timeline.getDocument();
var div=doc.createElement("div");
div.style.position="absolute";
div.style.overflow="hidden";
div.style.background=this._color;
if(this._opacity<100){
Timeline.Graphics.setOpacity(div,this._opacity);
}
this._layerDiv.appendChild(div);
if(this._timeline.isHorizontal()){
div.style.left=_348+"px";
div.style.width=this._width+"px";
div.style.top="0px";
div.style.height="100%";
}else{
div.style.top=_348+"px";
div.style.height=this._width+"px";
div.style.left="0px";
div.style.width="100%";
}
}
this._layerDiv.style.display="block";
};
Timeline.PointHighlightDecorator.prototype.softPaint=function(){
};

