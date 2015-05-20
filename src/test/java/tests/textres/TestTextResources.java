package tests.textres;

import static org.junit.Assert.assertTrue;
import genlib.GenLib;
import genlib.configurations.Config;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

public class TestTextResources {

	static {
		Logger LOG = Logger.getLogger(GenLib.class.getPackage().getName());
		LOG.setLevel(Level.SEVERE);
	}

	@Test
	public void testTextKeysEqualityOfNamesAndValues()
			throws IllegalArgumentException, IllegalAccessException {
		System.out.println("%%%%%%%%%TEXTKEY EQUALITY%%%%%%%%%");
		for (Field f : TextKeys.class.getFields()) {
			System.out.println(String.format(
					"	public static final String %s = \"%s\"", f.getName(),
					f.get(null)));
			assertTrue(f.getName().equals(f.get(null)));
		}
		System.out.println("DONE");
	}

	@Test
	public void testTextKeysToTextRes() throws IllegalArgumentException,
			IllegalAccessException {
		System.out.println("%%%%%%%%%TEXTKEY -> RESOURCES%%%%%%%%%");
		Config c = Config.getInstance();
		c.setLocale(Locale.ENGLISH);
		TextResource.reinit();
		String mapValue = null;

		// ENGLISH VERSION
		System.out.println("	ENGLISH VERSION:");
		for (Field f : TextKeys.class.getFields()) {
			mapValue = TextResource.getString((String) f.get(null));
			System.out.println(String
					.format("		Mapping: %s -> %s", f, mapValue));
			assertTrue(mapValue != null && !mapValue.isEmpty()
					&& mapValue != TextResource.no_value);
		}

		// SLOVAK VERSION
		c.setLocale(Locale.forLanguageTag("sk"));
		TextResource.reinit();
		System.out.println("	SLOVAK VERSION:");
		for (Field f : TextKeys.class.getFields()) {
			mapValue = TextResource.getString((String) f.get(null));
			System.out.println(String
					.format("		Mapping: %s -> %s", f, mapValue));
			assertTrue(mapValue != null && !mapValue.isEmpty()
					&& mapValue != TextResource.no_value);
		}

		System.out.println("DONE");
	}

	@Test
	public void testTextResToTextKeys() {
		System.out.println("%%%%%%%%%RESOURCES -> TEXTKEYS%%%%%%%%%");
		Config c = Config.getInstance();
		c.setLocale(Locale.ENGLISH);
		TextResource.reinit();

		Enumeration<String> keys = TextResource.getKeys();
		String key = null;
		Field mappedField = null;
		while (keys.hasMoreElements()) {
			key = keys.nextElement();
			try {
				mappedField = TextKeys.class.getField(key);
				System.out.println(String.format("	Mapping: %s -> %s", key,
						mappedField));
				assertTrue(key.equals(mappedField.get(null)));
			} catch (Exception e) {
				System.out.println(String.format(
						"	ERROR: TextKeys - NoSuchField: %s", key));
				assertTrue(false);
			}
		}

		System.out.println("DONE");
	}

	@Test
	public void testResourcesEquality() {
		System.out.println("%%%%%%%%%RESOURCES_EN <=> RESOURCES_SK%%%%%%%%%");
		Config c = Config.getInstance();

		// english set
		c.setLocale(Locale.ENGLISH);
		TextResource.reinit();
		Enumeration<String> enKeys = TextResource.getKeys();
		HashSet<String> enSet = new HashSet<String>();
		while (enKeys.hasMoreElements()) {
			enSet.add(enKeys.nextElement());
		}

		c.setLocale(Locale.forLanguageTag("sk"));
		TextResource.reinit();
		Enumeration<String> skKeys = TextResource.getKeys();
		HashSet<String> skSet = new HashSet<String>();
		while (skKeys.hasMoreElements()) {
			skSet.add(skKeys.nextElement());
		}

		boolean equality = skSet.equals(enSet);

		if (!equality) {
			HashSet<String> toAddSkKeys = new HashSet<String>(enSet);
			toAddSkKeys.removeAll(skSet);
			HashSet<String> toAddEnKeys = new HashSet<String>(skSet);
			toAddEnKeys.removeAll(enSet);
			System.out.println(String.format("	Missing keys in lang_en : %s",
					toAddEnKeys));
			System.out.println(String.format("	Missing keys in lang_sk : %s",
					toAddSkKeys));
		}

		assertTrue(equality);

		System.out.println("DONE");
	}

	@Test
	public void testEnResources() {
		System.out.println("%%%%%%%%%RESOURCES_EN%%%%%%%%%");
		Config c = Config.getInstance();
		c.setLocale(Locale.ENGLISH);
		TextResource.reinit();

		Enumeration<String> keys = TextResource.getKeys();
		String key = null;
		while (keys.hasMoreElements()) {
			key = keys.nextElement();
			System.out.println(String.format("	Evaluating: %s -> %s", key,
					TextResource.getString(key)));
			assertTrue(!TextResource.getString(key).isEmpty()
					&& TextResource.getString(key) != TextResource.no_value);
		}
		System.out.println("DONE");
	}

	@Test
	public void testSkResources() {
		System.out.println("%%%%%%%%%RESOURCES_SK%%%%%%%%%");
		Config c = Config.getInstance();
		c.setLocale(Locale.forLanguageTag("sk"));
		TextResource.reinit();

		Enumeration<String> keys = TextResource.getKeys();
		String key = null;
		while (keys.hasMoreElements()) {
			key = keys.nextElement();
			System.out.println(String.format("	Evaluating: %s -> %s", key,
					TextResource.getString(key)));
			assertTrue(!TextResource.getString(key).isEmpty()
					&& TextResource.getString(key) != TextResource.no_value);
		}
		System.out.println("DONE");
	}

}
