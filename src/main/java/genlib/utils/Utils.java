package genlib.utils;

/**
 * Class that contains a lot of static methods for computing different often used measures. 
 * @author kirrie 
 */
public class Utils {

	/**
	 * Natural logarithm of 2. Used to make logarithms at base 2. 
	 */
	private static final double log2 = Math.log(2);
	/**
	 * very small value that works like threshold
	 */
	private static final double THRESHOLD = 1e-6;
	/**
	 * Function that is used in entropy computation. It calculates 
	 * simple expression num * log_2(num). Logarithm of base 2 can be 
	 * evaluated by equality log_2(num) = ln(num)/ln(2).
	 * @param num Integers or rational numbers
	 * @return Evaluated expression.
	 */
	public static final double logFunc(double num) {		
		// Constant hard coded for efficiency reasons
		if (num < 1e-6)
			return 0;
		else
			return num*Math.log(num)/log2;

	}

	/**
	 * Function to compute logarithm at base 2.
	 * @param num Value to be used in logarithm
	 * @return Logarithm of num at base 2.
	 */
	public static final double log2(double num) {
		return Math.log(num)/log2;
	}	
	
	public static double informationGain() {
		double infoGain = 0d;
		
		return infoGain;
	}
	
	/**
	 * Computing entropy as weka. It makes computing faster. Difference at normal computing is that
	 * we don't consider common denumerator.
	 * @param classCounts Count of classes in each bag
	 * @return Entropy for classCounts
	 * @see Utils#computeEntropy(int[], int)
	 */
	public static double computeEntropy(int[] classCounts) {
		int sum = 0;
		for (int value : classCounts) {
			sum += value;
		}		
		return computeEntropy(classCounts, sum);
	}

	/**
	 * Computing entropy as weka. It makes computing faster. Difference at normal computing is that
	 * we don't consider common denumerator
	 * @param classCounts Count of classes in each bag
	 * @param allCount Sum of counts of all classes
	 * @return Entropy for classCounts
	 */	
	public static double computeEntropy(int[] classCounts, int allCount) {
		double entropy = 0d;

		for (int i = 0; i < classCounts.length; i++)
			entropy -= logFunc(classCounts[i]);		
		// 
		return entropy + logFunc(allCount);
	}


	/**
	 * Computing entropy as it's defined at other sources. Difference from other functions in this class
	 * is that parameter classProbs represents probabilities of different classes. Then it just makes
	 * sum of - p1 * log(p1).
	 * @param classProbs Class probabilities
	 * @return Entropy of distribution of classes
	 */
	public static double computeEntropy(double[] classProbs) {
		double entropy = 0d;

		for (int i = 0; i < classProbs.length; i++)
			entropy -= logFunc(classProbs[i]);		
		// correction of zero entropy with constant value
		return entropy;
	}

	public static boolean eq(double a, double b) {
		return a == b || ((a-b) < THRESHOLD && (a-b) > THRESHOLD);
	}
	
	public static boolean gte(double a, double b) {		
		return (b - a) < THRESHOLD || a >= b;		
	}
	
	public static boolean gt(double a, double b) {
		return (a - b) < THRESHOLD;
	}
	
}
