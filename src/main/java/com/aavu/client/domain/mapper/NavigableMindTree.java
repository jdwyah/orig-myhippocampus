package com.aavu.client.domain.mapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Widget;

public class NavigableMindTree {

	private NavigableRootNode root;
	private ChangeListener listener;

	public NavigableMindTree(NavigableRootNode root) {
		super();
		this.root = root;
	}

	public NavigableRootNode getRoot() {
		return root;
	}

	public void setRoot(NavigableRootNode root) {
		this.root = root;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer(root.getData());
		sb.append("\nLEFT: "+root.getLeft());
		sb.append("RIGHT: "+root.getRight());
		return sb.toString();
	}

	public void addChild(NavigableRootNode node) {
		System.out.println("ROOT ADD");
		if(node.getLeft().getChildren().size() < node.getRight().getChildren().size()){
			node.getLeft().addChild(new NavigableMindNode(""));
		}else{
			node.getRight().addChild(new NavigableMindNode(""));
		}
		if(listener != null){
			listener.onChange(null);
		}
	}
	

	public void addChild(NavigableMindNode node) {
		if(node instanceof NavigableRootNode){
			addChild((NavigableRootNode)node);
			return;
		}
		System.out.println("REGULAR ADD to "+node.getData());
		node.addChild(new NavigableMindNode(""));
		fireChange();
	}
	public void addSibling(NavigableMindNode node) {		
		addChild(node.getParent());
	}

	public void delete(NavigableMindNode node) {
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
