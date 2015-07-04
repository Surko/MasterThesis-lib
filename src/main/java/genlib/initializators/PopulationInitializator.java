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

	public T[] getPopulation();

	public T[] getOriginPopulation();

	public void initPopulation() throws Exception;

	public void setParam(String param);

	public void setData(Data data);

	public void setRandomGenerator(Random random);

	public void setPopulationSize(int popSize);

	public void setGenerator(ArrayList<? extends Generator<T>> generator);

	public int getPopulationSize();

	public Generator<T> getGenerator();

	public String getInitName();

	public boolean isWekaCompatible();

	public String objectInfo();

}
