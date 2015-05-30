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

	/**
	 * AverageEnum that serves the purpose of defining what kind of fitnesses
	 * averaging will be done. </p> Defined kinds of averaging: </br>
	 * {@link AverageEnum#OWNWEIGHT} </br> {@link AverageEnum#UNWEIGHT} </br>
	 * {@link AverageEnum#WEIGHT} </br> {@link AverageEnum#TOTAL}
	 * 
	 * @author Lukas Surin
	 *
	 */
	protected enum AverageEnum {
		/**
		 * Averaging of fitnesses with own fitness
		 */
		OWNWEIGHT,
		/**
		 * Averaging of fitnesses with number of classes
		 */
		UNWEIGHTED,
		/**
		 * Averaging of fitnesses with weights(supports), true values for
		 * classes
		 */
		WEIGHTED,
		/**
		 * Averaging of fitnesses with total number of instances
		 */
		TOTAL
	}

	/**
	 * ConfusionEnum that defines what kind of parameters are possible for
	 * fitness function that are based on confusion matrix.</p> Defined kinds of
	 * parameters: </br> {@link ConfusionEnum#AVERAGE} </br>
	 * {@link ConfusionEnum#INDEX} </br> {@link ConfusionEnum#MAXIMIZe}
	 * 
	 * @author Lukas Surin
	 *
	 */
	protected enum ConfusionEnum {
		/**
		 * Average parameter to defined what kind of averaging we use
		 * 
		 * @see AverageEnum
		 */
		AVERAGE,
		/**
		 * Index parameter of attribute for which we compute fitness
		 */
		INDEX,
		/**
		 * Maximize parameter that defines if maximizing the fitness is
		 * important. For example accuracy should be maximized. On the other
		 * hand treeSize should be minimized.
		 */
		MAXIMIZE
	}

	private static final Logger LOG = Logger
			.getLogger(TreeConfusionFitness.class.getName());
	/** for serialization */
	private static final long serialVersionUID = 3138044426446243798L;
	protected Data data;
	protected Boolean maximize = null;
	protected AverageEnum averageEnum = null;
	protected int attrIndex = -1;

	/**
	 * This method that overrides computeFitness from FitnessFunction class
	 * computes fitness for an individual handed as parameter. If the individual
	 * hasn't changed then we can return value of this fitness right away from
	 * individual. Method calls other method with the same name that depends on
	 * type of data on which this fitness function works (weka or built-in
	 * type). </p>It calls {@link #computeFitness(Instances, TreeIndividual)} or
	 * {@link #computeFitness(GenLibInstances, TreeIndividual)} depending on
	 * what kind of data are used.
	 */
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

		// Set the fitness into fitness array in individual
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
			LOG.log(Level.INFO, String.format(
					TextResource.getString(TextKeys.iExcessParam), paramLabel));
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
			parseParamLabels(parts[i], parts[i + 1]);
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
				LOG.log(Level.WARNING, String.format(
						TextResource.getString(TextKeys.eMissParam),
						ConfusionEnum.AVERAGE.name(), this.getFitnessName()));
				throw new MissingParamException(String.format(
						TextResource.getString(TextKeys.eMissParam),
						ConfusionEnum.AVERAGE.name(), this.getFitnessName()));
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
	protected abstract double[] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual);

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
