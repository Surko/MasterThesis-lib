package genlib.evolution;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.Individual;
import genlib.evolution.operators.Operator;
import genlib.evolution.selectors.Selector;

import java.util.ArrayList;

public class EvolutionAlgorithm<T extends Individual> {	
	private Population<T> population;
	private int numberOfGenerations;
	private double elitism;
	
	private ArrayList<FitnessFunction<T>> fitFunctions;
	private Selector selector;
	private ArrayList<Operator<T>> crossOperators, mutationOperators;
	
	public EvolutionAlgorithm(Population<T> population) {
		this.population = population;
	}
	
	public EvolutionAlgorithm(int numberOfGenerations) {
		this.numberOfGenerations = numberOfGenerations;
	}	
	
	public EvolutionAlgorithm(Population<T> population, int numberOfGenerations) {
		this.population = population;
		this.numberOfGenerations = numberOfGenerations;
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
	
	public void addSelector(Selector selector) {
		this.selector = selector;
	}
	
	public void setFitnessFunctions(ArrayList<FitnessFunction<T>> fitFunctions) {
		this.fitFunctions = fitFunctions;
	}
	
	public void addFitnessFunction(FitnessFunction<T> fitFunction) {
		if (fitFunctions == null)
			fitFunctions = new ArrayList<FitnessFunction<T>>();
		this.fitFunctions.add(fitFunction);
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
	
	public void run() {
		
	}
}
