package genlib.evolution.fitness.tree.confusion;

import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.data.GenLibInstances;

import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instances;

public class TreeFMeasureFitness extends TreeConfusionFitness {

	protected enum FMeasureEnum {
		BETA
	}

	private static final Logger LOG = Logger
			.getLogger(TreeFMeasureFitness.class.getName());
	/** for serialization */
	private static final long serialVersionUID = -2123585112288360577L;
	public static final String initName = "tFMsr";
	
	public double beta = 0d;

	@Override
 	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}

	@Override
	protected boolean parseParamLabels(String paramLabel, String paramValue) {
		if (super.parseParamLabels(paramLabel, paramValue)) {
			return true;
		}

		FMeasureEnum fMeasureEnum = FMeasureEnum.valueOf(paramLabel);
		if (fMeasureEnum == null) {
			LOG.log(Level.INFO, String.format(
					TextResource.getString(TextKeys.iExcessParam), paramLabel));
			return false;
		}

		switch (fMeasureEnum) {
		case BETA:
			this.beta = Double.valueOf(paramValue);	
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	protected double attributeConfusionValue(GenLibInstances instances,
			TreeIndividual individual) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected double[] totalConfusionValues(GenLibInstances instances,
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
	protected double[] totalConfusionValues(Instances instances,
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
