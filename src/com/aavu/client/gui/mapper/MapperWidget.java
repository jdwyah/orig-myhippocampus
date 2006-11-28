package com.aavu.client.gui.mapper;



import java.util.Iterator;

import com.aavu.client.domain.mapper.MapNode;
import com.aavu.client.domain.mapper.RootNode;
import com.aavu.client.domain.mapper.TopicMap;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MapperWidget extends PopupWindow implements ChangeListener{
	
	private static final int PUSHY = 30;
	private Manager manager;
	private AbsolutePanel mainPanel;
	private TopicMap map; 	
	private int width = 800;
	private RootBox rootBox;
	
	
	public MapperWidget(Manager _manager) {		
		super(_manager.myConstants.mapperTitle());
		this.manager = _manager;		

		mainPanel = new AbsolutePanel();		
		mainPanel.setPixelSize(width, 1000);

				
		map = testMap();
		map.addChangeListener(this);
	    initMap(map, width);
	    
	    mainPanel.setStyleName("H-Mapper");
		
	    VerticalPanel totalPanel = new VerticalPanel();
	    totalPanel.add(new Label(manager.myConstants.mapperHelpText()));
	    totalPanel.add(mainPanel);
	    setContentPanel(totalPanel);
	}
	
	private void initMap(TopicMap map, int width){
		
		int center = width/2;
		RootNode root = map.getRoot();
		
		System.out.println("Root at "+center+" "+center);
		
		rootBox = new RootBox(map,root,center,center);		
		mainPanel.add(rootBox,center,center);		
		
		drawMap(root,center);		
		
	}
	private void drawMap(RootNode root,int center){
		int leftWidth = root.getLeft().getWidthOfTree() - 1;
		int rigthWidth = root.getRight().getWidthOfTree() - 1;
		
		int startLeft = center - (leftWidth * PUSHY) /2; 		 
		int startRight = center - (rigthWidth * PUSHY) /2;
		
		System.out.println("Start "+ startLeft+" width "+leftWidth);
				
		drawLeft(rootBox, root.getLeft(), mainPanel, 0,startLeft);		
		System.out.println("INBETWEEN");				
		drawRight(rootBox, root.getRight(), mainPanel, 0,startRight);
	}
	

	private void drawLeft(EditBox left, MapNode node, AbsolutePanel panel, int i, int startLeft) {
		drawChildren(left, node, panel, true, i, startLeft);
	}
	private void drawRight(EditBox right, MapNode node, AbsolutePanel panel, int i, int startRight) {
		drawChildren(right, node, panel, false, i, startRight);
	}

	private void drawChildren(EditBox curBox,MapNode node,AbsolutePanel panel,boolean onLeft, int curIndex, int yBase) {
		System.out.println("-----");
		System.out.println(node.getData()+" curIdx "+curIndex+" base "+yBase+" ");
		
				
		int startX = curBox.getChildStartX(onLeft);
				
		System.out.println("curBox.getX() "+curBox.getX()+" startX "+startX);
					
		int vLineStart = -1;
		int vLineEnd = 0;
		
		int idx = 0;
		for (Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			MapNode child = (MapNode) iter.next();
			
			//Calculate Y location of the composite
			//
			double delta = curIndex + idx + ((child.getWidthOfTree() + 1)/2.0) - 1;
			int curY = (int) (delta * PUSHY)+ yBase;
			
			System.out.println("Node: "+node.getData()+" child: "+child.getData()+" Width "+child.getWidthOfTree());
			System.out.println("delta "+delta);			
			
			EditBox box = child.getBox();
			if(box == null){
				System.out.println("create new box");
				box = new EditBox(curBox.getTopicMap(),child,0,0,onLeft);
				panel.add(box,0,0);
			}else{
				System.out.println("box already made");
			}
			
			
			//
			//Calculate the X location for the Composite
			//
			int addAtThisX = startX;
			if(onLeft){				
				int childBoxWidth = box.getWidth();				
				addAtThisX = startX - childBoxWidth;
				System.out.println("ChildBox "+childBoxWidth+" AtX "+addAtThisX);
			}
			
			//Add
			//
			box.setX(addAtThisX);
			box.setY(curY);
			panel.setWidgetPosition(box, addAtThisX, curY);
			
			System.out.println("add "+child.getData()+" "+addAtThisX+" "+curY);
			
			
			//Recurse
			//
			drawChildren(box, child,panel,onLeft,curIndex+idx,yBase);
			
			//save vLine info
			//
			if(vLineStart == -1){
				vLineStart = curY;
			}
			vLineEnd = curY;
			
			//skip this many places in line
			//
			idx += child.getWidthOfTree();
		}
		
		//
		//H-Lines
		//
		curBox.updateChildSpur();
		
		//
		//Add V-Line
		//		
		if(node.getChildren().size() > 0){

			int lineStart = vLineStart + (EditBox.HEIGHT /2);
			int height = vLineEnd - vLineStart;
			
			curBox.doConnectorLine(onLeft,lineStart,height,panel);			
			
			System.out.println(node.getData()+" add vline height: "+height+" nodes "+(curBox.getNode().getWidthOfTree() - 1)+" start "+lineStart);
		}
	}

	private int getWidthForElem(Widget widg) {
		String wid = DOM.getAttribute(widg.getElement(), "width"); 
		System.out.println("wid "+wid);
		
		return 40;
		//return Integer.parseInt(wid);
		//return Integer.parseInt(wid.substring(0,wid.length()-2));
	}

	private TopicMap testMap(){
		RootNode root = new RootNode("root");
		
		MapNode left = new MapNode("dummy");
		MapNode right = new MapNode("dummyR");
		
		
		
//		MapNode r1 = new MapNode("r1");
//		MapNode r2 = new MapNode("r2");
//		right.addChild(r1);
//		right.addChild(r2);
//		
//		MapNode l1 = new MapNode("l1");
//		MapNode l2 = new MapNode("l2");
//		left.addChild(l1);
//		left.addChild(l2);
//		
//		MapNode l11 = new MapNode("l11");
//		l1.addChild(l11);
//		
//		MapNode l21 = new MapNode("l21");
//		MapNode l22 = new MapNode("l221");
//		MapNode l23 = new MapNode("l23");
//		l2.addChild(l21);
//		l2.addChild(l22);
//		l2.addChild(l23);
//		
//		MapNode l221 = new MapNode("l221");
//		MapNode l222 = new MapNode("l222");
//		MapNode l223 = new MapNode("l223");
//		l22.addChild(l221);
//		l22.addChild(l222);
//		l22.addChild(l223);
		
		root.setLeft(left);
		root.setRight(right);
				
		TopicMap tm = new TopicMap(root);
		
		System.out.println(tm);
		
		return tm;
	}

	public void onChange(Widget sender) {
		redraw();
	}

	private void redraw() {
		drawMap(map.getRoot(), width/2);		
	}
	
}
