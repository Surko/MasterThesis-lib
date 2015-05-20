package genlib.evolution.fitness.comparators;

import java.util.logging.Logger;

import genlib.evolution.individuals.Individual;

public class PriorityFitnessComparator<T extends Individual>  extends FitnessComparator<T> {
	/** Logger */
	private static final Logger LOG = Logger.getLogger(PriorityFitnessComparator.class.getName());
	/** for serialization */
	private static final long serialVersionUID = -4763112985337101338L;

	@Override
	public int compare(T o1, T o2) {
		return 0;
	}	
	
}
