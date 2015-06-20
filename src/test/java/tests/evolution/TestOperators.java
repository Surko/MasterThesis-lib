package tests.evolution;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.Random;

import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.operators.DecisionStumpMutation;
import genlib.evolution.operators.DefaultTreeCrossover;
import genlib.evolution.operators.DefaultTreeMutation;
import genlib.evolution.operators.NodeToLeafNominalMutation;
import genlib.evolution.population.Population;
import genlib.structures.Data;
import genlib.utils.Utils;
import genlib.utils.Utils.Sign;

import org.junit.Test;

import weka.datagenerators.classifiers.classification.RDG1;

public class TestOperators {

	private static Population<TreeIndividual> individuals;
	private static Data wekaData;

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
			wekaData = new Data(rdg.generateExamples(), new Random(0));
		} catch (Exception e) {}
	}

	@Test
	public void testDefaultMutationOperator() {
		DefaultTreeMutation dtm = new DefaultTreeMutation();
		assertTrue(dtm.objectInfo().equals(
				DefaultTreeMutation.initName + " x"));
		Population<TreeIndividual> childs = new Population<>();
		dtm.execute(individuals, childs);
		assertTrue(individuals != childs);
		assertTrue(individuals.getActualPopSize() == childs.getActualPopSize());
		for (int i = 0; i < individuals.getActualPopSize(); i++) {
			assertTrue(individuals.getIndividual(i) != childs.getIndividual(i));
		}
	}

	@Test
	public void testDefaultCrossOverOperator() {
		DefaultTreeCrossover dtx = new DefaultTreeCrossover();
		assertTrue(dtx.objectInfo().equals(
				DefaultTreeCrossover.initName + " x"));
		Population<TreeIndividual> childs = new Population<>();
		dtx.execute(individuals, childs);
		assertTrue(individuals != childs);
		assertTrue(individuals.getActualPopSize() == childs.getActualPopSize());
		for (int i = 0; i < individuals.getActualPopSize(); i++) {
			assertTrue(individuals.getIndividual(i) != childs.getIndividual(i));
		}
	}
	
	@Test
	public void testNodeToLeafMutation() {
		Population<TreeIndividual> copyOfIndividuals = new Population<TreeIndividual>(individuals);
		
		NodeToLeafNominalMutation ntl = new NodeToLeafNominalMutation();
		assertTrue(ntl.objectInfo().equals(
				NodeToLeafNominalMutation.initName + " PROB,0.0"));
		ntl.setRandomGenerator(new Random(0L));
		ntl.setOperatorProbability(1.0d);
		ntl.setData(wekaData);
		assertTrue(ntl.objectInfo().equals(
				NodeToLeafNominalMutation.initName + " PROB,1.0"));
		Population<TreeIndividual> childs = new Population<>();
		ntl.execute(copyOfIndividuals, childs);
		assertTrue(copyOfIndividuals != childs);
		System.out.println(copyOfIndividuals.getActualPopSize());
		System.out.println(childs.getActualPopSize());
		assertTrue(copyOfIndividuals.getActualPopSize() == childs.getActualPopSize());
		for (int i = 0; i < childs.getActualPopSize(); i++) {
			assertTrue(childs.getIndividual(i).getRootNode().isLeaf());
			assertTrue(childs.getIndividual(i).getRootNode().getAttribute() == -1);
			assertTrue(childs.getIndividual(i).getRootNode().getValue() == 0);
			assertNull(childs.getIndividual(i).getRootNode().getSign());
		}
	}
	
	@Test
	public void testDecisionStumpMutation() {
		Population<TreeIndividual> copyOfIndividuals = new Population<TreeIndividual>(individuals);
		
		DecisionStumpMutation dsm = new DecisionStumpMutation();
		assertTrue(dsm.objectInfo().equals(
				DecisionStumpMutation.initName + " PROB,0.0"));
		dsm.setRandomGenerator(new Random(0L));
		dsm.setOperatorProbability(1.0d);
		dsm.setData(wekaData);
		assertTrue(dsm.objectInfo().equals(
				DecisionStumpMutation.initName + " PROB,1.0"));
		Population<TreeIndividual> childs = new Population<>();
		dsm.execute(copyOfIndividuals, childs);
		assertTrue(copyOfIndividuals != childs);
		System.out.println(copyOfIndividuals.getActualPopSize());
		System.out.println(childs.getActualPopSize());
		assertTrue(copyOfIndividuals.getActualPopSize() == childs.getActualPopSize());
		for (int i = 0; i < childs.getActualPopSize(); i++) {			
			assertTrue(childs.getIndividual(i).getRootNode().getAttribute() == 1);
			assertTrue(childs.getIndividual(i).getRootNode().getValue() == 20d);
			assertTrue(childs.getIndividual(i).getRootNode().getSign() == Sign.LESS);
		}
	}

}
