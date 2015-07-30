package genlib.generators;

import genlib.evolution.individuals.Individual;

import java.io.Serializable;

/**
 * Interface that should be implemented by all individual generators.
 * 
 * @author Lukas Surin
 *
 * @param <T>
 *            type of individuals
 */
public interface Generator<T extends Individual> extends Runnable, Serializable {

	/**
	 * Method gets the individuals collected in this generator
	 * 
	 * @return
	 */
	public T[] getIndividuals();

	/**
	 * Method creates the individual/individuals.
	 * 
	 * @return individuals as array
	 * @throws Exception
	 *             when some problem occured creating individuals
	 */
	public T[] createPopulation() throws Exception;

	/**
	 * Method generates information about generator
	 * 
	 * @return info about generator
	 */
	public String getInfo();

	/**
	 * Method gets individual class type for this instance
	 * 
	 * @return individual class
	 */
	public Class<T> getIndividualClassType();

	/**
	 * Method sets the instances used to generate individuals.
	 * 
	 * @param data
	 *            used to generate individuals
	 */
	public void setInstances(Object data);

	/**
	 * Method sets the gathering generator, which can be used to pile up the
	 * individuals.
	 * 
	 * @param gatherGen
	 *            generator that serves as gatherer
	 */
	public void setGatherGen(Generator<T> gatherGen);

	/**
	 * Method sets the additional options for this generator
	 * 
	 * @param options
	 *            of this generator
	 * @throws Exception
	 *             thrown when some problem occurred
	 */
	public void setAdditionalOptions(String[] options) throws Exception;

	/**
	 * Method sets the parameters for this generator
	 * 
	 * @param param
	 *            string with parameters to be set
	 * @throws Exception
	 *             thrown when some problem occured parsing parameters
	 */
	public void setParam(String param) throws Exception;

	/**
	 * Method gets if the generator depends on weka.
	 * 
	 * @return true if generator depends on weka
	 */
	public boolean isWekaDependent();

	/**
	 * Method returns name of this generator
	 * 
	 * @return generator name
	 */
	public String getGenName();
}
