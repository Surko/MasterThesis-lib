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
 * @author Lukas Surin
 *
 */
public class TreeSpecificityFitness extends TreeConfusionFitness {

	/** for serialization */
	private static final long serialVersionUID = -239489913433851564L;
	/** name for this fitness function, should be t$LabelOfFitness$*/
	public static final String initName = "tSpecificity";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFitnessName() {
		return TreeSpecificityFitness.initName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double attributeConfusionValue(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double specificity = 0;
		double cn = 0;

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
				cn += 1;
				if (pClass != attrIndex) {
					specificity += 1;
				}
			}

			root = individual.getRootNode();

		}

		return specificity / cn;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double[] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		// firstly here will be inverse to condition negative => numInstances -
		// inv_cn = cn
		double[] specificity = new double[instances.numClasses()];

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
				specificity[tClass] += 1;
			} else {
				specificity[tClass] += 1;
				specificity[pClass] += 1;
			}

			root = individual.getRootNode();

		}

		// stored condition positives for each attribute. numinstances - cp = cn
		double[] classes = data.getClassCounts();
		for (int i = 0; i < specificity.length; i++) {
			specificity[i] = (instances.numInstances() - specificity[i])
					/ (instances.numInstances() - classes[i]);
		}

		return specificity;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected double attributeConfusionValue(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double specificity = 0;
		double cn = 0;

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
				cn += 1;
				if (pClass != attrIndex) {
					specificity += 1;
				}
			}

			root = individual.getRootNode();
		}

		return specificity / cn;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected double[] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		// firstly here will be inverse to condition negative => numInstances -
		// inv_cn = cn
		double[] specificity = new double[instances.numClasses()];

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
				specificity[tClass] += 1;
			} else {
				specificity[tClass] += 1;
				specificity[pClass] += 1;
			}

			root = individual.getRootNode();

		}

		// stored condition positives for each attribute. numinstances - cp = cn
		double[] classes = data.getClassCounts();
		for (int i = 0; i < specificity.length; i++) {
			specificity[i] = (instances.numInstances() - specificity[i])
					/ (instances.numInstances() - classes[i]);
		}

		return specificity;
	}

}
