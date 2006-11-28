package com.aavu.client.gui.mapper;

import com.aavu.client.domain.mapper.RootNode;
import com.aavu.client.domain.mapper.TopicMap;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;


public class RootBox extends EditBox {

	private RootNode root;

	private Image otherVLine;

	private boolean otherConnAdded;
	
	public RootBox(TopicMap map,RootNode root, int centerx, int centery) {
		super(map,root,centerx,centery,false);
		this.root = root;	
		root.getLeft().setBox(this);
		root.getRight().setBox(this);
		
		otherVLine = new Image("img/line.gif");
		otherVLine.setPixelSize(CONN_WIDTH,0);	
	}
	
	/**
	 * Overide
	 * don't draw the child spur if we don't have children
	 *
	 */
	public void updateChildSpur(){
		if(root.getRight().getChildren().size() > 0){
			childSpur.setVisible(true);
		}else{
			childSpur.setVisible(false);
		}
		if(root.getLeft().getChildren().size() > 0){
			parentSpur.setVisible(true);
		}else{
			parentSpur.setVisible(false);
		}
	}
	/**
	 * Override
	 * how big is the whole composite
	 * @return
	 */
	public int getWidth(){
		int rtn = getBoxWidth();
		if(root.getLeft().getChildren().size() > 0){
			rtn += SPUR_WIDTH;
		}
		if(root.getRight().getChildren().size() > 0){
			rtn += SPUR_WIDTH;
		}
		return rtn;
	}
	/**
	 * override 
	 * 
	 */
	public void doConnectorLine(boolean onLeft,int lineStart, int height, AbsolutePanel panel) {

		if(onLeft){
			if(!otherConnAdded){
				panel.add(otherVLine);
				otherConnAdded = true;
			}	
			super.doConnectorLine(otherVLine, onLeft, lineStart, height, panel);
		}else{
			super.doConnectorLine(onLeft, lineStart, height, panel);
		}
//		
//		Image vline = getConnectorLine();
//		if(!isConnectorAdded()){
//			panel.add(vline);
//			setConnectorAdded(true);
//		}
//		vline.setPixelSize(2, height);
//		
//		int vLineX = getX();
//		if(!onLeft){
//			vLineX += getWidth();
//		}
//		panel.setWidgetPosition(vline,vLineX,lineStart);
		
	}
//
//	
//	private void moveUp(Widget sender) {	
//		System.out.println("ROOT UP");
//		((MapNode)root.getLeft().getChildren().get(0)).getBox().setFocus(true);		
//	}
//
//
//	private void moveDown(Widget sender) {
//		System.out.println("ROOT DOWN");
//		if(root.getRight().getChildren().size() > 0){
//			((MapNode)root.getRight().getChildren().get(1)).getBox().setFocus(true);
//		}
//	}
	
}
