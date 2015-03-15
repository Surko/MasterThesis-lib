package genlib.classifier.popinit;

import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.gens.WekaSimpleStumpGenerator;
import genlib.classifier.splitting.InformationGainCriteria;
import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.locales.TextResource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import weka.core.Instances;

/**
 * Class can initialize stump population from weka datatype Instances. Methods
 * and structure of this class work exactly as in {@link RandomStumpCombinator}.
 * 
 * @author kirrie
 * @see RandomStumpCombinator
 */
public class WekaRandomStumpCombinator extends RandomStumpCombinator {
	
	/** for serialization */
	private static final long serialVersionUID = 3949778932252091677L;
	/** name of this initializator */
	public static final String initName = "wRanStump";
	
	public WekaRandomStumpCombinator() {}
	
	public WekaRandomStumpCombinator(int popSize, int depth, int divideParam,
			boolean resample) {
		super(popSize, depth, divideParam, resample);
	}

	public WekaRandomStumpCombinator(int popSize, int depth, int divideParam,
			boolean resample, TreeGenerator gen) {
		super(popSize, depth, divideParam, resample, gen);
	}

	private void initPopulation(Instances data) throws Exception {
		if (gen == null) {
			this.gen = new WekaSimpleStumpGenerator(
					new InformationGainCriteria());
			this.gen.setPopulationInitializator(this);
		}

		// not consistent generator with population initializator, throwing
		// exception
		if (gen.getGeneratorHeight() != 1)
			throw new Exception(String.format(TextResource
					.getString("eConsistencyStumpGen"), gen.getClass().getName()));

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
			// TODO waiting for stopped generators
			synchronized(gen) {
				while (!es.isTerminated())
					gen.wait();
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

				System.arraycopy(gen.createPopulation(), 0, population, i * n_attr,
						n_attr);
			}
			// Saving of generated stumps that are further used for example as a mutation element
			gen.setIndividuals(population);
		}

		// generates population from data into our global stumpPopulation
		combineTrees();
	}

	/**
	 * 
	 * @throws Exception
	 *             if data (saved as Object) is not of weka Instances type.
	 */
	@Override
	public void initPopulation() throws Exception {
		if (data instanceof Instances) {
			initPopulation((Instances) data);
		} else {
			throw new Exception(PermMessages._exc_badins);
		}
	}

	@Override
	public boolean isWekaCompatible() {
		return true;
	}

	@Override
	public String getInitName() {
		return initName;
	}
	
	public String objectInfo() {
		return String.format("type %s;gen %s;depth %s;divide %s;resample %s;threads %s", 
				initName,
				gen.getGenName(),
				maxDepth,
				divideParam,
				resample,
				nThreads);
	}
}
