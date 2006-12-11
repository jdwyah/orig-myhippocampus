package com.aavu.client.gui.mapper;



import java.util.Iterator;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.domain.mapper.NavigableMindNode;
import com.aavu.client.domain.mapper.NavigableRootNode;
import com.aavu.client.domain.mapper.NavigableMindTree;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MapperWidget extends PopupWindow implements ChangeListener{
	
	private static final int PUSHY = 30;
	private Manager manager;
	private AbsolutePanel mainPanel;
	private NavigableMindTree map; 	
	private int width = 0;
	private int height = 0;
	private RootBox rootBox;
	private MindTreeOcc mindTreeOcc;
	private Topic topic;
	
	public MapperWidget(Manager _manager,int width, int height) {		
		super(_manager.newFrame(),_manager.myConstants.mapperTitle());
		this.manager = _manager;		
		this.width = width;
		this.height = height;
		
		mainPanel = new AbsolutePanel();		
		mainPanel.setPixelSize(width, height);				
	    mainPanel.setStyleName("H-Mapper");
		
	    Button saveButton = new Button(_manager.myConstants.save());
	    saveButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				mindTreeOcc.getMindTree().setFromNavigableTree(map);
			
				//TODO extract logic to Topic.java
				//TODO this manager ref is static otherwise odd IllegalAccessError
				//http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/8c39a4e91805691a/8b1c2dea6755a70f?lnk=gst&q=IllegalAccessError&rnum=1#8b1c2dea6755a70f
				manager.getTopicCache().saveTree(mindTreeOcc.getMindTree(),new StdAsyncCallback(Manager.myConstants.mapperAsyncSave()){
					public void onSuccess(Object result) {
						super.onSuccess(result);
						MindTree res = (MindTree) result;
						mindTreeOcc.setMindTree(res);
						topic.getOccurences().add(mindTreeOcc);						
					}					
				});
			}});	    
	    
	    VerticalPanel totalPanel = new VerticalPanel();
	    totalPanel.add(new Label(manager.myConstants.mapperHelpText()));
	    totalPanel.add(saveButton);
	    totalPanel.add(mainPanel);
	    setContent(totalPanel);
	}

	public void loadTree(Topic topic,MindTreeOcc treeOcc) {
		this.mindTreeOcc = treeOcc;
		this.topic = topic;
		System.out.println("Load Tree: "+treeOcc);		
		map = treeOcc.getMindTree().getNavigableMindTree();
		System.out.println("Map: "+map);
		map.addChangeListener(this);
	    initMap(map);	    
	}	
	
	private void initMap(NavigableMindTree map){
		mainPanel.clear();
		
		int center = width/2;
		int centerH = height/2;
		NavigableRootNode root = map.getRoot();
		
		System.out.println("Root at "+center+" "+centerH);
		
		rootBox = new RootBox(map,root,center,centerH);		
		mainPanel.add(rootBox,center,centerH);		
		
		drawMap(root,centerH);		
		
	}
	private void drawMap(NavigableRootNode root,int centerHeight){
		int leftWidth = root.getLeft().getWidthOfTree() - 1;
		int rigthWidth = root.getRight().getWidthOfTree() - 1;
		
		int startLeft = centerHeight - (leftWidth * PUSHY) /2; 		 
		int startRight = centerHeight - (rigthWidth * PUSHY) /2;
		
		System.out.println("Start "+ startLeft+" width "+leftWidth);
				
		drawLeft(rootBox, root.getLeft(), mainPanel, 0,startLeft);		
		System.out.println("INBETWEEN");				
		drawRight(rootBox, root.getRight(), mainPanel, 0,startRight);
	}
	

	private void drawLeft(EditBox left, NavigableMindNode node, AbsolutePanel panel, int i, int startLeft) {
		drawChildren(left, node, panel, true, i, startLeft);
	}
	private void drawRight(EditBox right, NavigableMindNode node, AbsolutePanel panel, int i, int startRight) {
		drawChildren(right, node, panel, false, i, startRight);
	}

	private void drawChildren(EditBox curBox,NavigableMindNode node,AbsolutePanel panel,boolean onLeft, int curIndex, int yBase) {
		System.out.println("-----");
		System.out.println(node.getData()+" curIdx "+curIndex+" base "+yBase+" ");
		
				
		int startX = curBox.getChildStartX(onLeft);
				
		System.out.println("curBox.getX() "+curBox.getX()+" startX "+startX);
					
		int vLineStart = -1;
		int vLineEnd = 0;
		
		int idx = 0;
		for (Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			NavigableMindNode child = (NavigableMindNode) iter.next();
			
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

	public void onChange(Widget sender) {
		redraw();
	}

	private void redraw() {
		drawMap(map.getRoot(), height/2);		
	}

}
