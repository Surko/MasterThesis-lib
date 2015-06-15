package genlib.classifier.splitting;

import genlib.utils.Utils;
import weka.classifiers.trees.j48.Distribution;

public abstract class EntropyBasedCriteria<I,D> implements SplitCriteria<I,D> {	
	
	/** for serialization */
	private static final long serialVersionUID = 7370846015456945993L;

	public double oldEnt(Distribution distribution) {
		double oldEnt = 0d;

		for (int i = 0; i < distribution.numClasses(); i++)
			oldEnt += Utils.logFunc(distribution.perClass(i));

		return oldEnt;
	}

	public double newEnt(Distribution distribution) {
		double newEnt = 0d;

		for (int b = 0; b < distribution.numBags(); b++) {
			for (int c = 0; c < distribution.numClasses(); c++) {
				newEnt += Utils.logFunc(distribution.perClassPerBag(b, c));
			}
			newEnt -= Utils.logFunc(distribution.perBag(b));
		}

		return newEnt;
	}		
	
}
