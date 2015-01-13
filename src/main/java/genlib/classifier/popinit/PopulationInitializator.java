package genlib.classifier.popinit;

import genlib.classifier.gens.PopGenerator;
import genlib.evolution.individuals.Individual;

import java.io.Serializable;
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
 * @author kirrie
 * @param <T>
 *            Type of generator for population (TreeGenerator,...)
 * @see PopGenerator
 */
public interface PopulationInitializator<T extends PopGenerator<?>> extends
		Serializable {

	public enum Type {
		DECISION_STUMP,
		TREE;
	}	
	
	public Individual[] getPopulation();

	public void setInstances(Object data);

	public void initPopulation() throws Exception;

	public void setRandomGenerator(Random random);

	public int getPopulationSize();

	public void setPopulationSize(int popSize);

	public T getGenerator();

	public void setGenerator(T generator);
	
	public String objectInfo();
}
