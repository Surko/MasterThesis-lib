package tests.data;

import static org.junit.Assert.*;
import genlib.configurations.Config;
import genlib.structures.Data;

import java.util.Random;
import org.junit.Test;

import weka.core.Instances;
import weka.datagenerators.classifiers.classification.RDG1;

public class TestData {

	private static Instances wekaData;
	private static Config c;

	static {
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
	public void testDataParse() {
		c.init();
		c.changeProperty(Config.DATA, "TRAINRATIO,0.66");
				
		Data data = new Data(wekaData, new Random(0));
		data.setParam(c.getData());
		
		assertTrue(data.getTrainData().numInstances() == 66);
		assertTrue(data.getValidationData().numInstances() == 34);
		
		assertTrue(data.getTrainData() != data.getValidationData());
		assertTrue(data != data.getTrainData());
		assertTrue(data != data.getValidationData());
	}
}
