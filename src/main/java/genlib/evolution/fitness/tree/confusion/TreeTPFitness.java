package genlib.evolution.fitness.tree.confusion;

import java.util.Enumeration;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.data.GenLibInstance;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.Node;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Fitness function that computes true positive value/values from confusion
 * matrix for population and its individuals. It extends from
 * FitnessFunction<TreeIndividual> so it can be used with TreeIndividuals. It
 * has unique initName that can be referenced when initializing this function.
 * 
 * @author Lukas Surin
 *
 */
public class TreeTPFitness extends TreeConfusionFitness {

	/** for serialization */
	private static final long serialVersionUID = -5430029200022884315L;
	/** name for this fitness function, should be t$LabelOfFitness$ */
	public static final String initName = "tTP";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFitnessName() {
		return TreeTPFitness.initName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double attributeConfusionValue(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double tpValue = 0;

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
			if (tClass == attrIndex && pClass == attrIndex) {
				tpValue += 1;
			}

			root = individual.getRootNode();
		}

		return tpValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double[] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[] tpArray = new double[instances.numClasses()];

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
				tpArray[tClass] += 1;
			}

			root = individual.getRootNode();
		}
		return tpArray;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected double attributeConfusionValue(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double tpValue = 0;
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
			if (tClass == attrIndex && pClass == attrIndex) {
				tpValue += 1;
			}

			root = individual.getRootNode();
		}

		return tpValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected double[] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[] tpArray = new double[instances.numClasses()];

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
				tpArray[tClass] += 1;
			}

			root = individual.getRootNode();
		}

		return tpArray;
	}

}
