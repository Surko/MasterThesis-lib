package genlib.evolution.fitness.comparators;

import genlib.evolution.individuals.Individual;

import java.util.Comparator;
import java.util.logging.Logger;

public class WeightedFitnessComparator<T extends Individual> extends
		FitnessComparator<T> {
	/** Logger */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger
			.getLogger(WeightedFitnessComparator.class.getName());
	/** for serialization */
	private static final long serialVersionUID = -5325649645556578971L;
	/** weights to be utilized by this comparator */
	protected double[] weights;

	/**
	 * Constructor that creates instance of WeightedFitnessComparator.
	 * It initializes weights array to be of length fitCounts.
	 * @param fitCounts length of weights array to be set
	 */
	public WeightedFitnessComparator(int fitCounts) {
		this.weights = new double[fitCounts];
	}

	/**
	 * Goal of this method is to compare two individuals of specific type.
	 * Comparing is done by weightening fitness functions with weights defined
	 * in the field of same name.
	 * <p>
	 * Value to compare one individual with another is computed like this: </br>
	 * function1 * weight1 + ... + functionn * weightn
	 * </p>
	 * <p>
	 * Computed value is saved in the complex fitness field inside each
	 * indivudal.
	 * </p>
	 * Method override the Comparator compare method
	 * {@link Comparator#compare(Object, Object)}
	 * 
	 * @param o1
	 *            individual of type T to compare
	 * @param o2
	 *            individual of type T to compare
	 * @return computed weighted value for this comparator
	 */
	@Override
	public int compare(T o1, T o2) {
		double fit1 = 0, fit2 = 0;
		if (o1.hasChanged()) {
			for (int i = 0; i < weights.length; i++) {
				fit1 += weights[i] * fitFuncs.get(i).computeFitness(o1);
			}
			o1.setComplexFitness(fit1);
			o1.unchange();
		} else {
			fit1 = o1.getComplexFitness();
		}

		if (o2.hasChanged()) {
			for (int i = 0; i < weights.length; i++) {
				fit2 += weights[i] * fitFuncs.get(i).computeFitness(o2);
			}
			o2.setComplexFitness(fit2);
			o2.unchange();
		} else {
			fit2 = o2.getComplexFitness();
		}

		// reverted condition for comparison, so the
		// individual with greatest value will be first
		// descending order
		return -Double.compare(fit1, fit2);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This one is setting up the weights field from input parameter s.
	 * Weights in string are in format weight1,...,weightn.
	 * </p>
	 * @param s parameters in string format
	 */
	@Override
	public void setParam(String s) {

		if (s == null || s.isEmpty()) {
			for (int i = 0; i < weights.length; i++) {
				weights[i] = 1d;
			}
			return;
		}

		String[] sValues = s.split(",");
		int min = Math.min(sValues.length, weights.length);

		for (int i = 0; i < min; i++) {
			weights[i] = Double.parseDouble(sValues[i]);
		}
	}

	/**
	 * Method returns weights of this comparator/evaluator.
	 * @return weights in array format
	 */
	public double[] getWeights() {
		return weights;
	}
}
