package genlib.evolution.fitness.tree.confusion;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.data.GenLibInstance;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.Node;

import java.util.Enumeration;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Fitness function that computes specificity for population and its
 * individuals. It extends from FitnessFunction<TreeIndividual> so it can be
 * used with TreeIndividuals. It has unique initName that can be referenced when
 * initializing this function.
 * 
 * TODO methods are not ready
 * 
 * @author Lukas Surin
 *
 */
public class TreeSpecificityFitness extends TreeConfusionFitness {

	/** for serialization */
	private static final long serialVersionUID = -239489913433851564L;
	public static final String initName = "tSpecificity";

	@Override
	protected String getFitnessName() {
		return TreeSpecificityFitness.initName;
	}

	@Override
	protected double attributeConfusionValue(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double specificity = 0;
		double denominator = 0;

		Enumeration<GenLibInstance> eInstances = instances.getInstances();
		while (eInstances.hasMoreElements()) {
			GenLibInstance instance = eInstances.nextElement();
			
			while (!root.isLeaf()) {
				if (instance.getAttribute(root.getAttribute()).isNumeric()) {
					if (instance.getValueOfAttribute(root.getAttribute()) < root
							.getValue()) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance
							.getValueOfAttribute(root.getAttribute()));
				}
			}

			int tClass = (int) instance.getValueOfClass();
			int pClass = (int) root.getValue();

			if (tClass != attrIndex) {
				denominator += 1;
				if (pClass != attrIndex) {
					specificity += 1;
				}
			}
			
			root = individual.getRootNode();

		}

		return specificity / denominator;

	}

	@Override
	protected double[] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[] recallArray = new double[instances.numClasses()];

		Enumeration<GenLibInstance> eInstances = instances.getInstances();
		while (eInstances.hasMoreElements()) {
			GenLibInstance instance = eInstances.nextElement();
			while (!root.isLeaf()) {
				if (instance.getAttribute(root.getAttribute()).isNumeric()) {
					if (instance.getValueOfAttribute(root.getAttribute()) < root
							.getValue()) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance
							.getValueOfAttribute(root.getAttribute()));
				}
			}

			int tClass = (int) instance.getValueOfClass();
			if (tClass == (int) root.getValue()) {
				// add true positives for attribute
				recallArray[tClass] += 1;
			}

			root = individual.getRootNode();

		}

		double[] classCounts = data.getClassCounts();
		for (int i = 0; i < recallArray.length; i++) {
			// divide with all the positives will give recall
			recallArray[i] /= classCounts[i];
		}

		return recallArray;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected double attributeConfusionValue(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double specificity = 0;
		double denominator = 0;

		Enumeration<Instance> eInstances = instances.enumerateInstances();
		while (eInstances.hasMoreElements()) {
			Instance instance = eInstances.nextElement();			

			while (!root.isLeaf()) {
				if (instance.attribute(root.getAttribute()).isNumeric()) {
					if (instance.value(root.getAttribute()) < root.getValue()) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance.value(root
							.getAttribute()));
				}
			}

			int tClass = (int) instance.classValue();
			int pClass = (int) root.getValue();

			if (tClass != attrIndex) {
				denominator += 1;
				if (pClass != attrIndex) {
					specificity += 1;
				}
			}			

			root = individual.getRootNode();
		}

		return specificity / denominator;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected double[] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[] recallArray = new double[instances.numClasses()];

		Enumeration<Instance> eInstances = instances.enumerateInstances();
		while (eInstances.hasMoreElements()) {
			Instance instance = eInstances.nextElement();
			while (!root.isLeaf()) {
				if (instance.attribute(root.getAttribute()).isNumeric()) {
					if (instance.value(root.getAttribute()) < root.getValue()) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance.value(root
							.getAttribute()));
				}
			}

			int tClass = (int) instance.classValue();
			if (tClass == (int) root.getValue()) {
				recallArray[tClass] += 1;
			}

			root = individual.getRootNode();

		}

		double[] classCounts = data.getClassCounts();
		for (int i = 0; i < recallArray.length; i++) {
			// divide with all the positives will give recall
			recallArray[i] /= classCounts[i];
		}

		return recallArray;
	}

}
