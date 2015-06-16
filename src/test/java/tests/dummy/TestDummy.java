package tests.dummy;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import genlib.GenLib;
import genlib.annotations.PopInitAnnot;
import genlib.classifier.popinit.CompletedTrees;
import genlib.classifier.popinit.PopulationInitializator;
import genlib.classifier.splitting.InformationGainCriteria;
import genlib.classifier.splitting.SplitCriteria;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;
import genlib.evolution.population.Population;
import genlib.structures.trees.MultiWayHeightNode;
import genlib.structures.trees.MultiWayNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.junit.Test;

import weka.classifiers.trees.j48.Distribution;
import weka.core.Instances;

public class TestDummy {

	@Test
	public void test1() {
		double[] a = new double[] { 1, 2, 3, 4, 5 };
		double[] b = a;

		a[1] = 5;

		assertArrayEquals(a, b, 0d);				
	}

	@Test
	public void test2() {
		TreeIndividual individual = new TreeIndividual(false, true);
		MultiWayHeightNode node = (MultiWayHeightNode) individual.getRootNode();
		node.setChildCount(2);
		MultiWayNode[] multi = node.getChilds();
		multi[0] = new MultiWayHeightNode();

		assertTrue(multi[0] == node.getChildAt(0));
		assertTrue(node.getChildAt(1) == null);
	}

	@Test(timeout = 20)
	public void test3() {
		long sx = System.nanoTime();
		HashMap<String, Class<CompletedTrees>> s1 = new HashMap<>();
		HashMap<String, CompletedTrees> s2 = new HashMap<>();
		s1.put("comp", CompletedTrees.class);
		s2.put("comp", new CompletedTrees());

		long s;

		try {
			s = System.nanoTime();
			@SuppressWarnings("unused")
			CompletedTrees c1 = s1.get("comp").newInstance();
			System.out.println(System.nanoTime() - s);
		} catch (Exception e) {
		}

		try {
			s = System.nanoTime();
			@SuppressWarnings("unused")
			CompletedTrees c1 = s2.get("comp").getClass().newInstance();
			System.out.println(System.nanoTime() - s);
		} catch (Exception e) {
		}

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
			PopInitAnnot anot = (PopInitAnnot) rawAnot;
			assertTrue(anot.toInjectClasses().length == anot.toInjectNames().length);
			try {
				Field f = anot.toInjectClass().getField(anot.toInjectField());
				// it's static map, so null object as parameter is adequate
				@SuppressWarnings({ "unused", "unchecked" })
				HashMap<String, Class<? extends PopulationInitializator<?>>> h = (HashMap<String, Class<? extends PopulationInitializator<?>>>) f
						.get(null);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Test
	public void test5() {
		GenLib.reconfig();
	}

	@Test
	public void test6() {
		IPopulation.populationTypes.put("test", Population.class);
		try {
			IPopulation<?> iPop = IPopulation.populationTypes.get("test")
					.newInstance();

			IPopulation<TreeIndividual> ind = iPop.makeNewInstance();
			ind.add(new TreeIndividual(false, false));
			System.out.println(ind);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void test7() {
		SplitCriteria.splitCriterias.put("test",
				InformationGainCriteria.getInstance());

		@SuppressWarnings("unchecked")
		SplitCriteria<Instances, Distribution> splitCriteria = (SplitCriteria<Instances, Distribution>) SplitCriteria.splitCriterias
				.get("test");

		assertNotNull(splitCriteria);
	}
}
