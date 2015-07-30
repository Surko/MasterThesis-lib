package genlib.evolution.fitness.comparators;

import genlib.evolution.individuals.Individual;
import genlib.utils.Utils;

import java.util.logging.Logger;

/**
 * Class that compares fitness functions of individuals 
 * by priority. Firstly it compares first fitness functions.
 * If they are the same than it compares next fitness functions.
 * @author Lukas Surin
 *
 * @param <T> type of individual
 */
public class PriorityFitnessComparator<T extends Individual> extends
		FitnessComparator<T> {
	/** Logger */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger
			.getLogger(PriorityFitnessComparator.class.getName());
	/** for serialization */
	private static final long serialVersionUID = -4763112985337101338L;
	/** thresholds to be utilized by this comparator, <b>NOT USED IN THIS VERSION</b> */
	protected double[] thresholds;

	/**
	 * Constructor that creates instance of PriorityFitnessComparator. It
	 * initializes thresholds array to be of length fitCounts.
	 * 
	 * @param fitCounts
	 *            length of weights array to be set
	 */
	public PriorityFitnessComparator(int fitCounts) {
		this.thresholds = new double[fitCounts];
	}

	/**
	 * {@inheritDoc} </p> This method returns 1 because priority can't be
	 * evaluated.
	 */
	public double value(T o) {
		// not defined for this type of comparator
		return 0d;
	}

	/**
	 * {@inheritDoc} </p> It compares fitnesses of individuals by their priority
	 * that is defined by its ordering in {@link FitnessComparator#fitFuncs}.
	 */
	@Override
	public int compare(T o1, T o2) {		
		for (int i = 0; i < fitFuncs.size(); i++) {
			int comparison = Double.compare(fitFuncs.get(i).computeFitness(o1),
					fitFuncs.get(i).computeFitness(o2));
			if (comparison != 0) {
				return -comparison;
			}
		}

		return 0;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This one is setting up the threshold field from input parameter s.
	 * Thresholds in string are in format threshold1,...,thresholdn.
	 * </p>
	 * 
	 * @param s
	 *            parameters in string format
	 */
	@Override
	public void setParam(String s) {

		if (s == null || s.isEmpty() || s.equals(Utils.bPARAM)) {
			for (int i = 0; i < thresholds.length; i++) {
				thresholds[i] = 0d;
			}
			return;
		}

		String[] sValues = s.split(",");
		int min = Math.min(sValues.length, thresholds.length);

		for (int i = 0; i < min; i++) {
			thresholds[i] = Double.parseDouble(sValues[i]);
		}
	}

	/**
	 * Method returns thresholds of this comparator/evaluator.
	 * 
	 * @return thresholds in array format
	 */
	public double[] getThresholds() {
		return thresholds;
	}
}
