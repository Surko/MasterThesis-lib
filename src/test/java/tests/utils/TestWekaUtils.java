package tests.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.Data;
import genlib.structures.data.GenLibInstances;
import genlib.structures.extensions.HeightExtension;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.structures.trees.MultiWayNode;
import genlib.structures.trees.Node;
import genlib.utils.Utils;
import genlib.utils.Utils.Sign;
import genlib.utils.WekaUtils;

import java.util.HashMap;

import org.junit.Test;

import tests.TestProperties;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.datagenerators.classifiers.classification.RDG1;

public class TestWekaUtils {

	private static Instances wekaData, regData;
	
	static {
		try {
			// binary class problem
			String[] options = new String[] {
					"-r",
					"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
					"-S", "1", "-n", "100", "-a", "10", "-c", "2", "-N", "0",
					"-I", "0", "-M", "1", "-R", "10" };
			RDG1 rdg = new RDG1();
			rdg.setOptions(options);
			rdg.defineDataFormat();
			wekaData = rdg.generateExamples();
			
			options = new String[] {
					"-r",
					"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
					"-S", "1", "-n", "100", "-a", "10", "-c", "2", "-N", "0",
					"-I", "0", "-M", "1", "-R", "10" };
			
			rdg = new RDG1();
			rdg.setOptions(options);
			rdg.defineDataFormat();
			regData = rdg.generateExamples();

		} catch (Exception e) {

		}
	}
	
	@Test
	public void testAttrMaps() throws Exception {				
		HashMap<String, Integer> attrIndexMap = WekaUtils
				.makeAttrIndexMap(wekaData);
		HashMap<String, Integer>[] attrValueIndexMap = WekaUtils
				.makeAttrValueIndexMap(wekaData);

		assertTrue(attrIndexMap.size() == 11);
		assertTrue(attrValueIndexMap.length == 11);
		assertTrue(attrValueIndexMap[0].get("false") == 0);
		assertTrue(attrValueIndexMap[5].get("true") == 1);
		assertTrue(attrValueIndexMap[10].get("c0") == 0);
		assertTrue(attrValueIndexMap[10].get("c1") == 1);
	}

	@Test
	public void testWekaToNodeConversion() throws Exception {		
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
		TreeIndividual individual = WekaUtils.constructTreeIndividual(sTree,
				21, wekaData.numInstances(), attrIndexMap, attrValueIndexMap,
				false);

		Node root = individual.getRootNode();
		assertTrue(root.getChildCount() == 2);
		assertTrue(root.getAttribute() == 5);
		assertEquals(root.getSign(), Sign.EQUALS);
		assertTrue(root.getChildAt(0).isLeaf());
		assertTrue(root.getChildAt(0).getValue() == 0);
		assertNull(root.getChildAt(0).getChilds());
		assertNull(root.getChildAt(0).getSign());
		assertTrue(root.getChildAt(1).getChildCount() == 2);
		assertTrue(root.getChildAt(1).getAttribute() == 8);
		assertEquals(root.getChildAt(1).getSign(), Sign.EQUALS);
		assertTrue(Utils.computeHeight(root) == 6);

		Node n = root.getChildAt(1).getChildAt(1).getChildAt(0).getChildAt(1);
		assertTrue(n.getAttribute() == 4);
		assertTrue(n.getChildCount() == 2);
		assertTrue(n.getChildAt(0).isLeaf());
		assertNull(n.getChildAt(0).getChilds());
		assertTrue(n.getChildAt(0).getValue() == 1);
		assertTrue(Utils.computeHeight(n) == 1);

		n = root.getChildAt(1).getChildAt(0);
		assertTrue(n.getAttribute() == 9);
		assertTrue(n.getChildCount() == 2);
		assertTrue(n.getChildAt(1).isLeaf());
		assertNull(n.getChildAt(1).getChilds());
		assertTrue(n.getChildAt(1).getValue() == 1);
		assertTrue(Utils.computeHeight(n) == 4);

		n = root.getChildAt(1).getChildAt(1);
		assertTrue(n.getAttribute() == 1);
		assertTrue(n.getChildCount() == 2);
		assertTrue(n.getChildAt(1).isLeaf());
		assertNull(n.getChildAt(1).getChilds());
		assertTrue(n.getChildAt(1).getValue() == 0);
		assertTrue(Utils.computeHeight(n) == 3);

		individual = WekaUtils.constructTreeIndividual(sTree, 21,
				wekaData.numInstances(), attrIndexMap, attrValueIndexMap, true);
		root = individual.getRootNode();
		assertTrue(((HeightExtension) root).getTreeHeight() == 6);
		assertTrue(((HeightExtension) root.getChildAt(1).getChildAt(0))
				.getTreeHeight() == 4);
		assertTrue(((HeightExtension) root.getChildAt(1).getChildAt(1))
				.getTreeHeight() == 3);
	}

	@Test
	public void testConfusionMatrixCreation() {

		Instances wekaDataThree = null;
		TreeIndividual wekaIndividual = null, testIndividual = null, wekaThreeIndividual = null;

		try {			
			HashMap<String, Integer> attrIndexMap = WekaUtils
					.makeAttrIndexMap(wekaData);
			HashMap<String, Integer>[] attrValueIndexMap = WekaUtils
					.makeAttrValueIndexMap(wekaData);

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

			MultiWayDepthNode root = MultiWayDepthNode.makeNode(2, 1,
					Sign.LESS, 20d);
			MultiWayDepthNode[] childs = new MultiWayDepthNode[2];
			childs[0] = MultiWayDepthNode.makeLeaf(1);
			childs[1] = MultiWayDepthNode.makeLeaf(0);
			root.setChilds(childs);
			testIndividual = new TreeIndividual(root);

			// ternary class problem
			String[] options = new String[] {
					"-r",
					"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
					"-S", "1", "-n", "100", "-a", "10", "-c", "3", "-N", "0",
					"-I", "0", "-M", "1", "-R", "10" };
			RDG1 rdg = new RDG1();
			rdg.setOptions(options);
			rdg.defineDataFormat();
			wekaDataThree = rdg.generateExamples();

			attrIndexMap = WekaUtils.makeAttrIndexMap(wekaDataThree);
			attrValueIndexMap = WekaUtils.makeAttrValueIndexMap(wekaDataThree);

			sTree = "digraph J48Tree {\n"
					+ "N0 [label=\"a9\" ]\n"
					+ "N0->N1 [label=\"\'= false\'\"]\n"
					+ "N1 [label=\"a4\" ]\n"
					+ "N1->N2 [label=\"\'= false\'\"]\n"
					+ "N2 [label=\"a1\" ]\n"
					+ "N2->N3 [label=\"\'= false\'\"]\n"
					+ "N3 [label=\"\'c1 (17.0/2.0)\'\" shape=box style=filled ]\n"
					+ "N2->N4 [label=\"\'= true\'\"]\n"
					+ "N4 [label=\"a5\" ]\n"
					+ "N4->N5 [label=\"\'= false\'\"]\n"
					+ "N5 [label=\"\'c1 (3.0)\'\" shape=box style=filled ]\n"
					+ "N4->N6 [label=\"\'= true\'\"]\n"
					+ "N6 [label=\"\'c2 (5.0/1.0)\'\" shape=box style=filled ]\n"
					+ "N1->N7 [label=\"\'= true\'\"]\n"
					+ "N7 [label=\"a5\" ]\n"
					+ "N7->N8 [label=\"\'= false\'\"]\n"
					+ "N8 [label=\"a6\" ]\n"
					+ "N8->N9 [label=\"\'= false\'\"]\n"
					+ "N9 [label=\"\'c1 (4.0)\'\" shape=box style=filled ]\n"
					+ "N8->N10 [label=\"\'= true\'\"]\n"
					+ "N10 [label=\"\'c2 (5.0)\'\" shape=box style=filled ]\n"
					+ "N7->N11 [label=\"\'= true\'\"]\n"
					+ "N11 [label=\"a8\" ]\n"
					+ "N11->N12 [label=\"\'= false\'\"]\n"
					+ "N12 [label=\"\'c0 (6.0/1.0)\'\" shape=box style=filled ]\n"
					+ "N11->N13 [label=\"\'= true\'\"]\n"
					+ "N13 [label=\"a6\" ]\n"
					+ "N13->N14 [label=\"\'= false\'\"]\n"
					+ "N14 [label=\"a1\" ]\n"
					+ "N14->N15 [label=\"\'= false\'\"]\n"
					+ "N15 [label=\"\'c0 (3.0)\'\" shape=box style=filled ]\n"
					+ "N14->N16 [label=\"\'= true\'\"]\n"
					+ "N16 [label=\"\'c2 (3.0/1.0)\'\" shape=box style=filled ]\n"
					+ "N13->N17 [label=\"\'= true\'\"]\n"
					+ "N17 [label=\"\'c2 (3.0)\'\" shape=box style=filled ]\n"
					+ "N0->N18 [label=\"\'= true\'\"]\n"
					+ "N18 [label=\"a8\" ]\n"
					+ "N18->N19 [label=\"\'= false\'\"]\n"
					+ "N19 [label=\"\'c1 (28.0/2.0)\'\" shape=box style=filled ]\n"
					+ "N18->N20 [label=\"\'= true\'\"]\n"
					+ "N20 [label=\"a6\" ]\n"
					+ "N20->N21 [label=\"\'= false\'\"]\n"
					+ "N21 [label=\"a5\" ]\n"
					+ "N21->N22 [label=\"\'= false\'\"]\n"
					+ "N22 [label=\"\'c1 (6.0)\'\" shape=box style=filled ]\n"
					+ "N21->N23 [label=\"\'= true\'\"]\n"
					+ "N23 [label=\"a2\" ]\n"
					+ "N23->N24 [label=\"\'= false\'\"]\n"
					+ "N24 [label=\"a1\" ]\n"
					+ "N24->N25 [label=\"\'= false\'\"]\n"
					+ "N25 [label=\"\'c1 (2.0)\'\" shape=box style=filled ]\n"
					+ "N24->N26 [label=\"\'= true\'\"]\n"
					+ "N26 [label=\"\'c0 (3.0/1.0)\'\" shape=box style=filled ]\n"
					+ "N23->N27 [label=\"\'= true\'\"]\n"
					+ "N27 [label=\"\'c0 (4.0/1.0)\'\" shape=box style=filled ]\n"
					+ "N20->N28 [label=\"\'= true\'\"]\n"
					+ "N28 [label=\"a0\" ]\n"
					+ "N28->N29 [label=\"\'= false\'\"]\n"
					+ "N29 [label=\"\'c1 (2.0)\'\" shape=box style=filled ]\n"
					+ "N28->N30 [label=\"\'= true\'\"]\n"
					+ "N30 [label=\"a1\" ]\n"
					+ "N30->N31 [label=\"\'= false\'\"]\n"
					+ "N31 [label=\"\'c2 (3.0/1.0)\'\" shape=box style=filled ]\n"
					+ "N30->N32 [label=\"\'= true\'\"]\n"
					+ "N32 [label=\"\'c0 (3.0)\'\" shape=box style=filled ]\n"
					+ "}";

			wekaThreeIndividual = WekaUtils.constructTreeIndividual(sTree, 33,
					wekaDataThree.numInstances(), attrIndexMap, attrValueIndexMap,
					true);

		} catch (Exception e) {
			System.out.println("Something strange, in the neighborhood");
			e.printStackTrace();
		}

		double[][] confusionMatrix = WekaUtils.makeConfusionMatrix(
				wekaIndividual, wekaData);

		if (TestProperties.testPrints) {
			for (double[] row : confusionMatrix) {
				for (double element : row) {
					System.out.print(element + " ");
				}
				System.out.println();
			}
		}
		assertTrue(confusionMatrix[0][0] == 64.0);
		assertTrue(confusionMatrix[0][1] == 5.0);
		assertTrue(confusionMatrix[1][0] == 2.0);
		assertTrue(confusionMatrix[1][1] == 29.0);

		confusionMatrix = WekaUtils.makeConfusionMatrix(testIndividual,
				wekaData);

		if (TestProperties.testPrints) {
			for (double[] row : confusionMatrix) {
				for (double element : row) {
					System.out.print(element + " ");
				}
				System.out.println();
			}
		}

		assertTrue(confusionMatrix[0][0] == 34.0);
		assertTrue(confusionMatrix[0][1] == 15.0);
		assertTrue(confusionMatrix[1][0] == 32.0);
		assertTrue(confusionMatrix[1][1] == 19.0);

		confusionMatrix = WekaUtils.makeConfusionMatrix(wekaThreeIndividual,
				wekaDataThree);
		
		if (TestProperties.testPrints) {
			for (double[] row : confusionMatrix) {
				for (double element : row) {
					System.out.print(element + " ");
				}
				System.out.println();
			}
		}
		
		assertTrue(confusionMatrix[0][0] == 16.0);
		assertTrue(confusionMatrix[0][1] == 3.0);
		assertTrue(confusionMatrix[0][2] == 0.0);
		assertTrue(confusionMatrix[1][0] == 3.0);
		assertTrue(confusionMatrix[1][1] == 58.0);
		assertTrue(confusionMatrix[1][2] == 1.0);
		assertTrue(confusionMatrix[2][0] == 2.0);
		assertTrue(confusionMatrix[2][1] == 1.0);
		assertTrue(confusionMatrix[2][2] == 16.0);
	}

	@Test
	public void testFiltering() {
		MultiWayDepthNode root = MultiWayDepthNode.makeNode(2, 1,
				Sign.LESS, 1d);
		MultiWayDepthNode[] childs = new MultiWayDepthNode[2];
		childs[0] = MultiWayDepthNode.makeLeaf(1);
		childs[1] = MultiWayDepthNode.makeLeaf(0);
		root.setChilds(childs);		
		
		double[] filtered = WekaUtils.getFilteredInstancesClasses(wekaData, MultiWayNode.makeLeaf(2));
		System.out.println(filtered[0] + " " + filtered[1]);
		assertTrue(filtered.length == 2);
		filtered = WekaUtils.getFilteredInstancesClasses(wekaData, root.getChildAt(0));
		System.out.println(filtered[0] + " " + filtered[1]);
		assertTrue(filtered.length == 2);
	}
	
}
