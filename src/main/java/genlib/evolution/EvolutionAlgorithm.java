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

public class EvolutionAlgorithm<T extends Individual> implements Runnable,
		Serializable {

	/** for serialization */
	private static final long serialVersionUID = -8654725926081507012L;
	private static final Logger LOG = Logger.getLogger(EvolutionAlgorithm.class
			.getName());
	private IPopulation<T> actualPopulation;
	private int numberOfGenerations;
	private int fitNumOfThreads = 1;
	private int fitBlockSize = 1;
	// it's not used, but can be in future
	@SuppressWarnings("unused")
	private int operNumOfThreads;
	private double elitism;

	private Data data;
	private FitnessComparator<T> fitComp;
	private ArrayList<FitnessFunction<T>> fitFunctions;
	private ArrayList<Selector> selectors, envSelectors;
	private ArrayList<Operator<T>> crossOperators, mutationOperators;

	public EvolutionAlgorithm(Data data, IPopulation<T> population) {
		Config c = Config.getInstance();
		this.data = data;
		this.actualPopulation = population;
		this.fitNumOfThreads = c.getFitNumOfThreads();
		this.operNumOfThreads = c.getOperNumOfThreads();
	}

	public EvolutionAlgorithm(Data data, int numberOfGenerations) {
		Config c = Config.getInstance();
		this.data = data;
		this.numberOfGenerations = numberOfGenerations;
		this.fitNumOfThreads = c.getFitNumOfThreads();
		this.operNumOfThreads = c.getOperNumOfThreads();
	}

	public EvolutionAlgorithm(Data data, IPopulation<T> population,
			int numberOfGenerations) {
		Config c = Config.getInstance();
		this.data = data;
		this.actualPopulation = population;
		this.numberOfGenerations = numberOfGenerations;
		this.fitNumOfThreads = c.getFitNumOfThreads();
		this.operNumOfThreads = c.getOperNumOfThreads();
	}

	public Data getData() {
		return data;
	}

	public int getNumberOfGenerations() {
		return numberOfGenerations;
	}

	public IPopulation<T> getActualPopulation() {
		return actualPopulation;
	}

	public void setCrossOperators(ArrayList<Operator<T>> crossOperators) {
		this.crossOperators = crossOperators;
	}

	public void addCrossOperator(Operator<T> operator) {
		if (crossOperators == null)
			crossOperators = new ArrayList<Operator<T>>();
		crossOperators.add(operator);
	}

	public void setMutationOperators(ArrayList<Operator<T>> mutationOperators) {
		this.mutationOperators = mutationOperators;
	}

	public void addMutationOperator(Operator<T> operator) {
		if (mutationOperators == null)
			mutationOperators = new ArrayList<Operator<T>>();
		mutationOperators.add(operator);
	}

	public void setSelectors(ArrayList<Selector> selectors) {
		this.selectors = selectors;
	}

	public void addSelector(Selector selector) {
		if (selectors == null)
			selectors = new ArrayList<Selector>();
		selectors.add(selector);
	}

	public void setEnvSelectors(ArrayList<Selector> envSelectors) {
		this.envSelectors = envSelectors;
	}

	public void addEnvSelector(Selector selector) {
		if (envSelectors == null)
			envSelectors = new ArrayList<Selector>();
		envSelectors.add(selector);
	}

	public void setFitnessFunctions(ArrayList<FitnessFunction<T>> fitFunctions) {
		this.fitFunctions = fitFunctions;
		for (FitnessFunction<T> function : fitFunctions) {
			function.setEvolutionAlgorithm(this);
		}
	}

	public void setInitialPopulation(Population<T> population) {
		this.actualPopulation = population;
	}

	public void setNumberOfGenerations(int numberOfGenerations) {
		this.numberOfGenerations = numberOfGenerations;
	}

	public void setElitism(double elite) {
		this.elitism = elite;
	}

	public void setFitnessComparator(FitnessComparator<T> fitComp) {
		this.fitComp = fitComp;
	}

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
