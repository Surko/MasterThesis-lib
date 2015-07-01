package tests.classifier;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import genlib.classifier.common.EvolutionTreeClassifier;
import genlib.classifier.weka.WekaEvolutionTreeClassifier;
import genlib.configurations.Config;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.WeightedFitnessComparator;
import genlib.evolution.individuals.TreeIndividual;
import genlib.plugins.PluginManager;
import genlib.structures.Data;

import org.junit.Test;

import weka.core.Instances;
import weka.datagenerators.classifiers.classification.RDG1;

public class TestClassifier {

	private static final Config c;
	private static Instances wekaData;

	static {
		PluginManager.initPlugins();
		c = Config.getInstance();

		try {
			String[] options = new String[] {
					"-r",
					"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
					"-S", "1", "-n", "100", "-a", "10", "-c", "2", "-N", "0",
					"-I", "0", "-M", "1", "-R", "10" };
			RDG1 rdg = new RDG1();
			rdg.setOptions(options);
			rdg.defineDataFormat();
			wekaData = rdg.generateExamples();
		} catch (Exception e) {
		}
	}

	@Test
	public void testEvolutionTreeClassifier1() {
		c.init();
		c.changeProperty(Config.FIT_COMPARATOR, "WEIGHT 0.5,0.2,0.1");
		c.changeProperty(Config.FIT_FUNCTIONS, "tAcc x;tHeight x");
		try {
			EvolutionTreeClassifier etc = new EvolutionTreeClassifier(true);
			etc.makePropsFromString(false);
			FitnessComparator<TreeIndividual> comp = etc.getFitnessComparator();

			assertTrue(comp instanceof WeightedFitnessComparator);
			WeightedFitnessComparator<TreeIndividual> wComp = (WeightedFitnessComparator<TreeIndividual>) comp;
			assertTrue(wComp.getWeights().length == 2);
			assertArrayEquals(wComp.getWeights(), new double[] { 0.5, 0.2 }, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testEvolutionTreeClassifier2() {
		c.reset();
		c.setAbsentProperties();

		try {
			EvolutionTreeClassifier etc = new EvolutionTreeClassifier(true);
			etc.buildClassifier(wekaData);
			assertTrue(etc.getActualIndividuals().size() == 100);
			assertTrue(Double.compare(etc.getBestIndividuals().get(0)
					.getFitnessValue(0), 0.91d) == 0);
			System.out.println(etc.getBestIndividuals());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testEvolutionTreeClassifier3() {
		c.reset();
		c.setAbsentProperties();
		c.setMutationOperators("ntlNomM 0.04");
		c.setXoverOperators("subTreeX 0.8");
		c.setFitnessComparator("PRIORITY x");
		c.setFitnessFunctions("tAcc x;tSize x");
		try {
			EvolutionTreeClassifier etc = new EvolutionTreeClassifier(true);
			etc.setNumberOfGenerations(10);
			etc.buildClassifier(wekaData);
			assertTrue(etc.getActualIndividuals().size() == 100);
			//assertTrue(Double.compare(etc.getBestIndividual()
			//		.getFitnessValue(0), 0.92d) == 0);
			System.out.println(etc.getStartIndividual());
			System.out.println(etc.getBestIndividuals());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEvolutionTreeClassifier4() {
		c.reset();
		c.setAbsentProperties();
		c.setMutationOperators("ntlNomM 0.04;wekaDSM 0.04");
		c.setXoverOperators("subTreeX 0.8");
		c.setFitnessComparator("PRIORITY x");
		c.setFitnessFunctions("tAcc x;tSize x");
		try {
			EvolutionTreeClassifier etc = new EvolutionTreeClassifier(true);
			etc.setNumberOfGenerations(70);
			etc.buildClassifier(wekaData);
			assertTrue(etc.getActualIndividuals().size() == 100);
			assertTrue(etc.getFitnessFunctions().size() == 2);
			assertTrue(etc.getMutSet().size() == 2);
			assertTrue(etc.getXoverSet().size() == 1);
			//assertTrue(Double.compare(etc.getBestIndividual()
			//		.getFitnessValue(0), 0.92d) == 0);
			System.out.println(etc.getStartIndividual());
			System.out.println(etc.getBestIndividuals());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test(expected=Exception.class)
	public void testLoadingFile() throws Exception {
		WekaEvolutionTreeClassifier wetc = new WekaEvolutionTreeClassifier();
		Data data = wetc.makeDataFromFile("");
		System.out.println(data);
	}

}
