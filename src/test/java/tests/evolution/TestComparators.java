package tests.evolution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.ParetoFitnessComparator;
import genlib.evolution.fitness.comparators.PriorityFitnessComparator;
import genlib.evolution.fitness.comparators.SingleFitnessComparator;
import genlib.evolution.fitness.comparators.WeightedFitnessComparator;
import genlib.evolution.fitness.tree.TreeAccuracyFitness;
import genlib.evolution.fitness.tree.look.TreeSizeFitness;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.Data;
import genlib.structures.trees.MultiWayHeightNode;
import genlib.utils.Utils.Sign;
import genlib.utils.WekaUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;

import weka.datagenerators.classifiers.classification.RDG1;

public class TestComparators {

	public static Data wekaData;
	public static Data arrayData;
	public static TreeIndividual testIndividual, wekaIndividual,
			transIndividual;

	static {

		// without this we would not have big enough array for individuals
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

		} catch (Exception e) {
		}

		MultiWayHeightNode root = MultiWayHeightNode.makeNode(2, 1, Sign.LESS,
				20d);
		MultiWayHeightNode[] childs = new MultiWayHeightNode[2];
		childs[0] = MultiWayHeightNode.makeLeaf(1);
		childs[1] = MultiWayHeightNode.makeLeaf(0);
		root.setChilds(childs);
		testIndividual = new TreeIndividual(root);

		root = MultiWayHeightNode.makeLeaf(1d);
		transIndividual = new TreeIndividual(root);
	}

	@Test
	public void testSingleComparator() {
		transIndividual.change();
		testIndividual.change();
		wekaIndividual.change();

		FitnessComparator<TreeIndividual> comp = new SingleFitnessComparator<>();

		FitnessFunction<TreeIndividual> accFunction = new TreeAccuracyFitness();
		accFunction.setIndex(0);
		accFunction.setData(wekaData);
		FitnessFunction<TreeIndividual> depthFunction = new TreeSizeFitness();
		depthFunction.setIndex(1);

		ArrayList<FitnessFunction<TreeIndividual>> fitFuncs = new ArrayList<>();
		fitFuncs.add(accFunction);
		fitFuncs.add(depthFunction);
		FitnessFunction.registeredFunctions = fitFuncs.size();

		comp.setFitFuncs(fitFuncs);
		// test of first function
		comp.setParam("0");

		// compare contract
		// reflexivity
		assertTrue(comp.compare(wekaIndividual, wekaIndividual) == 0);
		assertTrue(comp.compare(testIndividual, testIndividual) == 0);
		assertTrue(comp.compare(transIndividual, transIndividual) == 0);
		// consistency
		assertTrue(comp.compare(testIndividual, wekaIndividual) > 0);
		assertTrue(comp.compare(wekaIndividual, testIndividual) < 0);
		// transitivity
		assertTrue(comp.compare(transIndividual, testIndividual) > 0);
		assertTrue(comp.compare(testIndividual, wekaIndividual) > 0);
		assertTrue(comp.compare(transIndividual, wekaIndividual) > 0);

		assertFalse(transIndividual.hasChanged());
		assertFalse(testIndividual.hasChanged());
		assertFalse(wekaIndividual.hasChanged());

		// Test of sorting
		ArrayList<TreeIndividual> individuals = new ArrayList<TreeIndividual>();
		individuals.add(testIndividual);
		individuals.add(wekaIndividual);
		individuals.add(transIndividual);

		Collections.sort(individuals, comp);
		assertTrue(individuals.get(0) == wekaIndividual);
		assertTrue(individuals.get(1) == testIndividual);
		assertTrue(individuals.get(2) == transIndividual);

		// test of first function
		comp.setParam("1");
		// force change is not recommended but for test purpose it's good enough
		transIndividual.change();
		testIndividual.change();
		wekaIndividual.change();

		// compare contract
		// reflexivity
		assertTrue(comp.compare(wekaIndividual, wekaIndividual) == 0);
		assertTrue(comp.compare(testIndividual, testIndividual) == 0);
		assertTrue(comp.compare(transIndividual, transIndividual) == 0);
		// consistency
		assertTrue(comp.compare(testIndividual, wekaIndividual) < 0);
		assertTrue(comp.compare(wekaIndividual, testIndividual) > 0);
		// transitivity
		assertTrue(comp.compare(transIndividual, testIndividual) < 0);
		assertTrue(comp.compare(testIndividual, wekaIndividual) < 0);
		assertTrue(comp.compare(transIndividual, wekaIndividual) < 0);

		assertFalse(transIndividual.hasChanged());
		assertFalse(testIndividual.hasChanged());
		assertFalse(wekaIndividual.hasChanged());

		individuals = new ArrayList<TreeIndividual>();
		individuals.add(testIndividual);
		individuals.add(wekaIndividual);
		individuals.add(transIndividual);

		Collections.sort(individuals, comp);
		assertTrue(individuals.get(0) == transIndividual);
		assertTrue(individuals.get(1) == testIndividual);
		assertTrue(individuals.get(2) == wekaIndividual);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testParetoComparator() {
		FitnessComparator<TreeIndividual> comp = new ParetoFitnessComparator<>();

		FitnessFunction<TreeIndividual> accFunction = new TreeAccuracyFitness();
		accFunction.setIndex(0);
		accFunction.setData(wekaData);
		FitnessFunction<TreeIndividual> depthFunction = new TreeSizeFitness();
		depthFunction.setIndex(1);

		ArrayList<FitnessFunction<TreeIndividual>> fitFuncs = new ArrayList<>();
		fitFuncs.add(accFunction);
		fitFuncs.add(depthFunction);
		FitnessFunction.registeredFunctions = fitFuncs.size();

		comp.setFitFuncs(fitFuncs);
		
		comp.compare(null, null);
	}

	@Test
	public void testWeightedComparator() {
		transIndividual.change();
		testIndividual.change();
		wekaIndividual.change();

		FitnessComparator<TreeIndividual> comp = new WeightedFitnessComparator<TreeIndividual>(
				2);

		transIndividual.change();
		testIndividual.change();
		wekaIndividual.change();

		FitnessFunction<TreeIndividual> accFunction = new TreeAccuracyFitness();
		accFunction.setIndex(0);
		accFunction.setData(wekaData);
		FitnessFunction<TreeIndividual> sizeFunction = new TreeSizeFitness();
		sizeFunction.setIndex(1);

		ArrayList<FitnessFunction<TreeIndividual>> fitFuncs = new ArrayList<>();
		fitFuncs.add(accFunction);
		fitFuncs.add(sizeFunction);
		FitnessFunction.registeredFunctions = fitFuncs.size();

		comp.setFitFuncs(fitFuncs);
		comp.setParam("1.0,0.5");

		// compare contract
		// reflexivity
		assertTrue(comp.compare(transIndividual, transIndividual) == 0);
		assertTrue(comp.compare(testIndividual, testIndividual) == 0);
		assertTrue(comp.compare(wekaIndividual, wekaIndividual) == 0);
		// consistency
		assertTrue(comp.compare(wekaIndividual, transIndividual) < 0);
		assertTrue(comp.compare(transIndividual, wekaIndividual) > 0);
		// transitivity, correct positions (descending)
		// wekaIndividual 0.9538095238095239
		// transIndividual 0.8400000000000001
		// testIndividual 0.6966666666666667
		assertTrue(comp.compare(wekaIndividual, transIndividual) < 0);
		assertTrue(comp.compare(transIndividual, testIndividual) < 0);
		assertTrue(comp.compare(wekaIndividual, testIndividual) < 0);

		assertFalse(transIndividual.hasChanged());
		assertFalse(testIndividual.hasChanged());
		assertFalse(wekaIndividual.hasChanged());

		System.out.println(wekaIndividual.getComplexFitness());
		System.out.println(testIndividual.getComplexFitness());
		System.out.println(transIndividual.getComplexFitness());

		assertTrue(Double.compare(1 * testIndividual.getFitnessValue(0) + 0.5
				* testIndividual.getFitnessValue(1),
				testIndividual.getComplexFitness()) == 0);
		assertTrue(Double.compare(1 * wekaIndividual.getFitnessValue(0) + 0.5
				* wekaIndividual.getFitnessValue(1),
				wekaIndividual.getComplexFitness()) == 0);
	}

	@Test
	public void testPriorityComparator() {
		FitnessComparator<TreeIndividual> comp = new PriorityFitnessComparator<TreeIndividual>();

		FitnessFunction<TreeIndividual> accFunction = new TreeAccuracyFitness();
		accFunction.setIndex(0);
		accFunction.setData(wekaData);
		FitnessFunction<TreeIndividual> depthFunction = new TreeSizeFitness();
		depthFunction.setIndex(1);

		ArrayList<FitnessFunction<TreeIndividual>> fitFuncs = new ArrayList<>();
		fitFuncs.add(accFunction);
		fitFuncs.add(depthFunction);
		FitnessFunction.registeredFunctions = fitFuncs.size();

		comp.setFitFuncs(fitFuncs);
	}

}
