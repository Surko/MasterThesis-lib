package genlib.evolution.population;

import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.operators.Operator;
import genlib.evolution.selectors.Selector;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Interface that should be implmeneted by all types of population containers.
 * 
 * @author Lukas Surin
 *
 * @param <T>
 *            type of individual
 */
public interface IPopulation<T extends Individual> {

	/**
	 * Method creates new instance of population with the same type as return
	 * object.
	 */
	public <S extends Individual> IPopulation<S> makeNewInstance();

	/**
	 * Method creates new instance of population that has the same type.
	 * 
	 * @return IPopulation
	 */
	public IPopulation<T> createNewInstance();

	/**
	 * Method which updates population. Usually it's not used, but it can
	 * possibly remove old individuals of change the max population in some
	 * cases of variable population.
	 */
	public void update();

	/**
	 * Method clears the individuals from population
	 */
	public void clear();

	/**
	 * Method adds the individual into population
	 * 
	 * @param individual
	 */
	public void add(T individual);

	/**
	 * Method adds all the individuals from one population into this population.
	 * 
	 * @param population
	 *            that we adding from
	 */
	public void addAll(IPopulation<T> population);

	/**
	 * Method sets the individuals of actual population.
	 * 
	 * @param individuals
	 *            of the population
	 */
	public void setIndividuals(T[] individuals);

	/**
	 * Deep copy of population from parameter into this population.
	 * 
	 * @param population
	 *            Population from which we copy individuals
	 */
	public void deepCopy(IPopulation<T> population);

	/**
	 * Sets the fitness comparator for this population.
	 * 
	 * @param fitComp
	 *            fitness comparator
	 */
	public void setFitnessComparator(FitnessComparator<T> fitComp);

	/**
	 * Sets the maximum population size
	 * 
	 * @param maxPopSize
	 *            maximal population size
	 */
	public void setMaxPopulationSize(int maxPopSize);

	/**
	 * Method gets the fitness comparator of this population
	 * 
	 * @return FitnessComparator
	 */
	public FitnessComparator<T> getFitnessComparator();

	/**
	 * Method gets the individuals as a list.
	 * 
	 * @return individuals as arraylist
	 */
	public ArrayList<T> getIndividuals();

	/**
	 * Method gets the best individual in this population (evaluated by fitness
	 * comparator).
	 * 
	 * @return best individual
	 */
	public T getBestIndividual();

	/**
	 * Method gets the actual population size/
	 * 
	 * @return actual population size
	 */
	public int getActualPopSize();

	/**
	 * Method gets the maximum population size.
	 * 
	 * @return maximum population size
	 */
	public int getMaxPopSize();

	/**
	 * Method gets the individual from population at place with index.
	 * 
	 * @param index
	 *            of the individual
	 * @return individual at index
	 */
	public T getIndividual(int index);

	/**
	 * Method which resamples population individuals.
	 */
	public void resample();

	/**
	 * Method that is used to compute fitness for all of the individuals inside
	 * this population
	 * 
	 * @param nThreads
	 *            number of threads
	 * @param blockSize
	 *            number of individuals processed simultaneously by one thread
	 */
	public void computeFitness(final int nThreads, final int blockSize);

	/**
	 * Method that sort the individuals.
	 */
	public void sortIndividuals();

	/**
	 * Method gets the sorted individuals as a list.
	 * 
	 * @return sorted individuals as arraylist
	 */
	public ArrayList<T> getSortedIndividuals();

	/**
	 * Method gets the sorted individual (sorted with different comparator) as a
	 * list
	 * 
	 * @param comparator
	 *            different comparator used for sorting
	 * @return sorted individuals as a arraylist
	 */
	public ArrayList<T> getSortedIndividuals(Comparator<T> comparator);

	/**
	 * Selection phase that selects the individuals from the population
	 * 
	 * @param selectors
	 *            used to select individuals
	 * @return selected individuals
	 */
	public IPopulation<T> selectionPhase(ArrayList<Selector> selectors);

	/**
	 * Selection phase that selects the individuals from the population.
	 * Selected individuals are returned and inserted into parameter population
	 * 
	 * @param population
	 *            where we insert selected individuals
	 * @param selectors
	 *            used to select individuals
	 * @return selected individuals
	 */
	public IPopulation<T> selectionPhase(IPopulation<T> population,
			ArrayList<Selector> selectors);

	/**
	 * Operator phase that execute operators on individuals in this population.
	 * 
	 * @param crossOperators
	 *            crossover operators
	 * @param mutationOperators
	 *            mutation operators
	 * @return population of processed individuals
	 */
	public IPopulation<T> operatorPhaseMates(
			ArrayList<Operator<T>> crossOperators,
			ArrayList<Operator<T>> mutationOperators);

	/**
	 * Operator phase that execute operators on individuals in this population.
	 * Operated individuals are returned as parameter <i>offspring</i>
	 * 
	 * @param offspring
	 *            population with operated individuals
	 * @param crossOperators
	 *            crossover operators
	 * @param mutationOperators
	 *            mutation operators
	 * @return population of processed individuals
	 */
	public IPopulation<T> operatorPhaseMates(IPopulation<T> offspring,
			ArrayList<Operator<T>> crossOperators,
			ArrayList<Operator<T>> mutationOperators);

	// public Population<T> operatorPhaseWithChilds(
	// ArrayList<Operator<T>> crossOperators,
	// ArrayList<Operator<T>> mutationOperators);

	/**
	 * Elite phase that selects the <i>elitismRate</i> of the best individuals
	 * from this population.
	 * 
	 * @param elitismRate
	 *            rate of the best individuals to be selected
	 * @return selected individuals
	 */
	public IPopulation<T> elitePhase(double elitismRate);

	/**
	 * Elite phase that selects the <i>elitismRate</i> of the best individuals
	 * from this population. Selected individuals are returned as parameter
	 * <i>elite</i>
	 * 
	 * @param elitismRate
	 *            rate of the best individuals to be selected
	 * @return selected individuals
	 */
	public IPopulation<T> elitePhase(IPopulation<T> elite, double elitismRate);

	/**
	 * Environmental selection phase that environmentally selects the
	 * individuals from this population.
	 * 
	 * @param envSelectors
	 *            used to environmentally select individuals
	 * @return environmentally selected individuals
	 */
	public IPopulation<T> envSelectionPhase(ArrayList<Selector> envSelectors);

	/**
	 * Environmental selection phase that environmentally selects the
	 * individuals from this population. Selected individuals are returned as
	 * parameter <i>envSelected</i>
	 * 
	 * @param envSelected
	 *            selected individuals
	 * @param envSelectors
	 *            used to environmentally select individuals
	 * @return environmentally selected individuals
	 */
	public IPopulation<T> envSelectionPhase(IPopulation<T> envSelected,
			ArrayList<Selector> envSelectors);
}
