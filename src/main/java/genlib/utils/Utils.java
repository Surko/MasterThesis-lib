package genlib.utils;

import genlib.evolution.Population;
import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.structures.trees.Node;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Random;

/**
 * Class that contains a lot of static methods for computing different often
 * used measures.
 * 
 * @author Lukas Surin
 */
public class Utils {

	/** delimiter for global use when delimiting different types of object */
	public static final String oDELIM = "(;|[ ]+)";
	/** delimiter for global use when delimiting parameters */
	public static final String pDELIM = ",";
	/**
	 * Enum of different signs that can appear in node field sign. This serves
	 * purpose in classifying data.
	 * 
	 * @author Lukas Surin
	 *
	 */
	public enum Sign {
		LESS(0), GREATER(1), EQUALS(2), NEQUALS(3), LESSEQ(4), GREATEQ(5);

		private int n;

		private Sign(int n) {
			this.n = n;
		}

		public int getValue() {
			return n;
		}
	}

	/** Random object which is used from all over the application */
	public static final Random randomGen = new Random(0);
	/** Natural logarithm of 2. Used to make logarithms at base 2.*/
	private static final double log2 = Math.log(2);
	/** very small value that works like threshold */
	private static final double THRESHOLD = 1e-6;
	/** filter for jar files */
	public static final FileFilter jarFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return file.getName().matches(".*[.]jar");
        }
    };
    public static boolean DEBUG = false;
	
	/**
	 * Function that is used in entropy computation. It calculates simple
	 * expression num * log_2(num). Logarithm of base 2 can be evaluated by
	 * equality log_2(num) = ln(num)/ln(2).
	 * 
	 * @param num
	 *            Integers or rational numbers
	 * @return Evaluated expression.
	 */
	public static final double logFunc(double num) {
		// Constant hard coded for efficiency reasons
		if (num < 1e-6)
			return 0;
		else
			return num * Math.log(num) / log2;

	}

	/**
	 * Function to compute logarithm at base 2.
	 * 
	 * @param num
	 *            Value to be used in logarithm
	 * @return Logarithm of num at base 2.
	 */
	public static final double log2(double num) {
		return Math.log(num) / log2;
	}

	/**
	 * Function which will compute information gain
	 * 
	 * @return Information Gain
	 */
	public static double informationGain() {
		double infoGain = 0d;

		return infoGain;
	}

	/**
	 * Computing entropy as weka. It makes computing faster. Difference at
	 * normal computing is that we don't consider common denumerator.
	 * 
	 * @param classCounts
	 *            Count of classes in each bag
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
	 * Computing entropy as weka. It makes computing faster. Difference at
	 * normal computing is that we don't consider common denumerator
	 * 
	 * @param classCounts
	 *            Count of classes in each bag
	 * @param allCount
	 *            Sum of counts of all classes
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
	 * Computing entropy as it's defined at other sources. Difference from other
	 * functions in this class is that parameter classProbs represents
	 * probabilities of different classes. Then it just makes sum of - p1 *
	 * log(p1).
	 * 
	 * @param classProbs
	 *            Class probabilities
	 * @return Entropy of distribution of classes
	 */
	public static double computeEntropy(double[] classProbs) {
		double entropy = 0d;

		for (int i = 0; i < classProbs.length; i++)
			entropy -= logFunc(classProbs[i]);
		// correction of zero entropy with constant value
		return entropy;
	}

	/**
	 * Method which returns true/false accordingly whether a equals b. Equality
	 * is even forced when there's only little difference between those values.
	 * This little difference is called THRESHOLD.
	 * 
	 * @param a
	 *            Value of a
	 * @param b
	 *            Value of b
	 * @return True/False whether values are equal
	 */
	public static boolean eq(double a, double b) {
		return a == b || ((a - b) < THRESHOLD && (a - b) > THRESHOLD);
	}

	/**
	 * Method which returns true/false accordingly whether a is greater or equal
	 * to b. This is even forced when there's only little difference between
	 * those values. This little difference is called THRESHOLD.
	 * 
	 * @param a
	 *            Value of a
	 * @param b
	 *            Value of b
	 * @return True/False whether a is greater or equal to b
	 */
	public static boolean gte(double a, double b) {
		return (b - a) < THRESHOLD || a >= b;
	}

	/**
	 * Method which returns true/false accordingly whether a is greater than b.
	 * This is even forced when there's only little difference between those
	 * values. This little difference is called THRESHOLD.
	 * 
	 * @param a
	 *            Value of a
	 * @param b
	 *            Value of b
	 * @return True/False whether a is greater than b
	 */
	public static boolean gt(double a, double b) {
		return (a - b) < THRESHOLD;
	}

	/**
	 * Method which computes height of a tree given by parameter root. Root must
	 * be of type Node.
	 * 
	 * @param root
	 *            Root node of a tree
	 * @return Height of given tree
	 */
	public static int computeHeight(Node root) {
		if (root == null) {
			return 0;
		}

		if (root.isLeaf()) {
			return 1;
		}

		int height = 0;

		for (Node child : root.getChilds()) {
			int n = computeHeight(child);
			height = n > height ? n : height;
		}

		return height + 1;

	}

	/**
	 * NOT IMPLEMENTED YET
	 * 
	 * @param data
	 * @return
	 */
	public static HashMap<String, Integer> makeAttrIndexMap(GenLibInstances data) {
		HashMap<String, Integer> retHashMap = new HashMap<String, Integer>();
		return retHashMap;
	}

	/**
	 * NOT IMPLEMENTED YET
	 * 
	 * @param data
	 * @return
	 */
	public static HashMap<String, Integer>[] makeAttrValueIndexMap(
			GenLibInstances data) {
		return null;
	}

	/**
	 * NOT IMPLEMENTED YET
	 * 
	 * @param data
	 * @return
	 */
	public static void makeAttrMap(GenLibInstances data,
			HashMap<String, Integer> attrIndexMap,
			HashMap<String, Integer>[] attrValueIndexMap) {

	}

	/**
	 * Debug method to create population from parameter individual 
	 * @param model individual to create population from 
	 * @return Population with individuals
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Individual> Population<T> debugPopulationFrom(T individual) {
		Population<T> individuals = new Population<>();	
				
		individuals.add(individual);
		for (int i = 0; i < 19; i++)
			individuals.add((T)individual.copy());
		
		return individuals;
	}
	
	/**
	 * Debug method to create population from dummy individual created inside the method.
	 * @return Population with individuals
	 */
	public static Population<TreeIndividual> debugTreePopulation() {
		Population<TreeIndividual> individuals = new Population<>();	
		MultiWayDepthNode root = MultiWayDepthNode.makeNode(2, 1, Sign.LESS, 20d);
		MultiWayDepthNode[] childs = new MultiWayDepthNode[2];
		childs[0] = MultiWayDepthNode.makeLeaf(1);
		childs[1] = MultiWayDepthNode.makeLeaf(0);
		root.setChilds(childs);
		TreeIndividual testIndividual = new TreeIndividual(root);
		individuals.add(testIndividual);
		for (int i = 0; i < 19; i++)
			individuals.add(new TreeIndividual(testIndividual));
		return individuals;
	}
	
}
