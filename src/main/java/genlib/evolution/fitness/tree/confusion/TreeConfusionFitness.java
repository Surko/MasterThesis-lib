package genlib.evolution.fitness.tree.confusion;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.TreeIndividual;
import genlib.exceptions.MissingParamException;
import genlib.locales.PermMessages;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.Data;
import genlib.structures.data.GenLibInstances;
import genlib.utils.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instances;

public abstract class TreeConfusionFitness extends
		FitnessFunction<TreeIndividual> {

	protected enum AverageEnum {
		OWNWEIGHT,
		UNWEIGHTED,
		WEIGHTED,
		TOTAL
	}
	
	protected enum ConfusionEnum {
		AVERAGE, INDEX, MAXIMIZE
	}

	private static final Logger LOG = Logger
			.getLogger(TreeConfusionFitness.class.getName());
	/** for serialization */
	private static final long serialVersionUID = 3138044426446243798L;
	protected Data data;
	protected Boolean maximize = null;
	protected AverageEnum averageEnum = null;
	protected int attrIndex = -1;

	@Override
	public final double computeFitness(TreeIndividual individual) {
		if (!individual.hasChanged()) {
			return individual.getFitnessValue(index);
		}

		double fitness = 0d;
		
		if (data.isInstances()) {
			fitness = computeFitness(data.toInstances(), individual);
		}
		if (data.isGenLibInstances()) {
			fitness = computeFitness(data.toGenLibInstances(), individual);
		}
		
		individual.setFitnessValue(index, fitness);
		return fitness;
	}

	@Override
	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}

	@Override
	public void setData(Data data) {
		this.data = data;
	}

	protected boolean parseParamLabels(String paramLabel, String paramValue) {
		ConfusionEnum confusionEnum = ConfusionEnum.valueOf(paramLabel);

		if (confusionEnum == null) {
			LOG.log(Level.INFO,
					String.format(
							TextResource.getString(TextKeys.iExcessParam),
							paramLabel));
			return false;
		}

		switch (confusionEnum) {
		case AVERAGE:
			this.averageEnum = AverageEnum.valueOf(paramValue);
			break;
		case INDEX:
			this.attrIndex = Integer.parseInt(paramValue);
			break;
		case MAXIMIZE:
			this.maximize = Boolean.valueOf(paramValue);
			break;
		default:
			return false;
		}
		
		return true;
	}
	
	@Override
	public void setParam(String param) {
		this.attrIndex = -1;
		this.averageEnum = null;
		this.maximize = null;

		if (param.equals(PermMessages._blank_param)) {
			return;
		}
		String[] parts = param.split(Utils.pDELIM);

		if (parts.length % 2 != 0) {
			throw new MissingParamException();
		}

		for (int i = 0; i < parts.length; i += 2) {
			parseParamLabels(parts[i], parts[i+1]);
		}
	}

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

		if (paramString.isEmpty()) {
			return String.format(PermMessages._fit_format, getFitnessName(),
					PermMessages._blank_param);
		}
		return String.format(PermMessages._fit_format, getFitnessName(),
				paramString);

	}

	protected double computeFitness(Instances instances,
			TreeIndividual individual) {
		// test if fitness should be computed in regards to one or all(-1)
		// attributes
		if (attrIndex == -1) {
			if (instances.numClasses() == 2) {
				attrIndex = 1;
				return attributeConfusionValue(instances, individual);
			}
			double fitness = 0d;
			double[] criteria = totalConfusionValues(instances, individual);
			
			if (averageEnum == null) {
				LOG.log(Level.WARNING,
						String.format(
								TextResource.getString(TextKeys.eMissParam),
								ConfusionEnum.AVERAGE.name(),
								this.getFitnessName()));
				throw new MissingParamException(String.format(
						TextResource.getString(TextKeys.eMissParam),
						ConfusionEnum.AVERAGE.name(),
						this.getFitnessName()));
			}
			
			switch (averageEnum) {
			case OWNWEIGHT:
				for (int i = 0; i < criteria.length; i++) {
					fitness += (criteria[i] * criteria[i]);
				}
				// divided by all instances
				fitness /= instances.numInstances();
				break;
			case WEIGHTED:
				double[] weight = data.getClassCounts(); 
				// weight is their support
				for (int i = 0; i < criteria.length; i++) {
					fitness += (weight[i] * criteria[i]);
				}
				// divided by all instances as defined in support
				fitness /= instances.numInstances();
				break;
			case UNWEIGHTED:
				// sum of all values
				for (int i = 0; i < criteria.length; i++) {
					fitness += criteria[i];
				}
				// and then average
				fitness /= criteria.length;
				break;
			case TOTAL:
				// sum of all values
				for (int i = 0; i < criteria.length; i++) {
					fitness += criteria[i];
				}
				// divided by all instances
				fitness /= instances.numInstances();
				break;
			}
			return fitness;
		} else {
			return attributeConfusionValue(instances, individual);
		}
	}

	protected double computeFitness(GenLibInstances instances,
			TreeIndividual individual) {
		// test if fitness should be computed in regards to one or all(-1)
		// attributes
		if (attrIndex == -1) {
			if (instances.numClasses() == 2) {
				attrIndex = 1;
				return attributeConfusionValue(instances, individual);
			}
					
			double fitness = 0d;
			double[] criteria = totalConfusionValues(instances, individual);
			switch (averageEnum) {
			case OWNWEIGHT:
				for (int i = 0; i < criteria.length; i++) {
					fitness += (criteria[i] * criteria[i]);
				}
				// divided by all instances
				fitness /= instances.numInstances();
				break;
			case WEIGHTED:
				double[] weight = data.getClassCounts(); 
				// weight is their support
				for (int i = 0; i < criteria.length; i++) {
					fitness += (weight[i] * criteria[i]);
				}
				// divided by all instances as defined in support
				fitness /= instances.numInstances();
				break;
			case UNWEIGHTED:
				// sum of all values
				for (int i = 0; i < criteria.length; i++) {
					fitness += criteria[i];
				}
				// and then average
				fitness /= criteria.length;
				break;
			case TOTAL:
				// sum of all values
				for (int i = 0; i < criteria.length; i++) {
					fitness += criteria[i];
				}
				// divided by all instances
				fitness /= instances.numInstances();
				break;
			}
			return fitness;
		} else {
			return attributeConfusionValue(instances, individual);
		}
	}

	/**
	 * Method which return false. This is because all the fitness functions
	 * based on confusion matrix are dependent on nominal attributes
	 */
	@Override
	public boolean canHandleNumeric() {
		return false;
	}

	/**
	 * Method which returns fitness value for specific atribute. It uses
	 * GenLibInstances as data.
	 * 
	 * @param instances
	 *            GenLibInstances
	 * @param individual
	 *            TreeIndividual on which we compute fitness
	 * @return value of particular fitness
	 */
	protected abstract double attributeConfusionValue(
			GenLibInstances instances, TreeIndividual individual);

	/**
	 * Method which returns fitness value for all atributes. It uses
	 * GenLibInstances as data. It returns array with 1 row. </br> In the first
	 * row there is fitness values for each attribute. </br>.
	 * 
	 * @param instances
	 *            GenLibInstances
	 * @param individual
	 *            TreeIndividual on which we compute fitness
	 * @return array with fitness values
	 */
	protected abstract double[] totalConfusionValues(
			GenLibInstances instances, TreeIndividual individual);

	/**
	 * Method which returns fitness value for specific atribute. It uses
	 * Instances from weka as data.
	 * 
	 * @param instances
	 *            Instances
	 * @param individual
	 *            TreeIndividual on which we compute fitness
	 * @return value of particular fitness
	 */
	protected abstract double attributeConfusionValue(Instances instances,
			TreeIndividual individual);

	/**
	 * Method which returns fitness value for all atributes. It uses Instances
	 * from weka as data. It returns array with 1 rows. </br> In the first row
	 * there is fitness values for each attribute. </br>
	 * 
	 * @param instances
	 *            Instances
	 * @param individual
	 *            TreeIndividual on which we compute fitness
	 * @return array with fitness values
	 */
	protected abstract double[] totalConfusionValues(Instances instances,
			TreeIndividual individual);

	/**
	 * Getter which returns name for the instance of fitness function.
	 * 
	 * @return name of this fitness function
	 */
	protected abstract String getFitnessName();
}
