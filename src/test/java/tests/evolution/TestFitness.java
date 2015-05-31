package tests.evolution;

import static org.junit.Assert.assertTrue;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.tree.TreeAccuracyFitness;
import genlib.evolution.fitness.tree.confusion.TreeFNFitness;
import genlib.evolution.fitness.tree.confusion.TreeFPFitness;
import genlib.evolution.fitness.tree.confusion.TreePrecisionFitness;
import genlib.evolution.fitness.tree.confusion.TreePrevalenceFitness;
import genlib.evolution.fitness.tree.confusion.TreeRecallFitness;
import genlib.evolution.fitness.tree.confusion.TreeSpecificityFitness;
import genlib.evolution.fitness.tree.confusion.TreeTNFitness;
import genlib.evolution.fitness.tree.confusion.TreeTPFitness;
import genlib.evolution.fitness.tree.look.TreeSizeFitness;
import genlib.evolution.fitness.tree.look.TreeHeightFitness;
import genlib.evolution.individuals.TreeIndividual;
import genlib.exceptions.MissingParamException;
import genlib.structures.Data;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.utils.Utils.Sign;
import genlib.utils.WekaUtils;

import java.util.HashMap;

import org.junit.Test;

import tests.TestProperties;
import weka.datagenerators.classifiers.classification.RDG1;

public class TestFitness {

	public static Data wekaData, wekaDataThree;
	public static Data arrayData;
	public static TreeIndividual testIndividual, wekaIndividual,
			wekaThreeIndividual;

	static {

		// without this individual don't have big enough array for fitness
		FitnessFunction.registeredFunctions = 2;

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
					false);

			// ternary class problem
			options = new String[] {
					"-r",
					"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
					"-S", "1", "-n", "100", "-a", "10", "-c", "3", "-N", "0",
					"-I", "0", "-M", "1", "-R", "10" };
			rdg = new RDG1();
			rdg.setOptions(options);
			rdg.defineDataFormat();
			wekaDataThree = new Data(rdg.generateExamples());

			attrIndexMap = wekaDataThree.getAttrIndexMap();
			attrValueIndexMap = wekaDataThree.getAttrValueIndexMap();

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
					wekaDataThree.numInstances(), attrIndexMap,
					attrValueIndexMap, true);

		} catch (Exception e) {
		}

		MultiWayDepthNode root = MultiWayDepthNode.makeNode(2, 1, Sign.LESS,
				20d);
		MultiWayDepthNode[] childs = new MultiWayDepthNode[2];
		childs[0] = MultiWayDepthNode.makeLeaf(1);
		childs[1] = MultiWayDepthNode.makeLeaf(0);
		root.setChilds(childs);
		testIndividual = new TreeIndividual(root);
	}

	@Test
	public void testFitnessObjectInfos() {
		FitnessFunction<TreeIndividual> function;

		function = new TreeAccuracyFitness();
		assertTrue(function.objectInfo().equals("tAcc x"));
		function.setParam("INDEX,0");
		assertTrue(function.objectInfo().equals("tAcc INDEX,0"));
		function.setParam("INDEX,0,AVERAGE,WEIGHTED");
		assertTrue(function.objectInfo().equals("tAcc INDEX,0"));

		function = new TreeSizeFitness();
		assertTrue(function.objectInfo().equals("tSize x"));

		function = new TreeHeightFitness();
		assertTrue(function.objectInfo().equals("tHeight x"));

		String name = TreePrevalenceFitness.initName;
		function = new TreePrevalenceFitness();
		assertTrue(function.objectInfo().equals(name + " x"));
		function.setParam("INDEX,0");
		assertTrue(function.objectInfo().equals(name + " INDEX,0"));
		function.setParam("INDEX,0,AVERAGE,WEIGHTED");
		assertTrue(function.objectInfo().equals(
				name + " INDEX,0,AVERAGE,WEIGHTED"));
		function.setParam("MAXIMIZE,true");
		assertTrue(function.objectInfo().equals(name + " MAXIMIZE,true"));

		name = TreeRecallFitness.initName;
		function = new TreeRecallFitness();
		assertTrue(function.objectInfo().equals(name + " x"));
		function.setParam("INDEX,0");
		assertTrue(function.objectInfo().equals(name + " INDEX,0"));
		function.setParam("INDEX,0,AVERAGE,WEIGHTED");
		assertTrue(function.objectInfo().equals(
				name + " INDEX,0,AVERAGE,WEIGHTED"));
		function.setParam("MAXIMIZE,true");
		assertTrue(function.objectInfo().equals(name + " MAXIMIZE,true"));

		name = TreePrecisionFitness.initName;
		function = new TreePrecisionFitness();
		assertTrue(function.objectInfo().equals(name + " x"));
		function.setParam("INDEX,0");
		assertTrue(function.objectInfo().equals(name + " INDEX,0"));
		function.setParam("INDEX,0,AVERAGE,WEIGHTED");
		assertTrue(function.objectInfo().equals(
				name + " INDEX,0,AVERAGE,WEIGHTED"));
		function.setParam("MAXIMIZE,true");
		assertTrue(function.objectInfo().equals(name + " MAXIMIZE,true"));

		name = TreeTPFitness.initName;
		function = new TreeTPFitness();
		assertTrue(function.objectInfo().equals(name + " x"));
		function.setParam("INDEX,0");
		assertTrue(function.objectInfo().equals(name + " INDEX,0"));
		function.setParam("INDEX,0,AVERAGE,WEIGHTED");
		assertTrue(function.objectInfo().equals(
				name + " INDEX,0,AVERAGE,WEIGHTED"));
		function.setParam("MAXIMIZE,true");
		assertTrue(function.objectInfo().equals(name + " MAXIMIZE,true"));

		name = TreeTNFitness.initName;
		function = new TreeTNFitness();
		assertTrue(function.objectInfo().equals(name + " x"));
		function.setParam("INDEX,0");
		assertTrue(function.objectInfo().equals(name + " INDEX,0"));
		function.setParam("INDEX,0,AVERAGE,WEIGHTED");
		assertTrue(function.objectInfo().equals(
				name + " INDEX,0,AVERAGE,WEIGHTED"));
		function.setParam("MAXIMIZE,true");
		assertTrue(function.objectInfo().equals(name + " MAXIMIZE,true"));

		name = TreeFPFitness.initName;
		function = new TreeFPFitness();
		assertTrue(function.objectInfo().equals(name + " x"));
		function.setParam("INDEX,0");
		assertTrue(function.objectInfo().equals(name + " INDEX,0"));
		function.setParam("INDEX,0,AVERAGE,WEIGHTED");
		assertTrue(function.objectInfo().equals(
				name + " INDEX,0,AVERAGE,WEIGHTED"));
		function.setParam("MAXIMIZE,true");
		assertTrue(function.objectInfo().equals(name + " MAXIMIZE,true"));

		name = TreeFNFitness.initName;
		function = new TreeFNFitness();
		assertTrue(function.objectInfo().equals(name + " x"));
		function.setParam("INDEX,0");
		assertTrue(function.objectInfo().equals(name + " INDEX,0"));
		function.setParam("INDEX,0,AVERAGE,WEIGHTED");
		assertTrue(function.objectInfo().equals(
				name + " INDEX,0,AVERAGE,WEIGHTED"));
		function.setParam("MAXIMIZE,true");
		assertTrue(function.objectInfo().equals(name + " MAXIMIZE,true"));
	}

	@Test
	public void testConfusionFitnessNotSet() {
		FitnessFunction<TreeIndividual> function = null;
		try {
			function = new TreeTPFitness();
			function.setData(wekaDataThree);
			function.computeFitness(wekaThreeIndividual);
			assertTrue(false);
		} catch (MissingParamException e) {
			assertTrue(true);
		}
		try {
			function = new TreeTNFitness();
			function.setData(wekaDataThree);
			function.computeFitness(wekaThreeIndividual);
			assertTrue(false);
		} catch (MissingParamException e) {
			assertTrue(true);
		}
		try {
			function = new TreeFNFitness();
			function.setData(wekaDataThree);
			function.computeFitness(wekaThreeIndividual);
			assertTrue(false);
		} catch (MissingParamException e) {
			assertTrue(true);
		}
		try {
			function = new TreeFPFitness();
			function.setData(wekaDataThree);
			function.computeFitness(wekaThreeIndividual);
			assertTrue(false);
		} catch (MissingParamException e) {
			assertTrue(true);
		}
		try {
			function = new TreePrevalenceFitness();
			function.setData(wekaDataThree);
			function.computeFitness(wekaThreeIndividual);
			assertTrue(false);
		} catch (MissingParamException e) {
			assertTrue(true);
		}
		try {
			function = new TreePrecisionFitness();
			function.setData(wekaDataThree);
			function.computeFitness(wekaThreeIndividual);
			assertTrue(false);
		} catch (MissingParamException e) {
			assertTrue(true);
		}
		try {
			function = new TreeRecallFitness();
			function.setData(wekaDataThree);
			function.computeFitness(wekaThreeIndividual);
			assertTrue(false);
		} catch (MissingParamException e) {
			assertTrue(true);
		}
		try {
			function = new TreeSpecificityFitness();
			function.setData(wekaDataThree);
			function.computeFitness(wekaThreeIndividual);
			assertTrue(false);
		} catch (MissingParamException e) {
			assertTrue(true);
		}
		try {
			function = new TreeSpecificityFitness();
			function.setData(wekaDataThree);
			function.computeFitness(wekaThreeIndividual);
			assertTrue(false);
		} catch (MissingParamException e) {
			assertTrue(true);
		}

	}

	@Test
	public void testAccuracyFitness() {
		FitnessFunction<TreeIndividual> function = new TreeAccuracyFitness();
		function.setIndex(0);
		function.setData(wekaData);
		int fIndex = function.getIndex();

		assertTrue(fIndex == 0);
		assertTrue(testIndividual.hasChanged());
		assertTrue(wekaIndividual.hasChanged());

		double f1 = function.computeFitness(testIndividual);
		double f2 = function.computeFitness(wekaIndividual);

		assertTrue(f1 != 0);
		assertTrue(f2 != 0);
		assertTrue(testIndividual.getFitnessValue(0) != 0);
		assertTrue(wekaIndividual.getFitnessValue(0) != 0);
		assertTrue(testIndividual.getFitnessValue(0) == f1);
		assertTrue(wekaIndividual.getFitnessValue(0) == f2);

		assertTrue(testIndividual.hasChanged());
		assertTrue(wekaIndividual.hasChanged());
		assertTrue(function.objectInfo().equals("tAcc x"));
	}

	@Test
	public void testSizeFitness() {
		FitnessFunction<TreeIndividual> function = new TreeSizeFitness();
		function.setIndex(1);
		function.setData(wekaData);
		int fIndex = function.getIndex();

		assertTrue(fIndex == 1);
		assertTrue(testIndividual.hasChanged());
		assertTrue(wekaIndividual.hasChanged());

		double f1 = function.computeFitness(testIndividual);
		double f2 = function.computeFitness(wekaIndividual);

		assertTrue(f1 != 0);
		assertTrue(f2 != 0);
		assertTrue(Double.compare(f1, 1 / 3d) == 0);
		assertTrue(Double.compare(f2, 1 / 21d) == 0);
		assertTrue(testIndividual.getFitnessValue(fIndex) != 0);
		assertTrue(wekaIndividual.getFitnessValue(fIndex) != 0);
		assertTrue(testIndividual.getFitnessValue(fIndex) == f1);
		assertTrue(wekaIndividual.getFitnessValue(fIndex) == f2);

		assertTrue(testIndividual.hasChanged());
		assertTrue(wekaIndividual.hasChanged());
	}

	@Test
	public void testHeightFitness() {
		FitnessFunction<TreeIndividual> function = new TreeHeightFitness();
		function.setIndex(1);
		function.setData(wekaData);
		int fIndex = function.getIndex();

		assertTrue(fIndex == 1);
		assertTrue(testIndividual.hasChanged());
		assertTrue(wekaIndividual.hasChanged());

		double f1 = function.computeFitness(testIndividual);
		double f2 = function.computeFitness(wekaIndividual);

		assertTrue(f1 != 0);
		assertTrue(f2 != 0);
		assertTrue(Double.compare(f1, 1d) == 0);
		assertTrue(Double.compare(f2, 1 / 6d) == 0);
		assertTrue(testIndividual.getFitnessValue(fIndex) != 0);
		assertTrue(wekaIndividual.getFitnessValue(fIndex) != 0);
		assertTrue(testIndividual.getFitnessValue(fIndex) == f1);
		assertTrue(wekaIndividual.getFitnessValue(fIndex) == f2);

		assertTrue(testIndividual.hasChanged());
		assertTrue(wekaIndividual.hasChanged());
	}

	@Test
	public void testPrevalenceFitness() {
		FitnessFunction<TreeIndividual> function = new TreePrevalenceFitness();
		function.setIndex(1);

	}

	@Test
	public void treePrecisionFitness() {
		FitnessFunction<TreeIndividual> function = new TreeRecallFitness();
		function.setIndex(1);
		function.setData(wekaData);

		int fIndex = function.getIndex();
		assertTrue(fIndex == 1);
		assertTrue(testIndividual.hasChanged());
		assertTrue(wekaIndividual.hasChanged());

		double f1 = function.computeFitness(testIndividual);
		double f2 = function.computeFitness(wekaIndividual);
		double f3 = 0d;

		if (TestProperties.testPrints) {
			System.out.println("testIndividual, recall fitness, binary: " + f1);
			System.out.println("wekaIndividual, recall fitness, binary: " + f2);
		}

		assertTrue(f1 == (19d / 34));
		assertTrue(testIndividual.getFitnessValue(1) == f1);
		assertTrue(f2 == (29d / 34));
		assertTrue(wekaIndividual.getFitnessValue(1) == f2);

		// Different index
		function.setParam("INDEX,0");

		f1 = function.computeFitness(testIndividual);
		f2 = function.computeFitness(wekaIndividual);

		if (TestProperties.testPrints) {
			System.out.println("testIndividual, recall fitness, attr 0: " + f1);
			System.out.println("wekaIndividual, recall fitness, attr 0: " + f2);
		}

		assertTrue(f1 == (34d / 66));
		assertTrue(testIndividual.getFitnessValue(1) == f1);
		assertTrue(f2 == (64d / 66));
		assertTrue(wekaIndividual.getFitnessValue(1) == f2);

		function = new TreeRecallFitness();
		function.setIndex(1);
		function.setData(wekaDataThree);
		function.setParam("AVERAGE,WEIGHTED");

		f1 = function.computeFitness(wekaThreeIndividual);

		if (TestProperties.testPrints) {
			System.out.println("wekaThreeIndividual, recall fitness, weighted: " + f1);
		}
		assertTrue(f1 == 0.9d);

		function.setParam("INDEX,0");
		f1 = function.computeFitness(wekaThreeIndividual);
		function.setParam("INDEX,1");
		f2 = function.computeFitness(wekaThreeIndividual);
		function.setParam("INDEX,2");
		f3 = function.computeFitness(wekaThreeIndividual);
		

		if (TestProperties.testPrints) {
			System.out.println("wekaThreeIndividual, recall fitness, attr 0: " + f1);
			System.out.println("wekaThreeIndividual, recall fitness, attr 1: " + f2);
			System.out.println("wekaThreeIndividual, recall fitness, attr 2: " + f3);
		}
	}
	
	@Test
	public void testRecallFitness() {
		FitnessFunction<TreeIndividual> function = new TreeRecallFitness();
		function.setIndex(1);
		function.setData(wekaData);

		int fIndex = function.getIndex();
		assertTrue(fIndex == 1);
		assertTrue(testIndividual.hasChanged());
		assertTrue(wekaIndividual.hasChanged());

		double f1 = function.computeFitness(testIndividual);
		double f2 = function.computeFitness(wekaIndividual);
		double f3 = 0d;

		if (TestProperties.testPrints) {
			System.out.println("testIndividual, recall fitness, binary: " + f1);
			System.out.println("wekaIndividual, recall fitness, binary: " + f2);
		}

		assertTrue(f1 == (19d / 34));
		assertTrue(testIndividual.getFitnessValue(1) == f1);
		assertTrue(f2 == (29d / 34));
		assertTrue(wekaIndividual.getFitnessValue(1) == f2);

		// Different index
		function.setParam("INDEX,0");

		f1 = function.computeFitness(testIndividual);
		f2 = function.computeFitness(wekaIndividual);

		if (TestProperties.testPrints) {
			System.out.println("testIndividual, recall fitness, attr 0: " + f1);
			System.out.println("wekaIndividual, recall fitness, attr 0: " + f2);
		}

		assertTrue(f1 == (34d / 66));
		assertTrue(testIndividual.getFitnessValue(1) == f1);
		assertTrue(f2 == (64d / 66));
		assertTrue(wekaIndividual.getFitnessValue(1) == f2);

		function = new TreeRecallFitness();
		function.setIndex(1);
		function.setData(wekaDataThree);
		function.setParam("AVERAGE,WEIGHTED");

		f1 = function.computeFitness(wekaThreeIndividual);

		if (TestProperties.testPrints) {
			System.out.println("wekaThreeIndividual, recall fitness, weighted: " + f1);
		}
		assertTrue(f1 == 0.9d);

		function.setParam("INDEX,0");
		f1 = function.computeFitness(wekaThreeIndividual);
		function.setParam("INDEX,1");
		f2 = function.computeFitness(wekaThreeIndividual);
		function.setParam("INDEX,2");
		f3 = function.computeFitness(wekaThreeIndividual);
		
		if (TestProperties.testPrints) {
			System.out.println("wekaThreeIndividual, recall fitness, attr 0: " + f1);
			System.out.println("wekaThreeIndividual, recall fitness, attr 1: " + f2);
			System.out.println("wekaThreeIndividual, recall fitness, attr 2: " + f3);
		}
	}
}
