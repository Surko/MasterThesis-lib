package genlib.classifier.splitting;

import java.util.Enumeration;

import genlib.structures.data.GenLibDistribution;
import genlib.structures.data.GenLibInstances;
import genlib.utils.Utils;
import weka.classifiers.trees.j48.Distribution;
import weka.core.Instance;
import weka.core.Instances;

public abstract class EntropyBasedCriteria implements SplitCriteria {	
	
	/** for serialization */
	private static final long serialVersionUID = 7370846015456945993L;
	protected double criteriaValue = 0d;
	protected double splitPoint = -1;
	
	@SuppressWarnings("unchecked")
	@Override
	public <I,D> D handleEnumeratedAttribute(I dataPart, int attIndex, int complexity) 
		throws Exception {
		if (dataPart instanceof Instances) {
			return (D)wEnumHandling((Instances)dataPart, attIndex, complexity);
		}
		if (dataPart instanceof GenLibInstances) 
			return (D)enumHandling((GenLibInstances)dataPart, attIndex, complexity);
		throw new Exception("No proper type of parameter dataPart");
	}
	
	protected Distribution wEnumHandling(Instances dataPart, int attIndex, int complexity) 
		throws Exception {
		Instance instance;		
		Distribution distribution = new Distribution(complexity, dataPart.numClasses());

		splitPoint = -1;
		Enumeration<?> enu = dataPart.enumerateInstances();
		while (enu.hasMoreElements()) {
			instance = (Instance) enu.nextElement();
			if (!instance.isMissing(attIndex))
				distribution.add((int)instance.value(attIndex),instance);
		}

		return distribution;
	}
	
	protected GenLibDistribution enumHandling(GenLibInstances dataPart, int attIndex, int complexity) 
		throws Exception {
		throw new UnsupportedOperationException();		
	}
	
	public double getSplitPoint() {
		return splitPoint;
	}
	
	public double getCriteriaValue() {
		return criteriaValue;
	}
	
	public final double oldEnt(Distribution distribution) {
		double oldEnt = 0d;
		
		for (int i = 0; i < distribution.numClasses(); i++) 
			oldEnt += Utils.logFunc(distribution.perClass(i));
		
		return oldEnt;
	}

	public final double newEnt(Distribution distribution) {
		double newEnt = 0d;
		
		for (int b = 0; b < distribution.numBags(); b++) {
			for (int c = 0 ; c < distribution.numClasses(); c++) {
				newEnt += Utils.logFunc(distribution.perClassPerBag(b, c));
			}
			newEnt -= Utils.logFunc(distribution.perBag(b));
		}
		
		return newEnt;
	}
	
}
