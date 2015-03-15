package genlib.utils;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.MultiWayDepthNode;
import genlib.structures.MultiWayNode;
import genlib.structures.Node;
import genlib.utils.Utils.Sign;

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

	public static TreeIndividual constructTreeIndividual(String sTree, double nodeCount,
			int instCount,
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
		double incorrClass = 0d;
		Node[] nodes = new Node[(int)nodeCount];
		int[][] edges = new int[(int)nodeCount - 1][];

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
				if (!m.group(4).isEmpty()) {					
					incorrClass += Double.parseDouble(m.group(4));
				}
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
				if (attrValueIndexMap[nodeIndex]==null) {
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
						nodes[edgeIndex].setValue(Double.parseDouble(m.group(4)));
						edges[edgeIndex][0] = Integer.parseInt(m.group(2));
						nodes[edgeIndex].setSign(makeSign(m.group(3)));
					} else {
						edges[edgeIndex][1] = Integer.parseInt(m.group(2));
					}			
				} else {
					edgeNodeIndex = attrValueIndexMap[nodes[edgeIndex]
						.getAttribute()].get(m.group(4));
					edges[edgeIndex][edgeNodeIndex] = Integer.parseInt(m.group(2));
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
		treeInd.setFitnessValue(FitnessFunction.TREE_ACCURACY, 1 - incorrClass/instCount);
		return treeInd;
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
