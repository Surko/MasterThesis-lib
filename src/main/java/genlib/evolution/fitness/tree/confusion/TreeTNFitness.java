package genlib.evolution.fitness.tree.confusion;

import java.util.Enumeration;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.data.GenLibInstance;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.Node;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Fitness function that computes true negative value/values from confusion
 * matrix for population and its individuals. It extends from
 * FitnessFunction<TreeIndividual> so it can be used with TreeIndividuals. It
 * has unique initName that can be referenced when initializing this function.
 * 
 * @author Lukas Surin
 *
 */
public class TreeTNFitness extends TreeConfusionFitness {

	/** for serialization */
	private static final long serialVersionUID = -6700987342218446344L;
	/** name for this fitness function, should be t$LabelOfFitness$ */
	public static final String initName = "tTN";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFitnessName() {
		return TreeTNFitness.initName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double attributeConfusionValue(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double tnValue = 0;

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
			if (tClass != attrIndex && pClass != attrIndex) {
				tnValue += 1;
			}

			root = individual.getRootNode();
		}

		return tnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double[] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[] tnArray = new double[instances.numClasses()];

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
			if (tClass != pClass) {
				for (int i = 0; i < instances.numClasses(); i++) {
					if (i != tClass && i != pClass) {
						tnArray[i] += 1;
					}
				}
			}

			root = individual.getRootNode();
		}

		return tnArray;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected double attributeConfusionValue(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double tnValue = 0;
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
			if (tClass != attrIndex && pClass != attrIndex) {
				tnValue += 1;
			}

			root = individual.getRootNode();
		}

		return tnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected double[] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[] tnArray = new double[instances.numClasses()];

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
			if (tClass != pClass) {
				for (int i = 0; i < instances.numClasses(); i++) {
					if (i != tClass && i != pClass) {
						tnArray[i] += 1;
					}
				}
			}

			root = individual.getRootNode();
		}

		return tnArray;
	}

}
