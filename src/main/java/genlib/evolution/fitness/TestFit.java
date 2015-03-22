package genlib.evolution.fitness;

import genlib.evolution.individuals.Individual;

public class TestFit extends FitnessFunction {

	@Override
	public double computeFitness(Individual individual) {
		return individual.getComplexFitness();
	}

	@Override
	public void setData(Object data) {}	
	
	@Override
	public Class<Individual> getIndividualClassType() {
		return Individual.class;
	}

	@Override
	public void setParam(String param) {}

}
