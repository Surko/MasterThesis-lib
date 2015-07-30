package genlib.structures.data;

/**
 * Instance that represents each one of the instance in GenLibInstances.
 * 
 * @author Lukas Surin
 *
 */
public interface GenLibInstance {
	/**
	 * Gets the attribute at index
	 * 
	 * @param index
	 *            of attribute
	 * @return GenLibAttribute of some attribute
	 */
	public GenLibAttribute getAttribute(int index);

	/**
	 * Gets the value of the attribute at specific index
	 * 
	 * @param index
	 *            of the attribute
	 * @return value of specific attribute
	 */
	public double getValueOfAttribute(int index);

	/**
	 * Gets the class attribute of this instance
	 * 
	 * @return GenLibAttribute of class
	 */
	public GenLibAttribute getClassAttribute();

	/**
	 * Gets the class value of this instance
	 * 
	 * @return value of class attribute
	 */
	public double getValueOfClass();
}
