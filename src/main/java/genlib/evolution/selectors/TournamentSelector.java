package genlib.evolution.selectors;

import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.population.IPopulation;

import java.util.ArrayList;
import java.util.Random;

public class TournamentSelector implements Selector {
	/** for serialization */
	private static final long serialVersionUID = 2541161181988190800L;

	public static final String initName = "Tmt";

	private Random rng;

	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin,
			FitnessComparator<T> comp, int count) {
		return select(origin, null, comp, count);
	}
	
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin,
			ArrayList<T> dest, FitnessComparator<T> comp, int count) {
		if (dest == null) {
			dest = new ArrayList<>();
		}

		for (int i = 0; i < count; i++) {
			int i1 = rng.nextInt(origin.size());
			int i2 = rng.nextInt(origin.size());

			T ind1 = origin.get(i1);
			T ind2 = origin.get(i2);

			if (comp.compare(ind1, ind2) == -1 && rng.nextDouble() < 0.8) {
				if (rng.nextDouble() < 0.8) {
					dest.add(ind1);
				} else {
					dest.add(ind2);
				}
			} else {
				if (rng.nextDouble() < 0.8) {
					dest.add(ind2);
				} else {
					dest.add(ind1);					
				}				
			}
		}

		return dest;
	}

	public <T extends Individual> IPopulation<T> select(IPopulation<T> origin,
			int count) {
		return select(origin, null, count);
	}
	
	public <T extends Individual> IPopulation<T> select(IPopulation<T> origin,
			IPopulation<T> dest, int count) {
		if (dest == null) {
			dest = origin.createNewInstance();
			dest.setFitnessComparator(origin.getFitnessComparator());
		}

		for (int i = 0; i < count; i++) {
			int i1 = rng.nextInt(origin.getActualPopSize());
			int i2 = rng.nextInt(origin.getActualPopSize());

			T ind1 = origin.getIndividual(i1);
			T ind2 = origin.getIndividual(i2);

			if (origin.getFitnessComparator().compare(ind1, ind2) == -1) {
				if (rng.nextDouble() < 0.8) {
					dest.add(ind1);
				} else {
					dest.add(ind2);
				}
			} else {
				if (rng.nextDouble() < 0.8) {
					dest.add(ind2);
				} else {
					dest.add(ind1);					
				}				
			}
		}

		return dest;
	}

	public void setRandomGenerator(Random random) {
		this.rng = random;
	}

	public void setParam(String s) {
	}
}
