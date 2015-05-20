package genlib.evolution.fitness;

import genlib.evolution.individuals.Individual;
import genlib.locales.PermMessages;
import genlib.structures.Data;
import genlib.utils.Utils;

@SuppressWarnings("rawtypes")
public class TestFit extends FitnessFunction {
	/** for serialization */
	private static final long serialVersionUID = -7959825267949047077L;
	public static final String initName = "tTest";
	
	@Override
	public double computeFitness(Individual individual) {
		return individual.getComplexFitness();
	}

	@Override
	public void setData(Data data) {}	
	
	@Override
	public Class<Individual> getIndividualClassType() {
		return Individual.class;
	}

	@Override
	public void setParam(String param) {}

	@Override
	public String objectInfo() {
		return String.format(PermMessages._fit_format, initName, PermMessages._blank_param);
	}

	
	@Override
	public boolean canHandleNumeric() {
		return true;
	}
	
}
