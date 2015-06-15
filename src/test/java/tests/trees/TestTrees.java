package tests.trees;


import java.util.HashMap;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.Data;
import genlib.structures.extensions.HeightExtension;
import genlib.structures.trees.BinaryHeightNode;
import genlib.structures.trees.MultiWayHeightNode;
import genlib.utils.Utils;
import genlib.utils.WekaUtils;
import genlib.utils.Utils.Sign;

import org.junit.Test;

import weka.datagenerators.classifiers.classification.RDG1;
import static org.junit.Assert.*;

public class TestTrees {

	public static Data wekaData;
	public static Data arrayData;
	public static TreeIndividual wekaIndividual;
	
	static {

		try {
			String[] options = new String[] {
					"-r",
					"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
					"-S", "1", "-n", "100", "-a", "10", "-c", "2", "-N", "0",
					"-I", "0", "-M", "1", "-R", "10" };
			RDG1 rdg = new RDG1();
			rdg.setOptions(options);
			rdg.defineDataFormat();
			wekaData = new Data(rdg.generateExamples());

			HashMap<String, Integer> attrIndexMap = wekaData.getAttrIndexMap();
			HashMap<String, Integer>[] attrValueIndexMap = wekaData
					.getAttrValueIndexMap();

			String sTree = "digraph J48Tree {\n"
					+ "N0 [label=\"a5\" ]\n"
					+ "N0->N1 [label=\"\'= false\'\"]\n"
					+ "N1 [label=\"\'c0 (44.0/2.0)\'\" shape=box style=filled ]\n"
					+ "N0->N2 [label=\"\'= true\'\"]\n"
					+ "N2 [label=\"a8\" ]\n"
					+ "N2->N3 [label=\"\'= false\'\"]\n"
					+ "N3 [label=\"a9\" ]\n"
					+ "N3->N4 [label=\"\'= false\'\"]\n"
					+ "N4 [label=\"a2\" ]\n"
					+ "N4->N5 [label=\"\'= false\'\"]\n"
					+ "N5 [label=\"\'c0 (4.0/1.0)\'\" shape=box style=filled ]\n"
					+ "N4->N6 [label=\"\'= true\'\"]\n"
					+ "N6 [label=\"a0\" ]\n"
					+ "N6->N7 [label=\"\'= false\'\"]\n"
					+ "N7 [label=\"a4\" ]\n"
					+ "N7->N8 [label=\"\'= false\'\"]\n"
					+ "N8 [label=\"\'c1 (2.0)\'\" shape=box style=filled ]\n"
					+ "N7->N9 [label=\"\'= true\'\"]\n"
					+ "N9 [label=\"\'c0 (2.0)\'\" shape=box style=filled ]\n"
					+ "N6->N10 [label=\"\'= true\'\"]\n"
					+ "N10 [label=\"\'c1 (5.0)\'\" shape=box style=filled ]\n"
					+ "N3->N11 [label=\"\'= true\'\"]\n"
					+ "N11 [label=\"\'c1 (15.0/1.0)\'\" shape=box style=filled ]\n"
					+ "N2->N12 [label=\"\'= true\'\"]\n"
					+ "N12 [label=\"a1\" ]\n"
					+ "N12->N13 [label=\"\'= false\'\"]\n"
					+ "N13 [label=\"a2\" ]\n"
					+ "N13->N14 [label=\"\'= false\'\"]\n"
					+ "N14 [label=\"a0\" ]\n"
					+ "N14->N15 [label=\"\'= false\'\"]\n"
					+ "N15 [label=\"\'c1 (4.0/1.0)\'\" shape=box style=filled ]\n"
					+ "N14->N16 [label=\"\'= true\'\"]\n"
					+ "N16 [label=\"\'c0 (3.0)\'\" shape=box style=filled ]\n"
					+ "N13->N17 [label=\"\'= true\'\"]\n"
					+ "N17 [label=\"a4\" ]\n"
					+ "N17->N18 [label=\"\'= false\'\"]\n"
					+ "N18 [label=\"\'c1 (5.0)\'\" shape=box style=filled ]\n"
					+ "N17->N19 [label=\"\'= true\'\"]\n"
					+ "N19 [label=\"\'c0 (2.0)\'\" shape=box style=filled ]\n"
					+ "N12->N20 [label=\"\'= true\'\"]\n"
					+ "N20 [label=\"\'c0 (14.0/2.0)\'\" shape=box style=filled ]\n"
					+ "}";

			wekaIndividual = WekaUtils.constructTreeIndividual(sTree, 21,
					wekaData.numInstances(), attrIndexMap, attrValueIndexMap,
					true);

		} catch (Exception e) {
		}
	}
	
	@Test
	public void testNodeCreation() {
		BinaryHeightNode bn = new BinaryHeightNode();
		assertTrue(bn instanceof HeightExtension);
		assertTrue(bn.getTreeHeight() == 0);
		assertTrue(bn.getAttribute() == -1);
		assertTrue(bn.getChilds() == null);
		assertTrue(bn.getValue() == Integer.MIN_VALUE);
		assertTrue(bn.getSign() == null);
		assertTrue(bn.isLeaf());
		assertTrue(bn.getParent() == null);
		bn = BinaryHeightNode.makeLeaf(1.234d);
		assertTrue(bn instanceof HeightExtension);
		assertTrue(bn.getTreeHeight() == 0);
		assertTrue(bn.getAttribute() == -1);
		assertTrue(bn.getChilds() == null);
		assertTrue(bn.getValue() == 1.234d);
		assertTrue(bn.getSign() == null);
		assertTrue(bn.isLeaf());
		assertTrue(bn.getParent() == null);
		bn = BinaryHeightNode.makeNode(0, Sign.EQUALS, 1.234d);
		assertTrue(bn instanceof HeightExtension);
		assertTrue(bn.getTreeHeight() == 0);
		assertTrue(bn.getAttribute() == 0);
		assertTrue(bn.getChildCount() == 2);		
		assertTrue(bn.getValue() == 1.234d);
		assertTrue(bn.getSign() == Sign.EQUALS);
		assertTrue(!bn.isLeaf());
		assertTrue(bn.getParent() == null);
		MultiWayHeightNode mn = new MultiWayHeightNode();
		assertTrue(mn instanceof HeightExtension);
		assertTrue(mn.getTreeHeight() == 0);
		assertTrue(mn.getAttribute() == -1);
		assertTrue(mn.getChilds() == null);
		assertTrue(mn.getValue() == Integer.MIN_VALUE);
		assertTrue(mn.getSign() == null);
		assertTrue(mn.isLeaf());
		assertTrue(mn.getParent() == null);
		mn = MultiWayHeightNode.makeLeaf(1.234d);
		assertTrue(mn instanceof HeightExtension);
		assertTrue(mn.getTreeHeight() == 0);
		assertTrue(mn.getAttribute() == -1);
		assertTrue(mn.getChilds() == null);
		assertTrue(mn.getValue() == 1.234d);
		assertTrue(mn.getSign() == null);
		assertTrue(mn.isLeaf());
		assertTrue(mn.getParent() == null);
		mn = MultiWayHeightNode.makeNode(5, 0, Sign.EQUALS, 1.234d);
		assertTrue(mn instanceof HeightExtension);
		assertTrue(mn.getTreeHeight() == 0);
		assertTrue(mn.getAttribute() == 0);
		assertTrue(mn.getChildCount() == 5);
		assertTrue(mn.getValue() == 1.234d);
		assertTrue(mn.getSign() == Sign.EQUALS);
		assertTrue(!mn.isLeaf());
		assertTrue(mn.getParent() == null);
		
		bn.makeLeaf();
		assertTrue(bn.isLeaf());
		assertNull(bn.getChilds());
		mn.makeLeaf();
		assertNull(mn.getChilds());	
		assertTrue(mn.isLeaf());
	}

	@Test
	public void testNodeHeight() {
		BinaryHeightNode bn = BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE);
		assertTrue(bn.getTreeHeight() == 0);
		bn.setChildAt(0, BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE));
		assertTrue(bn.getTreeHeight() == 1);
		bn.setChildAt(1, BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE));
		assertTrue(bn.getTreeHeight() == 1);
		assertTrue(bn.getChildAt(0).getParent() == bn);
		bn.getChildAt(0).setChildAt(0, new BinaryHeightNode());
		assertTrue(bn.getTreeHeight() == 2);
		bn.getChildAt(1).setChildAt(1, new BinaryHeightNode());
		assertTrue(bn.getTreeHeight() == 2);
		bn.clearChilds();
		assertTrue(bn.getTreeHeight() == 0);
		assertTrue(bn.getChildAt(0) == null && bn.getChildAt(1) == null);

		MultiWayHeightNode mn = new MultiWayHeightNode(5);		
		assertTrue(mn.getTreeHeight() == 0);
		mn.setChildAt(0, new MultiWayHeightNode(2));		
		assertTrue(mn.getTreeHeight() == 1);
		mn.setChildAt(1, new MultiWayHeightNode(3));
		assertTrue(mn.getTreeHeight() == 1);
		assertTrue(mn.getChildAt(0).getParent() == mn);
		mn.getChildAt(0).setChildAt(0, new MultiWayHeightNode());
		assertTrue(mn.getTreeHeight() == 2);
		mn.getChildAt(1).setChildAt(2, new MultiWayHeightNode());
		assertTrue(mn.getTreeHeight() == 2);
		mn.clearChilds();
		assertTrue(mn.getTreeHeight() == 0);
		assertTrue(mn.getChildAt(0) == null && mn.getChildAt(1) == null);	
		
		mn = (MultiWayHeightNode)wekaIndividual.getRootNode();
		assertTrue(mn.getTreeHeight() == 6);		
	}
	
	@Test
	public void testNodeHeightComputing() {
		BinaryHeightNode bn = BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE);		
		bn.setChildAt(0, BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE));
		bn.setChildAt(1, BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE));				
		bn.getChildAt(0).setChildAt(0, new BinaryHeightNode());
		bn.getChildAt(1).setChildAt(1, new BinaryHeightNode());		
		assertTrue(Utils.computeHeight(bn) == 2);
		
		MultiWayHeightNode mn = new MultiWayHeightNode(5);
		mn.setAttribute(0);
		mn.setChildAt(0, new MultiWayHeightNode(2));
		mn.setChildAt(1, new MultiWayHeightNode(3));
		mn.getChildAt(0).setAttribute(0);
		mn.getChildAt(1).setAttribute(0);
		mn.getChildAt(0).setChildAt(0, new MultiWayHeightNode());
		mn.getChildAt(1).setChildAt(2, new MultiWayHeightNode());		
		assertTrue(Utils.computeHeight(mn) == 2);	
		
		mn = (MultiWayHeightNode)wekaIndividual.getRootNode();
		assertTrue(Utils.computeHeight(mn) == 6);	
	}
	
	@Test
	public void testTreeSize() {
		BinaryHeightNode bn = BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE);
		assertTrue(bn.getTreeSize() == 1);
		bn.setChildAt(0, BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE));
		assertTrue(bn.getTreeSize() == 2);
		bn.setChildAt(1, BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE));
		assertTrue(bn.getTreeSize() == 3);
		assertTrue(bn.getChildAt(0).getParent() == bn);
		assertTrue(bn.getChildAt(1).getParent() == bn);
		assertTrue(bn.getChildAt(0).getTreeSize() == 1);
		assertTrue(bn.getChildAt(1).getTreeSize() == 1);
		bn.getChildAt(0).setChildAt(0, new BinaryHeightNode());
		assertTrue(bn.getTreeSize() == 4);
		bn.getChildAt(1).setChildAt(1, new BinaryHeightNode());
		assertTrue(bn.getTreeSize() == 5);
		bn.clearChilds();
		assertTrue(bn.getTreeSize() == 1);		

		MultiWayHeightNode mn = new MultiWayHeightNode(5);		
		assertTrue(mn.getTreeSize() == 1);
		mn.setChildAt(0, new MultiWayHeightNode(2));		
		assertTrue(mn.getTreeSize() == 2);
		mn.setChildAt(1, new MultiWayHeightNode(3));
		assertTrue(mn.getTreeSize() == 3);
		assertTrue(mn.getChildAt(0).getParent() == mn);
		assertTrue(mn.getChildAt(1).getParent() == mn);
		assertTrue(mn.getChildAt(2) == null);
		assertTrue(mn.getChildAt(3) == null);
		assertTrue(mn.getChildAt(4) == null);
		assertTrue(mn.getChildAt(0).getTreeSize() == 1);
		assertTrue(mn.getChildAt(1).getTreeSize() == 1);
		mn.getChildAt(0).setChildAt(0, new MultiWayHeightNode());
		assertTrue(mn.getTreeSize() == 4);
		mn.getChildAt(1).setChildAt(2, new MultiWayHeightNode());
		assertTrue(mn.getTreeSize() == 5);
		mn.clearChilds();
		assertTrue(mn.getTreeSize() == 1);
		
		mn = (MultiWayHeightNode)wekaIndividual.getRootNode();
		assertTrue(mn.getTreeSize() == 21);			
		assertTrue(Utils.computeSize(mn) == 21);	
	}

	@Test
	public void testTreeSizeComputing() {
		BinaryHeightNode bn = BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE);		
		bn.setChildAt(0, BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE));
		bn.setChildAt(1, BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE));				
		bn.getChildAt(0).setChildAt(0, new BinaryHeightNode());
		bn.getChildAt(1).setChildAt(1, new BinaryHeightNode());		
		assertTrue(Utils.computeSize(bn) == 5);
		
		MultiWayHeightNode mn = new MultiWayHeightNode(5);
		mn.setAttribute(0);
		mn.setChildAt(0, new MultiWayHeightNode(2));
		mn.setChildAt(1, new MultiWayHeightNode(3));
		mn.getChildAt(0).setAttribute(0);
		mn.getChildAt(1).setAttribute(0);
		mn.getChildAt(0).setChildAt(0, new MultiWayHeightNode());
		mn.getChildAt(1).setChildAt(2, new MultiWayHeightNode());		
		assertTrue(Utils.computeSize(mn) == 5);
		
	}
	
	@Test
	public void testNumNodesComputing() {
		BinaryHeightNode bn = BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE);		
		bn.setChildAt(0, BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE));
		bn.setChildAt(1, BinaryHeightNode.makeNode(0, null, Integer.MIN_VALUE));				
		bn.getChildAt(0).setChildAt(0, new BinaryHeightNode());
		bn.getChildAt(1).setChildAt(1, new BinaryHeightNode());		
		assertTrue(Utils.computeNumNodes(bn) == 3);
		
		MultiWayHeightNode mn = new MultiWayHeightNode(5);
		mn.setAttribute(0);
		mn.setChildAt(0, new MultiWayHeightNode(2));
		mn.setChildAt(1, new MultiWayHeightNode(3));
		mn.getChildAt(0).setAttribute(0);
		mn.getChildAt(1).setAttribute(0);
		mn.getChildAt(0).setChildAt(0, new MultiWayHeightNode());
		mn.getChildAt(1).setChildAt(2, new MultiWayHeightNode());		
		assertTrue(Utils.computeNumNodes(mn) == 3);
		
		mn = (MultiWayHeightNode)wekaIndividual.getRootNode();		
		assertTrue(Utils.computeNumNodes(mn) == 10);	
	}
	
}
