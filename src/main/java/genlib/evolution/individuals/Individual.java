package genlib.evolution.individuals;

import java.io.Serializable;



public abstract class Individual implements Serializable {

	/** for serialization */
	private static final long serialVersionUID = 8997460860747264220L;
	public static final int ACCURACY = 0;
	protected double[] fitness;
	// array with good and bad classification
	
	public void setFitnessValue(int index, double fitness) {
		this.fitness[index] = fitness;
	}
	
	public double getFitnessValue(int index) {
		return this.fitness[index];
	}

}
