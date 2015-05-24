package genlib.evolution.fitness.comparators;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.Individual;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public abstract class FitnessComparator<T extends Individual> implements
		Comparator<T>, Serializable {
	/** for serialization */
	private static final long serialVersionUID = -9014991059941979938L;
	/** fitness functions that are utilized by this comparator/evaluator */
	protected ArrayList<FitnessFunction<T>> fitFuncs;

	/**
	 * Enum with different types of fitness comparators/evaluators.
	 * These four are the most famous one : </br>
	 * {@link FitCompare#SINGLE} </br> 
	 * {@link FitCompare#PARETO}  </br>
	 * {@link FitCompare#PRIORITY} </br>
	 * {@link FitCompare#WEIGHT} </br>
	 * 
	 * @author Lukas Surin	 
	 */
	public enum FitCompare {
		/**
		 * Simple signle fitness evaluator. Only one function is utilized.
		 */
		SINGLE,
		/**
		 * Pareto fitness evaluator. All the functions are utilized.
		 * Comparator of this type should work according to
		 * pareto approach.
		 */
		PARETO,
		/**
		 * Priority fitness evaluator. All the functions are utilized.
		 * Comparator of this type should work according to
		 * priority approach.
		 */
		PRIORITY,
		/**
		 * Weighted fitness evaluator. All the functions are utilized.
		 * Comparator of this type should work according to
		 * weight approach.
		 */
		WEIGHT
	}

	/**
	 * Method which returns fitness functions for this comparator/evaluator.
	 * @return fitness functions of comparator/evaluator.
	 */
	public ArrayList<FitnessFunction<T>> getFitnessFuncs() {
		return fitFuncs;
	}

	/**
	 * This method will set the fitness functions to be utilized by this
	 * comparator/evaluator. How they are used depends on specific
	 * comparator/evaluator.
	 * 
	 * @param fitFuncs
	 *            fitness functions to be set
	 */
	public void setFitFuncs(ArrayList<FitnessFunction<T>> fitFuncs) {
		this.fitFuncs = fitFuncs;
	}

	/**
	 * Method which will set the param for this comparator. Each
	 * evaluator/comparator can parse the parameters from string s differently.
	 * Defaultly it does not utilize any parsing.
	 * 
	 * @param s
	 *            parameters in string format
	 */
	public void setParam(String s) {
	}
}
