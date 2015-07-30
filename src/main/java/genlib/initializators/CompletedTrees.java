package genlib.initializators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.exceptions.WrongDataException;
import genlib.generators.DummyTreeGenerator;
import genlib.generators.TreeGenerator;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.data.GenLibInstances;
import genlib.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	private void initPopulation(GenLibInstances data) throws Exception {
		if (gen == null) {
			this.gen = new DummyTreeGenerator();
			this.gen.setPopulationInitializator(this);
		}

		int multCoef = 1;
		population = new TreeIndividual[multCoef * divideParam];

		if (nThreads > 1) {
			gen.setIndividuals(population);
			ExecutorService es = Executors.newFixedThreadPool(nThreads);

			for (int i = 0; i < divideParam; i++) {
				GenLibInstances dataPart = null;
				if (!resample) {
					// size of instances = data.length / divideParam.
					dataPart = data.getPart(divideParam, i);
				} else {
					// instances of same length as training data. Sampling with
					// replacement
					dataPart = data.resample(random);
				}
				TreeGenerator popG = gen.copy();
				popG.setPopulationInitializator(this);
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
			gen.setPopulationInitializator(this);
			for (int i = 0; i < divideParam; i++) {
				GenLibInstances dataPart = null;
				if (!resample) {
					// size of instances = data.length / divideParam.
					dataPart = data.getPart(divideParam, i);
				} else {
					// instances of same length as training data. Sampling with
					// replacement
					dataPart = data.resample(random);
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
		if (data.isGenLibInstances()) {
			initPopulation(data.toGenLibInstances());
		} else {
			throw new WrongDataException(String.format(
					TextResource.getString(TextKeys.eTypeParameter),
					GenLibInstances.class.getName(), data.getData().getClass()
							.getName()));
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
				initName, gen.getGenName(), maxHeight, divideParam, resample,
				nThreads);
	}

	public CompletedTrees copy() {
		return new CompletedTrees();
	}

}
