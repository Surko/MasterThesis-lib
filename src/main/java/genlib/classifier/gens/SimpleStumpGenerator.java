package genlib.classifier.gens;

import genlib.classifier.splitting.SplitCriteria;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.ArrayInstances;

public class SimpleStumpGenerator extends TreeGenerator {	

	/** for serialization */
	private static final long serialVersionUID = -4049246410003742780L;
	public static final String initName = "SSGen";
	public SimpleStumpGenerator(SimpleStumpGenerator gen) {
		this.genHeight = gen.getGeneratorHeight();
		this.splitCriteria = splitCriteria.copy();		
	}

	/**
	 * Constructor of SimpleStumpGenerator that extends Generator of trees for
	 * init population. We set the maxDepth field to 1 because of simple stump type of trees
	 * that we generate.
	 */
	public SimpleStumpGenerator() {
		this.genHeight = 2;
	}

	/**
	 * Constructor of SimpleStumpGenerator that extends Generator of trees for
	 * init population. We set the maxDepth field to 1 because of simple stump type of trees
	 * that we generate. SplitCriteria parameter is going to serve a role of measure
	 * which we use for generating new simple stump trees.
	 * @param splitCriteria Splitting criteria for which we generate trees
	 */
	public SimpleStumpGenerator(SplitCriteria splitCriteria) {		
		this.genHeight = 2;	
		this.splitCriteria = splitCriteria;
	}

	@Override
	public TreeIndividual[] createPopulation() throws Exception {
		if (data instanceof ArrayInstances) {
			return createPopulation((ArrayInstances)data);
		} else {
			throw new Exception("Bad type of instances");
		}
	}

	private TreeIndividual[] createPopulation(ArrayInstances data) throws Exception {
		TreeIndividual[] population = new TreeIndividual[1];

		return population;
	}

	public SimpleStumpGenerator copy() {
		return new SimpleStumpGenerator(this);
	}

	@Override
	public String getInfo() {
		return "Single tree generator that generates stump decision trees";
	}
	
	public boolean isWekaCompatible() {
		return false;
	}
	
	public String getGenName() {
		return initName;
	}

}
