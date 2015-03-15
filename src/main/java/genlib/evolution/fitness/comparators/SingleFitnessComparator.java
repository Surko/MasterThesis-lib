package genlib.evolution.fitness.comparators;

import genlib.evolution.individuals.Individual;

public class SingleFitnessComparator<T extends Individual> extends FitnessComparator<T> {

	@Override
	public int compare(T o1, T o2) {
		// TODO - single comparison
		return 0;
	}
}
