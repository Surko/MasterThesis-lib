package genlib.structures.data;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Random;

/**
 * GenLibInstances is the object that contains data to be used in
 * classification. It is further inspired by its counterpart Instances from weka
 * lib, but it's not dependent on it, so it can be used in some extension of
 * this genlib that fully implements all of the functionality.
 * 
 * @author Lukas Surin
 *
 */
public interface GenLibInstances extends Serializable {
	/**
	 * Number of instances in this object
	 * 
	 * @return number of instances
	 */
	public int numInstances();

	/**
	 * Number of attributes in this instance.
	 * 
	 * @return number of attributes
	 */
	public int numAttributes();

	/**
	 * Number of classes for class attribute. If the class attribute is numeric
	 * than -1.
	 * 
	 * @return number of class values
	 */
	public int numClasses();

	/**
	 * Method which will return testData. We take inspiration in weka lib.
	 * 
	 * @param numFolds
	 *            number of folds to split all data
	 * @param numFold
	 *            fold which we return
	 * @return Newly created testData from this instance
	 */
	public GenLibInstances testData(int numFolds, int numFold);

	public void randomize(Random random);

	public GenLibInstances getPart(int allParts, int partNumber);

	public GenLibInstances resample(Random random);

	public Enumeration<GenLibInstance> getInstances();

	public GenLibDistribution getDistribution();

}
