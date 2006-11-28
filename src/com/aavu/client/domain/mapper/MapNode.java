package com.aavu.client.domain.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.gui.mapper.EditBox;

public class MapNode {

	private MapNode parent;
	private List children = new ArrayList();
	
	private String data;
	private int width = 0;
	private transient EditBox box;
	
	public MapNode(String data) {
		super();		
		this.data = data;
		
	}

	public void addChild(MapNode node){
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
				MapNode child = (MapNode) iter.next();
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

	public MapNode getParent() {
		return parent;
	}

	public void setParent(MapNode parent) {
		this.parent = parent;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}
	
	public String toString(){
		System.out.println("blank call");
		StringBuffer sb = new StringBuffer();
		return toString(sb,"");
	}
	public String toString(StringBuffer sb,String space){
		System.out.println("sb call "+getData());
		sb.append(space);
		sb.append(getData());
		sb.append("\n");
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			MapNode child = (MapNode) iter.next();
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
