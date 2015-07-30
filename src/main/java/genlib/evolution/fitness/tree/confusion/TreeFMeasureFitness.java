package genlib.evolution.fitness.tree.confusion;

import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.data.GenLibInstance;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.Node;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instance;
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
	/** beta * beta parameter which is used in computation of f-measure */
	public Double doubleBeta = Double.valueOf(1);

	/**
	 * {@inheritDoc} </p> Overriden to utilize Beta parameter.
	 */
	@Override
	protected void parseParamLabels(String paramLabel, String paramValue) {
		FMeasureEnum fMeasureEnum = FMeasureEnum.value(paramLabel);
		ConfusionEnum confusionEnum = ConfusionEnum.value(paramLabel);
		if (fMeasureEnum == null && confusionEnum == null) {
			LOG.log(Level.INFO, String.format(TextResource
					.getString(TextKeys.iExcessParam), String.format(
					PermMessages._param_format, paramLabel, paramValue)));				
			return;
		}

		if (fMeasureEnum != null) {
			switch (fMeasureEnum) {
			case BETA:
				this.doubleBeta = Double.valueOf(paramValue);
				this.doubleBeta *= this.doubleBeta;
				return;
			default:
				return;
			}
		}
		
		if (confusionEnum != null) {
			super.parseParamLabels(paramLabel, paramValue);
		}
	}

	/**
	 * {@inheritDoc} </p> Overriden to utilize Beta parameter.
	 */
	@Override
	public void setParam(String param) {
		doubleBeta = Double.valueOf(1);
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
		if (doubleBeta != null) {
			if (paramString.isEmpty()) {
				paramString = String.format(PermMessages._param_format,
						FMeasureEnum.BETA, Math.sqrt(doubleBeta));
			} else {
				paramString = String.format(PermMessages._param_format,
						paramString,
						FMeasureEnum.BETA + "," + Math.sqrt(doubleBeta));
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
		Node root = individual.getRootNode();
		double tp = 0, top = 0;

		Enumeration<GenLibInstance> eInstances = instances.getInstances();
		while (eInstances.hasMoreElements()) {
			GenLibInstance instance = eInstances.nextElement();
			while (!root.isLeaf()) {
				if (instance.getAttribute(root.getAttribute()).isNumeric()) {
					if (instance.getValueOfAttribute(root.getAttribute()) < root
							.getValue()) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance
							.getValueOfAttribute(root.getAttribute()));
				}
			}

			int pClass = (int) root.getValue();
			if (pClass != attrIndex) {
				root = individual.getRootNode();
				continue;
			}

			int tClass = (int) instance.getValueOfClass();
			if (tClass == pClass) {
				// add true positives for attribute
				tp += 1;
			}
			top += 1;

			root = individual.getRootNode();
		}

		double precision = tp / top;
		double recall = tp / data.getClassCounts()[attrIndex];

		return (1 + doubleBeta) * precision * recall
				/ (doubleBeta * precision + recall);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected double[] totalConfusionValues(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[] precisionArray = new double[instances.numClasses()];
		double[] recallArray = new double[instances.numClasses()];
		// in the end will serve the purpose of f-measure array
		double[] topArray = new double[instances.numClasses()];

		Enumeration<GenLibInstance> eInstances = instances.getInstances();
		while (eInstances.hasMoreElements()) {
			GenLibInstance instance = eInstances.nextElement();
			while (!root.isLeaf()) {
				if (instance.getAttribute(root.getAttribute()).isNumeric()) {
					if (instance.getValueOfAttribute(root.getAttribute()) < root
							.getValue()) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance
							.getValueOfAttribute(root.getAttribute()));
				}
			}

			int tClass = (int) instance.getValueOfClass();
			int pClass = (int) root.getValue();
			if (tClass == pClass) {
				// add true positives for attribute
				precisionArray[tClass] += 1;
			}

			topArray[pClass] += 1;
			root = individual.getRootNode();
		}

		double[] classCounts = data.getClassCounts();
		for (int i = 0; i < precisionArray.length; i++) {
			// divide true positives (saved in precision array)
			// with all the positives will give recall
			recallArray[i] = precisionArray[i] / classCounts[i];
			// divide with all the test positives will give precision
			precisionArray[i] /= topArray[i];
			// compute f-measure into top-array
			topArray[i] = (1 + doubleBeta) * precisionArray[i] * recallArray[i]
					/ (doubleBeta * precisionArray[i] + recallArray[i]);
		}

		return topArray;

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected double attributeConfusionValue(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double tp = 0, top = 0;

		Enumeration<Instance> eInstances = instances.enumerateInstances();
		while (eInstances.hasMoreElements()) {
			Instance instance = eInstances.nextElement();
			while (!root.isLeaf()) {
				if (instance.attribute(root.getAttribute()).isNumeric()) {
					if (instance.value(root.getAttribute()) < root.getValue()) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance.value(root
							.getAttribute()));
				}
			}

			int pClass = (int) root.getValue();
			if (pClass != attrIndex) {
				root = individual.getRootNode();
				continue;
			}

			int tClass = (int) instance.classValue();
			if (tClass == pClass) {
				tp += 1;
			}
			top += 1;

			root = individual.getRootNode();
		}

		double precision = tp / top;
		double recall = tp / data.getClassCounts()[attrIndex];

		return (1 + doubleBeta) * precision * recall
				/ (doubleBeta * precision + recall);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected double[] totalConfusionValues(Instances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double[] precisionArray = new double[instances.numClasses()];
		double[] recallArray = new double[instances.numClasses()];
		// in the end will serve the purpose of f-measure array
		double[] topArray = new double[instances.numClasses()];

		Enumeration<Instance> eInstances = instances.enumerateInstances();
		while (eInstances.hasMoreElements()) {
			Instance instance = eInstances.nextElement();
			while (!root.isLeaf()) {
				if (instance.attribute(root.getAttribute()).isNumeric()) {
					if (instance.value(root.getAttribute()) < root.getValue()) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance.value(root
							.getAttribute()));
				}
			}

			int tClass = (int) instance.classValue();
			int pClass = (int) root.getValue();
			if (tClass == pClass) {
				precisionArray[tClass] += 1;
			}

			topArray[pClass] += 1;
			root = individual.getRootNode();
		}

		double[] classCounts = data.getClassCounts();
		for (int i = 0; i < precisionArray.length; i++) {
			// divide true positives (saved in precision array)
			// with all the positives will give recall
			recallArray[i] = precisionArray[i] / classCounts[i];
			// divide with all the test positives will give precision
			precisionArray[i] /= topArray[i];
			// compute f-measure into top-array
			topArray[i] = (1 + doubleBeta) * precisionArray[i] * recallArray[i]
					/ (doubleBeta * precisionArray[i] + recallArray[i]);
		}

		return topArray;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getFitnessName() {
		return initName;
	}

}
