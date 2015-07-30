package genlib.initializators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.generators.TreeGenerator;
import genlib.generators.WekaSimpleStumpGenerator;
import genlib.locales.PermMessages;
import genlib.locales.TextResource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import weka.core.Instances;

/**
 * Class can initialize stump population from weka datatype Instances. Methods
 * and structure of this class work exactly as in {@link RandomStumpCombinator}.
 * 
 * @author Lukas Surin
 * @see RandomStumpCombinator
 */
public class WekaRandomStumpCombinator extends RandomStumpCombinator {

	/** for serialization */
	private static final long serialVersionUID = 3949778932252091677L;
	/** name of this initializator */
	public static final String initName = "wRanStump";

	/**
	 * Default constructor
	 */
	public WekaRandomStumpCombinator() {
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
	public WekaRandomStumpCombinator(int popSize, int depth, int divideParam,
			boolean resample) {
		super(popSize, depth, divideParam, resample);
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
	public WekaRandomStumpCombinator(int popSize, int depth, int divideParam,
			boolean resample, TreeGenerator gen) {
		super(popSize, depth, divideParam, resample, gen);
	}

	/**
	 * Method initialized the population using Instances. Divided or
	 * resampled dataset is used to construct decision trees. Construction can
	 * be done with multiple threads. After generation of trees the combination
	 * of them takes place.
	 * 
	 * @param data
	 *            to be used to initialize
	 * @throws Exception
	 *             thrown if problem occured initializing population
	 */
	private void initPopulation(Instances data) throws Exception {
		if (gen == null) {
			this.gen = new WekaSimpleStumpGenerator();
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
				Instances dataPart;
				if (!resample) {
					// size of instances = data.length / divideParam.
					dataPart = data.testCV(divideParam, i);
				} else {
					// instances of same length as training data. Sampling with
					// replacement
					dataPart = data.resample(random);
				}
				TreeGenerator popG = gen.copy();
				popG.setInstances(dataPart);
				// Gathering of created individuals into TreeGenerator
				popG.setGatherGen(gen);

				es.submit(popG);
			}

			es.shutdown();

			// Here we should be waiting for generators to stop. it's not done!!
			try {
				es.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {

			}

			population = gen.getIndividuals();
		} else {
			for (int i = 0; i < divideParam; i++) {
				Instances dataPart;
				if (!resample) {
					// size of instances = data.length / divideParam.
					dataPart = data.testCV(divideParam, i);
				} else {
					// instances of same length as training data. Sampling with
					// replacement
					dataPart = data.resample(random);
				}

				gen.setInstances(dataPart);

				System.arraycopy(gen.createPopulation(), 0, population, i
						* n_attr, n_attr);
			}
			// Saving of generated stumps that are further used for example as a
			// mutation element
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
		if (data.isInstances()) {
			initPopulation(data.toInstances());
		} else {
			throw new Exception(PermMessages._exc_badins);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWekaCompatible() {
		return true;
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
