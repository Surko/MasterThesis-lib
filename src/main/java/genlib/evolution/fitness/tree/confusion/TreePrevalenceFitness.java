package genlib.evolution.fitness.tree.confusion;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.data.GenLibInstances;

import java.util.Enumeration;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Fitness function that computes prevalence for population and its individuals.
 * It extends from FitnessFunction<TreeIndividual> so it can be used with
 * TreeIndividuals. It has unique initName that can be referenced when
 * initializing this function.
 * 
 * @author Lukas Surin
 *
 */
public class TreePrevalenceFitness extends TreeConfusionFitness {

	/** for serialization */
	private static final long serialVersionUID = 5404478348561054201L;
	public static final String initName = "tPreval";

	@Override
	protected String getFitnessName() {
		return TreePrevalenceFitness.initName;
	}

	@Override
	protected double attributeConfusionValue(GenLibInstances instances,
			TreeIndividual individual) {
		return instances.getDistribution().getClassCounts()[attrIndex]
				/ instances.numInstances();
	}

	@Override
	protected double[] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		int numInstances = instances.numInstances();
		double[] prevalArray = new double[numInstances];
		prevalArray = instances.getDistribution().getClassCounts();

		for (int i = 0; i < prevalArray.length; i++) {
			prevalArray[i] /= numInstances;			
		}

		return prevalArray;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected double attributeConfusionValue(Instances instances,
			TreeIndividual individual) {
		double preval = 0d;

		Enumeration<Instance> eInstances = instances.enumerateInstances();
		while (eInstances.hasMoreElements()) {
			Instance instance = eInstances.nextElement();

			int tClass = (int) instance.classValue();

			if (tClass == attrIndex) {
				preval += 1;
			}
		}

		return preval / instances.numInstances();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected double[] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		int numInstances = instances.numInstances();
		double[] prevalArray = new double[instances.numClasses()];

		Enumeration<Instance> eInstances = instances.enumerateInstances();
		while (eInstances.hasMoreElements()) {
			Instance instance = eInstances.nextElement();

			int tClass = (int) instance.classValue();
			prevalArray[tClass]++;
		}

		for (int i = 0; i < prevalArray.length; i++) {
			prevalArray[i] /= numInstances;
		}

		return prevalArray;
	}

}
