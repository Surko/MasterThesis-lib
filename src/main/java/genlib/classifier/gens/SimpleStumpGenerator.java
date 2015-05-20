package genlib.classifier.gens;

import genlib.classifier.splitting.SplitCriteria;
import genlib.evolution.individuals.TreeIndividual;

public abstract class SimpleStumpGenerator extends TreeGenerator {

	/** for serialization */
	private static final long serialVersionUID = -4049246410003742780L;

	public SimpleStumpGenerator(SimpleStumpGenerator gen) {
		this.genHeight = gen.getGeneratorHeight();
		this.splitCriteria = splitCriteria.copy();
	}

	/**
	 * Constructor of SimpleStumpGenerator that extends Generator of trees for
	 * init population. We set the maxDepth field to 1 because of simple stump
	 * type of trees that we generate.
	 */
	public SimpleStumpGenerator() {
		this.genHeight = 2;
	}

	/**
	 * Constructor of SimpleStumpGenerator that extends Generator of trees for
	 * init population. We set the maxDepth field to 1 because of simple stump
	 * type of trees that we generate. SplitCriteria parameter is going to serve
	 * a role of measure which we use for generating new simple stump trees.
	 * 
	 * @param splitCriteria
	 *            Splitting criteria for which we generate trees
	 */
	public SimpleStumpGenerator(SplitCriteria splitCriteria) {
		this.genHeight = 2;
		this.splitCriteria = splitCriteria;
	}

	@Override
	public String getInfo() {
		return "Single tree generator that generates stump decision trees";
	}

	@Override
	public abstract TreeIndividual[] createPopulation() throws Exception;

	public abstract SimpleStumpGenerator copy();

	/**
	 * Method which returns true iff weka lib is needed to generate trees.
	 * Without weka, runtime exception should be thrown when initializing this
	 * generator
	 * 
	 * @return true iff is weka dependent
	 */
	public abstract boolean isWekaDependent();

	public abstract String getGenName();

	@Override
	public abstract void setAdditionalOptions(String[] options)
			throws Exception;

	public abstract void setParam(String param);
}
