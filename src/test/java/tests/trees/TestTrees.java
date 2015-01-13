package tests.trees;

import static org.junit.Assert.*;
import org.junit.Test;

import genlib.structures.BinaryNode;
import genlib.structures.MultiWayNode;

public class TestTrees {

	@Test
	public void testNodeCreation() {
		BinaryNode bn = new BinaryNode();
		assertTrue(bn.getTreeDepth() == 0);
		assertTrue(bn.getAttribute() == -1);
		assertTrue(bn.getChildCount() == 2);
		assertTrue(bn.getValue() == Integer.MIN_VALUE);
		assertTrue(bn.isLeaf());
		assertTrue(bn.getParent() == null);
		MultiWayNode mn = new MultiWayNode(5);
		assertTrue(mn.getTreeDepth() == 0);
		assertTrue(mn.getAttribute() == -1);
		assertTrue(mn.getChildCount() == 5);
		assertTrue(mn.getValue() == Integer.MIN_VALUE);
		assertTrue(mn.isLeaf());
		assertTrue(mn.getParent() == null);
	}

	@Test
	public void testNodeSizing() {
		BinaryNode bn = new BinaryNode();
		bn.setChildAt(0, new BinaryNode());
		assertTrue(bn.getTreeDepth() == 1);
		bn.setChildAt(1, new BinaryNode());
		assertTrue(bn.getTreeDepth() == 1);
		assertTrue(bn.getChildAt(0).getParent() == bn);
		bn.getChildAt(0).setChildAt(0, new BinaryNode());
		assertTrue(bn.getTreeDepth() == 2);
		bn.getChildAt(1).setChildAt(1, new BinaryNode());
		assertTrue(bn.getTreeDepth() == 2);
		bn.clearChilds();
		assertTrue(bn.getTreeDepth() == 0);
		assertTrue(bn.getChildAt(0) == null && bn.getChildAt(1) == null);

		MultiWayNode mn = new MultiWayNode(5);
		mn.setChildAt(0, new MultiWayNode(2));
		assertTrue(mn.getTreeDepth() == 1);
		mn.setChildAt(1, new MultiWayNode(3));
		assertTrue(mn.getTreeDepth() == 1);
		assertTrue(mn.getChildAt(0).getParent() == mn);
		mn.getChildAt(0).setChildAt(0, new MultiWayNode());
		assertTrue(mn.getTreeDepth() == 2);
		mn.getChildAt(1).setChildAt(2, new MultiWayNode());
		assertTrue(mn.getTreeDepth() == 2);
		mn.clearChilds();
		assertTrue(mn.getTreeDepth() == 0);
		assertTrue(mn.getChildAt(0) == null && mn.getChildAt(1) == null);
	}

}
