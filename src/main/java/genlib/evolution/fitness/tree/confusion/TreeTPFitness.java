package genlib.evolution.fitness.tree.confusion;

import java.util.Enumeration;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.data.GenLibInstance;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.Node;
import weka.core.Instance;
import weka.core.Instances;

public class TreeTPFitness extends TreeConfusionFitness {

	/** for serialization */
	private static final long serialVersionUID = -5430029200022884315L;
	public static final String initName = "tTP";

	@Override
	protected String getFitnessName() {
		return TreeTPFitness.initName;
	}

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

	@Override
	protected double[][] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[][] tpArray = new double[2][instances.numClasses()];

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
				tpArray[0][tClass] += 1;
			}
			root = individual.getRootNode();
		}
		tpArray[1] = tpArray[0];
		return tpArray;
	}

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

	@SuppressWarnings("unchecked")
	@Override
	protected double[][] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[][] tpArray = new double[2][instances.numClasses()];

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
				tpArray[0][tClass] += 1;
			}
			root = individual.getRootNode();
		}

		tpArray[1] = tpArray[0];
		return tpArray;
	}

}
