package genlib.evolution.fitness.comparators;

import java.util.logging.Logger;

import genlib.evolution.individuals.Individual;

public class ParetoFitnessComparator<T extends Individual> extends FitnessComparator<T> {
	/** Logger */
	private static final Logger LOG = Logger.getLogger(ParetoFitnessComparator.class.getName());
	/** for serialization */
	private static final long serialVersionUID = -5365984778016129455L;

	@Override
	public int compare(T o1, T o2) {
		return 0;
	}
	
}
