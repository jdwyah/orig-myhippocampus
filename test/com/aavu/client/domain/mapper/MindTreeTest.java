package com.aavu.client.domain.mapper;

import java.util.Set;

import junit.framework.TestCase;

public class MindTreeTest extends TestCase {

	public void testGetNavigableMindTree() {
		MindTree tree = new MindTree(null);
		tree.setFromNavigableTree(testMap());
		NavigableMindTree navigable = tree.getNavigableMindTree();
		
		
		NavigableMindNode leftR = navigable.getRoot().getLeft();
		assertEquals(2,leftR.getChildren().size());
		
		NavigableMindNode l1 = (NavigableMindNode) leftR.getChildren().get(0);
		assertEquals("l1", l1.getData());
		assertEquals(1, l1.getChildren().size());
		
		NavigableMindNode l2 = (NavigableMindNode) leftR.getChildren().get(1);
		assertEquals("l2", l2.getData());
		assertEquals(3, l2.getChildren().size());
		
		NavigableMindNode l21 = (NavigableMindNode) l2.getChildren().get(0);
		assertEquals("l21", l21.getData());
		assertEquals(0, l21.getChildren().size());
		
		NavigableMindNode l222 = (NavigableMindNode) ((NavigableMindNode) l2.getChildren().get(1)).getChildren().get(1);
		assertEquals("l222", l222.getData());
		assertEquals(0, l222.getChildren().size());
		
		
	}

	/**
	 * see http://www.sitepoint.com/article/hierarchical-data-database/2
	 *
	 */
	public void testSetFromNavigableTree() {
		MindTree tree = new MindTree(null);

		tree.setFromNavigableTree(testMap());

		Set<MindTreeElement> elements = tree.getLeftSide();

		for (MindTreeElement element : elements) {
			System.out.println("Element "+element.getTitle()+" "+element.getLft()+" "+element.getRgt());
			
			if(element.getTitle().equals("l1")){
				assertEquals(1,element.getLft());
				assertEquals(4,element.getRgt());
			}
			if(element.getTitle().equals("dummy")){
				assertEquals(0,element.getLft());
				assertEquals(19,element.getRgt());
			}
			if(element.getTitle().equals("l223")){
				assertEquals(13,element.getLft());
				assertEquals(14,element.getRgt());
			}
			if(element.getTitle().equals("l2")){
				assertEquals(5,element.getLft());
				assertEquals(18,element.getRgt());
			}
			
			if(element.getTitle().equals("l221")){
				assertEquals(9,element.getLft());
				assertEquals(10,element.getRgt());
			}
		}

	}

	private NavigableMindTree testMap(){
		NavigableRootNode root = new NavigableRootNode("root");

		NavigableMindNode left = new NavigableMindNode("dummy");
		NavigableMindNode right = new NavigableMindNode("dummyR");


		NavigableMindNode r1 = new NavigableMindNode("r1");
		NavigableMindNode r2 = new NavigableMindNode("r2");
		right.addChild(r1);
		right.addChild(r2);

		NavigableMindNode l1 = new NavigableMindNode("l1");
		NavigableMindNode l2 = new NavigableMindNode("l2");
		left.addChild(l1);
		left.addChild(l2);

		NavigableMindNode l11 = new NavigableMindNode("l11");
		l1.addChild(l11);

		NavigableMindNode l21 = new NavigableMindNode("l21");
		NavigableMindNode l22 = new NavigableMindNode("l22");
		NavigableMindNode l23 = new NavigableMindNode("l23");
		l2.addChild(l21);
		l2.addChild(l22);
		l2.addChild(l23);

		NavigableMindNode l221 = new NavigableMindNode("l221");
		NavigableMindNode l222 = new NavigableMindNode("l222");
		NavigableMindNode l223 = new NavigableMindNode("l223");
		l22.addChild(l221);
		l22.addChild(l222);
		l22.addChild(l223);

		root.setLeft(left);
		root.setRight(right);

		NavigableMindTree tm = new NavigableMindTree(root);

		System.out.println(tm);

		return tm;
	}

}
