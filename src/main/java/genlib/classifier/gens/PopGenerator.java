package genlib.classifier.gens;

import java.io.Serializable;

import genlib.evolution.individuals.Individual;

public interface PopGenerator<T extends PopGenerator<?>> extends Runnable, Serializable {
	
	public Individual[] getIndividuals();
	public Individual[] createPopulation() throws Exception;
	public String getInfo();	
	public void setInstances(Object data);
	public void setGatherGen(T gatherGen);
	public T copy();
}
