package genlib.classifier.popinit;

import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.structures.ArrayInstances;

public class CompletedTrees extends TreePopulationInitializator {
	/** for serialization */
	private static final long serialVersionUID = 1312621842322682763L;
	/** Individuals that makes this population */
	protected TreeIndividual[] stumps;
	/** Number of threads that will be creating population */
	protected int nThreads;

	private void initPopulation(ArrayInstances data) throws Exception {		
		
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
	public String objectInfo() {
		return String.format(
				"type=%s;gen=%s;depth=%s;divide=%s;resample=%s;threads=%s",
				Type.TREE, gen.getGenName(), maxDepth, divideParam,
				resample, nThreads);
	}

}
