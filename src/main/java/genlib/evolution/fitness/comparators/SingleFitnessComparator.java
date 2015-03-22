package genlib.evolution.fitness.comparators;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.Individual;

public class SingleFitnessComparator<T extends Individual> extends FitnessComparator<T> {

	private FitnessFunction<T> function;
	
	@Override
	public int compare(T o1, T o2) {
		double fit1,fit2;
		if (function != null) {
			fit1 = function.computeFitness(o1);
			fit2 = function.computeFitness(o2);
		} else {
			fit1 = o1.getComplexFitness();
			fit2 = o1.getComplexFitness();
		}
		
		return fit1 < fit2 ? -1 : (fit1 == fit2 ? 0 : 1);
	}

	@Override
	public void setParam(String s) {
		int index = Integer.parseInt(s);
		if (index != -1) {
			this.function = fitFuncs.get(index);
		}
	}
	
	
}
