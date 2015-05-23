package tests.wekastuff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.data.GenLibInstances;
import genlib.structures.extensions.HeightExtension;
import genlib.structures.trees.Node;
import genlib.utils.Utils;
import genlib.utils.Utils.Sign;
import genlib.utils.WekaUtils;

import java.util.HashMap;

import org.junit.Test;

import weka.core.Instances;
import weka.datagenerators.classifiers.classification.RDG1;

public class TestWekaUtils {

	public static Instances wekaData;
	public static GenLibInstances arrayData;

	@Test
	public void testAttrMaps() throws Exception {
		String[] options = new String[] {
				"-r",
				"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
				"-S", "1", "-n", "100", "-a", "10", "-c", "2", "-N", "0", "-I",
				"0", "-M", "1", "-R", "10" };
		RDG1 rdg = new RDG1();
		rdg.setOptions(options);
		rdg.defineDataFormat();
		wekaData = rdg.generateExamples();

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
		String[] options = new String[] {
				"-r",
				"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
				"-S", "1", "-n", "100", "-a", "10", "-c", "2", "-N", "0", "-I",
				"0", "-M", "1", "-R", "10" };
		RDG1 rdg = new RDG1();
		rdg.setOptions(options);
		rdg.defineDataFormat();
		wekaData = rdg.generateExamples();

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
		assertTrue(Utils.computeHeight(root) == 7);

		Node n = root.getChildAt(1).getChildAt(1).getChildAt(0).getChildAt(1);
		assertTrue(n.getAttribute() == 4);
		assertTrue(n.getChildCount() == 2);
		assertTrue(n.getChildAt(0).isLeaf());
		assertNull(n.getChildAt(0).getChilds());
		assertTrue(n.getChildAt(0).getValue() == 1);
		assertTrue(Utils.computeHeight(n) == 2);

		individual = WekaUtils.constructTreeIndividual(sTree, 21,
				wekaData.numInstances(), attrIndexMap, attrValueIndexMap, true);
		root = individual.getRootNode();
		assertTrue(((HeightExtension)root).getTreeHeight() == 7);
	}

}
