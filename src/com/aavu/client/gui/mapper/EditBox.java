package com.aavu.client.gui.mapper;

import com.aavu.client.domain.mapper.NavigableMindNode;
import com.aavu.client.domain.mapper.NavigableRootNode;
import com.aavu.client.domain.mapper.NavigableMindTree;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

public class EditBox extends Composite implements FocusListener, KeyboardListener, ChangeListener {

	public static final int HEIGHT = 20;
	private static final int SPUR_HEIGHT = 2;
	protected static final int SPUR_WIDTH = 10;
	protected static final int CONN_WIDTH = 2;
	
	private int y;
	private int x;
	private NavigableMindNode node;
	private boolean onLeft;
	private NavigableMindTree map;	
	
	private TextBox myBox;	
	private Image connectorLine;
	private boolean connectorAdded;
	
	private HorizontalPanel mainPanel;
	protected Image parentSpur;
	protected Image childSpur;
	
	public EditBox(NavigableMindTree map,boolean onLeft){
		super();
		this.map = map;
		this.onLeft = onLeft;
		
		myBox = new TextBox();
		myBox.setStyleName("H-MapperTextBox");
		
		
				
		mainPanel = new HorizontalPanel();			
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.setStyleName("H-MapperBox");
		
		childSpur = new Image("img/line.gif");
		childSpur.setPixelSize(SPUR_WIDTH,SPUR_HEIGHT);
		childSpur.setStyleName("H-MapperSpur");
		
		parentSpur = new Image("img/line.gif");
		parentSpur.setPixelSize(SPUR_WIDTH,SPUR_HEIGHT);		
		parentSpur.setStyleName("H-MapperSpur");
		
		connectorLine = new Image("img/line.gif");
		connectorLine.setPixelSize(CONN_WIDTH,0);		
		
		if(!onLeft){
			myBox.addStyleName("H-MapperTextBox-Right");
			mainPanel.add(parentSpur);
			mainPanel.add(myBox);
			mainPanel.add(childSpur);						
		}else{			
			mainPanel.add(childSpur);
			mainPanel.add(myBox);
			mainPanel.add(parentSpur);				
		}
		
		initWidget(mainPanel);
	}
	

	public EditBox(NavigableMindTree map,NavigableMindNode node, int x, int y, boolean onLeft) {
		this(map,onLeft);		
		this.y = y;
		this.x = x;
		this.node = node;
		
		node.setBox(this);
		
		DOM.setStyleAttribute(getElement(), "top", y+"px");
		DOM.setStyleAttribute(getElement(), "left", x+"px");
		
		
		myBox.setSize("40px", HEIGHT+"px");
		myBox.setText(node.getData());
		myBox.addFocusListener(this);
		myBox.addKeyboardListener(this);
		myBox.addChangeListener(this);
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public NavigableMindNode getNode() {
		return node;
	}
	public void setNode(NavigableMindNode node) {
		this.node = node;
	}

	public Image getConnectorLine() {
		return connectorLine;
	}
	
	public boolean isConnectorAdded() {
		return connectorAdded;
	}
	public void setConnectorAdded(boolean connectorAdded) {
		this.connectorAdded = connectorAdded;
	}
	/**
	 * don't draw the child spur if we don't have children
	 *
	 */
	public void updateChildSpur(){
		if(node.getChildren().size() > 0){
			childSpur.setVisible(true);
		}else{
			childSpur.setVisible(false);
		}
	}

	public void onFocus(Widget sender) {		
		myBox.addStyleName("H-MapperTextBox-Focus");		
	}

	public void onLostFocus(Widget sender) {
		myBox.removeStyleName("H-MapperTextBox-Focus");
	}

	public boolean isShift(int modifiers){
		return (modifiers & MODIFIER_SHIFT) != 0;
	}
	public boolean isCtrl(int modifiers){
		return (modifiers & MODIFIER_CTRL) != 0;
	}
	
	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		// TODO Auto-generated method stub		
	}
	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		// TODO Auto-generated method stub		
	}


	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		
		
		//add child
		if(keyCode == KEY_ENTER){
			map.addChild(getNode());
		}
		//add sibling
		if(keyCode == KEY_DOWN && isShift(modifiers)){
			map.addSibling(getNode());
		}
		//delete
		if(keyCode == KEY_BACKSPACE && isCtrl(modifiers)){
			map.delete(getNode());
		}
		//delete
		if(keyCode == KEY_DELETE && isCtrl(modifiers)){
			map.delete(getNode());
		}
		
		
		if(keyCode == KEY_RIGHT && isCtrl(modifiers)){
			if(onLeft){
				moveUp(sender);
			}else{
				moveDown(sender);
			}
		}
		if(keyCode == KEY_LEFT && isCtrl(modifiers)){
			if(onLeft){
				moveDown(sender);
			}else{
				moveUp(sender);
			}
		}
		if(keyCode == KEY_UP){
			
			int index = node.getParent().getChildren().indexOf(node);
			
			if(index-1 >= 0){			
				((NavigableMindNode)node.getParent().getChildren().get(index-1)).getBox().setFocus(true);
			}
		}
		if(keyCode == KEY_DOWN){
			int index = node.getParent().getChildren().indexOf(node);
			if(index+1 < node.getParent().getChildren().size()){
				((NavigableMindNode)node.getParent().getChildren().get(index+1)).getBox().setFocus(true);
			}
		}
	}


	private void setFocus(boolean b) {
		myBox.setFocus(b);		
	}


	private void moveUp(Widget sender) {	
		if(node instanceof NavigableRootNode){
			NavigableRootNode root = (NavigableRootNode) node;
			System.out.println("ROOT UP");
			((NavigableMindNode)root.getLeft().getChildren().get(0)).getBox().setFocus(true);
			return;
		}
		node.getParent().getBox().setFocus(true);
	}

	private void moveDown(Widget sender) {
		if(node instanceof NavigableRootNode){
			NavigableRootNode root = (NavigableRootNode) node;
			System.out.println("ROOT DOWN");
			if(root.getRight().getChildren().size() > 0){
				((NavigableMindNode)root.getRight().getChildren().get(1)).getBox().setFocus(true);
			}			
		}
		else if(node.getChildren().size() > 0){
			((NavigableMindNode)node.getChildren().get(0)).getBox().setFocus(true);
		}
	}


	public NavigableMindTree getTopicMap() {		
		return map;
	}


	/**
	 * TextBox ChangeListener
	 * Update the size of the box
	 */
	public void onChange(Widget sender) {
		node.setData(myBox.getText());		
		myBox.setPixelSize(getBoxWidth(),HEIGHT);
		map.fireChange();
	}

	/**
	 * get the end of our box on the child side including all spurs.
	 * @return
	 */
	public int getChildStartX(boolean fromLeft) {
		if(fromLeft){
			return getX();			
		}else{
			return getX() + getWidth();
		}
	}

	/**
	 * How big is the box? 
	 * @return
	 */
	protected int getBoxWidth() {
		int res = myBox.getText().length() * 5; 
		return (40 > res) ? 40 : res;
	}
	
	/**
	 * how big is the whole composite
	 * @return
	 */
	public int getWidth(){
		if(node.getChildren().size() > 0){
			return (2*SPUR_WIDTH) + getBoxWidth();
		}
		return SPUR_WIDTH + getBoxWidth();
	}


	/**
	 * RootBox overrides this
	 * 
	 * @param onLeft
	 * @param lineStart
	 * @param height
	 * @param panel
	 */
	public void doConnectorLine(boolean onLeft,int lineStart, int height, AbsolutePanel panel) {

		Image vline = getConnectorLine();
		if(!isConnectorAdded()){
			panel.add(vline);
			setConnectorAdded(true);
		}	
		doConnectorLine(vline, onLeft, lineStart, height, panel);		
	}

	/**
	 * Will be called by RootBox 
	 * @param theLine
	 * @param onLeft
	 * @param lineStart
	 * @param height
	 * @param panel
	 */
	protected void doConnectorLine(Image theLine, boolean onLeft, int lineStart, int height, AbsolutePanel panel) {
		theLine.setPixelSize(2, height);
		
		int vLineX = getX();
		if(!onLeft){
			vLineX += getWidth();
		}
		panel.setWidgetPosition(theLine,vLineX,lineStart);
	}
	
	
}
