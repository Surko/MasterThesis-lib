package genlib.evolution.fitness.comparators;

import genlib.evolution.individuals.Individual;

import java.util.logging.Logger;

public class PriorityFitnessComparator<T extends Individual> extends
		FitnessComparator<T> {
	/** Logger */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger
			.getLogger(PriorityFitnessComparator.class.getName());
	/** for serialization */
	private static final long serialVersionUID = -4763112985337101338L;

	/**
	 * {@inheritDoc} </p> This method returns 1 because priority can't be
	 * evaluated.
	 */
	public double value(T o) {
		// not defined for this type of comparator
		return 0d;
	}

	/**
	 * {@inheritDoc} </p> It compares fitnesses of individuals by their priority
	 * that is defined by its ordering in {@link FitnessComparator#fitFuncs}.
	 */
	@Override
	public int compare(T o1, T o2) {

		for (int i = 0; i < fitFuncs.size(); i++) {
			int comparison = Double.compare(fitFuncs.get(i).computeFitness(o1),
					fitFuncs.get(i).computeFitness(o2));
			if (comparison != 0) {
				return -comparison;
			}
		}

		return 0;
	}

}
