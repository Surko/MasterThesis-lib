package genlib.evolution.fitness.comparators;

import genlib.evolution.individuals.Individual;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;

import java.util.logging.Logger;

/**
 * Not implemented fitness comparator that should
 * 
 * @author Lukas Surin
 *
 * @param <T>
 */
public class ParetoFitnessComparator<T extends Individual> extends
		FitnessComparator<T> {
	/** Logger */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger
			.getLogger(ParetoFitnessComparator.class.getName());
	/** for serialization */
	private static final long serialVersionUID = -5365984778016129455L;

	/**
	 * {@inheritDoc} </p> This method returns 1 because pareto can't be
	 * evaluated.
	 */
	public double value(T o) {
		return 0d;
	}

	/**
	 * {@inheritDoc} </p> This comparator is NOT implemented yet.
	 */
	@Override
	public int compare(T o1, T o2) {
		throw new UnsupportedOperationException(String.format(
				TextResource.getString(TextKeys.eNotImplemented),
				"pareto comparator"));
	}

}