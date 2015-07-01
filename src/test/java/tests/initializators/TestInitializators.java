package tests.initializators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import genlib.classifier.popinit.CompletedTrees;
import genlib.classifier.popinit.RandomStumpCombinator;
import genlib.classifier.popinit.WekaCompletedTrees;
import genlib.classifier.popinit.WekaRandomStumpCombinator;
import genlib.evolution.fitness.FitnessFunction;
import genlib.plugins.PluginManager;
import genlib.structures.Data;

import org.junit.Test;

import weka.datagenerators.classifiers.classification.RDG1;

public class TestInitializators {

	public static Data wekaData;
	public static Data genLibData;

	@Test
	public void testInitProperties() {
		String sClass = CompletedTrees.initName;
		String sObj = new CompletedTrees().getInitName();
		assertEquals(sClass, sObj);
		sClass = WekaCompletedTrees.initName;
		sObj = new WekaCompletedTrees().getInitName();
		assertEquals(sClass, sObj);
		sClass = RandomStumpCombinator.initName;
		sObj = new RandomStumpCombinator().getInitName();
		assertEquals(sClass, sObj);
		sClass = WekaRandomStumpCombinator.initName;
		sObj = new WekaRandomStumpCombinator().getInitName();
		assertEquals(sClass, sObj);
	}

	@Test
	public void testSingleInits() {
		PluginManager.popInits.put(CompletedTrees.initName,
				CompletedTrees.class);
		try {
			// FASTER ALTERNATIVE
			CompletedTrees compTree = (CompletedTrees) PluginManager.popInits
					.get("CompTree").newInstance();
			assertTrue(compTree.getClass().equals(CompletedTrees.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testNominalWekaInit() throws Exception {
		String[] options = new String[] {
				"-r",
				"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
				"-S", "1", "-n", "100", "-a", "10", "-c", "2", "-N", "0", "-I",
				"0", "-M", "1", "-R", "10" };
		RDG1 rdg = new RDG1();
		rdg.setOptions(options);
		rdg.defineDataFormat();
		wekaData = new Data(rdg.generateExamples(), new Random(0));

		FitnessFunction.registeredFunctions = 2;
		WekaCompletedTrees wct = new WekaCompletedTrees(200, 10, false);
		wct.setData(wekaData);
		wct.initPopulation();
		assertTrue(wct.getAttrIndexMap().size() == 11);
		assertTrue(wct.getAttrValueIndexMap().length == 11);
		assertTrue(wct.getAttrValueIndexMap()[0].get("false") == 0);
		assertTrue(wct.getAttrValueIndexMap()[5].get("true") == 1);
		assertTrue(wct.getAttrValueIndexMap()[10].get("c0") == 0);
		assertTrue(wct.getAttrValueIndexMap()[10].get("c1") == 1);
	}

	@Test
	public void testNumericWekaInit() throws Exception {
		String[] options = new String[] {
				"-r",
				"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_10_-I_0_-M_1_-R_10",
				"-S", "1", "-n", "100", "-a", "10", "-c", "2", "-N", "10",
				"-I", "0", "-M", "1", "-R", "10" };
		RDG1 rdg = new RDG1();
		rdg.setOptions(options);
		rdg.defineDataFormat();
		wekaData = new Data(rdg.generateExamples(), new Random(0));

		FitnessFunction.registeredFunctions = 2;
		WekaCompletedTrees wct = new WekaCompletedTrees(200, 10, false);
		assertTrue(wct.getDivideParam() == 10);
		assertFalse(wct.isResampling());
		wct.setData(wekaData);
		wct.initPopulation();
		assertTrue(wct.getAttrIndexMap().size() == 11);
		assertTrue(wct.getAttrValueIndexMap().length == 11);
		assertNull(wct.getAttrValueIndexMap()[0]);
		assertNull(wct.getAttrValueIndexMap()[5]);
		assertNotNull(wct.getAttrValueIndexMap()[10]);
		assertNotNull(wct.getPopulation());
		assertTrue(wct.getPopulation().length == 200);

		wct.setPopulationSize(1);
		wct.setData(wekaData);
		wct.initPopulation();
		assertTrue(wct.getPopulation().length == 1);
	}

}
