package genlib.evolution;

import genlib.classifier.popinit.PopulationInitializator;
import genlib.configurations.Config;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.operators.Operator;
import genlib.evolution.selectors.Selector;
import genlib.locales.TextResource;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvolutionAlgorithm<T extends Individual> {
	private static final Logger LOG = Logger.getLogger(EvolutionAlgorithm.class.getName());
	private Population<T> population;	
	private int numberOfGenerations;
	private int fitNumOfThreads;
	private int operNumOfThreads;
	private double elitism;
	
	private PopulationInitializator<T> popInit;
	private FitnessComparator<T> fitComp;
	private ArrayList<FitnessFunction<T>> fitFunctions;
	private ArrayList<Selector> selectors, envSelectors;
	private ArrayList<Operator<T>> crossOperators, mutationOperators;

	public EvolutionAlgorithm(Population<T> population) {
		Config c = Config.getInstance();
		this.population = population;
		this.fitNumOfThreads = c.getFitNumOfThreads();
		this.operNumOfThreads = c.getOperNumOfThreads();
	}

	public EvolutionAlgorithm(int numberOfGenerations) {
		Config c = Config.getInstance();
		this.numberOfGenerations = numberOfGenerations;
		this.fitNumOfThreads = c.getFitNumOfThreads();
		this.operNumOfThreads = c.getOperNumOfThreads();
	}

	public EvolutionAlgorithm(Population<T> population, int numberOfGenerations) {
		Config c = Config.getInstance();
		this.population = population;
		this.numberOfGenerations = numberOfGenerations;
		this.fitNumOfThreads = c.getFitNumOfThreads();
		this.operNumOfThreads = c.getOperNumOfThreads();
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
		this.population = population;
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
	
	public void run() throws Exception {
		if (fitComp == null || mutationOperators == null
				|| crossOperators == null || selectors == null) { 
			LOG.log(Level.SEVERE, TextResource.getString("eNullInputEvolution"));
			throw new NullPointerException(TextResource.getString("eNullInputEvolution"));
		}
		
		// setting up population field
		population.setFitnessComparator(fitComp);
		fitComp.setFitFuncs(fitFunctions);
		population.computeFitness(fitNumOfThreads);

		// phase of mate selection
		Population<T> mates = selectionPhase();		
		// phase of operators evaluation
		Population<T> offspring = operatorPhaseMates(mates);
		offspring.computeFitness(fitNumOfThreads);
		Population<T> elite = elitePhase();
		
	}

	/**
	 * 
	 * @return
	 */
	private Population<T> selectionPhase() {
		Population<T> mates = new Population<>();
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
				Population<T> toFill = selectors.get(selectors.size() - 1).select(
						population, missing);
				mates.addAll(toFill);
			}
		} else {
			mates = new Population<>(population);
			mates.resample();
		}
		
		return mates;
	}
	
	/**
	 * 
	 * @param mates
	 * @return
	 */
	private Population<T> operatorPhaseMates(Population<T> mates) {
		Population<T> offspring = new Population<>();
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
	private Population<T> operatorPhaseWithChilds(Population<T> mates) {
		Population<T> offspring = null;
		for (Operator<T> o : crossOperators) {
			offspring = new Population<T>();
			o.execute(mates, offspring);
			mates = offspring;
		}
		
		for (Operator<T> o : mutationOperators) {
			offspring = new Population<T>();
			o.execute(mates, offspring);
			mates = offspring;
		}
		
		return offspring;
	}
	
	/**
	 * 
	 * @return
	 */
	private Population<T> elitePhase() {
		Population<T> elite = new Population<>();
		for (int i = 0; i < elitism * population.getPopulationSize(); i++) {
			elite.add(population.getIndividual(i));
		}
		return elite;
	}
}
