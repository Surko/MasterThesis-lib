package genlib.utils;

import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.Population;
import genlib.structures.data.GenLibInstance;
import genlib.structures.data.GenLibInstances;
import genlib.structures.extensions.SizeExtension;
import genlib.structures.trees.MultiWayHeightNode;
import genlib.structures.trees.Node;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

/**
 * Class that contains a lot of static methods for computing different often
 * used measures.
 * 
 * @author Lukas Surin
 */
public class Utils {

	/** empty double arrray to return instead of null */
	public static final double[] empty_double_array = new double[0];
	/** empty int array to return instead of null */
	public static final int[] empty_int_array = new int[0];

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
		LESS("<"), GREATER(">"), EQUALS("="), NEQUALS("!="), LESSEQ("<="), GREATEQ(
				">=");

		private final String repr;

		private Sign(String repr) {
			this.repr = repr;
		}

		public String getValue() {
			return repr;
		}

	}

	/** Random object which is used from all over the application */
	public static final Random randomGen = new Random(0);
	/** Natural logarithm of 2. Used to make logarithms at base 2. */
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

	public static double[][] makeConfusionMatrix(TreeIndividual individual,
			GenLibInstances instances) {
		if (instances.numClasses() <= 0) {
			return (double[][]) Collections.EMPTY_LIST.toArray();
		}

		Node root = individual.getRootNode();
		double[][] confusion = new double[instances.numClasses()][instances
				.numClasses()];

		Enumeration<GenLibInstance> eInstances = instances.getInstances();
		while (eInstances.hasMoreElements()) {
			GenLibInstance instance = eInstances.nextElement();
			while (!root.isLeaf()) {
				if (instance.getAttribute(root.getAttribute()).isNumeric()) {
					if (instance.getValueOfAttribute(root.getAttribute()) < root
							.getValue()) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance
							.getValueOfAttribute(root.getAttribute()));
				}
			}

			int pClass = (int) root.getValue();
			int tClass = (int) instance.getValueOfClass();

			confusion[pClass][tClass] += 1;

			root = individual.getRootNode();
		}

		return confusion;
	}

	public static Sign makeSign(String sSign) {
		switch (sSign) {
		case "<":
			return Sign.LESS;
		case ">":
			return Sign.GREATER;
		case "<=":
			return Sign.LESSEQ;
		case ">=":
			return Sign.GREATEQ;
		case "!=":
			return Sign.NEQUALS;
		case "=":
			return Sign.EQUALS;
		default:
			return null;
		}
	}

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
	 * @param threshold
	 *            Value of threshold
	 * @return True/False whether values are equal
	 */
	public static boolean eq(double a, double b, double threshold) {
		return Double.compare(a, b) == 0
				|| ((a - b) < threshold && (b - a) > threshold);
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
		return a == b || ((a - b) < THRESHOLD && (b - a) > THRESHOLD);
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
	 * Method which gets number of leaves of a given tree. Root must be of type
	 * Node.
	 * 
	 * @param root
	 *            Root node of a tree
	 * @return Num nodes of given tree, if root is null than -1
	 */
	public static int computeNumNodes(Node root) {
		if (root == null) {
			return -1;
		}

		if (root.isLeaf()) {
			return 0;
		}

		int numNodes = 1;
		boolean node = false;

		for (Node child : root.getChilds()) {
			int n = computeNumNodes(child);
			if (n == -1) {
				continue;
			}
			node = true;
			numNodes += n;
		}

		if (node) {
			return numNodes;
		}

		return 0;

	}

	/**
	 * Method which gets number of nodes of a given tree. Root must be of type
	 * Node.
	 * 
	 * @param root
	 *            Root node of a tree
	 * @return Num nodes of given tree, if root is null than -1
	 */
	public static int computeNumLeaves(Node root) {
		if (root == null) {
			return -1;
		}

		if (root.isLeaf()) {
			return 1;
		}

		int numLeaves = 0;
		boolean node = false;

		for (Node child : root.getChilds()) {
			int n = computeNumLeaves(child);
			if (n == -1) {
				continue;
			}
			node = true;
			numLeaves += n;
		}

		if (node) {
			return numLeaves;
		}

		return 1;

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
			return -1;
		}

		if (root.isLeaf()) {
			return 0;
		}

		int height = -1;

		for (Node child : root.getChilds()) {
			int n = computeHeight(child);
			height = n > height ? n : height;
		}

		return height + 1;

	}

	/**
	 * Method which computes size of a tree given by parameter root. Root must
	 * be of type Node.
	 * 
	 * @param root
	 *            Root node of a tree
	 * @return Size of given tree
	 */
	public static int computeSize(Node root) {
		if (root == null) {
			return 0;
		}

		if (root.isLeaf()) {
			return 1;
		}

		int size = 1;

		for (Node child : root.getChilds()) {
			int n = computeSize(child);
			size += n;
		}

		return size;
	}

	/**
	 * Post processing method that fixes attributes in node if the same
	 * attribute was used with inconsistent value on path to root. </p> If there
	 * is any inconsistency than it replace the node with child and calls the
	 * fixNode on this replaced node.
	 * 
	 * @param root
	 *            from which we start fixing
	 */
	public static void fixNode(Node root) {
		if (root.isLeaf() || root.getParent() == null) {
			return;
		}

		int attr = root.getAttribute();
		double value = root.getValue();
		Sign sign = root.getSign();

		Node rootParent = root.getParent();
		int rootIndex = 0;

		for (int i = 0; i < rootParent.getChildCount(); i++) {
			if (rootParent.getChildAt(i) == root) {
				rootIndex = i;
				break;
			}
		}

		Node tempNode = root;
		Node tempParent = rootParent;

		while (tempParent != null) {
			if (tempParent.getAttribute() == attr) {
				if (tempParent.getSign() != null && sign != null) {
					//
					// switch (tempParent.getSign()) {
					// case EQUALS :
					//
					//
					// case GREATEQ :
					// case GREATER :
					// if (Sign.GREATER == sign || Sign.GREATEQ == sign) {
					// if (tempParent.getValue() >= value) {
					// rootParent.setChildAt(0, root.getChildAt(0));
					// }
					// }
					// fixNode(rootParent.getChildAt(rootIndex));
					// case LESS :
					// case LESSEQ :
					// if (Sign.LESS == sign || Sign.LESSEQ == sign) {
					// if (tempParent.getValue() <= value) {
					// rootParent.setChildAt(0, root.getChildAt(0));
					// }
					// }
					// fixNode(rootParent.getChildAt(rootIndex));
					// break;
					// case NEQUALS :
					// }
				} else {
					for (int i = 0; i < tempParent.getChildCount(); i++) {
						if (tempParent.getChildAt(i) == tempNode) {
							rootParent
									.setChildAt(rootIndex, root.getChildAt(i));
							break;
						}
					}

					fixNode(rootParent.getChildAt(rootIndex));
					return;
				}

			}

			tempNode = tempParent;
			tempParent = tempParent.getParent();
		}
	}

	/**
	 * Method that finds the node with index i1. Search is done in preorder.
	 * Version of this method is for SizeExtension nodes. It utilizes the saved
	 * values of tree sizes to search for node. It is faster than the
	 * counterpart ({@link #getNode(Node, int)}).
	 * 
	 * @param root
	 *            Node from which we start
	 * @param i1
	 *            index of node that we search for
	 * @return node with index i1
	 */
	public static Node getExtensionNode(Node root, int i1) {
		if (root == null) {
			return null;
		}

		if (i1 == 0) {
			return root;
		}

		if (!(root instanceof SizeExtension)) {
			return null;
		}

		i1--;
		while (root.getChilds() != null) {
			int size = 0;
			for (Node child : root.getChilds()) {
				size = ((SizeExtension) child).getTreeSize();

				if (i1 < size) {
					root = child;
					break;
				} else {
					i1 -= size;
				}
			}

			if (i1 == 0) {
				return root;
			}

			i1--;
		}
		throw new IndexOutOfBoundsException();

	}

	/**
	 * Method that finds the node with index i1. Search is done in preorder.
	 * This is the version for nodes without SizeExtension (little bit slower
	 * from the counterpart {@link #getExtensionNode(Node, int)})
	 * 
	 * @param root
	 *            Node from which we start
	 * @param i1
	 *            index of node that we search for
	 * @return node with index i1
	 */
	public static Node getNode(Node root, int i1) {
		if (root == null) {
			return null;
		}

		if (i1 == 0) {
			return root;
		}

		Stack<Node> stack = new Stack<Node>();
		stack.push(root);

		while (!stack.empty()) {
			Node node = stack.pop();
			if (i1 == 0) {
				return node;
			}

			for (int i = node.getChildCount() - 1; i >= 0; i--) {
				stack.push(node.getChildAt(i));
			}

			i1--;
		}

		throw new IndexOutOfBoundsException();
	}

	/**
	 * Method count the number of leaves in tree (recursively).
	 * 
	 * @param root
	 *            root from which we start
	 * @param leaves
	 *            list where we add leaves
	 * @return list with leaves
	 */
	public static ArrayList<Node> getLeavesRecursive(Node root,
			ArrayList<Node> leaves) {

		if (root.isLeaf()) {
			leaves.add(root);
			return leaves;
		}

		for (Node child : root.getChilds()) {
			if (child != null && child.isLeaf()) {
				leaves.add(child);
			} else {
				getLeavesRecursive(child, leaves);
			}

		}

		return leaves;
	}

	/**
	 * Method count the number of nodes in tree (recursively).
	 * 
	 * @param root
	 *            root from which we start
	 * @param leaves
	 *            list where we add nodes (not leaves)
	 * @return list with nodes (not leaves)
	 */
	public static ArrayList<Node> getNodesRecursive(Node root,
			ArrayList<Node> nodes) {

		if (!root.isLeaf()) {
			nodes.add(root);

			for (Node child : root.getChilds()) {
				if (child != null) {
					getNodesRecursive(child, nodes);
				}
			}
		}

		return nodes;
	}

	/**
	 * Method count the number of leaves in tree (with stack). Better to use
	 * {@link #getLeavesRecursive(Node, ArrayList)} because it's faster.
	 * 
	 * @param root
	 *            root from which we start
	 * @param leaves
	 *            list where we add leaves
	 * @return list with leaves
	 */
	public static ArrayList<Node> getLeaves(Node root, ArrayList<Node> leaves) {
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);

		while (!stack.empty()) {
			Node node = stack.pop();

			if (node != null) {
				if (!node.isLeaf()) {
					for (int i = node.getChildCount() - 1; i >= 0; i--) {
						stack.push(node.getChildAt(i));
					}
				} else {
					leaves.add(node);
				}
			}
		}

		return leaves;
	}

	/**
	 * Method count the number of leaves in tree (with stack). It calls
	 * {@link #getLeaves(Node, ArrayList)}. Better to use
	 * {@link #getLeavesRecursive(Node, ArrayList)} because it's faster.
	 * 
	 * @param root
	 *            root from which we start
	 * @return list with leaves
	 */
	public static ArrayList<Node> getLeaves(Node root) {
		return getLeaves(root, new ArrayList<Node>());
	}

	/**
	 * Method count the number of nodes in tree (with stack). Better to use
	 * {@link #getNodesRecursive(Node, ArrayList)} because it's faster.
	 * 
	 * @param root
	 *            root from which we start
	 * @param leaves
	 *            list where we add nodes (not leaves)
	 * @return list with nodes (not leaves)
	 */
	public static ArrayList<Node> getNodes(Node root, ArrayList<Node> nodes) {
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);

		while (!stack.empty()) {
			Node node = stack.pop();

			if (node != null) {
				if (!node.isLeaf()) {
					nodes.add(node);
					for (int i = node.getChildCount() - 1; i >= 0; i--) {
						stack.push(node.getChildAt(i));
					}
				}
			}
		}

		return nodes;
	}

	/**
	 * Method count the number of nodes in tree (with stack). It calls
	 * {@link #getNodes(Node, ArrayList)}. Better to use
	 * {@link #getNodesRecursive(Node, ArrayList)} because it's faster.
	 * 
	 * @param root
	 *            root from which we start
	 * @return list with nodes (not leaves)
	 */
	public static ArrayList<Node> getNodes(Node root) {
		return getNodes(root, new ArrayList<Node>());
	}

	public static boolean isValueProper(double instanceValue, Sign compare,
			double nodeValue) {

		if (compare == null) {
			throw new IllegalArgumentException();
		}

		switch (compare) {
		case EQUALS:
			return Double.compare(instanceValue, nodeValue) == 0;
		case GREATEQ:
			return Double.compare(instanceValue, nodeValue) != -1;
		case GREATER:
			return Double.compare(instanceValue, nodeValue) == 1;
		case LESS:
			return Double.compare(instanceValue, nodeValue) == -1;
		case LESSEQ:
			return Double.compare(instanceValue, nodeValue) != 1;
		case NEQUALS:
			return Double.compare(instanceValue, nodeValue) != 0;
		}

		throw new IllegalStateException();

	}

	public static double getFilteredInstancesRegression(
			GenLibInstances instances, Node root) {

		if (instances.numClasses() != -1) {
			return Double.MIN_VALUE;
		}

		double value = 0d;
		int numOfFiltered = 0;

		Enumeration<GenLibInstance> enumeration = instances.getInstances();
		while (enumeration.hasMoreElements()) {
			GenLibInstance instance = enumeration.nextElement();

			Node node = root;
			boolean shouldAdd = true;
			while (node.getParent() != null) {
				node = node.getParent();

				switch (node.getSign()) {
				case EQUALS:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) != 0) {
						shouldAdd = false;
						break;
					}
					break;
				case GREATEQ:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) == -1) {
						shouldAdd = false;
						break;
					}
					break;
				case GREATER:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) != 1) {
						shouldAdd = false;
						break;
					}
					break;
				case LESS:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) != -1) {
						shouldAdd = false;
						break;
					}
					break;
				case LESSEQ:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) == 1) {
						shouldAdd = false;
						break;
					}
					break;
				case NEQUALS:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) == 0) {
						shouldAdd = false;
						break;
					}
					break;
				default:
					break;
				}

				if (!shouldAdd) {
					break;
				}

			}

			if (shouldAdd) {
				numOfFiltered++;
				value += instance.getValueOfClass();
			}

		}

		return value / numOfFiltered;
	}

	public static double[] getFilteredInstancesClasses(
			GenLibInstances instances, Node root) {

		if (instances.numClasses() == -1) {
			return empty_double_array;
		}

		double[] classes = new double[instances.numClasses()];

		Enumeration<GenLibInstance> enumeration = instances.getInstances();
		while (enumeration.hasMoreElements()) {
			GenLibInstance instance = enumeration.nextElement();

			Node node = root;
			boolean shouldAdd = true;
			while (node.getParent() != null) {
				node = node.getParent();

				switch (node.getSign()) {
				case EQUALS:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) != 0) {
						shouldAdd = false;
						break;
					}
					break;
				case GREATEQ:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) == -1) {
						shouldAdd = false;
						break;
					}
					break;
				case GREATER:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) != 1) {
						shouldAdd = false;
						break;
					}
					break;
				case LESS:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) != -1) {
						shouldAdd = false;
						break;
					}
					break;
				case LESSEQ:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) == 1) {
						shouldAdd = false;
						break;
					}
					break;
				case NEQUALS:
					if (Double.compare(
							instance.getValueOfAttribute(node.getAttribute()),
							node.getValue()) == 0) {
						shouldAdd = false;
						break;
					}
					break;
				default:
					break;
				}

				if (!shouldAdd) {
					break;
				}

			}

			if (shouldAdd) {
				classes[(int) instance.getValueOfClass()]++;
			}

		}

		return classes;
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
	 * 
	 * @param model
	 *            individual to create population from
	 * @return Population with individuals
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Individual> Population<T> debugPopulationFrom(
			T individual) {
		Population<T> individuals = new Population<>();

		individuals.add(individual);
		for (int i = 0; i < 19; i++)
			individuals.add((T) individual.copy());

		return individuals;
	}

	/**
	 * Debug method to create population from dummy individual created inside
	 * the method.
	 * 
	 * @return Population with individuals
	 */
	public static Population<TreeIndividual> debugTreePopulation() {
		Population<TreeIndividual> individuals = new Population<>();
		individuals.setMaxPopulationSize(20);
		MultiWayHeightNode root = MultiWayHeightNode.makeNode(2, 1, Sign.LESS,
				20d);
		MultiWayHeightNode[] childs = new MultiWayHeightNode[2];
		childs[0] = MultiWayHeightNode.makeLeaf(1);
		childs[1] = MultiWayHeightNode.makeLeaf(0);
		root.setChilds(childs);
		TreeIndividual testIndividual = new TreeIndividual(root);
		individuals.add(testIndividual);
		for (int i = 0; i < 19; i++)
			individuals.add(new TreeIndividual(testIndividual));
		return individuals;
	}

}
