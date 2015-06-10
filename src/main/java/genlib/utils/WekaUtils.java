package genlib.utils;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.structures.trees.MultiWayNode;
import genlib.structures.trees.Node;
import genlib.utils.Utils.Sign;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class WekaUtils {

	@SuppressWarnings("unchecked")
	public static double[][] instacesToArray(Instances instances) {
		double[][] insArray = new double[instances.numInstances()][instances
				.numAttributes()];
		Enumeration<Instance> insEnum = instances.enumerateInstances();

		int index = 0;
		while (insEnum.hasMoreElements()) {
			insArray[index++] = insEnum.nextElement().toDoubleArray();
		}
		return insArray;
	}

	@SuppressWarnings("unchecked")
	public static double[][] makeConfusionMatrix(TreeIndividual individual,
			Instances instances) {
		if (instances.numClasses() <= 0) {
			return (double[][]) Collections.EMPTY_LIST.toArray();
		}

		Node root = individual.getRootNode();
		double[][] confusion = new double[instances.numClasses()][instances
				.numClasses()];

		Enumeration<Instance> eInstances = instances.enumerateInstances();
		while (eInstances.hasMoreElements()) {
			Instance instance = eInstances.nextElement();
			while (!root.isLeaf()) {
				if (instance.attribute(root.getAttribute()).isNumeric()) {
					if (instance.value(root.getAttribute()) < root.getValue()) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance.value(root
							.getAttribute()));
				}
			}

			int pClass = (int) root.getValue();
			int tClass = (int) instance.classValue();

			confusion[pClass][tClass] += 1;

			root = individual.getRootNode();
		}

		return confusion;
	}

	public static Sign makeSign(String sSign) {
		return Utils.makeSign(sSign);
	}

	public static TreeIndividual constructTreeIndividual(String sTree,
			double nodeCount, int instCount,
			HashMap<String, Integer> attrIndexMap,
			HashMap<String, Integer>[] attrValueIndexMap, boolean autoDepth) {
		String[] lines = sTree.split("\n");

		String leafString = "^N([0-9]+) \\[label=\"\'(.*) \\(([0-9.]+)[/]*([0-9.]*)\\)\'\".*$";
		Pattern leafPattern = Pattern.compile(leafString);
		String nodeString = "^N([0-9]+) \\[label=\"(.*)\" \\]$";
		Pattern nodePattern = Pattern.compile(nodeString);
		String edgeString = "^N([0-9]+)->N([0-9]*) \\[label=\"\'([<!=>]+) (.*)\'\"\\]$";
		Pattern edgePattern = Pattern.compile(edgeString);

		int classIndex = attrIndexMap.size() - 1;
		int lastEdge = 0;
		Node[] nodes = new Node[(int) nodeCount];
		int[][] edges = new int[(int) nodeCount - 1][];

		for (String st : lines) {
			Matcher m = leafPattern.matcher(st);
			// We matched a leaf node
			if (m.matches()) {
				Node node = null;
				if (autoDepth) {
					node = new MultiWayDepthNode();
				} else {
					node = new MultiWayNode();
				}

				nodes[Integer.parseInt(m.group(1))] = node;
				node.setValue(attrValueIndexMap[classIndex].get(m.group(2)));
				// if (!m.group(4).isEmpty()) {
				// incorrClass += Double.parseDouble(m.group(4));
				// }
				lastEdge++;
				continue;
			}

			m = nodePattern.matcher(st);
			// We matched a node
			if (m.matches()) {
				Node node = null;
				if (autoDepth) {
					node = new MultiWayDepthNode();
				} else {
					node = new MultiWayNode();
				}
				nodes[Integer.parseInt(m.group(1))] = node;
				int nodeIndex = attrIndexMap.get(m.group(2));
				if (attrValueIndexMap[nodeIndex] == null) {
					edges[lastEdge++] = new int[2];
					node.setChildCount(2);
				} else {
					int childCount = attrValueIndexMap[nodeIndex].size();
					edges[lastEdge++] = new int[childCount];
					node.setChildCount(childCount);
				}
				node.setAttribute(nodeIndex);
				continue;
			}

			m = edgePattern.matcher(st);
			// We have an edge
			if (m.matches()) {
				int edgeIndex = Integer.parseInt(m.group(1));
				int edgeNodeIndex = 0;
				if (attrValueIndexMap[nodes[edgeIndex].getAttribute()] == null) {
					if (nodes[edgeIndex].getValue() == Integer.MIN_VALUE) {
						nodes[edgeIndex]
								.setValue(Double.parseDouble(m.group(4)));
						edges[edgeIndex][0] = Integer.parseInt(m.group(2));
						nodes[edgeIndex].setSign(makeSign(m.group(3)));
					} else {
						edges[edgeIndex][1] = Integer.parseInt(m.group(2));
					}
				} else {
					edgeNodeIndex = attrValueIndexMap[nodes[edgeIndex]
							.getAttribute()].get(m.group(4));
					edges[edgeIndex][edgeNodeIndex] = Integer.parseInt(m
							.group(2));
					nodes[edgeIndex].setSign(makeSign(m.group(3)));
				}

			}

		}

		for (int i = 0; i < edges.length; i++) {
			if (edges[i] == null)
				continue;
			for (int j = 0; j < edges[i].length; j++)
				nodes[i].setChildAt(j, nodes[edges[i][j]]);
		}

		TreeIndividual treeInd = new TreeIndividual(nodes[0]);
		/*
		 * This is fitness value of this individual on slightly changed data. It
		 * stays here because some other operators,fitnesses can utilize this
		 */
		// treeInd.setFitnessValue(TreeAccuracyFitness.TREE_ACCURACY, 1 -
		// incorrClass/instCount);
		return treeInd;
	}

	public static double getFilteredInstancesRegression(Instances instances,
			Node root) {

		if (instances.numClasses() > 1) {
			return Double.MIN_VALUE;
		}

		double value = 0d;
		int numOfFiltered = 0;

		@SuppressWarnings("unchecked")
		Enumeration<Instance> enumeration = (Enumeration<Instance>) instances;
		while (enumeration.hasMoreElements()) {
			Instance instance = enumeration.nextElement();

			Node node = root;
			boolean shouldAdd = true;
			while (node.getParent() != null) {
				node = node.getParent();

				switch (node.getSign()) {
				case EQUALS:
					if (Double.compare(instance.value(node.getAttribute()),
							node.getValue()) != 0) {
						shouldAdd = false;
						break;
					}
					break;
				case GREATEQ:
					if (Double.compare(instance.value(node.getAttribute()),
							node.getValue()) == -1) {
						shouldAdd = false;
						break;
					}
					break;
				case GREATER:
					if (Double.compare(instance.value(node.getAttribute()),
							node.getValue()) != 1) {
						shouldAdd = false;
						break;
					}
					break;
				case LESS:
					if (Double.compare(instance.value(node.getAttribute()),
							node.getValue()) != -1) {
						shouldAdd = false;
						break;
					}
					break;
				case LESSEQ:
					if (Double.compare(instance.value(node.getAttribute()),
							node.getValue()) == 1) {
						shouldAdd = false;
						break;
					}
					break;
				case NEQUALS:
					if (Double.compare(instance.value(node.getAttribute()),
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
				value += instance.classValue();
			}

		}

		return value / numOfFiltered;
	}

	public static double[] getFilteredInstancesClasses(Instances instances,
			Node root) {
		if (instances.numClasses() <= 1) {
			return Utils.empty_double_array;
		}

		double[] classes = new double[instances.numClasses()];

		@SuppressWarnings("unchecked")
		Enumeration<Instance> enumeration = (Enumeration<Instance>) instances
				.enumerateInstances();
		while (enumeration.hasMoreElements()) {
			Instance instance = enumeration.nextElement();

			Node node = root;
			boolean shouldAdd = true;
			while (node.getParent() != null) {
				node = node.getParent();

				switch (node.getSign()) {
				case EQUALS:
					if (Double.compare(instance.value(node.getAttribute()),
							node.getValue()) != 0) {
						shouldAdd = false;
						break;
					}
					break;
				case GREATEQ:
					if (Double.compare(instance.value(node.getAttribute()),
							node.getValue()) == -1) {
						shouldAdd = false;
						break;
					}
					break;
				case GREATER:
					if (Double.compare(instance.value(node.getAttribute()),
							node.getValue()) != 1) {
						shouldAdd = false;
						break;
					}
					break;
				case LESS:
					if (Double.compare(instance.value(node.getAttribute()),
							node.getValue()) != -1) {
						shouldAdd = false;
						break;
					}
					break;
				case LESSEQ:
					if (Double.compare(instance.value(node.getAttribute()),
							node.getValue()) == 1) {
						shouldAdd = false;
						break;
					}
					break;
				case NEQUALS:
					if (Double.compare(instance.value(node.getAttribute()),
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
				classes[(int) instance.classValue()]++;
			}

		}
		return classes;
	}

	public static HashMap<String, Integer> makeAttrIndexMap(Instances data) {
		HashMap<String, Integer> retHashMap = new HashMap<String, Integer>();
		for (int i = 0; i < data.numAttributes(); i++) {
			Attribute attr = data.attribute(i);
			retHashMap.put(attr.name(), attr.index());
		}
		return retHashMap;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Integer>[] makeAttrValueIndexMap(
			Instances data) {
		HashMap<String, Integer>[] retHashMap = new HashMap[data
				.numAttributes()];
		for (int i = 0; i < data.numAttributes(); i++) {
			Attribute attr = data.attribute(i);
			HashMap<String, Integer> attrValueMap = new HashMap<String, Integer>();
			if (attr.isNominal()) {
				for (int j = 0; j < attr.numValues(); j++) {
					attrValueMap.put(attr.value(j), j);
				}
				retHashMap[attr.index()] = attrValueMap;
			}

		}
		return retHashMap;
	}

	public static void makeAttrMap(Instances data,
			HashMap<String, Integer> attrIndexMap,
			HashMap<String, Integer>[] attrValueIndexMap) {
		for (int i = 0; i < data.numAttributes(); i++) {
			Attribute attr = data.attribute(i);
			attrIndexMap.put(attr.name(), attr.index());
			HashMap<String, Integer> attrValueMap = new HashMap<String, Integer>();
			if (attr.isNominal()) {
				for (int j = 0; j < attr.numValues(); j++) {
					attrValueMap.put(attr.value(j), j);
				}
				attrValueIndexMap[attr.index()] = attrValueMap;
			}
		}
	}

}
