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
	 * than 1.
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

	/**
	 * Randomize instances with random object
	 * 
	 * @param random
	 *            object with which we randomize
	 */
	public void randomize(Random random);

	/**
	 * Get part of this GenLibInstances bounded by indeces. We copy
	 * <strong>count</strong> instances starting with index
	 * <strong>from</strong>.
	 * 
	 * @param from
	 *            index where we start copying
	 * @param count
	 *            how many instances we copy
	 * @return new GenLibInstances with copied instances
	 */
	public GenLibInstances getPart(int from, int count);

	/**
	 * Creates a new dataset of the same size using random sampling with
	 * replacement.
	 *
	 * @param random
	 *            object with which we resample
	 * @return newly created GenLibInstances with resampled
	 */
	public GenLibInstances resample(Random random);

	public Enumeration<GenLibInstance> getInstances();

	public GenLibDistribution getDistribution();

}
