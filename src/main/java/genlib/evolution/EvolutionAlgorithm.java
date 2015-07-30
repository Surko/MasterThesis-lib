package genlib.evolution;

import genlib.configurations.Config;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.operators.Operator;
import genlib.evolution.population.IPopulation;
import genlib.evolution.population.Population;
import genlib.evolution.selectors.Selector;
import genlib.locales.TextResource;
import genlib.structures.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class that represents EvolutionAlgorithm for specific type of
 * Individual. It contains methods to run algorithm and evolve actualPopulation.
 * Behavior of operators, selectors, etc are specific for each one of the
 * Population container.
 * 
 * @author Lukas Surin
 *
 * @param <T>
 *            individual type
 */
public class EvolutionAlgorithm<T extends Individual> implements Runnable,
		Serializable {

	/** for serialization */
	private static final long serialVersionUID = -8654725926081507012L;
	/** logger */
	private static final Logger LOG = Logger.getLogger(EvolutionAlgorithm.class
			.getName());
	/** actual population */
	private IPopulation<T> actualPopulation;
	/** max number of generations */
	private int numberOfGenerations;
	/** number of threads used to compute fitness */
	private int fitNumOfThreads = 1;
	/** blocks of individuals that one thread will process */
	private int fitBlockSize = 1;
	// it's not used, but can be in future
	@SuppressWarnings("unused")
	private int operNumOfThreads;
	/** elitism rate */
	private double elitism;

	/** data object */
	private Data data;
	/** fitness comparator */
	private FitnessComparator<T> fitComp;
	/** fitness functions */
	private ArrayList<FitnessFunction<T>> fitFunctions;
	/** selectors */
	private ArrayList<Selector> selectors, envSelectors;
	/** operators */
	private ArrayList<Operator<T>> crossOperators, mutationOperators;

	/**
	 * Constructor that creates EvolutionAlgorithm with initialized data and
	 * starting population.
	 * 
	 * @param data
	 *            with instances
	 * @param population
	 *            starting population
	 */
	public EvolutionAlgorithm(Data data, IPopulation<T> population) {
		Config c = Config.getInstance();
		this.data = data;
		this.actualPopulation = population;
		this.fitNumOfThreads = c.getFitNumOfThreads();
		this.operNumOfThreads = c.getOperNumOfThreads();
	}

	/**
	 * Constructor that creates EvolutionAlgorithm with initialized data and
	 * number of generations.
	 * 
	 * @param data
	 *            with instances
	 * @param numberOfGenerations
	 */
	public EvolutionAlgorithm(Data data, int numberOfGenerations) {
		Config c = Config.getInstance();
		this.data = data;
		this.numberOfGenerations = numberOfGenerations;
		this.fitNumOfThreads = c.getFitNumOfThreads();
		this.operNumOfThreads = c.getOperNumOfThreads();
	}

	/**
	 * Constructor that creates EvolutionAlgorithm with initialized data,
	 * starting population and maximal number of generations.
	 * 
	 * @param data
	 *            with instances
	 * @param population
	 *            starting population
	 * @param numberOfGenerations
	 */
	public EvolutionAlgorithm(Data data, IPopulation<T> population,
			int numberOfGenerations) {
		Config c = Config.getInstance();
		this.data = data;
		this.actualPopulation = population;
		this.numberOfGenerations = numberOfGenerations;
		this.fitNumOfThreads = c.getFitNumOfThreads();
		this.operNumOfThreads = c.getOperNumOfThreads();
	}

	/**
	 * Gets the data.
	 * 
	 * @return data object
	 */
	public Data getData() {
		return data;
	}

	/**
	 * Gets the maximal number of generations.
	 * 
	 * @return
	 */
	public int getNumberOfGenerations() {
		return numberOfGenerations;
	}

	/**
	 * Gets the actual evolved population of this algorithm.
	 * 
	 * @return actual population
	 */
	public IPopulation<T> getActualPopulation() {
		return actualPopulation;
	}

	/**
	 * Method sets the crossover operators.
	 * 
	 * @param crossOperators
	 *            used in this algorithm
	 */
	public void setCrossOperators(ArrayList<Operator<T>> crossOperators) {
		this.crossOperators = crossOperators;
	}

	/**
	 * Method adds new crossover operator.
	 * 
	 * @param operator
	 *            new crossover operator
	 */
	public void addCrossOperator(Operator<T> operator) {
		if (crossOperators == null)
			crossOperators = new ArrayList<Operator<T>>();
		crossOperators.add(operator);
	}

	/**
	 * Method sets the mutation operators.
	 * 
	 * @param mutationOperators
	 *            used in this algorithm
	 */
	public void setMutationOperators(ArrayList<Operator<T>> mutationOperators) {
		this.mutationOperators = mutationOperators;
	}

	/**
	 * Method adds new mutation operator.
	 * 
	 * @param operator
	 *            new mutation operator
	 */
	public void addMutationOperator(Operator<T> operator) {
		if (mutationOperators == null)
			mutationOperators = new ArrayList<Operator<T>>();
		mutationOperators.add(operator);
	}

	/**
	 * Method sets the selectors (mating).
	 * 
	 * @param selectors
	 *            used in this algorithm
	 */
	public void setSelectors(ArrayList<Selector> selectors) {
		this.selectors = selectors;
	}

	/**
	 * Method adds new selector (mating).
	 * 
	 * @param operator
	 *            new selector
	 */
	public void addSelector(Selector selector) {
		if (selectors == null)
			selectors = new ArrayList<Selector>();
		selectors.add(selector);
	}

	/**
	 * Method sets the environmental selectors.
	 * 
	 * @param envSelectors
	 *            used in this algorithm
	 */
	public void setEnvSelectors(ArrayList<Selector> envSelectors) {
		this.envSelectors = envSelectors;
	}

	/**
	 * Method adds new environmental selector.
	 * 
	 * @param operator
	 *            new selector
	 */
	public void addEnvSelector(Selector selector) {
		if (envSelectors == null)
			envSelectors = new ArrayList<Selector>();
		envSelectors.add(selector);
	}

	/**
	 * Method sets the fitness functions.
	 * 
	 * @param fitFunctions
	 *            used in this algorithm
	 */
	public void setFitnessFunctions(ArrayList<FitnessFunction<T>> fitFunctions) {
		this.fitFunctions = fitFunctions;
		for (FitnessFunction<T> function : fitFunctions) {
			function.setEvolutionAlgorithm(this);
		}
	}

	/**
	 * Method sets the starting population that is further evolved.
	 * 
	 * @param population
	 *            startin population
	 */
	public void setInitialPopulation(Population<T> population) {
		this.actualPopulation = population;
	}

	/**
	 * Method sets maximal number of generations that the algorithm will run
	 * 
	 * @param numberOfGenerations
	 *            maximal
	 */
	public void setNumberOfGenerations(int numberOfGenerations) {
		this.numberOfGenerations = numberOfGenerations;
	}

	/**
	 * Method sets the elitism rate of this run.
	 * 
	 * @param elite
	 *            elitism rate
	 */
	public void setElitism(double elite) {
		this.elitism = elite;
	}

	/**
	 * Method sets the fitness comparator.
	 * 
	 * @param fitComp
	 *            fitness comparator
	 */
	public void setFitnessComparator(FitnessComparator<T> fitComp) {
		this.fitComp = fitComp;
	}

	/**
	 * Run method that start the evolution algorithm. It has the main cycle for
	 * number of generations. There we call the method {@link #evolve()}.
	 */
	@Override
	public void run() {
		if (fitComp == null || mutationOperators == null
				|| crossOperators == null || envSelectors == null
				|| fitFunctions == null) {
			LOG.log(Level.SEVERE, TextResource.getString("eNullInputEvolution"));
			throw new NullPointerException(
					TextResource.getString("eNullInputEvolution"));
		}

		// setting up population field
		actualPopulation.setFitnessComparator(fitComp);
		// setting up fitness functions for this run
		fitComp.setFitFuncs(fitFunctions);
		// first computation of fitness for actual population
		actualPopulation.computeFitness(fitNumOfThreads, fitBlockSize);

		for (int i = 0; i < numberOfGenerations; i++) {
			// LOGGING OR OTHER ADDITIONAL METHODS CAN BE ADDED IF WE CARE
			actualPopulation.sortIndividuals();
			evolve();
			// TODO CONTROL OF BEST INDIVIDUAL, ETC...

		}

	}

	/**
	 * Main evolving method that selects individuals, execute operators on them,
	 * take the elite and selects the best with environmental selection. It
	 * calls update method on population in each step.
	 */
	protected void evolve() {
		// phase of mate selection
		IPopulation<T> selected = actualPopulation.selectionPhase(selectors);
		// phase of operators evaluation
		IPopulation<T> offspring = selected.operatorPhaseMates(crossOperators,
				mutationOperators);
		// computation of offspring fitness
		offspring.computeFitness(fitNumOfThreads, fitBlockSize);
		// elite phase - choosing elite individuals from original population
		IPopulation<T> finalPopulation = actualPopulation.elitePhase(elitism);
		// environmental selection phase from offspring to final population
		offspring.envSelectionPhase(finalPopulation, envSelectors);

		// change actual population with newly created generation.
		actualPopulation = finalPopulation;

		// update of population, can be decreasing of max size or other
		// types of variable size of population GAVaPS, PRoFIGA
		actualPopulation.update();
	}

}
