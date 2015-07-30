package genlib.classifier;

import genlib.classifier.origin.AbstractGenLibEvolution;
import genlib.classifier.weka.WekaEvolutionTreeClassifier;
import genlib.structures.Data;

/**
 * Main interface that must be implemented by newly created classifiers. It is
 * not dependent on weka or any other lib.
 * 
 * @author Lukas Surin
 * @see AbstractGenLibEvolution
 * @see WekaEvolutionTreeClassifier
 */
public interface Classifier {

	/**
	 * Method used to build classifier that should create model with instances
	 * from parameter data.
	 * 
	 * @param data
	 *            with instances used to build the model
	 * @throws Exception
	 *             thrown when error occured.
	 */
	public void buildClassifier(Data data) throws Exception;

	/**
	 * Method used to set the options of the classifier
	 * 
	 * @param options
	 *            array to setup the classifier
	 * @throws Exception
	 *             thrown when error occured
	 */
	public void setOptions(String[] options) throws Exception;

	/**
	 * Method used to classify instances from data parameter.
	 * 
	 * @param data
	 *            parameter with instances used to classify
	 * @return array with classification for each instance
	 * @throws Exception
	 *             thrown when error occured
	 */
	public double[] classifyData(Data data) throws Exception;

	/**
	 * Method which creates the dataset which should be classified.
	 * 
	 * @param sFile
	 *            string path of file
	 * @return Data object with loaded dataset
	 * @throws Exception
	 *             if some problem occured when loading file
	 */
	public Data makeDataFromFile(String sFile) throws Exception;
}
