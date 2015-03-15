package genlib.evolution.fitness.comparators;

import genlib.evolution.individuals.Individual;

public class ParetoFitnessComparator<T extends Individual> extends FitnessComparator<T> {
	
	@Override
	public int compare(T o1, T o2) {
		return 0;
	}
	
}
