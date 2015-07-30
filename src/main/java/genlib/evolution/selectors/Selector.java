package genlib.evolution.selectors;

import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.population.IPopulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Interface that should be implemented when creating new type of selector.
 * 
 * @author Lukas Surin
 *
 */
public interface Selector extends Serializable {

	/**
	 * Method that selects <i>count</i> individuals from <i>origin</i> list and
	 * using fitness comparator <i>comp</i> to evaluate individuals.
	 * 
	 * @param origin
	 *            list of individuals from which we select individuals
	 * @param comp
	 *            fitness comparator which we use to evaluate individuals
	 * @param count
	 *            number of individuals to select
	 * @return selected individuals in arraylist
	 */
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin,
			FitnessComparator<T> comp, int count);

	/**
	 * Method that selects <i>count</i> individuals from <i>origin</i> list and
	 * using fitness comparator <i>comp</i> to evaluate individuals. Selected
	 * individuals are inserted into <i>dest</i> list.
	 * 
	 * @param origin
	 *            list of individuals from which we select individuals
	 * @param dest
	 *            list of individuals to which we insert individuals
	 * @param comp
	 *            fitness comparator which we use to evaluate individuals
	 * @param count
	 *            number of individuals to select
	 * @return selected individuals in arraylist
	 */
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin,
			ArrayList<T> dest, FitnessComparator<T> comp, int count);

	/**
	 * Method that selects <i>count</i> individuals from <i>origin</i>
	 * population.
	 * 
	 * @param origin
	 *            population from which we select individuals
	 * @param count
	 *            number of individuals to select
	 * @return selected individuals
	 */
	public <T extends Individual> IPopulation<T> select(IPopulation<T> origin,
			int count);

	/**
	 * Method that selects <i>count</i> individuals from <i>origin</i>
	 * population. Selected individuals are inserted into <i>dest</i>
	 * population.
	 * 
	 * @param origin
	 *            population from which we select individuals
	 * @param count
	 *            number of individuals to select
	 * @return selected individuals
	 */
	public <T extends Individual> IPopulation<T> select(IPopulation<T> origin,
			IPopulation<T> dest, int count);

	/**
	 * Method that sets the random generator that can be used by selector.
	 * 
	 * @param random object
	 */
	public void setRandomGenerator(Random random);

	/**
	 * Method that is used to set the params of selector
	 * @param s text with parameters
	 */
	public void setParam(String s);
}
