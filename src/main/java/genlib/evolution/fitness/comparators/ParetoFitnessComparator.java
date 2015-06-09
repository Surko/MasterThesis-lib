package genlib.evolution.fitness.comparators;

import java.util.logging.Logger;

import genlib.evolution.individuals.Individual;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;

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

	@Override
	public int compare(T o1, T o2) {
		throw new UnsupportedOperationException(String.format(
				TextResource.getString(TextKeys.eNotImplemented), "pareto comparator"));
	}

}