package genlib.classifier.classifierextensions;

import genlib.classifier.weka.WekaEvolutionTreeClassifier;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Class that is the extension of classifier if we want it to be used with
 * Instances from Weka.
 * 
 * @author Lukas Surin
 * @see WekaEvolutionTreeClassifier
 * @see Classifier
 *
 */
public abstract class WekaClassifierExtension extends Classifier {
	/** for serialization */
	private static final long serialVersionUID = 5612582565193544791L;

	/**
	 * Abstract method build classifier that should create model with Instances
	 * from parameter data.
	 * 
	 * @param data
	 *            with Instances (from weka) used to build the model
	 * @throws Exception
	 *             thrown when error occured.
	 */
	public abstract void buildClassifier(Instances data) throws Exception;

	/**
	 * Method used to classify Instance (from weka).
	 * 
	 * @param instance
	 *            parameter with instance to be classified
	 * @return classification of instance
	 * @throws Exception
	 *             thrown when error occured
	 */
	public abstract double classifyInstance(Instance instance) throws Exception;
}
