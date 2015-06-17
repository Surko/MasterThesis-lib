package tests.individuals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.MultiWayHeightNode;
import genlib.structures.trees.Node;
import genlib.utils.Utils;
import genlib.utils.Utils.Sign;
import genlib.utils.WekaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.junit.Test;

import weka.core.Instances;
import weka.datagenerators.classifiers.classification.RDG1;

public class TestIndividuals {

	public static Instances wekaData;
	public static GenLibInstances arrayData;
	public static TreeIndividual t1, t2;

	static {
		String[] options = new String[] {
				"-r",
				"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
				"-S", "1", "-n", "100", "-a", "10", "-c", "2", "-N", "0", "-I",
				"0", "-M", "1", "-R", "10" };
		RDG1 rdg = new RDG1();
		try {
			rdg.setOptions(options);
			rdg.defineDataFormat();
			wekaData = rdg.generateExamples();
		} catch (Exception e) {
		}

		HashMap<String, Integer> attrIndexMap = WekaUtils
				.makeAttrIndexMap(wekaData);
		HashMap<String, Integer>[] attrValueIndexMap = WekaUtils
				.makeAttrValueIndexMap(wekaData);

		String sTree = "digraph J48Tree {\n" + "N0 [label=\"a5\" ]\n"
				+ "N0->N1 [label=\"\'= false\'\"]\n"
				+ "N1 [label=\"\'c0 (44.0/2.0)\'\" shape=box style=filled ]\n"
				+ "N0->N2 [label=\"\'= true\'\"]\n" + "N2 [label=\"a8\" ]\n"
				+ "N2->N3 [label=\"\'= false\'\"]\n" + "N3 [label=\"a9\" ]\n"
				+ "N3->N4 [label=\"\'= false\'\"]\n" + "N4 [label=\"a2\" ]\n"
				+ "N4->N5 [label=\"\'= false\'\"]\n"
				+ "N5 [label=\"\'c0 (4.0/1.0)\'\" shape=box style=filled ]\n"
				+ "N4->N6 [label=\"\'= true\'\"]\n" + "N6 [label=\"a0\" ]\n"
				+ "N6->N7 [label=\"\'= false\'\"]\n" + "N7 [label=\"a4\" ]\n"
				+ "N7->N8 [label=\"\'= false\'\"]\n"
				+ "N8 [label=\"\'c1 (2.0)\'\" shape=box style=filled ]\n"
				+ "N7->N9 [label=\"\'= true\'\"]\n"
				+ "N9 [label=\"\'c0 (2.0)\'\" shape=box style=filled ]\n"
				+ "N6->N10 [label=\"\'= true\'\"]\n"
				+ "N10 [label=\"\'c1 (5.0)\'\" shape=box style=filled ]\n"
				+ "N3->N11 [label=\"\'= true\'\"]\n"
				+ "N11 [label=\"\'c1 (15.0/1.0)\'\" shape=box style=filled ]\n"
				+ "N2->N12 [label=\"\'= true\'\"]\n" + "N12 [label=\"a1\" ]\n"
				+ "N12->N13 [label=\"\'= false\'\"]\n"
				+ "N13 [label=\"a2\" ]\n"
				+ "N13->N14 [label=\"\'= false\'\"]\n"
				+ "N14 [label=\"a0\" ]\n"
				+ "N14->N15 [label=\"\'= false\'\"]\n"
				+ "N15 [label=\"\'c1 (4.0/1.0)\'\" shape=box style=filled ]\n"
				+ "N14->N16 [label=\"\'= true\'\"]\n"
				+ "N16 [label=\"\'c0 (3.0)\'\" shape=box style=filled ]\n"
				+ "N13->N17 [label=\"\'= true\'\"]\n" + "N17 [label=\"a4\" ]\n"
				+ "N17->N18 [label=\"\'= false\'\"]\n"
				+ "N18 [label=\"\'c1 (5.0)\'\" shape=box style=filled ]\n"
				+ "N17->N19 [label=\"\'= true\'\"]\n"
				+ "N19 [label=\"\'c0 (2.0)\'\" shape=box style=filled ]\n"
				+ "N12->N20 [label=\"\'= true\'\"]\n"
				+ "N20 [label=\"\'c0 (14.0/2.0)\'\" shape=box style=filled ]\n"
				+ "}";

		FitnessFunction.registeredFunctions = 2;
		t1 = WekaUtils
				.constructTreeIndividual(sTree, 21, wekaData.numInstances(),
						attrIndexMap, attrValueIndexMap, false);
		t2 = WekaUtils
				.constructTreeIndividual(sTree, 21, wekaData.numInstances(),
						attrIndexMap, attrValueIndexMap, false);

	}

	@Test
	public void testIndividualCreation() {
		MultiWayHeightNode root = MultiWayHeightNode.makeNode(2, 1, Sign.LESS,
				20d);
		MultiWayHeightNode[] childs = new MultiWayHeightNode[2];
		childs[0] = MultiWayHeightNode.makeLeaf(1);
		childs[1] = MultiWayHeightNode.makeLeaf(0);
		root.setChilds(childs);
		TreeIndividual ind1 = new TreeIndividual(root);
		TreeIndividual[] testing = new TreeIndividual[10];

		for (int i = 0; i < 5; i++) {
			testing[i] = new TreeIndividual(ind1);
		}
		for (int i = 5; i < 10; i++) {
			testing[i] = ind1.copy();
		}

		for (int i = 0; i < 10; i++) {
			assertFalse(ind1 == testing[i]);
			assertFalse(ind1.getRootNode() == testing[i].getRootNode());
			assertFalse(ind1.getRootNode().getChildAt(0) == testing[i]
					.getRootNode().getChildAt(0));
			assertFalse(ind1.getRootNode().getChildAt(1) == testing[i]
					.getRootNode().getChildAt(1));
			assertTrue(ind1.getRootNode().getChildCount() == testing[i]
					.getRootNode().getChildCount());
			assertTrue(ind1.getRootNode().getAttribute() == 1);
			assertTrue(ind1.getRootNode().getAttribute() == testing[i]
					.getRootNode().getAttribute());
			assertTrue(ind1.getRootNode().getValue() == 20d);
			assertTrue(ind1.getRootNode().getValue() == testing[i]
					.getRootNode().getValue());
			assertTrue(ind1.getRootNode().getSign() == Sign.LESS);
			assertTrue(ind1.getRootNode().getSign() == testing[i].getRootNode()
					.getSign());
		}
	}

	@Test
	public void testIndividualEquality() throws Exception {
		assertTrue(t1.equals(t2));
	}

	@Test
	public void testIndividualSubTreeChange1() {
		Random random = new Random(0L);
		TreeIndividual t1Copy = t1.copy();
		TreeIndividual t2Copy = t2.copy();

		int treeSize1 = Utils.computeSize(t1Copy.getRootNode());
		int treeSize2 = Utils.computeSize(t2Copy.getRootNode());

		assertTrue(treeSize1 == treeSize2);

		int i1 = random.nextInt(treeSize1);
		int i2 = random.nextInt(treeSize2);
		System.out.println("normal subtree Index1 " + i1);
		System.out.println("normal subtree Index2 " + i2);

		assertTrue(i1 != i2);

		Node subTree1 = Utils.getNode(t1Copy.getRootNode(), i1);
		Node subTree2 = Utils.getNode(t2Copy.getRootNode(), i2);

		assertTrue(!subTree1.equals(subTree2));
		assertNotNull(subTree1.getParent());
		assertNotNull(subTree2.getParent());

		Node parent1 = subTree1.getParent();
		Node parent2 = subTree2.getParent();

		for (int i = 0; i < parent1.getChildCount(); i++) {
			if (parent1.getChildAt(i) == subTree1) {
				parent1.setChildAt(i, subTree2);
			}
		}

		for (int i = 0; i < parent2.getChildCount(); i++) {
			if (parent2.getChildAt(i) == subTree2) {
				parent2.setChildAt(i, subTree1);
			}
		}

		int nTreeSize1 = Utils.computeSize(t1Copy.getRootNode());
		int nTreeSize2 = Utils.computeSize(t2Copy.getRootNode());
		int subTreeSize1 = Utils.computeSize(subTree1);
		int subTreeSize2 = Utils.computeSize(subTree2);

		System.out.println(treeSize1);
		System.out.println(treeSize2);
		System.out.println(subTreeSize1);
		System.out.println(subTreeSize2);
		System.out.println(nTreeSize1);
		System.out.println(nTreeSize2);

		assertTrue(nTreeSize1 == treeSize1 - subTreeSize1 + subTreeSize2);
		assertTrue(nTreeSize2 == treeSize2 - subTreeSize2 + subTreeSize1);

	}

	@Test
	public void testIndividualSubTreeChange2() {
		Random random = new Random(0L);
		TreeIndividual t1Copy = t1.copy();
		TreeIndividual t2Copy = t2.copy();

		int treeSize1 = Utils.computeSize(t1Copy.getRootNode());
		int treeSize2 = Utils.computeSize(t2Copy.getRootNode());

		assertTrue(treeSize1 == treeSize2);

		int i1 = random.nextInt(treeSize1);
		int i2 = random.nextInt(treeSize2);
		System.out.println("extended subtree Index1 " + i1);
		System.out.println("extended subtree Index2 " + i2);

		assertTrue(i1 != i2);

		Node subTree1 = Utils.getExtensionNode(t1Copy.getRootNode(), i1);
		Node subTree2 = Utils.getExtensionNode(t2Copy.getRootNode(), i2);

		assertTrue(!subTree1.equals(subTree2));
		assertNotNull(subTree1.getParent());
		assertNotNull(subTree2.getParent());

		Node parent1 = subTree1.getParent();
		Node parent2 = subTree2.getParent();

		for (int i = 0; i < parent1.getChildCount(); i++) {
			if (parent1.getChildAt(i) == subTree1) {
				parent1.setChildAt(i, subTree2);
			}
		}

		for (int i = 0; i < parent2.getChildCount(); i++) {
			if (parent2.getChildAt(i) == subTree2) {
				parent2.setChildAt(i, subTree1);
			}
		}

		int nTreeSize1 = Utils.computeSize(t1Copy.getRootNode());
		int nTreeSize2 = Utils.computeSize(t2Copy.getRootNode());
		int subTreeSize1 = Utils.computeSize(subTree1);
		int subTreeSize2 = Utils.computeSize(subTree2);

		System.out.println(treeSize1);
		System.out.println(treeSize2);
		System.out.println(subTreeSize1);
		System.out.println(subTreeSize2);
		System.out.println(nTreeSize1);
		System.out.println(nTreeSize2);

		assertTrue(nTreeSize1 == treeSize1 - subTreeSize1 + subTreeSize2);
		assertTrue(nTreeSize2 == treeSize2 - subTreeSize2 + subTreeSize1);

	}

	@Test
	public void testComputationOfSizes() {
		TreeIndividual t1Copy = t1.copy();
		
		assertTrue(Utils.computeHeight(t1Copy.getRootNode()) == 6);
		assertTrue(Utils.computeSize(t1Copy.getRootNode()) == 21);
		assertTrue(Utils.computeNumNodes(t1Copy.getRootNode()) == 10);
		assertTrue(Utils.computeNumLeaves(t1Copy.getRootNode()) == 11);
		
		assertTrue(t1Copy.getTreeHeight() == 6);
		assertTrue(t1Copy.getNumLeaves() == 11);
		assertTrue(t1Copy.getNumNodes() == 10);
	}
	
	@Test
	public void testLeaves() {
		TreeIndividual t1Copy = t1.copy();				
		
		ArrayList<Node> leaves1 = null;		
		leaves1 = Utils.getLeaves(t1Copy.getRootNode());			
		
		ArrayList<Node> leaves2 = new ArrayList<>();
		Utils.getLeavesRecursive(t1Copy.getRootNode(), leaves2);
		
		System.out.println("stack leaves count " + leaves1.size());
		System.out.println("recursive leaves count " + leaves2.size());
		
		assertTrue(leaves1.size() == leaves2.size());
		assertTrue(leaves1.equals(leaves2));
		
		leaves1 = new ArrayList<>();		
		leaves1 = Utils.getLeaves(t1Copy.getRootNode(), leaves1);
		
		leaves2 = new ArrayList<>();
		Utils.getLeavesRecursive(t1Copy.getRootNode(), leaves2);
		
		System.out.println("stack leaves count " + leaves1.size());
		System.out.println("recursive leaves count " + leaves2.size());
		
		assertTrue(leaves1.size() == leaves2.size());
		assertTrue(leaves1.equals(leaves2));
		
	}
	
	@Test
	public void testNodes() {
		TreeIndividual t1Copy = t1.copy();		
				
		ArrayList<Node> nodes1 = null;		
		nodes1 = Utils.getNodes(t1Copy.getRootNode());			
		
		ArrayList<Node> nodes2 = new ArrayList<>();
		Utils.getNodesRecursive(t1Copy.getRootNode(), nodes2);
		
		System.out.println("stack nodes count " + nodes1.size());
		System.out.println("recursive nodes count " + nodes2.size());
		
		assertTrue(nodes1.size() == nodes2.size());
		assertTrue(nodes1.equals(nodes2));
		
		nodes1 = new ArrayList<>();		
		nodes1 = Utils.getNodes(t1Copy.getRootNode(), nodes1);
		
		nodes2 = new ArrayList<>();
		Utils.getNodesRecursive(t1Copy.getRootNode(), nodes2);
		
		System.out.println("stack nodes count " + nodes1.size());
		System.out.println("recursive nodes count " + nodes2.size());
		
		assertTrue(nodes1.size() == nodes2.size());
		assertTrue(nodes1.equals(nodes2));
		
	}
	
	@Test
	public void testRegression1() {
		Random random = new Random(0L);
		TreeIndividual t1Copy = t1.copy();

		int treeSize1 = Utils.computeSize(t1Copy.getRootNode());

		int r = 0;
		@SuppressWarnings("unused")
		Node subTree = null;

		long start1 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			r = random.nextInt(treeSize1);
			subTree = Utils.getNode(t1Copy.getRootNode(), r);
		}
		long end1 = System.nanoTime();

		System.out.println("TreeSize Not Optimized " + (end1 - start1));

		long start2 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			r = random.nextInt(treeSize1);
			subTree = Utils.getExtensionNode(t1Copy.getRootNode(), r);
		}
		long end2 = System.nanoTime();

		System.out.println("TreeSize Optimized " + (end2 - start2));

		assertTrue((end2 - start2) < (end1 - start1));
	}
	
	@Test
	public void testRegression2() {		
		TreeIndividual t1Copy = t1.copy();		
			
		long start1 = System.nanoTime();
		ArrayList<Node> leaves1 = new ArrayList<>();
		for (int i = 0; i < 100000; i++) {			
			leaves1 = Utils.getLeaves(t1Copy.getRootNode(), leaves1);
			leaves1.clear();
		}
		long end1 = System.nanoTime();

		System.out.println("Leaf count Not Optimized " + (end1 - start1));

		long start2 = System.nanoTime();
		ArrayList<Node> leaves2 = new ArrayList<>();
		for (int i = 0; i < 100000; i++) {			
			Utils.getLeavesRecursive(t1Copy.getRootNode(), leaves2);
			leaves2.clear();
		}
		long end2 = System.nanoTime();

		System.out.println("Leaf count Optimized " + (end2 - start2));

		//assertTrue((end2 - start2) < (end1 - start1));
	}
	
	@Test
	public void testRegression3() {		
		TreeIndividual t1Copy = t1.copy();		
			
		long start1 = System.nanoTime();
		ArrayList<Node> nodes1 = new ArrayList<>();
		for (int i = 0; i < 100000; i++) {			
			nodes1 = Utils.getNodes(t1Copy.getRootNode(), nodes1);
			nodes1.clear();
		}
		long end1 = System.nanoTime();

		System.out.println("Node count Not Optimized " + (end1 - start1));

		long start2 = System.nanoTime();
		ArrayList<Node> nodes2 = new ArrayList<>();
		for (int i = 0; i < 100000; i++) {			
			Utils.getNodesRecursive(t1Copy.getRootNode(), nodes2);
			nodes2.clear();
		}
		long end2 = System.nanoTime();

		System.out.println("Node count Optimized " + (end2 - start2));

		//assertTrue((end2 - start2) < (end1 - start1));
	}
}
