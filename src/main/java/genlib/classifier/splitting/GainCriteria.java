package genlib.classifier.splitting;

import genlib.structures.data.GenLibDistribution;
import genlib.structures.data.GenLibInstances;

import java.util.Enumeration;

import weka.classifiers.trees.j48.Distribution;
import weka.core.Instance;
import weka.core.Instances;

public class GainCriteria extends EntropyBasedCriteria {

	/** for serialization */
	private static final long serialVersionUID = 763871293015177102L;

	@SuppressWarnings("unchecked")
	@Override
	public <I,D> D handleNumericAttribute(I dataPart, int attIndex, int complexity) 
			throws Exception {
		if (dataPart instanceof Instances) 
			return (D)wNumHandling((Instances)dataPart, attIndex, complexity);		
		if (dataPart instanceof GenLibInstances) 
			return (D)numHandling((GenLibInstances)dataPart, attIndex, complexity);
		throw new Exception("No proper type of parameter dataPart");
	} 
	
	private Distribution wNumHandling(Instances dataPart, int attIndex, int complexity) 
			throws Exception {
		Instance instance;
		int fstMissing = 0;
		double minBagCount;
		
		criteriaValue = 0d;
		Distribution distribution = new Distribution(2, dataPart.numClasses());

		Enumeration<?> enu = dataPart.enumerateInstances();
		while (enu.hasMoreElements()) {
			instance = (Instance) enu.nextElement();
			if (instance.isMissing(attIndex))
				break;			
			distribution.add(1,instance);
			fstMissing++;
		}

		minBagCount = (distribution.total()/complexity/10);
		minBagCount = minBagCount <= 2 ? 2 : (minBagCount >= 25 ? 25 : minBagCount);

		if (fstMissing < minBagCount * 2) return distribution;

		double oldEntropy = oldEnt(distribution);

		return distribution;
	}

	protected GenLibDistribution numHandling(GenLibInstances dataPart, int attIndex, int complexity) 
			throws Exception {
		throw new UnsupportedOperationException();
	}	

	@Override
	public <I> double computeCriteria(I data, int classIndex) throws Exception{
		if (data instanceof Instances) 
			return computeGain((Instances)data, classIndex); 	
		if (data instanceof GenLibInstances) 
			return computeGain((GenLibInstances)data, classIndex);				
		throw new Exception("No proper type of parameter data");
	}
	
	@Override
	public <D> double computeCriteria(D distribution) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <D> double computeCriteria(D distribution, double totalIns) {
		throw new UnsupportedOperationException();
	}	
	
	protected double computeGain(Instances data, int classIndex) {
		throw new UnsupportedOperationException();
	}
	
	protected double computeGain(GenLibInstances data, int classIndex) {
		throw new UnsupportedOperationException();
	}

	// TODO - compute gain pre distribucie
	
	@Override
	public double computeCriteria() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setInstanceOrDistribution(Object insOrDist) {
		throw new UnsupportedOperationException();	
	}

	public GainCriteria copy() {
		return new GainCriteria();
	}
	
}
