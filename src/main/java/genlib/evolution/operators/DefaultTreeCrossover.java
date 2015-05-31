package genlib.evolution.operators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;
import genlib.evolution.population.Population;

/**
 * Default Tree Crossover is simple class with very simple functionality. It
 * just copies parents into childs.
 * 
 * @author Lukas Surin
 *
 */
public class DefaultTreeCrossover extends Operator<TreeIndividual> {

	/** for serialization */
	private static final long serialVersionUID = -7572230936501803000L;
	public static final String initName = "dtX";

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
