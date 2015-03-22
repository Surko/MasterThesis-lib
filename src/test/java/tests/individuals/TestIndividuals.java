package tests.individuals;

import java.util.HashMap;

import genlib.evolution.Population;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.ArrayInstances;
import genlib.structures.MultiWayDepthNode;
import genlib.utils.WekaUtils;
import genlib.utils.Utils.Sign;

import org.junit.Test;

import static org.junit.Assert.*;
import weka.core.Instances;
import weka.datagenerators.classifiers.classification.RDG1;

public class TestIndividuals {

	public static Instances wekaData;
	public static ArrayInstances arrayData;
	
	@Test
	public void testIndividualCreation() {		
		MultiWayDepthNode root = MultiWayDepthNode.makeNode(2, 1, Sign.LESS, 20d);
		MultiWayDepthNode[] childs = new MultiWayDepthNode[2];
		childs[0] = MultiWayDepthNode.makeLeaf(1);
		childs[1] = MultiWayDepthNode.makeLeaf(0);
		root.setChilds(childs);
		TreeIndividual ind1 = new TreeIndividual(root);		
		TreeIndividual[] testing = new TreeIndividual[10];
		
		for (int i = 0; i < 5; i++) {
			testing[i] = new TreeIndividual(ind1);
		}
		for (int i = 5; i < 10; i++) {
			testing[i] = ind1.copy();
		}
		
		for (int i = 0; i < 10; i++) {
			assertFalse(ind1 == testing[i]);			
			assertFalse(ind1.getRootNode() == testing[i].getRootNode());			
			assertFalse(ind1.getRootNode().getChildAt(0) == testing[i].getRootNode().getChildAt(0));
			assertFalse(ind1.getRootNode().getChildAt(1) == testing[i].getRootNode().getChildAt(1));
			assertTrue(ind1.getRootNode().getChildCount() == testing[i].getRootNode().getChildCount());
			assertTrue(ind1.getRootNode().getAttribute() == 1);
			assertTrue(ind1.getRootNode().getAttribute() == testing[i].getRootNode().getAttribute());
			assertTrue(ind1.getRootNode().getValue() == 20d);
			assertTrue(ind1.getRootNode().getValue() == testing[i].getRootNode().getValue());
			assertTrue(ind1.getRootNode().getSign() == Sign.LESS);			
			assertTrue(ind1.getRootNode().getSign() == testing[i].getRootNode().getSign());
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
		
		TreeIndividual.registeredFunctions = 2;
		TreeIndividual t1 = WekaUtils.constructTreeIndividual(sTree, 21, wekaData.numInstances(), attrIndexMap, attrValueIndexMap, false);
		TreeIndividual t2 = WekaUtils.constructTreeIndividual(sTree, 21, wekaData.numInstances(), attrIndexMap, attrValueIndexMap, false);
		
		assertTrue(t1.equals(t2));
		
	}

	
	
}
