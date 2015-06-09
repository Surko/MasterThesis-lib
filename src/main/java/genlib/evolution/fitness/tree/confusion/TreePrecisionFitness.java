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
public class TreePrecisionFitness extends TreeConfusionFitness {

	/** for serialization */
	private static final long serialVersionUID = 7017263653864815690L;
	/** name for this fitness function, should be t$LabelOfFitness$*/
	public static final String initName = "tPrecision";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFitnessName() {
		return TreePrecisionFitness.initName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double attributeConfusionValue(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double tp = 0, top = 0;

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

			int pClass = (int) root.getValue();
			if (pClass != attrIndex) {
				root = individual.getRootNode();
				continue;
			}

			int tClass = (int) instance.getValueOfClass();
			if (tClass == pClass) {
				// add true positives for attribute
				tp += 1;
			}
			top += 1;

			root = individual.getRootNode();
		}

		return tp / top;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double[] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[] precisionArray = new double[instances.numClasses()];
		double[] topArray = new double[instances.numClasses()];

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
			if (tClass == pClass) {
				// add true positives for attribute
				precisionArray[tClass] += 1;
			}
			
			topArray[pClass] += 1;
			root = individual.getRootNode();
		}

		for (int i = 0; i < precisionArray.length; i++) {
			// divide with all the positives will give recall
			precisionArray[i] /= topArray[i];
		}
		
		return precisionArray;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected double attributeConfusionValue(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double tp = 0, top = 0;

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

			int pClass = (int) root.getValue();
			if (pClass != attrIndex) {
				root = individual.getRootNode();
				continue;
			}

			int tClass = (int) instance.classValue();
			if (tClass == pClass) {
				tp += 1;
			}
			top += 1;

			root = individual.getRootNode();
		}

		return tp / top;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected double[] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[] precisionArray = new double[instances.numClasses()];
		double[] topArray = new double[instances.numClasses()];

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
			if (tClass == pClass) {
				precisionArray[tClass] += 1;
			}
			
			topArray[pClass] += 1;
			root = individual.getRootNode();
		}
		
		for (int i = 0; i < precisionArray.length; i++) {
			// divide with all the test positives will give precision
			precisionArray[i] /= topArray[i];
		}

		return precisionArray;
	}

}
