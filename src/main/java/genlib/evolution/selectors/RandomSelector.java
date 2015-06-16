package genlib.evolution.selectors;

import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.population.IPopulation;

import java.util.ArrayList;
import java.util.Random;

public class RandomSelector implements Selector {
	/** for serialization */
	private static final long serialVersionUID = -3268012087543397623L;
	public static final String initName = "random";
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

		int size = origin.size();
		for (int i = 0; i < count; i++) {
			int index = rng.nextInt(size);
			dest.add(origin.get(index));
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

		int size = origin.getActualPopSize();
		for (int i = 0; i < count; i++) {
			int index = rng.nextInt(size);
			dest.add(origin.getIndividual(index));
		}

		return dest;
	}

	public void setRandomGenerator(Random random) {
		this.rng = random;
	}

	public void setParam(String s) {
	}
}
