package genlib.classifier.gens;

import java.io.Serializable;

import genlib.evolution.individuals.Individual;

public interface PopGenerator<T extends Individual> extends Runnable, Serializable {
	
	public T[] getIndividuals();
	public T[] createPopulation() throws Exception;
	public String getInfo();	
	public void setInstances(Object data);
	public void setGatherGen(PopGenerator<T> gatherGen);
	public void setAdditionalOptions(String[] options) throws Exception;
	public boolean isWekaCompatible();
	public String getGenName();
}
