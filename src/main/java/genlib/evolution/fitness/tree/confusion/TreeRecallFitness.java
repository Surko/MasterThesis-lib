package genlib.evolution.fitness.tree.confusion;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.data.GenLibInstance;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.Node;

import java.util.Enumeration;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Fitness function that computes sensitivity/recall for population and its
 * individuals. It extends from FitnessFunction<TreeIndividual> so it can be
 * used with TreeIndividuals. It has unique initName that can be referenced when
 * initializing this function.
 * 
 * @author Lukas Surin
 *
 */
public class TreeRecallFitness extends TreeConfusionFitness {

	/** for serialization */
	private static final long serialVersionUID = 8140227134246347256L;
	public static final String initName = "tRecall";

	@Override
	protected String getFitnessName() {
		return TreeRecallFitness.initName;
	}

	@Override
	protected double attributeConfusionValue(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double tp = 0;

		Enumeration<GenLibInstance> eInstances = instances.getInstances();
		while (eInstances.hasMoreElements()) {
			GenLibInstance instance = eInstances.nextElement();
			int tClass = (int) instance.getValueOfClass();

			if (tClass != attrIndex) {
				continue;
			}

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

			int pClass = (int) root.getValue();
			if (pClass == attrIndex) {
				tp += 1;
			}
			root = individual.getRootNode();

		}

		return tp / instances.getDistribution().getClassCounts()[attrIndex];

	}

	@Override
	protected double[][] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[][] recallArray = new double[2][instances.numClasses()];		

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
				recallArray[0][tClass] += 1;
			}
			recallArray[1][tClass] += 1;
			root = individual.getRootNode();

		}

		for (int i = 0; i < recallArray.length; i++) {
			// divide with all the positives will give recall
			recallArray[0][i] /= recallArray[1][i];
		}

		return recallArray;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected double attributeConfusionValue(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double tp = 0d;
		double condPositive = 0;

		Enumeration<Instance> eInstances = instances.enumerateInstances();
		while (eInstances.hasMoreElements()) {
			Instance instance = eInstances.nextElement();
			int tClass = (int) instance.classValue();

			if (tClass != attrIndex) {
				continue;
			}

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

			if (attrIndex == (int) root.getValue()) {
				// add true positives for attribute
				tp += 1;
			}
			condPositive += 1;

			root = individual.getRootNode();
		}

		return tp/condPositive;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected double[][] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[][] recallArray = new double[2][instances.numClasses()];	

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
				recallArray[0][tClass] += 1;
			}
			recallArray[1][tClass] += 1;
			root = individual.getRootNode();

		}

		for (int i = 0; i < recallArray.length; i++) {
			recallArray[0][i] /= recallArray[1][i];
		}

		return recallArray;
	}
	
}
