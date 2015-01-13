package genlib.evolution;

import genlib.evolution.individuals.Individual;
import genlib.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Population<T extends Individual> {

	private Comparator<T> comparator;
	private ArrayList<T> individuals;
	private int popSize;

	public Population() {
		this.individuals = new ArrayList<>();
	}

	public Population(ArrayList<T> individuals, int popSize) {
		this.individuals = individuals;
		this.popSize = popSize;
	}

	public Population(T[] individuals, int popSize) {
		this.individuals = new ArrayList<>(Arrays.asList(individuals));
		this.popSize = popSize;
	}

	public void resample() {
		Collections.shuffle(individuals, Utils.randomGen);
	}

	/**
	 * Sort individuals inside this object.
	 */
	public void sortIndividuals() {
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
	
	public T getBestIndividual() {
		return getSortedIndividuals().get(0);
	}

	public int getPopulationSize() {
		return popSize;
	}

	public ArrayList<T> getIndividuals() {
		return individuals;
	}

}
