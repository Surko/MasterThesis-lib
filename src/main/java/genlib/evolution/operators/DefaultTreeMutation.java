package genlib.evolution.operators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;
import genlib.evolution.population.Population;

/**
 * Default Tree Mutation is simple class with very simple functionality. It just
 * copies parents into childs.
 * 
 * @author Lukas Surin
 *
 */
public class DefaultTreeMutation extends Operator<TreeIndividual> {

	/** for serialization */
	private static final long serialVersionUID = -3461868874024321660L;
	public static final String initName = "dtM";

	@Override
	public void setOperatorProbability(double prob) {
	}

	@Override
	public double getOperatorProbability() {
		return 1;
	}

	@Override
	public void execute(IPopulation<TreeIndividual> parents,
			IPopulation<TreeIndividual> childs) {
		childs.deepCopy(parents);
	}

	@Override
	public String objectInfo() {
		return String.format("%s %s", initName, 1.0);
	}

}
