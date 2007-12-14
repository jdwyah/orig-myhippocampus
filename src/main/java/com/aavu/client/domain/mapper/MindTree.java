package com.aavu.client.domain.mapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.mapper.generated.AbstractMindTree;

public class MindTree extends AbstractMindTree implements Serializable {

	/**
	 * lft comaparator
	 */
	private static Comparator lftComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			MindTreeElement o = (MindTreeElement) o1;
			MindTreeElement oo = (MindTreeElement) o2;
			return o.getLft() - oo.getLft();
		}
	};


	public MindTree() {
	}

	public MindTree(Topic topic) {
		setTopic(topic);
	}

	public NavigableMindTree getNavigableMindTree() {

		NavigableRootNode root = new NavigableRootNode("root");

		NavigableMindNode left = new NavigableMindNode("dummy");
		NavigableMindNode right = new NavigableMindNode("dummyR");

		root.setLeft(left);
		root.setRight(right);

		NavigableMindTree tm = new NavigableMindTree(root);

		GWTSortedMap leftMap = new GWTSortedMap(lftComparator);
		for (Iterator iter = getLeftSide().iterator(); iter.hasNext();) {
			MindTreeElement element = (MindTreeElement) iter.next();
			leftMap.put(element, null);
		}
		GWTSortedMap rightMap = new GWTSortedMap(lftComparator);
		for (Iterator iter = getRightSide().iterator(); iter.hasNext();) {
			MindTreeElement element = (MindTreeElement) iter.next();
			rightMap.put(element, null);
		}

		populate(leftMap.keySet(), left);
		populate(rightMap.keySet(), right);

		System.out.println(tm.toString());
		return tm;
	}

	/**
	 * MapElements must be in lft increasing order!
	 * 
	 * @param side
	 * @param node
	 */
	private void populate(Set<MindTreeElement> side, NavigableMindNode node) {

		List<MindTreeElement> stack = new ArrayList<MindTreeElement>();
		List<NavigableMindNode> nodestack = new ArrayList<NavigableMindNode>();

		for (MindTreeElement element : side) {


			NavigableMindNode toAdd = new NavigableMindNode(element.getTitle());

			if (element.getLft() == 0) {
				stack.add(element);
				nodestack.add(node);
				continue;
			}

			System.out.println("Element " + element.getTitle());


			MindTreeElement addTo = null;
			NavigableMindNode addToNode = null;
			while (9 == 9) {
				addTo = (MindTreeElement) stack.get(stack.size() - 1);
				addToNode = (NavigableMindNode) nodestack.get(nodestack.size() - 1);

				System.out.println("el " + element.getLft() + " er " + element.getRgt() + " atl "
						+ addTo.getLft() + " atr " + addTo.getRgt());
				if (element.getLft() < addTo.getLft() || element.getRgt() > addTo.getRgt()) {
					System.out.println("><");
					stack.remove(stack.size() - 1);
					nodestack.remove(nodestack.size() - 1);
				} else {
					break;
				}
			}

			addToNode.addChild(toAdd);

			nodestack.add(toAdd);
			stack.add(element);

		}

		System.out.println("Node Stack " + nodestack.size());
	}


	/**
	 * set from Navigable nodes, ie turn GUI into DB savable MindTreeElements
	 * 
	 * @param tree
	 */
	public void setFromNavigableTree(NavigableMindTree tree) {
		setSide(getLeftSide(), tree.getRoot().getLeft());
		setSide(getRightSide(), tree.getRoot().getRight());
	}

	private void setSide(Set<MindTreeElement> side, NavigableMindNode node) {

		int curIdx = 0;

		side
				.add(new MindTreeElement(node.getData(), null, curIdx, addChildren(side, curIdx,
						node)));

	}

	/**
	 * recursively create the Modified Preorder Tree Traversal see
	 * http://www.sitepoint.com/article/hierarchical-data-database/2
	 * 
	 * @param side
	 * @param curIdx
	 * @param node
	 * @return
	 */
	private int addChildren(Set side, int curIdx, NavigableMindNode node) {

		int rtn = curIdx;

		if (node.getChildren().size() != 0) {

			int startVal = curIdx;

			for (Iterator iter = node.getChildren().iterator(); iter.hasNext();) {

				startVal++;

				NavigableMindNode child = (NavigableMindNode) iter.next();

				rtn = addChildren(side, startVal, child);
				side.add(new MindTreeElement(child.getData(), null, startVal, rtn));

				startVal = rtn;
			}
		}
		return rtn + 1;
	}

}
