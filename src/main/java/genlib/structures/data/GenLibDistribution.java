package genlib.structures.data;

import java.io.Serializable;

/**
 * Interface that defines the distribution of values in instances object.
 * 
 * @author Lukas Surin
 *
 */
public interface GenLibDistribution extends Serializable {
	/**
	 * Gets the distribution of class values for specific instances
	 * 
	 * @return distribution of classes
	 */
	public double[] getClassCounts();
}
