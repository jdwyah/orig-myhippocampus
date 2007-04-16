package com.aavu.client.domain.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.gui.mapper.EditBox;

public class NavigableMindNode {

	private NavigableMindNode parent;
	private List children = new ArrayList();
	
	private String data;
	private int width = 0;
	private transient EditBox box;
	
	public NavigableMindNode(String data) {
		super();		
		this.data = data;
		
	}

	public void addChild(NavigableMindNode node){
		node.setParent(this);
		children.add(node);
	}
	
	public int getWidthOfTree(){
		if(children.size() == 0){
			width = 1;			
		}
		else {
			int rtn = 0;
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				NavigableMindNode child = (NavigableMindNode) iter.next();
				rtn += child.getWidthOfTree();
			}
			width = rtn;
		}
		return width ;
	}

	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public NavigableMindNode getParent() {
		return parent;
	}

	public void setParent(NavigableMindNode parent) {
		this.parent = parent;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		return toString(sb,"");
	}
	public String toString(StringBuffer sb,String space){
		sb.append(space);
		sb.append(getData());
		sb.append("\n");
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			NavigableMindNode child = (NavigableMindNode) iter.next();
			child.toString(sb, space+"    ");
		}		
		return sb.toString();
	}

	public void setBox(EditBox box) {
		this.box = box;
	}	
	public EditBox getBox(){
		return box;
	}
	
}