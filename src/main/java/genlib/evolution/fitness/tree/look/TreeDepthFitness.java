package genlib.evolution.fitness.tree.look;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.structures.Data;
import genlib.structures.extensions.DepthExtension;
import genlib.structures.trees.Node;
import genlib.utils.Utils;

public class TreeDepthFitness extends FitnessFunction<TreeIndividual> {

	/** for serialization */
	private static final long serialVersionUID = -3857040653870276306L;
	public static final String initName = "tDepth";

	/**
	 * This method that overrides computeFitness from FitnessFunction class
	 * computes tree depth for an individual handed as parameter. If the
	 * individual hasn't changed then we can return value of this fitness right
	 * away from individual. Method calls node method getTreeHeight in that case
	 * if the node is instance of DepthExtension. Otherwise the height is
	 * computed by Utils method computeHeight. Because we want to use this
	 * fitness in max optimalization then we need to invert this value.
	 */
	@Override
	public double computeFitness(TreeIndividual individual) {
		if (!individual.hasChanged()) {
			return individual.getFitnessValue(index);
		}

		double height = 0;

		if (individual.getRootNode() instanceof DepthExtension) {
			height = individual.getRootNode().getTreeHeight() - 1;
		} else {
			Node root = individual.getRootNode();
			height = Utils.computeHeight(root) - 1;
		}
		individual.setFitnessValue(index, height == 0 ? 2 : 1 / height);
		return individual.getFitnessValue(index);
	}

	@Override
	public void setData(Data data) {
	}

	@Override
	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}

	@Override
	public void setParam(String param) {
	}

	@Override
	public String objectInfo() {
		return String.format(PermMessages._fit_format, initName, PermMessages._blank_param);
	}
	
	@Override
	public boolean canHandleNumeric() {
		return true;
	}
}
