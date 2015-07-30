package genlib.evolution.fitness;

import genlib.evolution.EvolutionAlgorithm;
import genlib.evolution.fitness.tree.TreeAccuracyFitness;
import genlib.evolution.fitness.tree.look.TreeHeightFitness;
import genlib.evolution.individuals.Individual;
import genlib.evolution.population.Population;
import genlib.structures.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Interface for fitness functions that defines basic to-be implemented methods
 * to compute fitness for Individual or individuals inside of a population.
 * </br> Different type of fitnesses are decided by index which fit to static
 * values in this class. You can look at particular examples of implemented
 * fitness functions
 * 
 * @author Lukas Surin
 * @see TreeAccuracyFitness
 * @see TreeHeightFitness
 *
 */
public abstract class FitnessFunction<T extends Individual> implements
		Serializable {

	/** for serialization */
	private static final long serialVersionUID = -90043146335682343L;

	/** How many fitness functions is already registered */
	public static int registeredFunctions = 0;
	/** instance of evolution algorithm for future use */
	protected EvolutionAlgorithm<T> ea;
	/** index of this function in individual */
	protected int index = -1;

	/**
	 * Method which should compute fitness for specific class.
	 * You can look at particular examples of implemented fitness functions and
	 * their compute fitness method. Usually this method contains some type of
	 * differentiation of which data we use (weka or built-in type). Other thing
	 * you can take into consideration is that individual saves his state of
	 * changing from last time, so it can be utilized for speeding up running
	 * time of computation.
	 * 
	 * @param individual
	 *            individual for which we compute fitness
	 * @return fitness value for an individual
	 * @see TreeAccuracyFitness#computeFitness(genlib.evolution.individuals.TreeIndividual)
	 * @see TreeHeightFitness#computeFitness(genlib.evolution.individuals.TreeIndividual)
	 */
	public abstract double computeFitness(T individual);

	/**
	 * Method that cycles through population and compute fitness for each one of
	 * the individuals.
	 * 
	 * @param population
	 *            for which we compute fitness
	 */
	public void computeFitness(Population<T> population) {
		for (T individual : population.getIndividuals())
			computeFitness(individual);
	}

	/**
	 * Method that cycles individuals from start to end and compute fitness
	 * their fitnesses.
	 * 
	 * @param population
	 *            individuals for which we compute fitness
	 * @param start
	 *            index
	 * @param end
	 *            index
	 */
	public void computeFitness(ArrayList<T> population, int start, int end) {
		end = Math.min(population.size(), end);
		for (int i = start; i < end; i++) {
			computeFitness(population.get(i));
		}
	}

	/**
	 * Method returns the individual class type (Individual.class,
	 * TreeIndividual.class).
	 * 
	 * @return individual class type
	 */
	public abstract Class<T> getIndividualClassType();

	/**
	 * Method sets the data to be used by fitness function
	 * @param data to be used 
	 */
	public abstract void setData(Data data);

	/**
	 * Method sets the instance of evolution algorithm. NOT USED IN ANY 
	 * OF THE FUNCTION BUT LEFT FOR FUTURE VERSIONS.
	 * @param ea
	 */
	public void setEvolutionAlgorithm(EvolutionAlgorithm<T> ea) {
		this.ea = ea;
	}

	/**
	 * Get the index (used in individuals) of this function. It should be unique
	 * 
	 * @return index of this function
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Set the index (used in individuals) of this function. It should be unique
	 * 
	 * @param index
	 *            to be set to this function (unique).
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Method which should return true if this fitness function can handle numeric
	 * class atributes.
	 * 
	 * @return true iff fitness function can handle numeric class attribute
	 */
	public abstract boolean canHandleNumeric();

	/**
	 * This method should set the parameters for function if it's needed. A lot
	 * of functions doesn't need it so they could leave it empty.
	 * 
	 * @param param
	 *            parameter from which will be set additional parameters for
	 *            this function
	 */
	public abstract void setParam(String param);

	/**
	 * Method which should return String object with info about this instance.
	 * (name of function with additional parameters)
	 * 
	 * @return info about fitness function
	 */
	public abstract String objectInfo();
}
