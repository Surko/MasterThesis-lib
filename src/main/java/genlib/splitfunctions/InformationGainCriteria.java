package genlib.splitfunctions;

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
