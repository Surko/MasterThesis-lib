package genlib.evolution.operators;

import java.util.Random;

import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;
import genlib.locales.PermMessages;
import genlib.structures.Data;

/**
 * Default Tree Mutation is simple class with very simple functionality. It just
 * copies parents into childs.
 * 
 * @see Operator
 * @author Lukas Surin
 *
 */
public class DefaultTreeMutation implements Operator<TreeIndividual> {

	/** for serialization */
	private static final long serialVersionUID = -3461868874024321660L;
	/** name of this operator */
	public static final String initName = "dtM";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOperatorProbability(double prob) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getOperatorProbability() {
		return 1;
	}

	/**
	 * {@inheritDoc} </p> This default mutation will just leave the parents
	 * alone.
	 */
	@Override
	public void execute(IPopulation<TreeIndividual> parents,
			IPopulation<TreeIndividual> childs) {
		if (childs != null) {
			childs.deepCopy(parents);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWekaCompatible() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWekaDependent() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * </p>
	 * This operator does not set the Data object.
	 */
	@Override
	public void setData(Data data) {}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String objectInfo() {
		return String.format(PermMessages._fit_format, initName, 1.0);
	}

	/**
	 * {@inheritDoc}
	 * <\p>
	 * This operator does not set the Random object.
	 */
	@Override
	public void setRandomGenerator(Random random) {}

}
