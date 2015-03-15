package tests.individuals;

import java.util.HashMap;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.ArrayInstances;
import genlib.utils.WekaUtils;

import org.junit.Test;
import static org.junit.Assert.*;
import weka.core.Instances;
import weka.datagenerators.classifiers.classification.RDG1;

public class TestIndividuals {

	public static Instances wekaData;
	public static ArrayInstances arrayData;
	
	@Test
	public void testIndividualCreation() {
		if (wekaData == null) {
			
		}
	}
	
	@Test
	public void testIndividualEquality() throws Exception{
		
		String[] options = new String[]{"-r",
				"weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_10_-c_2_-N_0_-I_0_-M_1_-R_10",
				"-S","1","-n","100","-a","10","-c","2",
				"-N","0","-I","0","-M","1","-R","10"};		
		RDG1 rdg = new RDG1();
		rdg.setOptions(options);	
		rdg.defineDataFormat();
		wekaData = rdg.generateExamples();
		
		HashMap<String,Integer> attrIndexMap = WekaUtils.makeAttrIndexMap(wekaData);
		HashMap<String,Integer>[] attrValueIndexMap = WekaUtils.makeAttrValueIndexMap(wekaData);					
		
		String sTree = "digraph J48Tree {\n"
					+	"N0 [label=\"a5\" ]\n"
					+	"N0->N1 [label=\"\'= false\'\"]\n"
					+	"N1 [label=\"\'c0 (44.0/2.0)\'\" shape=box style=filled ]\n"
					+	"N0->N2 [label=\"\'= true\'\"]\n"
					+	"N2 [label=\"a8\" ]\n"
					+	"N2->N3 [label=\"\'= false\'\"]\n"
					+	"N3 [label=\"a9\" ]\n"
					+	"N3->N4 [label=\"\'= false\'\"]\n"
					+	"N4 [label=\"a2\" ]\n"
					+	"N4->N5 [label=\"\'= false\'\"]\n"
					+	"N5 [label=\"\'c0 (4.0/1.0)\'\" shape=box style=filled ]\n"
					+	"N4->N6 [label=\"\'= true\'\"]\n"
					+	"N6 [label=\"a0\" ]\n"
					+	"N6->N7 [label=\"\'= false\'\"]\n"
					+	"N7 [label=\"a4\" ]\n"
					+	"N7->N8 [label=\"\'= false\'\"]\n"
					+	"N8 [label=\"\'c1 (2.0)\'\" shape=box style=filled ]\n"
					+	"N7->N9 [label=\"\'= true\'\"]\n"
					+	"N9 [label=\"\'c0 (2.0)\'\" shape=box style=filled ]\n"
					+	"N6->N10 [label=\"\'= true\'\"]\n"
					+	"N10 [label=\"\'c1 (5.0)\'\" shape=box style=filled ]\n"
					+	"N3->N11 [label=\"\'= true\'\"]\n"
					+	"N11 [label=\"\'c1 (15.0/1.0)\'\" shape=box style=filled ]\n"
					+	"N2->N12 [label=\"\'= true\'\"]\n"
					+	"N12 [label=\"a1\" ]\n"
					+	"N12->N13 [label=\"\'= false\'\"]\n"
					+	"N13 [label=\"a2\" ]\n"
					+	"N13->N14 [label=\"\'= false\'\"]\n"
					+	"N14 [label=\"a0\" ]\n"
					+	"N14->N15 [label=\"\'= false\'\"]\n"
					+	"N15 [label=\"\'c1 (4.0/1.0)\'\" shape=box style=filled ]\n"
					+	"N14->N16 [label=\"\'= true\'\"]\n"
					+	"N16 [label=\"\'c0 (3.0)\'\" shape=box style=filled ]\n"
					+	"N13->N17 [label=\"\'= true\'\"]\n"
					+	"N17 [label=\"a4\" ]\n"
					+	"N17->N18 [label=\"\'= false\'\"]\n"
					+	"N18 [label=\"\'c1 (5.0)\'\" shape=box style=filled ]\n"
					+	"N17->N19 [label=\"\'= true\'\"]\n"
					+	"N19 [label=\"\'c0 (2.0)\'\" shape=box style=filled ]\n"
					+	"N12->N20 [label=\"\'= true\'\"]\n"
					+	"N20 [label=\"\'c0 (14.0/2.0)\'\" shape=box style=filled ]\n"
					+	"}";
		
		TreeIndividual t1 = WekaUtils.constructTreeIndividual(sTree, 21, wekaData.numInstances(), attrIndexMap, attrValueIndexMap, false);
		TreeIndividual t2 = WekaUtils.constructTreeIndividual(sTree, 21, wekaData.numInstances(), attrIndexMap, attrValueIndexMap, false);
		
		assertTrue(t1.equals(t2));
		
	}

	
	
}
