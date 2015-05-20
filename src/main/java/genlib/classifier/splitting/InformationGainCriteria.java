package genlib.classifier.splitting;

import java.util.Enumeration;

import genlib.structures.data.GenLibDistribution;
import genlib.structures.data.GenLibInstances;
import genlib.utils.Utils;
import weka.classifiers.trees.j48.Distribution;
import weka.core.Instance;
import weka.core.Instances;

public class InformationGainCriteria extends EntropyBasedCriteria {
	
	/** for serialization */
	private static final long serialVersionUID = -7729464952878929743L;

	/**
	 * Copy Constructor
	 * @param igc Object to copy
	 */
	public InformationGainCriteria() {
		this.criteriaValue = 0d;
		this.splitPoint = 0d;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <I,D> D handleNumericAttribute(I dataPart, int attIndex, int classComplexity) 
			throws Exception {
		if (dataPart instanceof Instances) 
			return (D)wNumHandling((Instances)dataPart, attIndex, classComplexity);		
		if (dataPart instanceof GenLibInstances) 
			return (D)numHandling((GenLibInstances)dataPart, attIndex, classComplexity);
		throw new Exception("No proper type of parameter dataPart");
	}

	private Distribution wNumHandling(Instances dataPart, int attIndex, int classComplexity) 
			throws Exception {
		Instance instance;
		int fstMissing = 0;
		double minBagCount;
		
		criteriaValue = 0d;
		splitPoint = 0d;
		Distribution distribution = new Distribution(2, dataPart.numClasses());

		Enumeration<?> enu = dataPart.enumerateInstances();
		while (enu.hasMoreElements()) {
			instance = (Instance) enu.nextElement();
			if (instance.isMissing(attIndex))
				break;			
			distribution.add(1,instance);
			fstMissing++;
		}

		minBagCount = (distribution.total()/classComplexity/10);
		minBagCount = minBagCount <= 2 ? 2 : (minBagCount >= 25 ? 25 : minBagCount);

		if (fstMissing < minBagCount * 2) return distribution;

		double oldEntropy = oldEnt(distribution);

		// Consistency with weka
		double sumOfWeights = dataPart.sumOfWeights();
		double actualInfo = 0d;
		int bestSplit = -1;
		int start = 0;
		int i = (int)Math.ceil(minBagCount);	
		
		while (i < fstMissing) {
			if (dataPart.instance(i-1).value(attIndex) <
					dataPart.instance(i).value(attIndex)) {
				distribution.shiftRange(1, 0, dataPart, start, i);
				
				if (distribution.perBag(1) >= minBagCount) {
					actualInfo = computeInfo(distribution, sumOfWeights, oldEntropy);
					
					if (Utils.gt(actualInfo, criteriaValue)) {
						criteriaValue = actualInfo;
						bestSplit = i-1;
					}					
				}
				start = i;
			}
			i++;
		}
		
		if (bestSplit == -1 || criteriaValue == 0) return null; 
		
		splitPoint = (dataPart.instance(bestSplit+1).value(attIndex)+
			       dataPart.instance(bestSplit).value(attIndex))/2;
		
		/*
		 * Final setting of distribution with appropriate splitting point
		 */
		distribution = new Distribution(2, dataPart.numClasses());
		distribution.addRange(0,dataPart,0,bestSplit+1);
	    distribution.addRange(1,dataPart,bestSplit+1,fstMissing);
		
		return distribution;
	}

	private GenLibDistribution numHandling(GenLibInstances dataPart, int attIndex, int complexity) 
			throws Exception {
		throw new UnsupportedOperationException();	

	}

	@Override
	public double computeCriteria() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setInstanceOrDistribution(Object insOrDist) {
		throw new UnsupportedOperationException();
	}

	public <I> double computeCriteria(I data, int classIndex) throws Exception{
		if (data instanceof Instances) 			
			return computeInfo(new Distribution((Instances)data));
		if (data instanceof GenLibInstances) 
			return computeInfo(((GenLibInstances)data).getDistribution());			
		throw new Exception("No proper type of parameter data");
	}

	@Override
	public <D> double computeCriteria(D distribution) throws Exception {
		if (distribution instanceof Distribution) 			
			return computeInfo((Distribution)distribution);
		if (distribution instanceof GenLibDistribution) 
			return computeInfo((GenLibDistribution)distribution);				
		throw new Exception("No proper type of parameter data");
	}

	@Override
	public <D> double computeCriteria(D distribution, double totalIns)
			throws Exception {
		if (distribution instanceof Distribution) 			
			return computeInfo((Distribution)distribution,totalIns);
		if (distribution instanceof GenLibDistribution) 
			return computeInfo((GenLibDistribution)distribution, totalIns);				
		throw new Exception("No proper type of parameter data");
	}
	
	/**
	 * Information gain computed as in C4.5.
	 * @param distribution Distribution of classes in bags 
	 * @param totalInst Number of all instances without those that miss values
	 * @return Information gain as it's defined for C4.5 
	 */
	protected final double computeInfo(Distribution distribution, double totalInst) {	
		double infoGain = 0d;
		double noUnk = totalInst - distribution.total();
		double unkRate = noUnk / totalInst;
		infoGain = (oldEnt(distribution)-newEnt(distribution));
		infoGain *= (1-unkRate);
		// we are dividing because we are not considering division in computing entropies.		 
		return Utils.eq(infoGain,0d) ? 0d : infoGain/distribution.total();
	}
	
	/**
	 * Information gain computed as in C4.5.
	 * @param distribution Distribution of classes in bags 
	 * @param totalInst Number of all instances without those that miss values
	 * @param oldEnt Computed old distribution entropy (for speedup of often used oldEnt computation)
	 * @return Information gain as it's defined for C4.5 
	 */
	protected final double computeInfo(Distribution distribution, double totalInst, double oldEnt) {	
		double infoGain = 0d;
		double noUnk = totalInst - distribution.total();
		double unkRate = noUnk / totalInst;
		infoGain = (oldEnt-newEnt(distribution));
		infoGain *= (1-unkRate);
		// we are dividing because we are not considering division in computing entropies.		 
		return Utils.eq(infoGain,0d) ? 0d : infoGain/distribution.total();
	}

	/**
	 * Information gain computed as it is defined. We are not considering if there
	 * are any missing values.
	 * @param distribution Distribution of classes in bags
	 * @return Information gain as we know it
	 */	
	protected final double computeInfo(Distribution distribution) {
		double infoGain = (oldEnt(distribution)-newEnt(distribution));
		return Utils.eq(infoGain,0d) ? 0d : infoGain/distribution.total();
	}
	
	protected double computeInfo(GenLibDistribution distribution) {
		throw new UnsupportedOperationException();
	}
	
	protected double computeInfo(GenLibDistribution distribution, double totalInst) {
		throw new UnsupportedOperationException();
	}

	public InformationGainCriteria copy() {
		return new InformationGainCriteria();
	}

}
