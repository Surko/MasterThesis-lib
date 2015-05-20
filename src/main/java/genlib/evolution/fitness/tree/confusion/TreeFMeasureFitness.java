package genlib.evolution.fitness.tree.confusion;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.data.GenLibInstances;
import weka.core.Instances;

public class TreeFMeasureFitness extends TreeConfusionFitness {
	/** for serialization */
	private static final long serialVersionUID = -2123585112288360577L;
	public static final String initName = "tFMsr";

	@Override
	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}

	@Override
	protected double attributeConfusionValue(GenLibInstances instances,
			TreeIndividual individual) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double[][] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected double attributeConfusionValue(Instances instances,
			TreeIndividual individual) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double[][] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getFitnessName() {
		// TODO Auto-generated method stub
		return null;
	}

}
