package com.aavu.client.domain.mapper;

public class NavigableRootNode extends NavigableMindNode {

	private NavigableMindNode left;
	private NavigableMindNode right;
	
	public NavigableRootNode(String data) {
		super(data);
	}

	public NavigableMindNode getLeft() {
		return left;
	}

	public void setLeft(NavigableMindNode left) {
		this.left = left;
	}

	public NavigableMindNode getRight() {
		return right;
	}

	public void setRight(NavigableMindNode right) {
		this.right = right;
	}

	
}
