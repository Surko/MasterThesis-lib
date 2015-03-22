package tests.evolution;

import static org.junit.Assert.*;

import java.util.ArrayList;

import genlib.evolution.Population;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.TreeAccuracyFitness;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.SingleFitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.ArrayInstances;
import genlib.structures.MultiWayDepthNode;
import genlib.utils.Utils.Sign;

import org.junit.Test;

import weka.core.Instances;
import weka.datagenerators.classifiers.classification.RDG1;

public class TestPopulation {

	private static Population<TreeIndividual> individuals;
	private static TreeIndividual testIndividual;
	public static Instances wekaData;
	public static ArrayInstances arrayData;

	static {
		try {
			String[] options = new String[]{"-r",
					"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
					"-S","1","-n","100","-a","10","-c","2",
					"-N","0","-I","0","-M","1","-R","10"};		
			RDG1 rdg = new RDG1();
			rdg.setOptions(options);	
			rdg.defineDataFormat();
			wekaData = rdg.generateExamples();
		} catch (Exception e) {}

		Individual.registeredFunctions = 2;
		individuals = new Population<>();	
		MultiWayDepthNode root = MultiWayDepthNode.makeNode(2, 1, Sign.LESS, 20d);
		MultiWayDepthNode[] childs = new MultiWayDepthNode[2];
		childs[0] = MultiWayDepthNode.makeLeaf(1);
		childs[1] = MultiWayDepthNode.makeLeaf(0);
		root.setChilds(childs);
		testIndividual = new TreeIndividual(root);
		individuals.add(testIndividual);
		for (int i = 0; i < 19; i++)
			individuals.add(new TreeIndividual(testIndividual));		
	}

	@Test
	public void testPopulationScaling() {
		assertTrue(individuals.getPopulationSize() == 20);		
		individuals.add(new TreeIndividual(testIndividual));
		assertTrue(individuals.getPopulationSize() == 21);		
		individuals.clear();
		assertTrue(individuals.getPopulationSize() == 0);
	}

	@Test(timeout = 30)
	public void testPopulationFitComputation() {
		FitnessFunction<TreeIndividual> function = new TreeAccuracyFitness();
		function.setData(wekaData);
		ArrayList<FitnessFunction<TreeIndividual>> fitFuncs = new ArrayList<>();
		fitFuncs.add(function);
		FitnessComparator<TreeIndividual> comparator = new SingleFitnessComparator<>();
		comparator.setFitFuncs(fitFuncs);
		comparator.setParam("0");

		individuals.setFitnessComparator(comparator);
		long start = System.nanoTime();
		individuals.computeFitness(1,1);
		System.out.println(System.nanoTime() - start);				
				
		start = System.nanoTime();
		individuals.computeFitness(1,1);
		System.out.println(System.nanoTime() - start);
		
		for (TreeIndividual individual : individuals.getIndividuals()) {
			individual.change();
		}
		
		start = System.nanoTime();
		individuals.computeFitness(1,1);
		System.out.println(System.nanoTime() - start);
		
		for (TreeIndividual individual : individuals.getIndividuals()) {
			individual.change();
		}
		
		start = System.nanoTime();
		individuals.computeFitness(5,10);
		System.out.println(System.nanoTime() - start);


	}

}
