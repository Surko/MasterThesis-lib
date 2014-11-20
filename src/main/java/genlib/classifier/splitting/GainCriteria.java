package genlib.classifier.splitting;

import genlib.structures.ArrayDistribution;
import genlib.structures.ArrayInstances;

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
		if (dataPart instanceof ArrayInstances) 
			return (D)numHandling((ArrayInstances)dataPart, attIndex, complexity);
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

	private ArrayDistribution numHandling(ArrayInstances dataPart, int attIndex, int complexity) 
			throws Exception {

		ArrayDistribution distribution = new ArrayDistribution();

		return distribution;

	}	

	@Override
	public <I> double computeCriteria(I data, int classIndex) throws Exception{
		if (data instanceof Instances) 
			return computeGain((Instances)data, classIndex); 	
		if (data instanceof ArrayInstances) 
			return computeGain((ArrayInstances)data, classIndex);				
		throw new Exception("No proper type of parameter data");
	}
	
	@Override
	public <D> double computeCriteria(D distribution) {
		// TODO
		return 0;
	}

	@Override
	public <D> double computeCriteria(D distribution, double totalIns) {
		// TODO
		return 0;
	}	
	
	private double computeGain(Instances data, int classIndex) {
		return 0d;
	}
	
	private double computeGain(ArrayInstances data, int classIndex) {
		return 0d;
	}

	// TODO - compute gain pre distribucie
	
	@Override
	public double computeCriteria() {
		// NOT USED - can be used if this class will be prototype object for each run of generator 
		return 0;
	}
	
	@Override
	public void setInstanceOrDistribution(Object insOrDist) {
		// NOT USED - can be used if this class will be prototype object for each run of generator 		
	}

	public GainCriteria copy() {
		return new GainCriteria();
	}
	
}
