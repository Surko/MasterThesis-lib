package genlib.classifier.popinit;

import genlib.classifier.gens.SimpleStumpGenerator;
import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.splitting.InformationGainCriteria;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.ArrayInstances;
import genlib.structures.Node;

import java.util.Random;

/**
 * Class can initialize
 * {@link <a href="http://en.wikipedia.org/wiki/Decision_stump">stump</a>}
 * population from our datatype ArrayInstances. It implements
 * PopulationInitializator<TreeGenerator> that describes that this object is
 * population initializator. Generic parameter TreeGenerator serves purpose of
 * further defining which generator this initializator use. Initialization is
 * done by calling {@link #initPopulation()} method implemented from interface
 * PopulationInitializator. Firstly, to initialize, it has to generate stumps
 * via instance of TreeGenerator. Generated stumps are then combined (that's why
 * CombStumpInitializator) at leaves for as long as trees does not reach
 * maxDepth in method {@link #combineTrees()}. 
 * 
 * @author kirrie
 * @see PopulationInitializator
 * @see WekaCombStumpsInitializator
 */
public class CombStumpsInitializator implements
		PopulationInitializator<TreeGenerator> {

	/** for serialization */
	private static final long serialVersionUID = 4669352194412819499L;

	/** depth of generated trees. */
	protected int maxDepth;
	/** number of division of trainin data */
	protected int divideParam;
	/** only resampling instead of dividing */
	protected boolean resample;
	/** Individuals that makes this population */
	protected TreeIndividual[] stumpPopulation;

	/** Generator of tree population */
	protected TreeGenerator gen;
	/** Random seeded object unique for this run of algorithm */
	protected Random random;
	/** Object of all instances */
	protected Object data;
	/** Number of threads that will be creating population */
	protected int nThreads;
	/** Final population size */
	protected int popSize;

	public CombStumpsInitializator(int popSize, int maxDepth, int divideParam,
			boolean resample, TreeGenerator gen) {
		this.maxDepth = maxDepth;
		this.divideParam = divideParam;
		this.resample = resample;
		this.gen = gen;
		this.popSize = popSize;
	}

	public CombStumpsInitializator(int popSize, int maxDepth, int divideParam,
			boolean resample) {
		this.maxDepth = maxDepth;
		this.divideParam = divideParam;
		this.resample = resample;
		this.gen = new SimpleStumpGenerator();
		this.popSize = popSize;
	}

	private void initPopulation(ArrayInstances data) throws Exception {
		if (gen == null)
			this.gen = new SimpleStumpGenerator(new InformationGainCriteria());

		combineTrees();
	}

	/**
	 * 
	 * @throws Exception
	 *             if data (saved as Object) is not of ArrayInstances type.
	 */
	@Override
	public void initPopulation() throws Exception {
		if (data instanceof ArrayInstances) {
			initPopulation((ArrayInstances) data);
		} else {
			throw new Exception("Bad type of instances");
		}
	}

	/**
	 * Shared method with stumps population initializators that combines trees
	 * created inside initPopulation method. Trees are combined at each child
	 * node so if chosen tree contains child it will be replaced with other tree
	 * chosen from population. It combines trees as long as combined tree did
	 * not reached depth saved in field depth. Trees to be combined have to have
	 * depth = 1 because stumps are simple trees with only root and leaves.
	 */
	protected void combineTrees() {
		TreeIndividual[] combinedTrees = new TreeIndividual[popSize];
		int max = stumpPopulation.length;

		for (int j = 0; j < popSize; j++) {
			TreeIndividual chosen = stumpPopulation[random.nextInt(max)];

			TreeIndividual combined = new TreeIndividual(chosen);
			combinedTrees[j] = combined;

			// we already have trees in stumppopulation with needed depth
			if (maxDepth == 1)
				continue;

			combineNode(combined.getRootNode());
		}

		this.stumpPopulation = combinedTrees;
	}

	private void combineNode(Node node) {

		Node[] childs = node.getChilds();
		TreeIndividual chosen = null;
		for (int childIndex = 0; childIndex < node.getChildCount(); childIndex++) {
			chosen = stumpPopulation[random.nextInt(stumpPopulation.length)];
			childs[childIndex] = chosen.getRootNode().copy();
		}
		combineNodes(childs, 2);
	}

	private void combineNodes(Node[] nodes, int d) {
		if (d >= maxDepth) {
			return;
		}

		TreeIndividual chosen = null;
		for (int k = 0; k < nodes.length; k++) {
			Node[] childs = nodes[k].getChilds();
			for (int childIndex = 0; childIndex < nodes[k].getChildCount(); childIndex++) {
				chosen = stumpPopulation[random.nextInt(stumpPopulation.length)];
				childs[childIndex] = chosen.getRootNode().copy();
			}
			combineNodes(childs, d + 1);
		}

	}

	/**
	 * Method that returns population of generated individuals from
	 * TreeGenerator.
	 */
	public TreeIndividual[] getPopulation() {
		return stumpPopulation;
	}

	public int getDepth() {
		return maxDepth;
	}

	public int getDivideParam() {
		return divideParam;
	}

	@Override
	public TreeGenerator getGenerator() {
		return gen;
	}

	public void setRandomGenerator(Random random) {
		this.random = random;
	}

	public void setDepth(int depth) {
		this.maxDepth = depth;
	}

	public void setDivideParam(int divideParam) {
		this.divideParam = divideParam;
	}

	public void setResample(boolean resample) {
		this.resample = resample;
	}

	@Override
	public void setGenerator(TreeGenerator gen) {
		this.gen = gen;
	}

	@Override
	public void setInstances(Object data) {
		this.data = data;
	}

	@Override
	public int getPopulationSize() {
		return popSize;
	}

	@Override
	public void setPopulationSize(int popSize) {
		this.popSize = popSize;
	}
}
