package genlib.classifier.gens;

import genlib.evolution.individuals.Individual;

import java.io.Serializable;

public interface PopGenerator<T extends Individual> extends Runnable,
		Serializable {

	public T[] getIndividuals();

	public T[] createPopulation() throws Exception;

	public String getInfo();

	public Class<T> getIndividualClassType();

	public void setInstances(Object data);

	public void setGatherGen(PopGenerator<T> gatherGen);

	public void setAdditionalOptions(String[] options) throws Exception;

	public void setParam(String param) throws Exception;

	public boolean isWekaDependent();

	public String getGenName();
}
