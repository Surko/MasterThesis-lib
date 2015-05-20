package tests.initializators;

import static org.junit.Assert.*;
import genlib.classifier.popinit.CompletedTrees;
import genlib.classifier.popinit.RandomStumpCombinator;
import genlib.classifier.popinit.TreePopulationInitializator;
import genlib.classifier.popinit.WekaCompletedTrees;
import genlib.classifier.popinit.WekaRandomStumpCombinator;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.Individual;
import genlib.structures.Data;
import genlib.structures.data.GenLibInstances;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import weka.core.Instances;
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
		CompletedTrees.treePopInits.put(CompletedTrees.initName, CompletedTrees.class);
		try {
			// FASTER ALTERNATIVE			
			CompletedTrees compTree = (CompletedTrees) TreePopulationInitializator.treePopInits.get("CompTree").newInstance();			
			assertTrue(compTree.getClass().equals(CompletedTrees.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testNominalWekaInit() throws Exception {
		String[] options = new String[]{"-r",
				"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
				"-S","1","-n","100","-a","10","-c","2",
				"-N","0","-I","0","-M","1","-R","10"};		
		RDG1 rdg = new RDG1();
		rdg.setOptions(options);	
		rdg.defineDataFormat();
		wekaData = new Data(rdg.generateExamples());
		
		FitnessFunction.registeredFunctions = 2;
		WekaCompletedTrees wct = new WekaCompletedTrees(200,10,false);
		wct.setInstances(wekaData);
		wct.initPopulation();
		assertTrue(wct.getAttrIndexMap().size() == 11);
		assertTrue(wct.getAttrValueIndexMap().length == 11);
		assertTrue(wct.getAttrValueIndexMap()[0].get("false")==0);
		assertTrue(wct.getAttrValueIndexMap()[5].get("true")==1);
		assertTrue(wct.getAttrValueIndexMap()[10].get("c0")==0);
		assertTrue(wct.getAttrValueIndexMap()[10].get("c1")==1);
	}
	
	@Test
	public void testNumericWekaInit() throws Exception {
		String[] options = new String[]{"-r",
				"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_10_-I_0_-M_1_-R_10",
				"-S","1","-n","100","-a","10","-c","2",
				"-N","10","-I","0","-M","1","-R","10"};		
		RDG1 rdg = new RDG1();
		rdg.setOptions(options);	
		rdg.defineDataFormat();
		wekaData = new Data(rdg.generateExamples());
		
		FitnessFunction.registeredFunctions = 2;
		WekaCompletedTrees wct = new WekaCompletedTrees(200,10,false);
		assertTrue(wct.getDivideParam()==10);
		assertFalse(wct.isResampling());		
		wct.setInstances(wekaData);
		wct.initPopulation();
		assertTrue(wct.getAttrIndexMap().size() == 11);
		assertTrue(wct.getAttrValueIndexMap().length == 11);
		assertNull(wct.getAttrValueIndexMap()[0]);
		assertNull(wct.getAttrValueIndexMap()[5]);
		assertNotNull(wct.getAttrValueIndexMap()[10]);
		assertNotNull(wct.getPopulation());	
		assertTrue(wct.getPopulation().length == 200);
		
		wct.setPopulationSize(1);
		wct.setInstances(wekaData);
		wct.initPopulation();		
		assertTrue(wct.getPopulation().length == 1);						
	}
	
	
	
}
