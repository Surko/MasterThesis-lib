package genlib.classifier.popinit;

import genlib.classifier.gens.SimpleStumpGenerator;
import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.splitting.InformationGainCriteria;
import genlib.evolution.individuals.TreeIndividual;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import weka.core.Instances;

/**
 * Class can initialize stump population from weka datatype Instances. Methods
 * and structure of this class work exactly as in
 * {@link CombStumpsInitializator}.
 * 
 * @author kirrie
 * @see CombStumpsInitializator
 */
public class WekaCombStumpsInitializator extends CombStumpsInitializator {
	/** for serialization */
	private static final long serialVersionUID = 3949778932252091677L;

	public WekaCombStumpsInitializator(int popSize, int depth, int divideParam,
			boolean resample) {
		super(popSize, depth, divideParam, resample);
	}

	public WekaCombStumpsInitializator(int popSize, int depth, int divideParam,
			boolean resample, TreeGenerator gen) {
		super(popSize, depth, divideParam, resample, gen);
	}

	private void initPopulation(Instances data) throws Exception {
		if (gen == null)
			this.gen = new SimpleStumpGenerator(new InformationGainCriteria());

		int n_attr = data.numAttributes() - 1;
		stumpPopulation = new TreeIndividual[n_attr * divideParam];

		if (nThreads > 1) {
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

			// Here we should be waiting for generating to stop. it's not done!!
			// TODO waiting for stopped generators
			try {
				while (!es.isTerminated())
					wait();
			} catch (Exception e) {

			}

			stumpPopulation = gen.getIndividuals();
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

				System.arraycopy(gen.createPopulation(), 0, stumpPopulation, i
						* n_attr, n_attr);
			}
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
			throw new Exception("Bad type of instances");
		}
	}

}
