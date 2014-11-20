package genlib.classifier.popinit;

import genlib.classifier.gens.PopGenerator;
import genlib.evolution.individuals.Individual;

import java.io.Serializable;
import java.util.Random;

/**
 * PopInitializator interface that is implemented by classes which are supposed
 * to make init population for an evolutionary algorithm. This interface is not
 * dependent on any library (weka,...) so it can be implemented in either way
 * what current project actually uses.
 * 
 * @author kirrie
 * @param <T>
 *            Type of generator for population (TreeGenerator,...)
 * @see PopGenerator
 */
public interface PopulationInitializator<T extends PopGenerator<?>> extends
		Serializable {

	public Individual[] getPopulation();

	public void setInstances(Object data);

	public void initPopulation() throws Exception;

	public void setRandomGenerator(Random random);

	public int getPopulationSize();

	public void setPopulationSize(int popSize);

	public T getGenerator();

	public void setGenerator(T generator);
}
