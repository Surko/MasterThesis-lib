package tests.dummy;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import genlib.GenLib;
import genlib.annotations.PopInitAnnot;
import genlib.classifier.popinit.CompletedTrees;
import genlib.classifier.popinit.PopulationInitializator;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.structures.trees.MultiWayNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

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
		TreeIndividual individual = new TreeIndividual(false,true);
		MultiWayDepthNode node = (MultiWayDepthNode)individual.getRootNode();
		node.setChildCount(2);
		MultiWayNode[] multi = node.getChilds();
		multi[0] = new MultiWayDepthNode();
		
		assertTrue(multi[0] == node.getChildAt(0));
		assertTrue(node.getChildAt(1) == null);			
	}
	
	@Test(timeout=20)
	public void test3() {
		long sx = System.nanoTime();
		HashMap<String,Class<CompletedTrees>> s1 = new HashMap<>();
		HashMap<String,CompletedTrees> s2 = new HashMap<>();
		s1.put("comp", CompletedTrees.class);
		s2.put("comp", new CompletedTrees());
		
		long s;		
		
		try {
			s = System.nanoTime();
			CompletedTrees c1 = s1.get("comp").newInstance();
			System.out.println(System.nanoTime() - s);
		} catch (Exception e) {}
		
		try {
			s = System.nanoTime();
			CompletedTrees c1 = s2.get("comp").getClass().newInstance();
			System.out.println(System.nanoTime() - s);
		} catch (Exception e) {}
		
		System.out.println(System.nanoTime() - sx);
	}
	
	@Test
	public void test4() {
		Package p = Package.getPackage("genlib.classifier.popinit");
		if (p.getAnnotations() == null) {
			return;
		}		
		
		assertTrue(p.getAnnotations().length == 1);
		
		for (Annotation rawAnot : p.getAnnotations()) {
			PopInitAnnot anot = (PopInitAnnot)rawAnot;
			assertTrue(anot.toInjectClasses().length == anot.toInjectNames().length);
			try {
				Field f = anot.toInjectClass().getField(anot.toInjectField());
				// it's static map, so null object as parameter is adequate				
				HashMap<String,Class<? extends PopulationInitializator<?>>> h = (HashMap<String, Class<? extends PopulationInitializator<?>>>) f.get(null);											
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
	}
	
	@Test
	public void test5() {
		GenLib.reconfig();
	}
		
	
}
