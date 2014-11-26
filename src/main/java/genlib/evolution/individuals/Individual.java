package genlib.evolution.individuals;

import java.io.Serializable;


public abstract class Individual implements Comparable<Individual>, Serializable {

	/** for serialization */
	private static final long serialVersionUID = 8997460860747264220L;
	protected double fitness;

	public double getFitness() {
		return fitness;
	}
	
	@Override
	public int compareTo(Individual o) {
		return this.fitness < o.getFitness() ? -1 : (this.fitness > o.getFitness() ? 1 : 0);
	}

}
