package tests.trees;


import genlib.structures.extensions.HeightExtension;
import genlib.structures.trees.BinaryDepthNode;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.utils.Utils;
import genlib.utils.Utils.Sign;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestTrees {

	@Test
	public void testNodeCreation() {
		BinaryDepthNode bn = new BinaryDepthNode();
		assertTrue(bn instanceof HeightExtension);
		assertTrue(bn.getTreeHeight() == 0);
		assertTrue(bn.getAttribute() == -1);
		assertTrue(bn.getChilds() == null);
		assertTrue(bn.getValue() == Integer.MIN_VALUE);
		assertTrue(bn.getSign() == null);
		assertTrue(bn.isLeaf());
		assertTrue(bn.getParent() == null);
		bn = BinaryDepthNode.makeLeaf(1.234d);
		assertTrue(bn instanceof HeightExtension);
		assertTrue(bn.getTreeHeight() == 0);
		assertTrue(bn.getAttribute() == -1);
		assertTrue(bn.getChilds() == null);
		assertTrue(bn.getValue() == 1.234d);
		assertTrue(bn.getSign() == null);
		assertTrue(bn.isLeaf());
		assertTrue(bn.getParent() == null);
		bn = BinaryDepthNode.makeNode(0, Sign.EQUALS, 1.234d);
		assertTrue(bn instanceof HeightExtension);
		assertTrue(bn.getTreeHeight() == 0);
		assertTrue(bn.getAttribute() == 0);
		assertTrue(bn.getChildCount() == 2);		
		assertTrue(bn.getValue() == 1.234d);
		assertTrue(bn.getSign() == Sign.EQUALS);
		assertTrue(!bn.isLeaf());
		assertTrue(bn.getParent() == null);
		MultiWayDepthNode mn = new MultiWayDepthNode();
		assertTrue(mn instanceof HeightExtension);
		assertTrue(mn.getTreeHeight() == 0);
		assertTrue(mn.getAttribute() == -1);
		assertTrue(mn.getChilds() == null);
		assertTrue(mn.getValue() == Integer.MIN_VALUE);
		assertTrue(mn.getSign() == null);
		assertTrue(mn.isLeaf());
		assertTrue(mn.getParent() == null);
		mn = MultiWayDepthNode.makeLeaf(1.234d);
		assertTrue(mn instanceof HeightExtension);
		assertTrue(mn.getTreeHeight() == 0);
		assertTrue(mn.getAttribute() == -1);
		assertTrue(mn.getChilds() == null);
		assertTrue(mn.getValue() == 1.234d);
		assertTrue(mn.getSign() == null);
		assertTrue(mn.isLeaf());
		assertTrue(mn.getParent() == null);
		mn = MultiWayDepthNode.makeNode(5, 0, Sign.EQUALS, 1.234d);
		assertTrue(mn instanceof HeightExtension);
		assertTrue(mn.getTreeHeight() == 0);
		assertTrue(mn.getAttribute() == 0);
		assertTrue(mn.getChildCount() == 5);
		assertTrue(mn.getValue() == 1.234d);
		assertTrue(mn.getSign() == Sign.EQUALS);
		assertTrue(!mn.isLeaf());
		assertTrue(mn.getParent() == null);
	}

	@Test
	public void testNodeDepth() {
		BinaryDepthNode bn = BinaryDepthNode.makeNode(0, null, Integer.MIN_VALUE);
		assertTrue(bn.getTreeHeight() == 0);
		bn.setChildAt(0, BinaryDepthNode.makeNode(0, null, Integer.MIN_VALUE));
		assertTrue(bn.getTreeHeight() == 1);
		bn.setChildAt(1, BinaryDepthNode.makeNode(0, null, Integer.MIN_VALUE));
		assertTrue(bn.getTreeHeight() == 1);
		assertTrue(bn.getChildAt(0).getParent() == bn);
		bn.getChildAt(0).setChildAt(0, new BinaryDepthNode());
		assertTrue(bn.getTreeHeight() == 2);
		bn.getChildAt(1).setChildAt(1, new BinaryDepthNode());
		assertTrue(bn.getTreeHeight() == 2);
		bn.clearChilds();
		assertTrue(bn.getTreeHeight() == 0);
		assertTrue(bn.getChildAt(0) == null && bn.getChildAt(1) == null);

		MultiWayDepthNode mn = new MultiWayDepthNode(5);		
		assertTrue(mn.getTreeHeight() == 0);
		mn.setChildAt(0, new MultiWayDepthNode(2));		
		assertTrue(mn.getTreeHeight() == 1);
		mn.setChildAt(1, new MultiWayDepthNode(3));
		assertTrue(mn.getTreeHeight() == 1);
		assertTrue(mn.getChildAt(0).getParent() == mn);
		mn.getChildAt(0).setChildAt(0, new MultiWayDepthNode());
		assertTrue(mn.getTreeHeight() == 2);
		mn.getChildAt(1).setChildAt(2, new MultiWayDepthNode());
		assertTrue(mn.getTreeHeight() == 2);
		mn.clearChilds();
		assertTrue(mn.getTreeHeight() == 0);
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
		BinaryDepthNode bn = BinaryDepthNode.makeNode(0, null, Integer.MIN_VALUE);		
		bn.setChildAt(0, BinaryDepthNode.makeNode(0, null, Integer.MIN_VALUE));
		bn.setChildAt(1, BinaryDepthNode.makeNode(0, null, Integer.MIN_VALUE));				
		bn.getChildAt(0).setChildAt(0, new BinaryDepthNode());
		bn.getChildAt(1).setChildAt(1, new BinaryDepthNode());		
		assertTrue(Utils.computeHeight(bn) == 2);
		
		MultiWayDepthNode mn = new MultiWayDepthNode(5);
		mn.setAttribute(0);
		mn.setChildAt(0, new MultiWayDepthNode(2));
		mn.setChildAt(1, new MultiWayDepthNode(3));
		mn.getChildAt(0).setAttribute(0);
		mn.getChildAt(1).setAttribute(0);
		mn.getChildAt(0).setChildAt(0, new MultiWayDepthNode());
		mn.getChildAt(1).setChildAt(2, new MultiWayDepthNode());		
		assertTrue(Utils.computeHeight(mn) == 2);
		
	}

}
