package tests.evolution;

import static org.junit.Assert.assertTrue;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.SingleFitnessComparator;
import genlib.evolution.fitness.tree.TreeAccuracyFitness;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.Population;
import genlib.structures.Data;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.utils.Utils;
import genlib.utils.Utils.Sign;

import java.util.ArrayList;

import org.junit.Test;

import weka.datagenerators.classifiers.classification.RDG1;

public class TestPopulation {

	private static Population<TreeIndividual> individuals;
	private static TreeIndividual testIndividual;
	public static Data wekaData;
	public static Data arrayData;

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
		} catch (Exception e) {
		}

		FitnessFunction.registeredFunctions = 2;
		MultiWayDepthNode root = MultiWayDepthNode.makeNode(2, 1, Sign.LESS,
				20d);
		MultiWayDepthNode[] childs = new MultiWayDepthNode[2];
		childs[0] = MultiWayDepthNode.makeLeaf(1);
		childs[1] = MultiWayDepthNode.makeLeaf(0);
		root.setChilds(childs);
		testIndividual = new TreeIndividual(root);
		individuals = Utils.debugPopulationFrom(testIndividual);
	}

	@Test
	public void testPopulationScaling() {
		assertTrue(individuals.getActualPopSize() == 20);
		individuals.add(new TreeIndividual(testIndividual));
		assertTrue(individuals.getActualPopSize() == 21);
		individuals.clear();
		assertTrue(individuals.getActualPopSize() == 0);
	}

	@Test(timeout = 100)
	public void testPopulationFitComputation() {
		FitnessFunction<TreeIndividual> function = new TreeAccuracyFitness();
		function.setIndex(0);
		function.setData(wekaData);
		ArrayList<FitnessFunction<TreeIndividual>> fitFuncs = new ArrayList<>();
		fitFuncs.add(function);
		FitnessComparator<TreeIndividual> comparator = new SingleFitnessComparator<>();
		comparator.setFitFuncs(fitFuncs);
		comparator.setParam("0");

		individuals.setFitnessComparator(comparator);		
		individuals.computeFitness(1, 1);		
		individuals.computeFitness(1, 1);
		for (TreeIndividual individual : individuals.getIndividuals()) {
			individual.change();
		}
		individuals.computeFitness(1, 1);	
		for (TreeIndividual individual : individuals.getIndividuals()) {
			individual.change();
		}
		individuals.computeFitness(5, 10);		
	}

}
