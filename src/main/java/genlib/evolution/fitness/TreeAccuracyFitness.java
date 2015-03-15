package genlib.evolution.fitness;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.ArrayInstances;
import genlib.structures.Node;

import java.util.Enumeration;

import weka.core.Instance;
import weka.core.Instances;

public class TreeAccuracyFitness extends FitnessFunction<TreeIndividual> {

	public static final String initName = "tAcc";
	private Object testData;

	/** Constructor that will set index of tree accuracy fitness which is used in an individual fitness array.*/ 
	public TreeAccuracyFitness() {
		this.index = TREE_ACCURACY;
	}
	
	/**
	 * This method that overrides computeFitness from FitnessFunction class computes
	 * accuracy for an individual handed as parameter. If the individual hasn't changed then
	 * we can return value of this fitness right away from individual. Method calls other method
	 * with the same name that depends on type of data on which this fitness function
	 * works (weka or built-in type).
	 */
	@Override
	public double computeFitness(TreeIndividual individual) {
		if (!individual.hasChanged()) {
			return individual.getFitnessValue(index);
		}
		
		if (testData instanceof Instances) {
			return computeFitness((Instances) testData, individual);			
		}
		if (testData instanceof ArrayInstances) {
			return computeFitness((ArrayInstances)testData, individual);
		}
		
		return 0;

	}

	@SuppressWarnings("unchecked")
	private double computeFitness(Instances instances, TreeIndividual individual) {	
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
		double val = correct/allData;
		individual.setFitnessValue(index, val);
		// set to unchange for speeding up next time computation if the individual stays the same.
		individual.unchange();
		return val;
	}
	
	@SuppressWarnings("unchecked")
	private double computeFitness(ArrayInstances instances, TreeIndividual individual) {
		Node root = individual.getRootNode();
		double allData = instances.numInstances();
		double correct = 0;
		
		double val = correct/allData;
		// set to unchange for speeding up next time computation if the individual stays the same.
		individual.unchange();
		return correct;
	}

	@Override
	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}
}
