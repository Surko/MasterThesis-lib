package genlib.evolution.fitness.tree.look;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.structures.Data;

public class TreeSizeFitness extends FitnessFunction<TreeIndividual> {

	/** for serialization */
	private static final long serialVersionUID = -3857040653870276306L;
	public static final String initName = "tSize";

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

		double size = individual.getTreeSize();

		individual.setFitnessValue(index, 1d / size);
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
		return String.format(PermMessages._fit_format, initName,
				PermMessages._blank_param);
	}

	@Override
	public boolean canHandleNumeric() {
		return true;
	}
}
