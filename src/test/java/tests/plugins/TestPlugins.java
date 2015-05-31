package tests.plugins;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import genlib.GenLib;
import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.popinit.TreePopulationInitializator;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.operators.Operator;
import genlib.evolution.selectors.Selector;
import genlib.plugins.PluginManager;

import org.junit.Test;

import tests.TestProperties;

public class TestPlugins {

	static {
		if (!TestProperties.testPrints)
			Logger.getLogger(GenLib.class.getPackage()
					.getName()).setLevel(Level.OFF);
	}
	
	@Test
	public void testInternalPlugins() {		
		PluginManager.initPlugins();

		assertTrue(TreeGenerator.treeGens.size() == 2);
		assertTrue(TreePopulationInitializator.treePopInits.size() == 4);		
		assertTrue(FitnessFunction.tFitFuncs.size() == 13);
		assertTrue(Operator.tMOper.size() == 1);
		assertTrue(Operator.tXOper.size() == 1);
		assertTrue(Selector.envSelectors.size() == 2);
		assertTrue(Selector.selectors.size() == 2);
	}

	@Test
	public void testExternalPlugins() {
		
	}
	
}
