package com.aavu.client.domain.mapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Widget;

public class TopicMap {

	private RootNode root;
	private ChangeListener listener;

	public TopicMap(RootNode root) {
		super();
		this.root = root;
	}

	public RootNode getRoot() {
		return root;
	}

	public void setRoot(RootNode root) {
		this.root = root;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer(root.getData());
		sb.append("LEFT: "+root.getLeft());
		sb.append("RIGHT: "+root.getRight());
		return sb.toString();
	}

	public void addChild(RootNode node) {
		System.out.println("ROOT ADD");
		if(node.getLeft().getChildren().size() < node.getRight().getChildren().size()){
			node.getLeft().addChild(new MapNode(""));
		}else{
			node.getRight().addChild(new MapNode(""));
		}
		if(listener != null){
			listener.onChange(null);
		}
	}
	public void addChild(MapNode node) {
		if(GWT.getTypeName(node).equals("com.aavu.client.domain.mapper.RootNode")){
			addChild((RootNode)node);
			return;
		}
		System.out.println("REGULAR ADD");
		node.addChild(new MapNode(""));
		fireChange();
	}
	public void addSibling(MapNode node) {		
		addChild(node.getParent());
	}

	public void delete(MapNode node) {
		node.getParent().getChildren().remove(node);
		fireChange();
	}

	public void fireChange(){
		if(listener != null){
			listener.onChange(null);
		}
	}
	
	public void addChangeListener(ChangeListener listener) {
		this.listener = listener;		
	}

	

}
