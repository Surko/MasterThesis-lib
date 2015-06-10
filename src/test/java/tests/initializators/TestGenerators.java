package tests.initializators;

import genlib.classifier.gens.WekaJ48TreeGenerator;
import genlib.classifier.gens.WekaTreeGenerator;
import genlib.structures.data.GenLibInstances;

import org.junit.Test;

import static org.junit.Assert.*;
import weka.core.Instances;
import weka.datagenerators.classifiers.classification.RDG1;

public class TestGenerators {

	public static Instances wekaData;
	public static GenLibInstances arrayData;
	
	@Test
	public void testJ48Generator() throws Exception {
		String[] options = new String[]{"-r",
				"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
				"-S","1","-n","100","-a","10","-c","2",
				"-N","0","-I","0","-M","1","-R","10"};		
		RDG1 rdg = new RDG1();
		rdg.setOptions(options);	
		rdg.defineDataFormat();
		wekaData = rdg.generateExamples();		
		assertNotNull(wekaData);
		assertTrue(wekaData.numInstances()==100);
		
		options = new String[]{"-C","0.25","-M","2"};		
		WekaJ48TreeGenerator genBlank = new WekaJ48TreeGenerator();
		genBlank.setInstances(wekaData);
		assertNotNull(genBlank.getJ48());
		WekaJ48TreeGenerator genParam = new WekaJ48TreeGenerator(options);
		genParam.setInstances(wekaData);
		assertNotNull(genParam.getJ48());
		genBlank.setOptions(options);	
		
		genBlank.getJ48().buildClassifier(wekaData);		
		genParam.getJ48().buildClassifier(wekaData);							
		assertEquals(genBlank.getJ48().toString(), genParam.getJ48().toString());
		
	}
	
	@Test
	public void testWekaGenerator() throws Exception {
		String[] options = new String[]{"-r",
				"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
				"-S","1","-n","100","-a","10","-c","2",
				"-N","0","-I","0","-M","1","-R","10"};		
		RDG1 rdg = new RDG1();
		rdg.setOptions(options);	
		rdg.defineDataFormat();
		wekaData = rdg.generateExamples();		
		assertNotNull(wekaData);
		assertTrue(wekaData.numInstances()==100);
		
		options = new String[]{"-C","0.25","-M","2"};		
		WekaTreeGenerator genBlank = new WekaTreeGenerator();
		genBlank.setInstances(wekaData);
		assertNull(genBlank.getClassifier());
		WekaTreeGenerator genParam = new WekaTreeGenerator();
		genParam.setParam("TYPE,weka.classifiers.trees.ADTree,-B,10,-E,3");
		genParam.setInstances(wekaData);
		assertNotNull(genParam.getClassifier());
		
		genParam.getClassifier().buildClassifier(wekaData);		
	}
	
}
