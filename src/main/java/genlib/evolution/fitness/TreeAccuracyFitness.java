package genlib.evolution.fitness;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.ArrayInstances;
import genlib.structures.Node;

import java.util.Enumeration;

import weka.core.Instance;
import weka.core.Instances;

public class TreeAccuracyFitness extends FitnessFunction<TreeIndividual> {

	private Object data;

	@Override
	public void computeFitness(TreeIndividual individual) {

		if (data instanceof Instances) {
			computeFitness((Instances) data, individual);
			return;
		}
		if (data instanceof ArrayInstances) {
		}

	}

	private void computeFitness(Instances instances, TreeIndividual individual) {
		Node root = individual.getRootNode();
		double allData = instances.numInstances();
		double correct = 0;

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
			if (instance.classValue() == root.getValue())
				correct++;
		}
		individual.setFitnessValue(ACCURACY, correct / allData);
	}
}
