package genlib.initializators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.generators.DummyTreeGenerator;
import genlib.generators.TreeGenerator;
import genlib.locales.PermMessages;
import genlib.locales.TextResource;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.Node;
import genlib.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import weka.core.Instances;

/**
 * Class can initialize
 * {@link <a href="http://en.wikipedia.org/wiki/Decision_stump">stump</a>}
 * population from our datatype GenLibInstances. It implements
 * PopulationInitializator<TreeGenerator> that describes that this object is
 * population initializator. Generic parameter TreeGenerator serves purpose of
 * further defining which generator this initializator use. Initialization is
 * done by calling {@link #initPopulation()} method implemented from interface
 * PopulationInitializator. Firstly, to initialize, it has to generate stumps
 * via instance of TreeGenerator. Generated stumps are then combined (that's why
 * CombStumpInitializator) at leaves for as long as trees does not reach
 * maxDepth in method {@link #combineTrees()}.
 * 
 * @author Lukas Surin
 * @see PopulationInitializator
 * @see WekaRandomStumpCombinator
 */
public class RandomStumpCombinator extends TreePopulationInitializator {

	/** for serialization */
	private static final long serialVersionUID = 4669352194412819499L;
	/** name of this initializator */
	public static final String initName = "RanStump";

	/**
	 * Default constructor
	 */
	public RandomStumpCombinator() {
	}

	/**
	 * Constructor that sets the population size, maxDepth, divideParam,
	 * resample tag and generator.
	 * 
	 * @param popSize
	 *            population size
	 * @param maxDepth
	 *            maximal depth
	 * @param divideParam
	 *            divide parameter of data set
	 * @param resample
	 *            resampling data set
	 * @param gen
	 *            generator used to generate stumps to combine
	 */
	public RandomStumpCombinator(int popSize, int maxDepth, int divideParam,
			boolean resample, TreeGenerator gen) {
		this.maxHeight = maxDepth;
		this.divideParam = divideParam;
		this.resample = resample;
		this.gen = gen;
		this.popSize = popSize;
		// default random gen
		this.random = Utils.randomGen;
	}

	/**
	 * Constructor that sets the population size, maxDepth, divideParam and
	 * resample tag
	 * 
	 * @param popSize
	 *            population size
	 * @param maxDepth
	 *            maximal depth
	 * @param divideParam
	 *            divide parameter of data set
	 * @param resample
	 *            resampling data set
	 */
	public RandomStumpCombinator(int popSize, int maxDepth, int divideParam,
			boolean resample) {
		this.maxHeight = maxDepth;
		this.divideParam = divideParam;
		this.resample = resample;
		this.popSize = popSize;
		// default random gen
		this.random = Utils.randomGen;
	}

	/**
	 * Method initialized the population using GenLibInstances. Divided or
	 * resampled dataset is used to construct decision trees. Construction can
	 * be done with multiple threads. After generation of trees the combination
	 * of them takes place.
	 * 
	 * @param data
	 *            to be used to initialize
	 * @throws Exception
	 *             thrown if problem occured initializing population
	 */
	private void initPopulation(GenLibInstances data) throws Exception {
		if (gen == null) {
			this.gen = new DummyTreeGenerator();
			this.gen.setPopulationInitializator(this);
		}

		// not consistent generator with population initializator, throwing
		// exception
		if (gen.getGeneratorHeight() != 1)
			throw new Exception(String.format(TextResource
					.getString("eConsistencyStumpGen"), gen.getClass()
					.getName()));

		int n_attr = data.numAttributes() - 1;
		population = new TreeIndividual[n_attr * divideParam];

		if (nThreads > 1) {
			gen.setIndividuals(population);
			ExecutorService es = Executors.newFixedThreadPool(nThreads);

			for (int i = 0; i < divideParam; i++) {
				Instances dataPart = null;
				if (!resample) {
					// size of instances = data.length / divideParam.
				} else {
					// instances of same length as training data. Sampling with
					// replacement
				}
				TreeGenerator popG = gen.copy();
				popG.setInstances(dataPart);
				// Gathering of created individuals into TreeGenerator
				popG.setGatherGen(gen);

				es.submit(popG);
			}

			es.shutdown();

			// Here we should be waiting for generating to stop. it's not done!!
			try {
				es.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {

			}

			population = gen.getIndividuals();
		} else {
			for (int i = 0; i < divideParam; i++) {
				Instances dataPart = null;
				if (!resample) {
					// size of instances = data.length / divideParam.
				} else {
					// instances of same length as training data. Sampling with
					// replacement
				}

				gen.setInstances(dataPart);

				System.arraycopy(gen.createPopulation(), 0, population, i
						* n_attr, n_attr);
			}
			gen.setIndividuals(population);
		}

		// generates population from data into our global stumpPopulation
		combineTrees();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initPopulation() throws Exception {
		if (data instanceof GenLibInstances) {
			initPopulation((GenLibInstances) data);
		} else {
			throw new Exception(PermMessages._exc_badins);
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
		TreeIndividual[] combPopulation = new TreeIndividual[popSize];
		int max = population.length;

		for (int j = 0; j < popSize; j++) {
			TreeIndividual chosen = population[random.nextInt(max)];

			TreeIndividual combined = new TreeIndividual(chosen);

			combPopulation[j] = combined;

			// we already have trees in stumppopulation with needed depth
			if (maxHeight == 2)
				continue;

			combineAtNode(combined.getRootNode());
		}
		population = combPopulation;
	}

	/**
	 * Method which is used to combine individuals. Combination is done by
	 * replacing leaf with randomly chosen stump to construct bigger and bigger
	 * trees.
	 * 
	 * @param node
	 *            which we combine
	 */
	private void combineAtNode(Node node) {

		Node[] childs = node.getChilds();
		TreeIndividual chosen = null;
		for (int childIndex = 0; childIndex < node.getChildCount(); childIndex++) {
			chosen = population[random.nextInt(population.length)];
			childs[childIndex] = chosen.getRootNode().copy();
			childs[childIndex].setParent(node);
		}
		// Can do because we know that we are combining stumps with depth = 1
		combineNodes(childs, maxHeight - 2);
	}

	/**
	 * Method which works the same as {@link #combineAtNode(Node)} but for more
	 * nodes until d == 0.
	 * 
	 * @param nodes
	 *            to be combined
	 * @param d
	 *            actual height
	 */
	private void combineNodes(Node[] nodes, int d) {
		if (d == 0) {
			return;
		}

		TreeIndividual chosen = null;
		for (int k = 0; k < nodes.length; k++) {
			Node[] childs = nodes[k].getChilds();
			for (int childIndex = 0; childIndex < nodes[k].getChildCount(); childIndex++) {
				chosen = population[random.nextInt(population.length)];
				childs[childIndex] = chosen.getRootNode().copy();
				childs[childIndex].setParent(nodes[k]);
			}
			combineNodes(childs, d - 1);
		}

	}

	/**
	 * Method that returns generated stumps
	 * 
	 * @return individuals that represents stumps
	 */
	public TreeIndividual[] getGeneratedStumps() {
		return gen.getIndividuals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWekaCompatible() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInitName() {
		return initName;
	}

	/**
	 * {@inheritDoc}
	 */
	public String objectInfo() {
		return String.format(
				"type %s;gen %s;depth %s;divide %s;resample %s;threads %s",
				initName, gen.getGenName(), maxHeight, divideParam, resample,
				nThreads);
	}

}
