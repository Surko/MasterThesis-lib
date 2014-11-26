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
		Config.getInstance().init();
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
		c.init();
		// Same object
		assertTrue(Config.getInstance() == Config.getInstance());
		// changeable existing properties
		assertTrue(c.changeProperty("locale", Config.getInstance().getLocale()
				.toString().equals("en") ? "sk" : "en"));
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
		c.init();
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
