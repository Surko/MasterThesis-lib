package tests.init;

import static org.junit.Assert.*;

import java.io.File;

import genlib.GenDTLib;
import genlib.classifier.common.EvolutionTreeClassifier;
import genlib.configurations.Config;
import genlib.configurations.PathManager;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.SingleFitnessComparator;
import genlib.evolution.fitness.tree.TreeAccuracyFitness;
import genlib.evolution.operators.DefaultTreeCrossover;
import genlib.evolution.operators.DefaultTreeMutation;
import genlib.evolution.operators.Operator;
import genlib.evolution.selectors.Selector;
import genlib.generators.WekaJ48TreeGenerator;
import genlib.initializators.TreePopulationInitializator;
import genlib.locales.TextResource;
import genlib.plugins.PluginManager;

import org.junit.Test;

public class TestInit {

	@Test
	public void testConfigFileCreation() {
		Config.getInstance();
		assertTrue(new File(PathManager.getInstance().getRootPath(),
				"config.properties").exists());
	}

	@Test
	public void testLoggerInit() {
		// Must be only two handlers
		assertTrue(GenDTLib.LOG.getHandlers().length == 2);
		// Logger does not send to parent loggers
		assertFalse(GenDTLib.LOG.getUseParentHandlers());
	}

	@Test
	public void testConfigInit() {
		Config c = Config.getInstance();		
		// Same object
		assertTrue(Config.getInstance() == Config.getInstance());
		// changeable existing properties
		assertTrue(c.changeProperty("locale", Config.getInstance().getLocale()
				.toString().equals("en") ? "sk" : "en"));
		c.reset();
		assertEquals(c.getFitnessComparator(), "SINGLE 0");
		assertEquals(c.getFitnessFunctions(), "tAcc x");
		assertTrue(c.getElitismRate() == 0.15);
		assertTrue(c.getOperNumOfThreads() == 1);
		assertTrue(c.getGenNumOfThreads() == 1);
		assertEquals(c.getSelectors(), "Tmt x");
		assertEquals(c.getEnvSelectors(), "Tmt x");
		assertEquals(c.getPopulationInit(), "wCompTree MAXHEIGHT,2");
		assertTrue(c.getSeed() == 28041991);
		assertTrue(c.getPopulationSize() == 100);
		
		c.changeProperty(Config.ELITISM, "0.2");
		c.changeProperty(Config.ENV_SELECTORS, "Tmt 0");
		c.changeProperty(Config.FIT_COMPARATOR, "SINGLE2");
		c.changeProperty(Config.FIT_FUNCTIONS, "test1");
		c.changeProperty(Config.GEN_THREADS, "10");
		c.changeProperty(Config.OPER_THREADS, "5");
		c.changeProperty(Config.POP_INIT,"type DECISION_STUMP;depth 2");
		c.changeProperty(Config.POP_SIZE, "10000");
		c.changeProperty(Config.SEED, "0");
		c.changeProperty(Config.SELECTORS, "RW 0");
		assertEquals(c.getFitnessComparator(), "SINGLE2");
		assertEquals(c.getFitnessFunctions(), "test1");
		assertTrue(c.getElitismRate() == 0.2);
		assertTrue(c.getOperNumOfThreads() == 5);		
		assertTrue(c.getGenNumOfThreads() == 10);
		assertEquals(c.getSelectors(), "RW 0");
		assertEquals(c.getEnvSelectors(), "Tmt 0");
		assertEquals(c.getPopulationInit(), "type DECISION_STUMP;depth 2");
		assertTrue(c.getSeed() == 0);
		assertTrue(c.getPopulationSize() == 10000);
		
	}	
	
	@Test
	public void testPathManagerInit() {
		PathManager pm = PathManager.getInstance();
		pm.init();
		assertNotNull(pm.getRootPath());
		assertNotNull(pm.getLocalePath());
	}

	@Test
	public void testGenLibAccessConfiguration() {
		GenDTLib.getApplicationVersion();
		assertTrue(Config.configured);
		// only once init
		assertFalse(GenDTLib.reconfig());
	}

	@Test
	public void testTextMessageInit() {
		Config c = Config.getInstance();		
		TextResource.clear();
		// First reinit
		assertTrue(TextResource.reinit());
		// Second reinit shouldn't be needed
		assertFalse(TextResource.reinit());

		c.changeProperty("locale", c.getLocale().toString().equals("en") ? "sk"
				: "en");
		// Locales in ResourceBundle and Config are different. It should reinit.
		assertTrue(TextResource.reinit());
	}

	@Test
	public void testEvolutionTreeClassifierInit() throws Exception {
		Config c = Config.getInstance();
		c.reset();
		c.setAbsentProperties();
		PluginManager.initPlugins();
		
		EvolutionTreeClassifier etc = new EvolutionTreeClassifier(true);
		etc.makePropsFromString(false);			
				
		assertTrue(PluginManager.fitFuncs.size() == 13);
		assertTrue(PluginManager.mutOper.size() == 5);
		assertTrue(PluginManager.xOper.size() == 2);
		assertTrue(PluginManager.selectors.size() == 3);
		assertTrue(PluginManager.envSelectors.size() == 3);
		
		assertTrue(etc.getFitnessComparator() != null);
		
		assertTrue(etc.getFitnessComparator() instanceof SingleFitnessComparator); 
		assertTrue(etc.getFitnessFunctions().size() == 1);
		assertTrue(etc.getFitnessFunctions().get(0) instanceof TreeAccuracyFitness);
		assertTrue(etc.getMutSet().size() == 1);
		assertTrue(etc.getMutSet().get(0) instanceof DefaultTreeMutation);
		assertTrue(Double.compare(etc.getMutSet().get(0).getOperatorProbability(), 1) == 0);
		assertTrue(etc.getXoverSet().size() == 1);
		assertTrue(etc.getXoverSet().get(0) instanceof DefaultTreeCrossover);
		assertTrue(Double.compare(etc.getXoverSet().get(0).getOperatorProbability(), 1) == 0);
		assertTrue(etc.getPopInitializator() != null);
		assertTrue(etc.getPopInitializator() instanceof TreePopulationInitializator);
		assertTrue(etc.getPopInitializator().getMaxHeight() == 2);
		assertTrue(etc.getPopInitializator().getGenerator() != null);
		assertTrue(etc.getPopInitializator().getGenerator() instanceof WekaJ48TreeGenerator);
		
	}
	
}
