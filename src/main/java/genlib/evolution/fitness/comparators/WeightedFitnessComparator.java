package genlib.evolution.fitness.comparators;

import genlib.evolution.individuals.Individual;

public class WeightedFitnessComparator<T extends Individual>  extends FitnessComparator<T> {

	protected double[] weights;	

	public WeightedFitnessComparator(int fitCounts) {
		this.weights = new double[fitCounts];
	}
	
	@Override
	public int compare(T o1, T o2) {
		double fit1 = 0,fit2 = 0;
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
		
		return fit1 < fit2 ? -1 : (fit1 == fit2 ? 0 : 1);					
	}

	@Override
	public void setParam(String s) {				
		
		if (s == null || s.isEmpty()) {
			for (int i = 0; i < weights.length; i++) {
				weights[i] = 1d;
			}
			return;
		}
		
		String[] sValues = s.split(",");
		int min = Math.min(sValues.length,weights.length);
		
		for (int i = 0; i < min; i++) {
			weights[i] = Double.parseDouble(sValues[i]);
		}
	}
	
	public double[] getWeights() {
		return weights;
	}
}
