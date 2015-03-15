package genlib.classifier.popinit;

import genlib.annotations.PopInitAnnot;
import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.gens.WekaJ48TreeGenerator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.structures.ArrayInstances;
import genlib.utils.Utils;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import weka.core.Instances;

public class CompletedTrees extends TreePopulationInitializator {

	/** for serialization */
	private static final long serialVersionUID = 1312621842322682763L;
	/** name of this initializator */
	public static final String initName = "CompTree";
	
	public CompletedTrees() {
	}

	public CompletedTrees(int popSize, int divideParam, boolean resample) {
		this.popSize = popSize;
		this.divideParam = divideParam;
		this.resample = resample;
		this.random = Utils.randomGen;
	}

	@SuppressWarnings("unchecked")
	private void initPopulation(ArrayInstances data) throws Exception {
		if (gen == null) {
			this.gen = new WekaJ48TreeGenerator(new String[] { "-C", "0.25",
					"-M", "2" });
			this.gen.setPopulationInitializator(this);
		}

		attrIndexMap = new HashMap<>();
		attrValueIndexMap = new HashMap[data.numAttributes()];
		Utils.makeAttrMap(data, attrIndexMap, attrValueIndexMap);

		int multCoef = 1;
		population = new TreeIndividual[multCoef * divideParam];

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
			synchronized (gen) {
				while (!es.isTerminated())
					gen.wait();
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
						* multCoef, multCoef);
			}

			gen.setIndividuals(population);
		}

		fillPopulation();
	}

	protected void fillPopulation() {
		TreeIndividual[] filledPopulation = new TreeIndividual[popSize];

		int max = population.length;

		for (int j = 0; j < popSize; j++) {
			TreeIndividual chosen = population[random.nextInt(max)];

			TreeIndividual tree = new TreeIndividual(chosen);

			filledPopulation[j] = tree;
		}

		this.population = filledPopulation;
	}

	@Override
	public void initPopulation() throws Exception {
		if (data instanceof ArrayInstances) {
			initPopulation((ArrayInstances) data);
		} else {
			throw new Exception(PermMessages._exc_badins);
		}
	}

	@Override
	public boolean isWekaCompatible() {
		return false;
	}

	@Override
	public String getInitName() {
		return initName;
	}


	@Override
	public String objectInfo() {
		return String.format(
				"type %s;gen %s;depth %s;divide %s;resample %s;threads %s",
				initName, gen.getGenName(), maxDepth, divideParam, resample,
				nThreads);
	}

	public CompletedTrees copy() {
		return new CompletedTrees();
	}

}
