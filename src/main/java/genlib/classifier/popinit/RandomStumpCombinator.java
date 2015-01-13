package genlib.classifier.popinit;

import genlib.classifier.gens.SimpleStumpGenerator;
import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.splitting.InformationGainCriteria;
import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.locales.TextResource;
import genlib.structures.ArrayInstances;
import genlib.structures.Node;
import genlib.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import weka.core.Instances;

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
 * @see WekaRandomStumpCombinator
 */
public class RandomStumpCombinator extends TreePopulationInitializator {

	/** for serialization */
	private static final long serialVersionUID = 4669352194412819499L;

	/** Individuals that makes this population */
	protected TreeIndividual[] stumps;
	/** Number of threads that will be creating population */
	protected int nThreads;

	public RandomStumpCombinator(int popSize, int maxDepth, int divideParam,
			boolean resample, TreeGenerator gen) {
		this.maxDepth = maxDepth;
		this.divideParam = divideParam;
		this.resample = resample;
		this.gen = gen;
		this.popSize = popSize;
		// default random gen
		this.random = Utils.randomGen;
	}

	public RandomStumpCombinator(int popSize, int maxDepth, int divideParam,
			boolean resample) {
		this.maxDepth = maxDepth;
		this.divideParam = divideParam;
		this.resample = resample;		
		this.popSize = popSize;
		// default random gen
		this.random = Utils.randomGen;
	}

	private void initPopulation(ArrayInstances data) throws Exception {			
		if (gen == null)
			this.gen = new SimpleStumpGenerator(new InformationGainCriteria());
		
		// not consistent generator with population initializator, throwing
		// exception
		if (gen.getGeneratorDepth() != 1)
			throw new Exception(String.format(TextResource
					.getString("eConsistencyStumpGen"), gen.getClass().getName()));


		int n_attr = data.getNumOfAttributes() - 1;
		stumps = new TreeIndividual[n_attr * divideParam];

		if (nThreads > 1) {
			gen.setIndividuals(stumps);
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
			// TODO waiting for stopped generators
			try {
				while (!es.isTerminated())
					wait();
			} catch (Exception e) {

			}

			stumps = gen.getIndividuals();
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

				System.arraycopy(gen.createPopulation(), 0, stumps, i
						* n_attr, n_attr);
			}
			gen.setIndividuals(stumps);
		}

		// generates population from data into our global stumpPopulation
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
		population = new TreeIndividual[popSize];
		int max = stumps.length;

		for (int j = 0; j < popSize; j++) {
			TreeIndividual chosen = stumps[random.nextInt(max)];

			TreeIndividual combined = new TreeIndividual(chosen);

			population[j] = combined;

			// we already have trees in stumppopulation with needed depth
			if (maxDepth == 1)
				continue;

			combineAtNode(combined.getRootNode());
		}
	}

	private void combineAtNode(Node node) {

		Node[] childs = node.getChilds();
		TreeIndividual chosen = null;
		for (int childIndex = 0; childIndex < node.getChildCount(); childIndex++) {
			chosen = stumps[random.nextInt(stumps.length)];
			childs[childIndex] = chosen.getRootNode().copy();
			childs[childIndex].setParent(node);
		}
		// Can do because we know that we are combining stumps with depth = 1
		combineNodes(childs, maxDepth - 2);
		node.setTreeDepthForced(maxDepth);
	}

	private void combineNodes(Node[] nodes, int d) {
		if (d == 0) {
			return;
		}

		TreeIndividual chosen = null;
		for (int k = 0; k < nodes.length; k++) {
			Node[] childs = nodes[k].getChilds();
			for (int childIndex = 0; childIndex < nodes[k].getChildCount(); childIndex++) {
				chosen = stumps[random.nextInt(stumps.length)];
				childs[childIndex] = chosen.getRootNode().copy();
				childs[childIndex].setParent(nodes[k]);
			}
			combineNodes(childs, d - 1);
			nodes[k].setTreeDepthForced(d);

		}

	}

	public TreeIndividual[] getGeneratedStumps() {
		return stumps;
	}
	
	public String objectInfo() {
		return String.format("type=%s;gen=%s;depth=%s;divide=%s;resample=%s;threads=%s", 
				Type.DECISION_STUMP,
				gen.getGenName(),
				maxDepth,
				divideParam,
				resample,
				nThreads);
	}

}
