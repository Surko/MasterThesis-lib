package genlib.classifier.gens;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.MultiWayNode;
import weka.classifiers.trees.j48.Distribution;
import weka.core.Attribute;
import weka.core.Instances;

public class WekaSimpleStumpGenerator extends SimpleStumpGenerator {	

	/** for serialization */
	private static final long serialVersionUID = 9199025876883616284L;

	private TreeIndividual[] createPopulation(Instances data) throws Exception {
		individuals = new TreeIndividual[data.numAttributes()-1];		
		individualCount = 0;
		
		for (int attrIndex = 0; attrIndex < individuals.length; attrIndex++) {			
			Attribute cAttr = data.attribute(attrIndex);
			if (cAttr.isNominal()) {
				// generate not binary child for each attribute
				TreeIndividual attrIndividual = new TreeIndividual(false);
				individuals[attrIndex] = attrIndividual;
				MultiWayNode rootNode = (MultiWayNode)attrIndividual.getRootNode();
				// configure root node with attribute and count of child nodes
				rootNode.setAttribute(attrIndex);
				rootNode.setChildLength(cAttr.numValues());
				// distribution of classes for each child
				Distribution distribution = splitCriteria.handleEnumeratedAttribute(data,attrIndex,cAttr.numValues());
				for (int bagIndex = 0; bagIndex < cAttr.numValues(); bagIndex++) {
					MultiWayNode classNode = new MultiWayNode();
					classNode.setParent(rootNode);
					classNode.setValue(distribution.maxClass(bagIndex));						
					rootNode.setChildAt(bagIndex, classNode);
				}
				rootNode.setCriteriaValue(splitCriteria.computeCriteria(distribution));
			}
			if (cAttr.isNumeric()) {
				// generate not binary child for each attribute
				TreeIndividual attrIndividual = new TreeIndividual(false);
				individuals[attrIndex] = attrIndividual;
				MultiWayNode rootNode = (MultiWayNode)attrIndividual.getRootNode();
				// configure of root node with attribute and count of child nodes
				rootNode.setAttribute(attrIndex);
				rootNode.setChildLength(cAttr.numValues());
				// distribution of classes for each child
				Distribution distribution = splitCriteria.handleNumericAttribute(data,attrIndex,data.numClasses());
				// It should be checked if distribution is null but for simple stumps it's not needed
				// Numeric attribute is two-divided 
				MultiWayNode leftNode = new MultiWayNode();
				MultiWayNode rightNode = new MultiWayNode();
				leftNode.setParent(rootNode);
				leftNode.setValue(distribution.maxClass(0));
				rightNode.setParent(rootNode);
				rightNode.setValue(distribution.maxClass(1));
				rootNode.setValue(splitCriteria.getSplitPoint());
				rootNode.setChildAt(0, leftNode);											
				rootNode.setChildAt(1, rightNode);																
				rootNode.setCriteriaValue(splitCriteria.getCriteriaValue());
			}
			individualCount++;
		}

		return individuals;
	}

	@Override
	public TreeIndividual[] createPopulation() throws Exception {
		if (data instanceof Instances) {
			return createPopulation((Instances)data);
		} else {
			throw new Exception("Bad type of instances");
		}
	}

	@Override
	public String getInfo() {
		return "Single tree generator, extended for weka use, that generates stump decision trees";
	}	

}
