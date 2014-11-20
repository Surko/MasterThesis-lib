package genlib.evolution;

import genlib.evolution.individuals.Individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Population<T extends Individual> {

	private ArrayList<T> individuals;
	private int popSize;

	public Population(ArrayList<T> individuals, int popSize) {
		this.individuals = individuals;
		this.popSize = popSize;
	}

	public Population(T[] individuals, int popSize) {
		this.individuals = new ArrayList<>(Arrays.asList(individuals));
		this.popSize = popSize;
	}

	/**
	 * Sort individuals inside this object.
	 */
	public void sortIndividuals() {
		Collections.sort(individuals);
	}

	/**
	 * Getting sorted individuals without change of placing in the object field
	 * individuals.
	 * 
	 * @return Sorted individuals
	 */
	public ArrayList<T> getSortedIndividuals() {
		ArrayList<T> sorted = new ArrayList<>(individuals);
		Collections.sort(sorted);
		return sorted;
	}

	public int getPopulationSize() {
		return popSize;
	}

	public ArrayList<T> getIndividuals() {
		return individuals;
	}

}
