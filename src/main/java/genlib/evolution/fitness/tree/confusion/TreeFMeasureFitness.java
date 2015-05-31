package genlib.evolution.fitness.tree.confusion;

import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.data.GenLibInstances;

import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instances;

/**
 * Fitness function that computes f-measure with parameter beta for population
 * and its individuals. It extends from FitnessFunction<TreeIndividual> so it
 * can be used with TreeIndividuals. It has unique initName that can be
 * referenced when initializing this function.
 * 
 * @author Lukas Surin
 *
 */
public class TreeFMeasureFitness extends TreeConfusionFitness {

	/**
	 * FMeasureEnum that defines what kind of parameters are possible for
	 * FMeasure function</p>
	 * 
	 * It is used in conjuction with {@link ConfusionEnum} values. Defined kinds
	 * of parameters: </br> {@link FMeasureEnum#BETA}
	 * 
	 * @author Lukas Surin
	 *
	 */
	protected enum FMeasureEnum {
		/**
		 * Beta parameter to define beta value for f-measure fitness
		 */
		BETA;

		/**
		 * Method which will get the FMeasureEnum value from name. If the name
		 * is not defined than returns null.
		 * 
		 * @param name
		 *            string to be converted into FMeasureEnum
		 * @return FMeasureEnum from string
		 */
		public static FMeasureEnum value(String name) {
			if (name.equals(BETA.name())) {
				return BETA;
			}

			return null;
		}
	}

	private static final Logger LOG = Logger
			.getLogger(TreeFMeasureFitness.class.getName());
	/** for serialization */
	private static final long serialVersionUID = -2123585112288360577L;
	/** name for this fitness function, should be t$LabelOfFitness$ */
	public static final String initName = "tFMsr";
	/** beta parameter which is used in computation of f-measure */
	public Double beta = null;

	/**
	 * {@inheritDoc} </p> Overriden to utilize Beta parameter.
	 */
	@Override
	protected boolean parseParamLabels(String paramLabel, String paramValue) {
		if (super.parseParamLabels(paramLabel, paramValue)) {
			return true;
		}

		FMeasureEnum fMeasureEnum = FMeasureEnum.value(paramLabel);
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

	/**
	 * {@inheritDoc} </p> Overriden to utilize Beta parameter.
	 */
	@Override
	public void setParam(String param) {
		beta = null;
		super.setParam(param);
	};

	/**
	 * {@inheritDoc} </p> Overriden to utilize Beta parameter.
	 */
	@Override
	public String objectInfo() {
		String paramString = "";
		if (attrIndex > -1) {
			paramString = String.format(PermMessages._param_format,
					ConfusionEnum.INDEX, attrIndex);
		}
		if (averageEnum != null) {
			if (paramString.isEmpty()) {
				paramString = String.format(PermMessages._param_format,
						ConfusionEnum.AVERAGE, averageEnum);
			} else {
				paramString = String.format(PermMessages._param_format,
						paramString, ConfusionEnum.AVERAGE + "," + averageEnum);
			}
		}
		if (maximize != null) {
			if (paramString.isEmpty()) {
				paramString = String.format(PermMessages._param_format,
						ConfusionEnum.MAXIMIZE, maximize);
			} else {
				paramString = String.format(PermMessages._param_format,
						paramString, ConfusionEnum.MAXIMIZE + "," + maximize);
			}
		}
		if (beta != null) {
			if (paramString.isEmpty()) {
				paramString = String.format(PermMessages._param_format,
						FMeasureEnum.BETA, beta);
			} else {
				paramString = String.format(PermMessages._param_format,
						paramString, FMeasureEnum.BETA + "," + beta);
			}
		}

		if (paramString.isEmpty()) {
			return String.format(PermMessages._fit_format, getFitnessName(),
					PermMessages._blank_param);
		}
		return String.format(PermMessages._fit_format, getFitnessName(),
				paramString);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double attributeConfusionValue(GenLibInstances instances,
			TreeIndividual individual) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double[] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double attributeConfusionValue(Instances instances,
			TreeIndividual individual) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double[] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFitnessName() {
		return initName;
	}

}
