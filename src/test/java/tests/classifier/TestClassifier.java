package tests.classifier;

import static org.junit.Assert.*;

import java.util.ArrayList;

import genlib.classifier.common.EvolutionTreeClassifier;
import genlib.configurations.Config;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.WeightedFitnessComparator;
import genlib.evolution.individuals.TreeIndividual;
import genlib.plugins.PluginManager;

import org.junit.Test;



public class TestClassifier {

	private static final Config c;
	
	static {
		PluginManager.initPlugins();
		
		c = Config.getInstance();
		c.init();
		c.changeProperty(Config.FIT_COMPARATOR, "WEIGHT 0.5,0.2,0.1");
		c.changeProperty(Config.FIT_FUNCTIONS, "tAcc x;tDepth x");
	}
	
	@Test
	public void testEvolutionTreeClassifier() {
		try {			
			EvolutionTreeClassifier etc = new EvolutionTreeClassifier(true);
			etc.makePropsFromString(false);
			FitnessComparator<TreeIndividual> comp = etc.getFitnessComparator();
			ArrayList<FitnessFunction<TreeIndividual>> funcs = etc.getFitnessFunctions();
			
			assertTrue(comp instanceof WeightedFitnessComparator);
			WeightedFitnessComparator<TreeIndividual> wComp = (WeightedFitnessComparator<TreeIndividual>)comp;
			assertTrue(wComp.getWeights().length == 2);
			assertArrayEquals(wComp.getWeights(), new double[] {0.5,0.2},0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
