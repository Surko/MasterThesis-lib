package tests.evolution;

import static org.junit.Assert.assertTrue;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.TestFit;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.SingleFitnessComparator;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;
import genlib.evolution.population.Population;
import genlib.evolution.selectors.RouletteWheelSelector;
import genlib.evolution.selectors.TournamentSelector;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.utils.Utils.Sign;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

public class TestSelectors {

	private static Population<TreeIndividual> individuals;

	static {
		FitnessFunction.registeredFunctions = 2;
		individuals = new Population<>();
		FitnessComparator<TreeIndividual> fitComp = new SingleFitnessComparator<TreeIndividual>();
		ArrayList<FitnessFunction<TreeIndividual>> fitFuncs = new ArrayList<>();
		@SuppressWarnings("unchecked")
		FitnessFunction<TreeIndividual> function = new TestFit();
		fitFuncs.add(function);
		fitComp.setFitFuncs(fitFuncs);
		fitComp.setParam("0");
		individuals.setFitnessComparator(fitComp);
		MultiWayDepthNode root = MultiWayDepthNode.makeNode(2, 1, Sign.LESS,
				20d);
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
	public void testTournamentSelectorSelection() {
		TournamentSelector selector = new TournamentSelector();
		selector.setRandomGenerator(new Random(0));
		individuals.getIndividual(0).setComplexFitness(50);
		individuals.getIndividual(0).setFitnessValue(0, 1.234);
		IPopulation<TreeIndividual> selectedPopulation = selector.select(
				individuals, 20);
		assertTrue(selectedPopulation.getActualPopSize() == 20);

		boolean atLeastOne = false;
		int total = 0;
		for (int i = 0; i < selectedPopulation.getActualPopSize(); i++) {
			total = selectedPopulation.getIndividual(i).getFitnessValue(0) == 1.234 ? total + 1
					: total;
			atLeastOne = atLeastOne
					|| selectedPopulation.getIndividual(i).getFitnessValue(0) == 1.234;
		}
		System.out.println("Choosed by Tournament selector: " + total);
		assertTrue(atLeastOne);

		individuals.getIndividual(0).setComplexFitness(0);
		individuals.getIndividual(0).setFitnessValue(0, 0);
	}

	@Test
	public void testRouletteSelectorSelection() {
		RouletteWheelSelector selector = new RouletteWheelSelector();
		selector.setRandomGenerator(new Random(0));
		individuals.getIndividual(0).setComplexFitness(50);
		individuals.getIndividual(0).setFitnessValue(0, 1.234);
		IPopulation<TreeIndividual> selectedPopulation = selector.select(
				individuals, 20);
		assertTrue(selectedPopulation.getActualPopSize() == 20);

		boolean atLeastOne = false;
		int total = 0;
		for (int i = 0; i < selectedPopulation.getActualPopSize(); i++) {
			total = selectedPopulation.getIndividual(i).getFitnessValue(0) == 1.234 ? total + 1
					: total;
			atLeastOne = atLeastOne
					|| selectedPopulation.getIndividual(i).getFitnessValue(0) == 1.234;
		}
		System.out.println("Choosed by RoulletteWheelSelector: " + total);
		assertTrue(atLeastOne);

		individuals.getIndividual(0).setComplexFitness(0);
		individuals.getIndividual(0).setFitnessValue(0, 0);
	}
}
