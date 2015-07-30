package genlib.classifier.classifierextensions;

import genlib.classifier.origin.AbstractGenLibEvolution;
import genlib.structures.data.GenLibInstance;
import genlib.structures.data.GenLibInstances;

/**
 * Class that is the extension of classifier if we want it to be used with
 * GenLibInstances.
 * 
 * @author Lukas Surin
 * @see AbstractGenLibEvolution
 *
 */
public interface GenLibClassifierExtension {

	/**
	 * Method used to build classifier that should create model with
	 * GenLibInstances from parameter data.
	 * 
	 * @param data
	 *            with GenLibInstances used to build the model
	 * @throws Exception
	 *             thrown when error occured.
	 */
	public void buildClassifier(GenLibInstances data) throws Exception;

	/**
	 * Method used to classify GenLibInstance.
	 * 
	 * @param instance
	 *            parameter with instance to be classified
	 * @return classification of instance
	 * @throws Exception
	 *             thrown when error occured
	 */
	public double classifyInstance(GenLibInstance instance) throws Exception;
}
