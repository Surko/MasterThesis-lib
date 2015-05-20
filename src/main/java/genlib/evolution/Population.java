package genlib.evolution;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Population<T extends Individual> implements Serializable {

	/** for serialization */
	private static final long serialVersionUID = -8405304400658666989L;
	private FitnessComparator<T> comparator;
	private ArrayList<T> individuals;
	private int popSize;
	private Random randomGen;	

	public Population() {
		this.individuals = new ArrayList<>();
		this.popSize = 0;
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}

	@SuppressWarnings("unchecked")
	public Population(Population<T> population) {
		this.comparator = population.comparator;
		this.popSize = population.popSize;
		this.individuals = new ArrayList<>();
		for (T ind : population.getIndividuals()) {
			individuals.add((T)ind.copy());
		}
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

	/**
	 * Method which clears individuals arraylist.
	 */
	public void clear() {
		this.popSize = 0;
		this.individuals.clear();
	}

	public void add(T individual) {		
		individuals.add(individual);
		popSize++;
	}

	/**
	 * 
	 * @param population
	 */
	public void addAll(Population<T> population) {
		individuals.addAll(population.getIndividuals());
		popSize = individuals.size();
	}
	
	/**
	 * Deep copy of population from parameter into this population.
	 * @param population Population from which we copy individuals
	 */
	@SuppressWarnings("unchecked")
	public void deepCopy(Population<T> population) {
		for (T ind : population.getIndividuals()) {
			individuals.add((T)ind.copy());
		}
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
	
	public void computeFitness(final int nThreads, final int blockSize) {
		if (nThreads > 1) {
			ExecutorService es = Executors.newFixedThreadPool(nThreads);
			
			if (blockSize == 1) {
				// execution of individual one by one
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
			} else {
				// block execution of individuals
				for (final FitnessFunction<T> function : comparator
						.getFitnessFuncs()) {
					for (int i = 0; i < popSize; i+=blockSize) {
						final int start = i;
						es.submit(new Runnable() {
							@Override
							public void run() {
								function.computeFitness(individuals, start, start+blockSize);
							}
						});
					}
				}
			}

			es.shutdown();

			try {
				es.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				
			}
		} else {
			// in this case block execution is worthless
			for (FitnessFunction<T> function : comparator.getFitnessFuncs()) {
				function.computeFitness(this);
			}
		}

		// unchange individuals, because all of the fitness functions has been computed.
		// next call of this method will be really fast 
  		// for (final T individual : individuals) {
  		//	individual.unchange();
  		// }
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
