package tests.init;

import static org.junit.Assert.*;

import java.io.File;

import genlib.GenLib;
import genlib.configurations.Config;
import genlib.configurations.PathManager;
import genlib.locales.TextResource;

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
		assertTrue(GenLib.LOG.getHandlers().length == 2);
		// Logger does not send to parent loggers
		assertFalse(GenLib.LOG.getUseParentHandlers());
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
		assertEquals(c.getFitnessComparator(), "SINGLE");
		assertEquals(c.getFitnessFunctions(), "tAcc");
		assertTrue(c.getElitismRate() == 0.15);
		assertTrue(c.getOperNumOfThreads() == 1);
		assertTrue(c.getGenNumOfThreads() == 1);
		assertEquals(c.getSelectors(), "Tmt 0");
		assertEquals(c.getEnvSelectors(), "Tmt 0");
		assertEquals(c.getPopulationInit(), "type DECISION_STUMP;depth 2");
		assertTrue(c.getSeed() == 28041991);
		assertTrue(c.getPopulationSize() == 100);
		
		c.changeProperty("elitism", "0.2");
		c.changeProperty("env-selectors", "Tmt 0");
		c.changeProperty("fit-eval", "SINGLE2");
		c.changeProperty("fit-functions", "test1");
		c.changeProperty("gen-threads", "10");
		c.changeProperty("oper-threads", "5");
		c.changeProperty("population-init","type DECISION_STUMP;depth 2");
		c.changeProperty("pop-size", "10000");
		c.changeProperty("seed", "0");
		c.changeProperty("selectors", "RW 0");
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
		GenLib.getApplicationVersion();
		assertTrue(Config.configured);
		// only once init
		assertFalse(GenLib.reconfig());
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

}
