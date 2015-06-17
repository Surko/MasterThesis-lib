package genlib.evolution.individuals;

import genlib.evolution.fitness.FitnessFunction;
import genlib.exceptions.NotInitializedFieldException;
import genlib.locales.TextResource;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Lukas Surin
 * @see TreeIndividual
 */
public abstract class Individual implements Serializable {

	/** for serialization */
	private static final long serialVersionUID = 8997460860747264220L;
	private static final Logger LOG = Logger.getLogger(Individual.class
			.getName());
	/**
	 * fitness values for this individual. Each element is different type of
	 * fitness
	 */
	protected double[] fitness;
	/**
	 * Fitness value which is in some way combination of fitness values in
	 * array. Type of combination can be sum, weighted sum or other mean of view
	 * on multi criterial fitness functions.
	 */
	protected double complexFitness = 0;
	/** true/false depending whether individual has changed or hasn't */
	protected boolean hasChanged = true;

	/**
	 * Method which will set value of fitness into array of fitnesses in this
	 * object. Index should not exceed amount of elements in this array.
	 * 
	 * @param index
	 *            index at which is the element save
	 * @param fitness
	 *            value of fitness
	 * @see FitnessFunction
	 */
	public void setFitnessValue(int index, double fitness) {
		if (this.fitness == null) {
			LOG.log(Level.SEVERE, String.format(
					TextResource.getString("eFieldInit"), "fitness"));
			throw new NotInitializedFieldException(String.format(
					TextResource.getString("eFieldInit"), "fitness"));
		}
		if (index >= this.fitness.length) {
			LOG.log(Level.SEVERE, TextResource.getString("eIndexOutOfBounds"));
			throw new ArrayIndexOutOfBoundsException(
					TextResource.getString("eIndexOutOfBounds"));
		}
		this.fitness[index] = fitness;
	}

	/**
	 * Sets the complex/combined fitness value for this individual
	 * 
	 * @param complexFitness
	 *            value of this complex fitness
	 */
	public void setComplexFitness(double complexFitness) {
		this.complexFitness = complexFitness;
	}

	/**
	 * Gets the value of fitness for this individual at index.
	 * 
	 * @param index
	 *            from which we take fitness value
	 * @return fitness value at index
	 */
	public double getFitnessValue(int index) {
		if (index == -1) {
			return complexFitness;
		}
		return this.fitness[index];
	}

	/**
	 * Gets the complex/combined fitness value for this individual.
	 * 
	 * @return combined fitness value
	 */
	public double getComplexFitness() {
		return complexFitness;
	}

	/**
	 * Make this individual be recognized as the one that has changed. If the
	 * individual changed than the computation of fitness is inevitable.
	 */
	public void change() {
		hasChanged = true;
	}

	/**
	 * Make this individual be recognized as the one that hasn't changed. Used
	 * for speeding up the computation of fitness values.
	 */
	public void unchange() {
		hasChanged = false;
	}

	/**
	 * Gets the state of this individual if it changed from the last time we
	 * unchanged him by calling unchange method.
	 * 
	 * @return true/false if individual has changed
	 */
	public boolean hasChanged() {
		return hasChanged;
	}

	/**
	 * Method makes copy of individual fields.
	 * 
	 * @return Copy of this individual
	 */
	public abstract Individual copy();

	/**
	 * Overriden method that returns the representation of this individual.
	 */
	public abstract String toString();
	
}
