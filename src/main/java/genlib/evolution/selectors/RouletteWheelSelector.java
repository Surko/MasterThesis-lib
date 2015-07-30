package genlib.evolution.selectors;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.population.IPopulation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class which implements selector interface and selects individuals as it's
 * defined by roulette wheel selector (individuals are selected proportionally
 * to their fitness)
 * 
 * @author Lukas Surin
 *
 */
public class RouletteWheelSelector implements Selector {
	/** for serialization */
	private static final long serialVersionUID = -3268012087543397623L;
	/** name of selector */
	public static final String initName = "Rw";
	/** random object */
	private Random rng;
	/** fitness index which we use to create "wheel" */
	private int fitnessIndex = 0;

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
	@SuppressWarnings("unchecked")
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin,
			ArrayList<T> dest, FitnessComparator<T> comp, int count) {
		if (dest == null) {
			dest = new ArrayList<>();
		}

		int length = origin.size();

		double fitSum = 0d;
		double[] fitnesses = new double[length];

		if (fitnessIndex < 0) {
			// full comparator fitness. Pareto & Priority returns ones.
			for (int i = 0; i < length; i++) {
				fitnesses[i] = comp.value(origin.get(i));
				if (fitnesses[i] == 0) {
					fitnesses[i] = 1;
				}
				fitSum += fitnesses[i];
			}
		} else {
			// fitness with specific index
			FitnessFunction<T> function = comp.getFitnessFuncs().get(
					fitnessIndex);
			int individualFitIndex = function.getIndex();

			for (int i = 0; i < length; i++) {
				fitnesses[i] = origin.get(i)
						.getFitnessValue(individualFitIndex);
				if (fitnesses[i] == 0) {
					fitnesses[i] = 1;
				}
				fitSum += fitnesses[i];
			}
		}

		for (int i = 0; i < length; i++) {
			fitnesses[i] /= fitSum;
		}

		for (int i = 0; i < count; i++) {
			double roulette = rng.nextDouble();
			fitSum = 0;

			for (int j = 0; j < fitnesses.length; j++) {
				fitSum += fitnesses[j];
				if (fitSum > roulette) {
					dest.add((T) origin.get(j).copy());
					break;
				}
			}
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

		FitnessFunction<T> function = origin.getFitnessComparator()
				.getFitnessFuncs().get(fitnessIndex);
		int individualFitIndex = function.getIndex();
		int length = origin.getActualPopSize();

		double fitSum = 0d;
		double[] fitnesses = new double[length];

		for (int i = 0; i < length; i++) {
			fitnesses[i] = origin.getIndividual(i).getFitnessValue(
					individualFitIndex);
			if (fitnesses[i] == 0) {
				fitnesses[i] = 1;
			}
			fitSum += fitnesses[i];
		}

		for (int i = 0; i < length; i++) {
			fitnesses[i] /= fitSum;
		}

		for (int i = 0; i < count; i++) {
			double roulette = rng.nextDouble();
			fitSum = 0;

			for (int j = 0; j < fitnesses.length; j++) {
				fitSum += fitnesses[j];
				if (fitSum > roulette) {
					dest.add(origin.getIndividual(j));
					break;
				}
			}
		}

		return dest;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRandomGenerator(Random random) {
		this.rng = random;
	}

	
	public void setParam(String s) {
		this.fitnessIndex = Integer.parseInt(s);
	}
}
