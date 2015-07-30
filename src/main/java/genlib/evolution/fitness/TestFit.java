package genlib.evolution.fitness;

import genlib.evolution.individuals.Individual;
import genlib.locales.PermMessages;
import genlib.structures.Data;

/**
 * Class that is used for testing purposes.
 * 
 * @author Lukas Surin
 *
 */
@SuppressWarnings("rawtypes")
public class TestFit extends FitnessFunction {
	/** for serialization */
	private static final long serialVersionUID = -7959825267949047077L;
	/** name of this function */
	public static final String initName = "tTest";

	/**
	 * Computation consists of just returning complex fitness of tree
	 * individual.
	 */
	@Override
	public double computeFitness(Individual individual) {
		return individual.getComplexFitness();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setData(Data data) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<Individual> getIndividualClassType() {
		return Individual.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParam(String param) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String objectInfo() {
		return String.format(PermMessages._fit_format, initName,
				PermMessages._blank_param);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canHandleNumeric() {
		return true;
	}

}
