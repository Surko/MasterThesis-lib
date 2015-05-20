package tests.trees;


import genlib.structures.extensions.DepthExtension;
import genlib.structures.trees.BinaryDepthNode;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestTrees {

	@Test
	public void testNodeCreation() {
		BinaryDepthNode bn = new BinaryDepthNode();
		assertTrue(bn instanceof DepthExtension);
		assertTrue(bn.getTreeHeight() == 1);
		assertTrue(bn.getAttribute() == -1);
		assertTrue(bn.getChildCount() == 2);
		assertTrue(bn.getValue() == Integer.MIN_VALUE);
		assertTrue(bn.isLeaf());
		assertTrue(bn.getParent() == null);
		MultiWayDepthNode mn = new MultiWayDepthNode(5);
		assertTrue(mn instanceof DepthExtension);
		assertTrue(mn.getTreeHeight() == 1);
		assertTrue(mn.getAttribute() == -1);
		assertTrue(mn.getChildCount() == 5);
		assertTrue(mn.getValue() == Integer.MIN_VALUE);
		assertTrue(mn.isLeaf());
		assertTrue(mn.getParent() == null);
	}

	@Test
	public void testNodeDepth() {
		BinaryDepthNode bn = new BinaryDepthNode();
		bn.setChildAt(0, new BinaryDepthNode());
		assertTrue(bn.getTreeHeight() == 2);
		bn.setChildAt(1, new BinaryDepthNode());
		assertTrue(bn.getTreeHeight() == 2);
		assertTrue(bn.getChildAt(0).getParent() == bn);
		bn.getChildAt(0).setChildAt(0, new BinaryDepthNode());
		assertTrue(bn.getTreeHeight() == 3);
		bn.getChildAt(1).setChildAt(1, new BinaryDepthNode());
		assertTrue(bn.getTreeHeight() == 3);
		bn.clearChilds();
		assertTrue(bn.getTreeHeight() == 1);
		assertTrue(bn.getChildAt(0) == null && bn.getChildAt(1) == null);

		MultiWayDepthNode mn = new MultiWayDepthNode(5);		
		mn.setChildAt(0, new MultiWayDepthNode(2));
		assertTrue(mn.getTreeHeight() == 2);
		mn.setChildAt(1, new MultiWayDepthNode(3));
		assertTrue(mn.getTreeHeight() == 2);
		assertTrue(mn.getChildAt(0).getParent() == mn);
		mn.getChildAt(0).setChildAt(0, new MultiWayDepthNode());
		assertTrue(mn.getTreeHeight() == 3);
		mn.getChildAt(1).setChildAt(2, new MultiWayDepthNode());
		assertTrue(mn.getTreeHeight() == 3);
		mn.clearChilds();
		assertTrue(mn.getTreeHeight() == 1);
		assertTrue(mn.getChildAt(0) == null && mn.getChildAt(1) == null);
		
		bn.makeLeaf();
		assertTrue(bn.isLeaf());
		assertNull(bn.getChilds());
		mn.makeLeaf();
		assertNull(mn.getChilds());	
		assertTrue(mn.isLeaf());
	}
	
	@Test
	public void testNodeHeightComputing() {
		BinaryDepthNode bn = new BinaryDepthNode();
		bn.setAttribute(0);
		bn.setChildAt(0, new BinaryDepthNode());
		bn.setChildAt(1, new BinaryDepthNode());
		bn.getChildAt(0).setAttribute(0);
		bn.getChildAt(1).setAttribute(0);		
		bn.getChildAt(0).setChildAt(0, new BinaryDepthNode());
		bn.getChildAt(1).setChildAt(1, new BinaryDepthNode());	
		assertTrue(Utils.computeHeight(bn) == 3);
		
		MultiWayDepthNode mn = new MultiWayDepthNode(5);
		mn.setAttribute(0);
		mn.setChildAt(0, new MultiWayDepthNode(2));
		mn.setChildAt(1, new MultiWayDepthNode(3));
		mn.getChildAt(0).setAttribute(0);
		mn.getChildAt(1).setAttribute(0);
		mn.getChildAt(0).setChildAt(0, new MultiWayDepthNode());
		mn.getChildAt(1).setChildAt(2, new MultiWayDepthNode());		
		assertTrue(Utils.computeHeight(mn) == 3);
		
	}

}
