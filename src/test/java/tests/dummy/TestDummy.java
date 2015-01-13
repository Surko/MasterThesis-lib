package tests.dummy;

import static org.junit.Assert.*;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.MultiWayNode;

import org.junit.Test;

public class TestDummy {

	@Test
	public void test1() {
		double[] a = new double[] {1,2,3,4,5};
		double[] b = a;
		
		a[1] = 5;
		
		assertArrayEquals(a, b, 0d);
	}

	@Test
	public void test2() {
		TreeIndividual individual = new TreeIndividual(false);
		MultiWayNode node = (MultiWayNode)individual.getRootNode();
		node.setChildLength(2);
		MultiWayNode[] multi = node.getChilds();
		multi[0] = new MultiWayNode();
		
		assertTrue(multi[0] == node.getChildAt(0));
		assertTrue(node.getChildAt(1) == null);
	}
	
}
