package genlib.evolution.selectors;

import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.population.IPopulation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class which implements selector interface and selects individuals randomly.
 * 
 * @author Lukas Surin
 *
 */
public class RandomSelector implements Selector {
	/** for serialization */
	private static final long serialVersionUID = -3268012087543397623L;
	/** name of selector */
	public static final String initName = "random";
	/** random object */
	private Random rng;

	/**
	 * {@inheritDoc}
	 */
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin,
			FitnessComparator<T> comp, int count) {
		return select(origin, null, comp, count);
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	public <T extends Individual> IPopulation<T> select(IPopulation<T> origin,
			int count) {
		return select(origin, null, count);
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	public void setRandomGenerator(Random random) {
		this.rng = random;
	}

	/**
	 * This method is not used
	 */
	public void setParam(String s) {
	}
}
