package genlib.initializators;

import genlib.evolution.individuals.Individual;
import genlib.generators.Generator;
import genlib.structures.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * PopInitializator interface that is implemented by classes which are supposed
 * to make init population for an evolutionary algorithm. This interface is not
 * dependent on any library (weka,...) so it can be implemented in either way
 * what current project actually uses. Initialization is done by calling
 * {@link #initPopulation()} and it generates individuals in a form of
 * Individual object. That's because in some cases of population initializators
 * we can compute fitness function of these generated individuals right as they
 * are created. Fill the population pool is then only matter of copying
 * individuals with their apropriate fitness. It eases up the computation
 * because otherwise we should have to first compute fitness (test instances)
 * after calling initPopulation (that itself traverse the tree and tests
 * instances) and then copying.
 * 
 * @author Lukas Surin
 * @param <T>
 *            Type of generator for population (TreeGenerator,...)
 * @see Generator
 */
public interface PopulationInitializator<T extends Individual> extends
		Serializable {

	/**
	 * Method gets the initialized population
	 * 
	 * @return starting population
	 */
	public T[] getPopulation();

	/**
	 * Method gets the origin population that is further processed.
	 * 
	 * @return origin population
	 */
	public T[] getOriginPopulation();

	/**
	 * Method initializes the population, call the individual generator, process
	 * the generated individuals and copies them until some size is reached.
	 * 
	 * @throws Exception
	 *             if some problem occured
	 */
	public void initPopulation() throws Exception;

	/**
	 * Method sets the parameters for this population initializator.
	 * 
	 * @param param
	 *            string with parameters for this initializator
	 */
	public void setParam(String param);

	/**
	 * Method sets the data used to initialize population
	 */
	public void setData(Data data);

	/**
	 * Method sets the random generator
	 * 
	 * @param random
	 *            object
	 */
	public void setRandomGenerator(Random random);

	/**
	 * Method sets how many individuals will be generated and initialized
	 * 
	 * @param popSize
	 *            number of individuals
	 */
	public void setPopulationSize(int popSize);

	/**
	 * Method sets the generator used to generate individuals
	 * 
	 * @param generator
	 *            to be used to generate individuals
	 */
	public void setGenerator(ArrayList<? extends Generator<T>> generator);

	/**
	 * Method gets number of individuals that must be generated
	 * 
	 * @return number of individuals to generate
	 */
	public int getPopulationSize();

	/**
	 * Method returns the generator used to generate individuals
	 * 
	 * @return individual generator
	 */
	public Generator<T> getGenerator();

	/**
	 * Method gets the name of this population initializator.
	 * 
	 * @return name of this initializator
	 */
	public String getInitName();

	/**
	 * Method tests if the initializer is compatible with weka.
	 * 
	 * @return true iff initializer is compatible with weka
	 */
	public boolean isWekaCompatible();

	/**
	 * Method gets the info about object
	 * 
	 * @return object info
	 */
	public String objectInfo();

}
