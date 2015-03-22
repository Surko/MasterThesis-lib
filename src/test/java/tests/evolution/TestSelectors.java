package tests.evolution;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import genlib.evolution.Population;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.TestFit;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.SingleFitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.selectors.RouletteWheelSelector;
import genlib.structures.MultiWayDepthNode;
import genlib.utils.Utils;
import genlib.utils.Utils.Sign;

import org.junit.Test;

public class TestSelectors {
	
	private static Population<TreeIndividual> individuals;
	
	static {		
		Individual.registeredFunctions = 2;
		individuals = new Population<>();	
		FitnessComparator<TreeIndividual> fitComp = new SingleFitnessComparator<TreeIndividual>();
		ArrayList<FitnessFunction<TreeIndividual>> fitFuncs = new ArrayList<>();		
		FitnessFunction<TreeIndividual> function = new TestFit();		
		fitFuncs.add(function);
		fitComp.setFitFuncs(fitFuncs);
		individuals.setFitnessComparator(fitComp);
		MultiWayDepthNode root = MultiWayDepthNode.makeNode(2, 1, Sign.LESS, 20d);
		MultiWayDepthNode[] childs = new MultiWayDepthNode[2];
		childs[0] = MultiWayDepthNode.makeLeaf(1);
		childs[1] = MultiWayDepthNode.makeLeaf(0);
		root.setChilds(childs);
		TreeIndividual ind1 = new TreeIndividual(root);
		individuals.add(ind1);
		for (int i = 0; i < 19; i++)
			individuals.add(new TreeIndividual(ind1));		
	}
	
	@Test
	public void testRouletteMethods() {
		RouletteWheelSelector selector = new RouletteWheelSelector();
		selector.setRandomGenerator(new Random(0));
		Population<TreeIndividual> selectedPopulation = selector.select(individuals, 5);
		assertTrue(selectedPopulation.getPopulationSize() == 5);
		TreeIndividual compInd = individuals.getIndividual(0);
		for (int i = 0; i < 5; i++) {
			TreeIndividual ind = selectedPopulation.getIndividual(i);
			
			for (int j = 0; j < individuals.getPopulationSize(); j++) {
				assertFalse(ind == individuals.getIndividual(j));
				assertFalse(ind.getRootNode() == individuals.getIndividual(j).getRootNode());
			}
		}
		
		for (int j = 0; j < individuals.getPopulationSize(); j++) {
			individuals.getIndividual(j).setComplexFitness(20);
		}
		
		for (int i = 0; i < 5; i++) {
			TreeIndividual ind = selectedPopulation.getIndividual(i);
			assertTrue(ind.getComplexFitness() == Double.MIN_VALUE);
		}
	}
	
	@Test
	public void testTournament() {
		
	}
}
