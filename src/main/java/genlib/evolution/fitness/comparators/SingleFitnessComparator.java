package genlib.evolution.fitness.comparators;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.Individual;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.utils.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SingleFitnessComparator<T extends Individual> extends
		FitnessComparator<T> {
	/** Logger */
	private static final Logger LOG = Logger
			.getLogger(SingleFitnessComparator.class.getName());
	/** for serialization */
	private static final long serialVersionUID = 5637425664834090230L;
	private FitnessFunction<T> function;

	@Override
	public int compare(T o1, T o2) {
		double fit1, fit2;
		if (function != null) {
			fit1 = function.computeFitness(o1);
			o1.unchange();
			fit2 = function.computeFitness(o2);
			o2.unchange();
		} else {
			if (Utils.DEBUG) {
				LOG.log(Level.INFO,
						TextResource.getString(TextKeys.comparator_complex_fit));
			}
			fit1 = o1.getComplexFitness();
			fit2 = o2.getComplexFitness();
		}

		// reverted condition for comparison
		return -Double.compare(fit1, fit2);
	}

	@Override
	public void setParam(String s) {
		int index = Integer.parseInt(s);
		if (index != -1) {
			this.function = fitFuncs.get(index);
		}
	}

}
