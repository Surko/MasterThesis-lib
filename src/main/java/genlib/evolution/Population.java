package genlib.evolution;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.utils.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Population<T extends Individual> {

	private FitnessComparator<T> comparator;
	private ArrayList<T> individuals;
	private int popSize;
	private Random randomGen;

	public Population() {
		this.individuals = new ArrayList<>();
		this.popSize = 0;
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}

	public Population(Population<T> population) {
		this.comparator = population.comparator;
		this.popSize = population.popSize;
		this.individuals = new ArrayList<>(population.individuals);
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}

	public Population(ArrayList<T> individuals) {
		this.individuals = individuals;
		this.popSize = individuals.size();
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}
	
	public Population(ArrayList<T> individuals, int popSize) {
		this.individuals = individuals;
		this.popSize = popSize;
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}

	public Population(T[] individuals, int popSize) {
		this.individuals = new ArrayList<>(Arrays.asList(individuals));
		this.popSize = popSize;
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}

	public void clear() {
		this.popSize = 0;
		this.individuals.clear();
	}
	
	public void add(T individual) {		
		individuals.add(individual);
		popSize++;
	}
	
	public void addAll(Population<T> population) {
		individuals.addAll(population.getIndividuals());
		popSize = individuals.size();
	}

	public void resample() {
		Collections.shuffle(individuals, randomGen);
	}

	/**
	 * Sort individuals inside this object.
	 */
	public void sortIndividuals() {
		// TODO WE ARE AFTER DESCDENDING ORDER INSTEAD OF ASCENDING.
		Collections.sort(individuals, comparator);
	}

	/**
	 * Getting sorted individuals without change of placing in the object field
	 * individuals. Sorting is done by default comparator provided by
	 * individuals.
	 * 
	 * @return Sorted individuals
	 */
	public ArrayList<T> getSortedIndividuals() {
		ArrayList<T> sorted = new ArrayList<>(individuals);
		Collections.sort(sorted, comparator);
		return sorted;
	}

	/**
	 * Getting sorted individuals without change of placing in the object field
	 * individuals. Sorting is done by comparator provided by parameter
	 * comparator
	 * 
	 * @param comparator
	 *            Comparator that sorts individuals
	 * @return Sorted individuals
	 */
	public ArrayList<T> getSortedIndividuals(Comparator<T> comparator) {
		ArrayList<T> sorted = new ArrayList<>(individuals);
		Collections.sort(sorted, comparator);
		return sorted;
	}

	public void computeFitness(int nThreads) {
		if (nThreads > 1) {
			ExecutorService es = Executors.newFixedThreadPool(nThreads);
			for (final FitnessFunction<T> function : comparator
					.getFitnessFuncs()) {
				for (final T individual : individuals) {
					es.submit(new Runnable() {
						@Override
						public void run() {
							function.computeFitness(individual);
						}
					});
				}
			}

			es.shutdown();
		} else {
			for (FitnessFunction<T> function : comparator.getFitnessFuncs()) {
				function.computeFitness(this);
			}
		}
	}

	public T getIndividual(int index) {
		return individuals.get(index);
	}
	
	public T getBestIndividual() {
		return getSortedIndividuals().get(0);
	}

	public int getPopulationSize() {
		return popSize;
	}

	public ArrayList<T> getIndividuals() {
		return individuals;
	}

	public FitnessComparator<T> getFitnessComparator() {
		return comparator;
	}

	public void setFitnessComparator(FitnessComparator<T> fitComp) {
		this.comparator = fitComp;
	}

}
