package genlib.classifier.popinit;

import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.gens.WekaJ48TreeGenerator;
import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.utils.WekaUtils;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import weka.core.Instances;

public class WekaCompletedTrees extends CompletedTrees {
	
	/** for serialization */
	private static final long serialVersionUID = 6362474859353821356L;
	/** name of this initializator */
	public static final String initName = "wCompTree";
	
	public WekaCompletedTrees() {}
	
	public WekaCompletedTrees(int popSize, int divideParam, boolean resample) {
		super(popSize, divideParam, resample);
	}
	
	@SuppressWarnings("unchecked")
	private void initPopulation(Instances data) throws Exception {
		if (gen == null) {
			this.gen = new WekaJ48TreeGenerator(new String[] {"-C","0.25","-M","2"});
			this.gen.setPopulationInitializator(this);
		}
		
		attrIndexMap = new HashMap<>();
		attrValueIndexMap = new HashMap[data.numAttributes()];		
		WekaUtils.makeAttrMap(data, attrIndexMap, attrValueIndexMap);

		int multCoef = 1;
		population = new TreeIndividual[multCoef * divideParam];
		
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

			// Here we should be waiting for generating to stop. 
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

				System.arraycopy(gen.createPopulation(), 0, population, i * multCoef,
						multCoef);
			}
			
			gen.setIndividuals(population);
		}
		
		fillPopulation();
	}
	
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
	public String objectInfo() {
		return String.format(
				"type %s;gen %s;depth %s;divide %s;resample %s;threads %s",
				initName,
				gen.getGenName(),
				maxDepth,
				divideParam,
				resample,
				nThreads);
	}
	
	@Override
	public String getInitName() {
		return initName;
	}
	
}
