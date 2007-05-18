package com.aavu.client.gui.timeline;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameEvent;
import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ViewPanel;
import com.aavu.client.gui.dhtmlIslands.RemembersPosition;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.glossary.SimpleTopicDisplay;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class NewHippoTimeLine extends ViewPanel {

	
	
	private class LabelWrapper extends Label implements RemembersPosition {

		private int left;
		private int top;

		public LabelWrapper(String s,int left, int top){
			super(s);
			this.left = left;
			this.top = top;
		}
		
		public int getLeft() {
			return left;
		}

		public int getTop() {
			return top;
		}

		public Widget getWidget() {
			
			return this;
		}
		
	}
	private class PreviewPopup extends PopupWindow {

		private static final int HEIGHT = 200;
		private static final int WIDTH = 400;
				
		public PreviewPopup(GInternalFrame frame,Topic topic,int top,int left) {
			super(frame,topic.getTitle(),WIDTH,HEIGHT);
			SimpleTopicDisplay display = new SimpleTopicDisplay(topic,manager,this,WIDTH,HEIGHT);		
			frame.setLocation(top, left);
			setContent(display);
		}		
	}
		
	private class TLOWrapper extends Composite implements RemembersPosition {
		private int left;
		private TimeLineObj tlo;
		private int top;
		
		public TLOWrapper(final TimeLineObj tlo,int left, int top){
			this.tlo = tlo;
			this.left = left;
			this.top = top;
			
			HorizontalPanel panel = new HorizontalPanel();
			
			Label label = new Label(tlo.getTopic().getTopicTitle());
			
			panel.add(ConstHolder.images.bullet_blue().createImage());
			panel.add(label);
			
			label.addClickListener(new ClickListener(){

				public void onClick(final Widget sender) {
					manager.getTopicCache().getTopic(tlo.getTopic(), new StdAsyncCallback("Fetch Preview"){

						//@Override
						public void onSuccess(Object result) {							
							super.onSuccess(result);
							
							PreviewPopup p = new PreviewPopup(manager.newFrame(),(Topic) result,0,0);									
						}});					
				}});
			
			initWidget(panel);
		}
		
		public int getLeft() {
			return left;
		}

		public int getTop() {
			return top;
		}

		public Widget getWidget() {
			
			return this;
		}
	}
	
	private static final int HEIGHT = 400;
	
	private static final int MIN_RESIZE = 300;
	private static final int WIDTH = 680;
	
	
	private int height;
	private int intervalTop = 300;
	private int lastDepth;
	private int lastKey;
	
	private Manager manager;
	
	private int slot;

	
	private int[] slots;

	private int slotWidth = 250;
	private int xSpan = 250;
	
	

	private int yPush = 30;

	private int zoneStart;
	
	
	
	public NewHippoTimeLine(Manager manager){
		this(manager,WIDTH,HEIGHT,null);
	}



	public NewHippoTimeLine(Manager manager,int width,int height, CloseListener close){
		
		super();
		
		this.manager = manager;
		this.height = height;
		
		intervalTop = height - 20;		
		
		setDoYTranslate(false);
		
		init(height);
	
		
	
		setPixelSize(width, height);		
		
		addStyleName("H-Timeline");
	}
	
	private void add(TimeLineObj tlo, int depth, int key) {	
		
		System.out.println("-----------------");
		System.out.println("add "+tlo+" depth "+depth+" key "+key);
		
		if(depth == lastDepth && key == lastKey){
			
		}else{
			
			//System.out.println("!= NEW zone "+key+" "+TreeOfTime.getLabelForDepth(depth,key,tlo.getDate())+" LastZone start "+zoneStart);
			
			zoneStart += xSpan;			
			
			LabelWrapper startIntervalLabel = new LabelWrapper(TreeOfTime.getLabelForDepth(depth,key,tlo.getDate()),zoneStart,intervalTop ); 
			addObject(startIntervalLabel);
		}
		
		
		
		double pct = TreeOfTime.getPctAtDepth(depth, tlo.getDate());
		
		int left = (int) (xSpan * pct) + zoneStart;
		
		slot = getBestSlotFor(left,tlo.getTopic().getTopicTitle());
		
		int top = slot * yPush;
		
		System.out.println("add at pct "+pct+" top "+top+" left "+left);
		
		TLOWrapper dw = new TLOWrapper(tlo,left,top);
		
		
		addObject(dw);
		
		lastDepth = depth;
		lastKey = key;
	}



	public void clear(){
		super.clear();
		init(height);
	}
	
	
	private int getBestSlotFor(int left, String string) {
		int i = 0;
		for (; i < slots.length; i++) {
			int lastLeftForThisSlot = slots[i];
			
			//System.out.println("gb "+i+" "+lastLeftForThisSlot+" "+left);
			if(lastLeftForThisSlot < left){
				slots[i] = left + 10*string.length();
				System.out.println("choose "+i);
				return i;
			}			
		}
		System.out.println("fail!!!!!!!!");
		return i;
	}

	private void init(int height) {
		lastDepth = 0;
		lastKey = 0;		
		slots = new int[(int)Math.floor(height / yPush) - 1];
		zoneStart = -xSpan;
		for (int i = 0; i < slots.length; i++) {
			slots[i] = zoneStart -1;//leave room for 0's
		}
	}

	
	public void load(List timelines) {
				
		clear();
		
		TreeOfTime tree = new TreeOfTime(1,3,slots.length);
		
		for (Iterator iter = timelines.iterator(); iter.hasNext();) {
			TimeLineObj tlo = (TimeLineObj) iter.next();
			tree.add(tlo);
		}
		
		tree.visit(new Visitor(){		
			public void found(Object date, int depth, int key) {
				add((TimeLineObj) date, depth, key);
			}});
		
		System.out.println(tree.toPrettyString());
		
		redraw();
	}
	
	private void redraw() {
		moveBy(0, 0);
	}



	public void resize(GFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
