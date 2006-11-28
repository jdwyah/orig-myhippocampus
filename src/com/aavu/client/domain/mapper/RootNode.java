package com.aavu.client.domain.mapper;

public class RootNode extends MapNode {

	private MapNode left;
	private MapNode right;
	
	public RootNode(String data) {
		super(data);
	}

	public MapNode getLeft() {
		return left;
	}

	public void setLeft(MapNode left) {
		this.left = left;
	}

	public MapNode getRight() {
		return right;
	}

	public void setRight(MapNode right) {
		this.right = right;
	}

	
}
