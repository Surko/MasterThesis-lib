package genlib.classifier.gens;

import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class WekaJ48TreeGenerator extends TreeGenerator  {
	
	/** for serialization */
	private static final long serialVersionUID = -266323902512927931L;	
	private J48 tree;
	
	public WekaJ48TreeGenerator() {
		this.tree = new J48();		
	}
	
	public WekaJ48TreeGenerator(String[] options) throws Exception {
		this.tree = new J48();
		this.tree.setOptions(options);		
	}	

	@Override
	public Individual[] createPopulation() throws Exception {
		if (data instanceof Instances) {
			return createPopulation((Instances)data);
		} else {
			throw new Exception("Bad type of instances");
		}
	}

	private TreeIndividual[] createPopulation(Instances data) throws Exception {
		tree.buildClassifier(data);
		String prefixTree = tree.prefix();
		return individuals;		
	}
	
	public void setOptions(String[] options) throws Exception{
		this.tree.setOptions(options);
	}
	
	@Override
	public TreeGenerator copy() {
		return null;
	}

	public J48 getJ48() {
		return tree;
	}
	
	@Override
	public String getGenName() {		
		return "J48";
	}
	
	@Override
	public String getInfo() {
		return "Tree generator which generates C4.5 trees via weka J48 algorithm";
	}

}
