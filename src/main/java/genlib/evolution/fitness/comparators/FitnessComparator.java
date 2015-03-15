package genlib.evolution.fitness.comparators;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.Individual;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class FitnessComparator<T extends Individual> implements Comparator<T> {
	protected ArrayList<FitnessFunction<T>> fitFuncs;
	
	public enum FitCompare {
		SINGLE,
		PARETO,
		PRIORITY,
		WEIGHT
	}

	public ArrayList<FitnessFunction<T>> getFitnessFuncs() {
		return fitFuncs;
	}
	
	public void setFitFuncs(ArrayList<FitnessFunction<T>> fitFuncs) {
		this.fitFuncs = fitFuncs;
	}

	public void setParam(String s) {}
}

