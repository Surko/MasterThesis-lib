package genlib.structures.data;

/**
 * Interface for GenLibAttribute that should represents each attribute in
 * GenLibInstances.
 * 
 * @author Lukas Surin
 *
 */
public interface GenLibAttribute {
	/**
	 * Method gets number of distinct values for this attribute
	 * 
	 * @return number of values for this attribute
	 */
	public int numOfValues();

	/**
	 * Gets if the attribute is numeric or not
	 * @return true iff attribute is numeric
	 */
	public boolean isNumeric();

	/**
	 * Gets if the attribute is nominal or not
	 * @return true iff attribute is nominal
	 */
	public boolean isNominal();
}
