package genlib.splitfunctions;

import java.io.Serializable;

/**
 * Interface that is common for all types of SplitCriteria.
 * 
 * @author Lukas Surin
 *
 * @param <I>
 *            instances type (Instances, GenLibInstances)
 * @param <D>
 *            distribution (Distribution, GenLibDistribution)
 */
public interface SplitCriteria<I, D> extends Serializable {

	/**
	 * Method that computes criteria for instances. It must know what index is
	 * the class one.
	 * 
	 * @param data
	 *            instances
	 * @param classIndex
	 *            index of the class attribute
	 * @return value of the criteria
	 * @throws Exception
	 *             thrown if something wrong happened
	 */
	public double computeCriteria(I data, int classIndex) throws Exception;

	/**
	 * Method that computes criteria for some distribution. This one works right
	 * with the distribution so it does not need instances or classIndex.
	 * 
	 * @param distribution
	 *            of the values
	 * @return value of the criteria
	 * @throws Exception
	 *             thrown if something wrong happened
	 */
	public double computeCriteria(D distribution) throws Exception;

	/**
	 * Method that computes criteria for some distribution. This one works right
	 * with the distribution so it does not need instances or classIndex.
	 * 
	 * @param distribution
	 *            of the values
	 * @return totalIns number of all instances
	 * @throws Exception
	 *             thrown if something wrong happened
	 */
	public double computeCriteria(D distribution, double totalIns)
			throws Exception;

	/**
	 * Method copies the actual split criteria.
	 * @return new instance of this criteria
	 */
	public SplitCriteria<I, D> copy();
}
