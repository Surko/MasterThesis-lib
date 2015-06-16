package tests.evolution;

import static org.junit.Assert.assertNotNull;
import genlib.evolution.EvolutionAlgorithm;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.SingleFitnessComparator;
import genlib.evolution.fitness.tree.look.TreeHeightFitness;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.operators.DefaultTreeCrossover;
import genlib.evolution.operators.DefaultTreeMutation;
import genlib.evolution.population.Population;
import genlib.evolution.selectors.TournamentSelector;
import genlib.structures.Data;
import genlib.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import weka.datagenerators.classifiers.classification.RDG1;

public class TestEvolution {

	private static Data wekaData;
	private static Population<TreeIndividual> individuals;

	static {		
		individuals = Utils.debugTreePopulation();
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
	}

	@Test
	public void testEvolutionDefaultFields() {
		EvolutionAlgorithm<TreeIndividual> ea = new EvolutionAlgorithm<>(
				wekaData, 1);
		assertNotNull(ea.getData());
	}

	@Test(expected = NullPointerException.class)
	public void testEvolution1() {
		EvolutionAlgorithm<TreeIndividual> ea = new EvolutionAlgorithm<>(
				wekaData, 1);
		ea.setInitialPopulation(individuals);
		ea.run();
	}

	@Test(expected = NullPointerException.class)
	public void testEvolution2() {
		EvolutionAlgorithm<TreeIndividual> ea = new EvolutionAlgorithm<>(
				wekaData, 1);
		ea.setInitialPopulation(individuals);
		ea.addSelector(new TournamentSelector());
		ea.run();
	}

	@Test(expected = NullPointerException.class)
	public void testEvolution3() {
		EvolutionAlgorithm<TreeIndividual> ea = new EvolutionAlgorithm<>(
				wekaData, 1);
		ea.setInitialPopulation(individuals);
		ea.addSelector(new TournamentSelector());
		ea.addEnvSelector(new TournamentSelector());
		ea.run();
	}

	@Test(expected = NullPointerException.class)
	public void testEvolution4() {
		EvolutionAlgorithm<TreeIndividual> ea = new EvolutionAlgorithm<>(
				wekaData, 1);
		ea.setInitialPopulation(individuals);
		ea.addSelector(new TournamentSelector());
		ea.addEnvSelector(new TournamentSelector());
		ea.addMutationOperator(new DefaultTreeMutation());
		ea.run();
	}

	@Test(expected = NullPointerException.class)
	public void testEvolution5() {
		EvolutionAlgorithm<TreeIndividual> ea = new EvolutionAlgorithm<>(
				wekaData, 1);
		ea.setInitialPopulation(individuals);
		ea.addSelector(new TournamentSelector());
		ea.addEnvSelector(new TournamentSelector());
		ea.addMutationOperator(new DefaultTreeMutation());
		ea.addCrossOperator(new DefaultTreeCrossover());
		ea.run();
	}

	@Test(expected = NullPointerException.class)
	public void testEvolution6() {
		EvolutionAlgorithm<TreeIndividual> ea = new EvolutionAlgorithm<>(
				wekaData, 1);
		ea.setInitialPopulation(individuals);
		ea.addSelector(new TournamentSelector());
		ea.addEnvSelector(new TournamentSelector());
		ea.addMutationOperator(new DefaultTreeMutation());
		ea.addCrossOperator(new DefaultTreeCrossover());
		ea.setFitnessComparator(new SingleFitnessComparator<TreeIndividual>());
		ea.run();
	}

	@Test
	public void testEvolution7() {
		FitnessFunction.registeredFunctions = 1;
		Population<TreeIndividual> testIndividuals = Utils.debugTreePopulation(); 
		testIndividuals.getIndividual(0).getFitnessValue(0);
		EvolutionAlgorithm<TreeIndividual> ea = new EvolutionAlgorithm<>(
				wekaData, 1);
		ea.setInitialPopulation(testIndividuals);
		TournamentSelector sel = new TournamentSelector();
		sel.setRandomGenerator(new Random(0));
		ea.addSelector(sel);
		TournamentSelector envSel = new TournamentSelector();
		envSel.setRandomGenerator(new Random(0));
		ea.addEnvSelector(envSel);
		ea.addMutationOperator(new DefaultTreeMutation());
		ea.addCrossOperator(new DefaultTreeCrossover());
		FitnessComparator<TreeIndividual> comp = new SingleFitnessComparator<TreeIndividual>();
		ArrayList<FitnessFunction<TreeIndividual>> fit = new ArrayList<>();
		FitnessFunction<TreeIndividual> func = new TreeHeightFitness();
		func.setIndex(0);
		fit.add(func);
		FitnessFunction.registeredFunctions = 1;
		ea.setFitnessComparator(comp);
		ea.setFitnessFunctions(fit);
		ea.run();
		
		TreeIndividual bestIndividual = ea.getActualPopulation().getBestIndividual();
		assertNotNull(bestIndividual);
	}

}
