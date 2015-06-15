package genlib.classifier.splitting;

import genlib.utils.Utils;

import java.util.Enumeration;

import weka.classifiers.trees.j48.Distribution;
import weka.classifiers.trees.j48.InfoGainSplitCrit;
import weka.core.Instance;
import weka.core.Instances;

public class InformationGainCriteria extends
		EntropyBasedCriteria<Instances, Distribution> {

	/** for serialization */
	private static final long serialVersionUID = -7729464952878929743L;
	private InfoGainSplitCrit infoGain;
	public static final String initName = "infoGain";
	private final static InformationGainCriteria instance = new InformationGainCriteria();

	public static InformationGainCriteria getInstance() {
		return instance;
	}
	
	private Object readResolve() {		
		return instance;
	}
	
	/**
	 * Default Constructor
	 */
	private InformationGainCriteria() {
		infoGain = new InfoGainSplitCrit();
	}

	@Override
	public Distribution handleEnumeratedAttribute(Instances dataPart,
			int attIndex, int complexity) throws Exception {
		Instance instance;
		Distribution distribution = new Distribution(complexity,
				dataPart.numClasses());
		infoGain.splitCritValue(distribution);		
		Enumeration<?> enu = dataPart.enumerateInstances();
		while (enu.hasMoreElements()) {
			instance = (Instance) enu.nextElement();
			if (!instance.isMissing(attIndex))
				distribution.add((int) instance.value(attIndex), instance);
		}

		return distribution;
	}

	@Override
	public Distribution handleNumericAttribute(Instances dataPart,
			int attIndex, int complexity) throws Exception {
		Instance instance;
		int fstMissing = 0;
		double minBagCount;

		double criteriaValue = 0d;		
		Distribution distribution = new Distribution(2, dataPart.numClasses());

		Enumeration<?> enu = dataPart.enumerateInstances();
		while (enu.hasMoreElements()) {
			instance = (Instance) enu.nextElement();
			if (instance.isMissing(attIndex))
				break;
			distribution.add(1, instance);
			fstMissing++;
		}

		minBagCount = (distribution.total() / complexity / 10);
		minBagCount = minBagCount <= 2 ? 2 : (minBagCount >= 25 ? 25
				: minBagCount);

		if (fstMissing < minBagCount * 2)
			return distribution;

		double oldEntropy = oldEnt(distribution);

		// Consistency with weka
		double sumOfWeights = dataPart.sumOfWeights();
		double actualInfo = 0d;
		int bestSplit = -1;
		int start = 0;
		int i = (int) Math.ceil(minBagCount);

		while (i < fstMissing) {
			if (dataPart.instance(i - 1).value(attIndex) < dataPart.instance(i)
					.value(attIndex)) {
				distribution.shiftRange(1, 0, dataPart, start, i);

				if (distribution.perBag(1) >= minBagCount) {
					actualInfo = computeInfo(distribution, sumOfWeights,
							oldEntropy);

					if (Utils.gt(actualInfo, criteriaValue)) {
						criteriaValue = actualInfo;
						bestSplit = i - 1;
					}
				}
				start = i;
			}
			i++;
		}

		if (bestSplit == -1 || criteriaValue == 0)
			return null;

		//splitPoint = (dataPart.instance(bestSplit + 1).value(attIndex) + dataPart
		//		.instance(bestSplit).value(attIndex)) / 2;

		/*
		 * Final setting of distribution with appropriate splitting point
		 */
		distribution = new Distribution(2, dataPart.numClasses());
		distribution.addRange(0, dataPart, 0, bestSplit + 1);
		distribution.addRange(1, dataPart, bestSplit + 1, fstMissing);

		return distribution;
	}

	/**
	 * Information gain computed as in C4.5.
	 * 
	 * @param distribution
	 *            Distribution of classes in bags
	 * @param totalInst
	 *            Number of all instances without those that miss values
	 * @return Information gain as it's defined for C4.5
	 */
	@SuppressWarnings("unused")
	private final double computeInfo(Distribution distribution, double totalInst) {
		double infoGain = 0d;
		double noUnk = totalInst - distribution.total();
		double unkRate = noUnk / totalInst;
		infoGain = (oldEnt(distribution) - newEnt(distribution));
		infoGain *= (1 - unkRate);
		// we are dividing because we are not considering division in computing
		// entropies.
		return Utils.eq(infoGain, 0d) ? 0d : infoGain / distribution.total();
	}

	/**
	 * Information gain computed as in C4.5.
	 * 
	 * @param distribution
	 *            Distribution of classes in bags
	 * @param totalInst
	 *            Number of all instances without those that miss values
	 * @param oldEnt
	 *            Computed old distribution entropy (for speedup of often used
	 *            oldEnt computation)
	 * @return Information gain as it's defined for C4.5
	 */
	private final double computeInfo(Distribution distribution,
			double totalInst, double oldEnt) {
		double infoGain = 0d;
		double noUnk = totalInst - distribution.total();
		double unkRate = noUnk / totalInst;
		infoGain = (oldEnt - newEnt(distribution));
		infoGain *= (1 - unkRate);
		// we are dividing because we are not considering division in computing
		// entropies.
		return Utils.eq(infoGain, 0d) ? 0d : infoGain / distribution.total();
	}	

	@Override
	public double computeCriteria(Instances data, int totalIns)
			throws Exception {
		Distribution distribution = new Distribution(data);
		if (totalIns <= 0) {
			return computeCriteria(distribution);
		} else {
			return computeCriteria(distribution, totalIns);
		}
	}

	/**
	 * Information gain computed as it is defined. We are not considering if
	 * there are any missing values.
	 * 
	 * @param distribution
	 *            Distribution of classes in bags
	 * @return Information gain as we know it
	 */
	@Override
	public double computeCriteria(Distribution distribution) throws Exception {
		double infoGain = (oldEnt(distribution) - newEnt(distribution));
		return Utils.eq(infoGain, 0d) ? 0d : infoGain / distribution.total();
	}

	/**
	 * Information gain computed as it is defined. We are not considering if
	 * there are any missing values.
	 * 
	 * @param distribution
	 *            Distribution of classes in bags
	 * @return Information gain as we know it
	 */
	@Override
	public double computeCriteria(Distribution distribution, double totalIns)
			throws Exception {
		double infoGain = (oldEnt(distribution) - newEnt(distribution));
		return Utils.eq(infoGain, 0d) ? 0d : infoGain / totalIns;
	}

	public InformationGainCriteria copy() {
		return new InformationGainCriteria();
	}

}
