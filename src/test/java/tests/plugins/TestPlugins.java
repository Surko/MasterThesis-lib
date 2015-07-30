package tests.plugins;

import static org.junit.Assert.assertTrue;
import genlib.GenDTLib;
import genlib.plugins.PluginManager;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import tests.TestProperties;

public class TestPlugins {

	static {
		if (!TestProperties.testPrints)
			Logger.getLogger(GenDTLib.class.getPackage().getName()).setLevel(
					Level.OFF);
	}

	@Test
	public void testInternalPlugins() {
		PluginManager.initPlugins();

		assertTrue(PluginManager.classifiers.size() == 1);
		assertTrue(PluginManager.gens.size() == 3);
		assertTrue(PluginManager.popInits.size() == 4);
		assertTrue(PluginManager.fitFuncs.size() == 13);
		assertTrue(PluginManager.mutOper.size() == 5);
		assertTrue(PluginManager.xOper.size() == 2);
		assertTrue(PluginManager.envSelectors.size() == 3);
		assertTrue(PluginManager.selectors.size() == 3);
		assertTrue(PluginManager.populationTypes.size() == 1);
		assertTrue(PluginManager.splitCriterias.size() == 1);
	}

	@Test
	public void testExternalPlugins() {

	}

}
