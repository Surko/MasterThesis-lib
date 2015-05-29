package genlib.evolution.fitness;

import genlib.evolution.EvolutionAlgorithm;
import genlib.evolution.Population;
import genlib.evolution.fitness.tree.TreeAccuracyFitness;
import genlib.evolution.fitness.tree.look.TreeHeightFitness;
import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface for fitness functions that defines basic to-be implemented methods
 * to compute fitness for Individual or individuals inside of a population. </br>
 * Different type of fitnesses are decided by index which fit to static values in this class. 
 * You can look at particular examples of implemented fitness functions
 * @author Lukas Surin 
 * @see TreeAccuracyFitness
 * @see TreeHeightFitness
 *
 */
public abstract class FitnessFunction<T extends Individual> implements Serializable {		
	
	/** for serialization */
	private static final long serialVersionUID = -90043146335682343L;
	public static final HashMap<String, Class<FitnessFunction<TreeIndividual>>> tFitFuncs = new HashMap<>();
	
	/** How many fitness functions is already registered */
	public static int registeredFunctions = 0;
	protected EvolutionAlgorithm<T> ea;
	protected int index = -1;
	
	/**
	 * Abstract method which should compute fitness for specific class that 
	 * you create. You can look at particular examples of implemented fitness functions
	 * and their compute fitness method. Usually this method contains some type of 
	 * differentiation of which data we use (weka or built-in type). Other thing you can take 
	 * into consideration is that individual saves his state of changing from last time, so it can be utilized 
	 * for speeding up running time of computation.
	 * @param individual individual for which we compute fitness
	 * @return fitness value for an individual
	 * @see TreeAccuracyFitness#computeFitness(genlib.evolution.individuals.TreeIndividual)
	 * @see TreeHeightFitness#computeFitness(genlib.evolution.individuals.TreeIndividual)
	 */
	public abstract double computeFitness(T individual);

	public void computeFitness(Population<T> population) {		
		for (T individual : population.getIndividuals())
			computeFitness(individual);
	}

	public void computeFitness(ArrayList<T> population, int start, int end) {
		end = Math.min(population.size(), end);
		for (int i = start; i < end; i++) {		
			computeFitness(population.get(i));
		}
	}
	
	public abstract Class<T> getIndividualClassType();
	
	public abstract void setData(Data data);
	
	public void setEvolutionAlgorithm(EvolutionAlgorithm<T> ea) {
		this.ea = ea;		
	}	
	
	public int getIndex() {
		return index;
	}	
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * Method which should return if this fitness function can 
	 * handle numeric class atributes. 
	 * @return true iff fitness function can handle numeric class attribute
	 */
	public abstract boolean canHandleNumeric();
	/**
	 * This method should set the parameters for function if it's needed. A lot of functions
	 * doesn't need it so they could leave it empty.
	 * @param param parameter from which will be set additional parameters for this function
	 */
	public abstract void setParam(String param);
	/**
	 * Method which should return String object with info about this instance. Name of
	 * function with additional parameters.
	 * @return info about fitness function
	 */
	public abstract String objectInfo();
}
