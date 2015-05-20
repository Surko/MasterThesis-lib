package tests.evolution;

import static org.junit.Assert.*;
import genlib.classifier.Classifier;
import genlib.evolution.Population;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.operators.DefaultTreeCrossover;
import genlib.evolution.operators.DefaultTreeMutation;
import genlib.utils.Utils;

import org.junit.Test;

public class TestOperators {
	
	private static Population<TreeIndividual> individuals;
	
	static {
		individuals = Utils.debugTreePopulation();		
	}
	
	@Test
	public void testDefaultMutationOperator() {
		DefaultTreeMutation dtm = new DefaultTreeMutation();		
		assertTrue(dtm.objectInfo().equals(DefaultTreeMutation.initName + " 1.0"));
		Population<TreeIndividual> childs = new Population<>();
		dtm.execute(individuals, childs);
		assertTrue(individuals != childs);
		assertTrue(individuals.getPopulationSize() == childs.getPopulationSize());
		for (int i = 0; i < individuals.getPopulationSize(); i++) {
			assertTrue(individuals.getIndividual(i) != childs.getIndividual(i));
		}
	}
	
	@Test
	public void testDefaultCrossOverOperator() {
		DefaultTreeCrossover dtx = new DefaultTreeCrossover();		
		assertTrue(dtx.objectInfo().equals(DefaultTreeCrossover.initName + " 1.0"));
		Population<TreeIndividual> childs = new Population<>();
		dtx.execute(individuals, childs);
		assertTrue(individuals != childs);
		assertTrue(individuals.getPopulationSize() == childs.getPopulationSize());
		for (int i = 0; i < individuals.getPopulationSize(); i++) {
			assertTrue(individuals.getIndividual(i) != childs.getIndividual(i));
		}
	}
		
}
