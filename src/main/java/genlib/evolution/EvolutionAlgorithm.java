package genlib.evolution;

import genlib.classifier.popinit.PopulationInitializator;
import genlib.configurations.Config;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.operators.Operator;
import genlib.evolution.selectors.Selector;
import genlib.exceptions.EmptyConfigParamException;
import genlib.locales.TextResource;
import genlib.structures.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvolutionAlgorithm<T extends Individual> implements Runnable, Serializable {
	
	/** for serialization */
	private static final long serialVersionUID = -8654725926081507012L;
	private static final Logger LOG = Logger.getLogger(EvolutionAlgorithm.class.getName());
	private Population<T> actualPopulation;		
	private int numberOfGenerations;
	private int fitNumOfThreads;
	private int fitBlockSize = 1;
	// it's not used, but can be in future
	@SuppressWarnings("unused")
	private int operNumOfThreads;
	private double elitism;
	
	private Data data;
	private PopulationInitializator<T> popInit;
	private FitnessComparator<T> fitComp;
	private ArrayList<FitnessFunction<T>> fitFunctions;
	private ArrayList<Selector> selectors, envSelectors;
	private ArrayList<Operator<T>> crossOperators, mutationOperators;

	public EvolutionAlgorithm(Data data, Population<T> population) {
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

	public EvolutionAlgorithm(Data data, Population<T> population, int numberOfGenerations) {
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
	
	public void setCrossOperators(ArrayList<Operator<T>> crossOperators) {
		this.crossOperators = crossOperators;
		for (Operator<T> operator : crossOperators) {
			operator.setEvolutionAlgorithm(this);
		}
	}

	public void addCrossOperator(Operator<T> operator) {
		if (crossOperators == null)
			crossOperators = new ArrayList<Operator<T>>();
		operator.setEvolutionAlgorithm(this);
		crossOperators.add(operator);
	}

	public void setMutationOperators(ArrayList<Operator<T>> mutationOperators) {
		this.mutationOperators = mutationOperators;
		for (Operator<T> operator : mutationOperators) {
			operator.setEvolutionAlgorithm(this);
		}
	}

	public void addMutationOperator(Operator<T> operator) {
		if (mutationOperators == null)
			mutationOperators = new ArrayList<Operator<T>>();
		operator.setEvolutionAlgorithm(this);		
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

	public void setPopInit(PopulationInitializator<T> popInit) {
		this.popInit = popInit;
	}

	public PopulationInitializator<T> getPopInit() {
		return popInit;
	}

	@Override
	public void run() {
		if (fitComp == null || mutationOperators == null
				|| crossOperators == null || selectors == null) { 
			LOG.log(Level.SEVERE, TextResource.getString("eNullInputEvolution"));
			throw new NullPointerException(TextResource.getString("eNullInputEvolution"));
		}

		// setting up population field
		actualPopulation.setFitnessComparator(fitComp);
		// setting up fitness functions for this run
		fitComp.setFitFuncs(fitFunctions);
		// first computation of fitness for actual population
		actualPopulation.computeFitness(fitNumOfThreads, fitBlockSize);

		for (int i = 0; i < numberOfGenerations; i++) {
			// LOGGING OR OTHER ADDITIONAL METHODS CAN BE ADDED IF WE CARE
			evolve();
			// TODO CONTROL OF BEST INDIVIDUAL, ETC...

		}

	}

	protected void evolve() {		
		// phase of mate selection
		Population<T> selected = selectionPhase(actualPopulation);		
		// phase of operators evaluation
		Population<T> offspring = operatorPhaseMates(selected);		
		// computation of offspring fitness
		offspring.computeFitness(fitNumOfThreads, fitBlockSize);
		// elite phase - choosing elite individuals from original population
		Population<T> finalPopulation = elitePhase();
		// environmental selection phase from offspring to final population
		envSelectionPhase(offspring, finalPopulation);		

		// change actual population with newly created generation.
		actualPopulation = finalPopulation;
	}

	/* SELECTIONPHASE METHODS*/

	@SuppressWarnings("unused")
	private Population<T> selectionPhase() {
		return selectionPhase(actualPopulation);
	}

	/**
	 * 
	 * @return
	 */
	private Population<T> selectionPhase(Population<T> population) {
		Population<T> mates = new Population<>();
		mates.setFitnessComparator(population.getFitnessComparator());
		if (selectors.size() > 0) {
			int selSize = selectors.size();
			int toSel = population.getPopulationSize() / selSize;

			for (int i = 0; i < selSize; i++) {				
				Population<T> toAdd = selectors.get(i).select(
						population, toSel);
				mates.addAll(toAdd);
			}

			// consider the case when selectors did not create enough mates => adding the rest.
			int missing = population.getPopulationSize() - mates.getPopulationSize();

			if (missing > 0) {
				Population<T> toFill = selectors.get(selSize - 1).select(
						population, missing);
				mates.addAll(toFill);
			}
		} else {
			mates = new Population<>(population);
			mates.resample();
		}

		return mates;
	}

	/* OPERATORPHASE METHODS*/

	/**
	 * 
	 * @param mates
	 * @return
	 */
	private Population<T> operatorPhaseMates(Population<T> mates) {
		return operatorPhaseMates(mates,null);
	}

	private Population<T> operatorPhaseMates(Population<T> mates, Population<T> offspring) {
		if (offspring == null) {
			offspring = new Population<>();
		}		
		
		offspring.setFitnessComparator(mates.getFitnessComparator());
		
		for (Operator<T> o : crossOperators) {			
			o.execute(mates, offspring);			
		}

		for (Operator<T> o : mutationOperators) {
			o.execute(mates, offspring);
		}

		return offspring;
	}

	/**
	 * 
	 * @param mates
	 * @return
	 */
	@SuppressWarnings("unused")
	private Population<T> operatorPhaseWithChilds(Population<T> mates) {				
		Population<T> offspring = null;
		for (Operator<T> o : crossOperators) {
			offspring = new Population<T>();
			offspring.setFitnessComparator(mates.getFitnessComparator());
			o.execute(mates, offspring);
			mates = offspring;
		}

		for (Operator<T> o : mutationOperators) {
			offspring = new Population<T>();
			offspring.setFitnessComparator(mates.getFitnessComparator());
			o.execute(mates, offspring);
			mates = offspring;
		}

		return offspring;
	}

	/* ELITEPHASE METHODS*/ 

	/**
	 * 
	 * @return
	 */
	private Population<T> elitePhase() {
		return elitePhase(actualPopulation, null);
	}

	/**
	 * 
	 * @return
	 */
	private Population<T> elitePhase(Population<T> population, Population<T> elite) {
		if (elite == null) {
			elite = new Population<>();
		}
		
		elite.setFitnessComparator(population.getFitnessComparator());
		
		for (int i = 0; i < elitism * population.getPopulationSize(); i++) {
			elite.add(population.getIndividual(i));
		}
		return elite;
	}

	/* ENVSELECTIONPHASE METHODS */

	/**
	 * Environmental selection phase method without parameters. This one chooses/selects new individuals from
	 * actualPopulation. It does not fill existing population (that's why there's null parameter
	 * inside called method) so it has to create new one which is returned.
	 * @return Newly created population from selected individuals
	 */
	@SuppressWarnings("unused")
	private Population<T> envSelectionPhase() {
		return envSelectionPhase(actualPopulation, null);
	}

	/**
	 * Environmental selection phase method with one provided parameter. It chooses/selects new individuals from
	 * this parameter. It does not fill existing population (that's why there's null parameter
	 * inside called method) so it has to create new one which is returned.
	 * @param population Parameter from which we select new individuals
	 * @return Newly created population from selected individuals
	 */
	@SuppressWarnings("unused")
	private Population<T> envSelectionPhase(Population<T> population) {
		return envSelectionPhase(population, null);
	}

	/**
	 * Environmental selection phase method with two provided parameters. It chooses/selects new individuals from
	 * first parameter. If the second population parameter is not null then it does fill it
	 * and returns. Returning is here just for the sake of other sibling methods envSelectionPhase which 
	 * use this feature.
	 * @param population Parameter from which we select new individuals
	 * @param envSelected Population parameter to which we add selected individuals
	 * @return Newly created or already existing population with selected individuals.
	 */
	private Population<T> envSelectionPhase(Population<T> population, Population<T> envSelected) {
		if (envSelected == null) {
			envSelected = new Population<>();
		}
		
		envSelected.setFitnessComparator(population.getFitnessComparator());

		if (envSelectors.size() > 0) {
			int envSize = envSelectors.size();
			int toSel = (actualPopulation.getPopulationSize() - envSelected.getPopulationSize()) / envSize;

			for (int i = 0; i < envSize; i++) {				
				Population<T> toAdd = selectors.get(i).select(
						population, toSel);
				envSelected.addAll(toAdd);				
			}

			// consider the case when selectors did not create enough mates => adding the rest.
			int missing = actualPopulation.getPopulationSize() - envSelected.getPopulationSize();

			if (missing > 0) {
				Population<T> toFill = envSelectors.get(envSize - 1).select(
						population, missing);
				envSelected.addAll(toFill);
			}		
		} else {
			// should not get in here
			throw new EmptyConfigParamException();
		}
		return envSelected;
	}
}
