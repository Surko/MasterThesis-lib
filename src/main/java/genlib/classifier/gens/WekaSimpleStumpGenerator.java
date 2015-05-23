package genlib.classifier.gens;

import genlib.classifier.splitting.SplitCriteria;
import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.trees.MultiWayNode;
import weka.classifiers.trees.j48.Distribution;
import weka.core.Attribute;
import weka.core.Instances;

public class WekaSimpleStumpGenerator extends SimpleStumpGenerator {	

	/** for serialization */
	private static final long serialVersionUID = 9199025876883616284L;
	public static final String initName = "wSSGen";
	
	public WekaSimpleStumpGenerator() {
		super();		
	}

	public WekaSimpleStumpGenerator(WekaSimpleStumpGenerator gen) {
		super(gen);
	}

	public WekaSimpleStumpGenerator(SplitCriteria splitCriteria) {
		super(splitCriteria);
	}

	private TreeIndividual[] createPopulation(Instances data) throws Exception {
		individuals = new TreeIndividual[data.numAttributes()-1];		
		individualCount = 0;
		
		for (int attrIndex = 0; attrIndex < individuals.length; attrIndex++) {			
			Attribute cAttr = data.attribute(attrIndex);
			if (cAttr.isNominal()) {
				// generate not binary child for each attribute
				TreeIndividual attrIndividual = new TreeIndividual(false,treeInit.getAutoDepth());
				individuals[attrIndex] = attrIndividual;
				MultiWayNode rootNode = (MultiWayNode)attrIndividual.getRootNode();
				// configure root node with attribute and count of child nodes
				rootNode.setAttribute(attrIndex);
				rootNode.setChildCount(cAttr.numValues());	
				MultiWayNode[] childs = rootNode.getChilds();
				// distribution of classes for each child
				Distribution distribution = splitCriteria.handleEnumeratedAttribute(data,attrIndex,cAttr.numValues());
				for (int bagIndex = 0; bagIndex < cAttr.numValues(); bagIndex++) {
					MultiWayNode classNode = new MultiWayNode();					
					classNode.setValue(distribution.maxClass(bagIndex));						
					childs[bagIndex] =  classNode;
				}
				rootNode.setCriteriaValue(splitCriteria.computeCriteria(distribution));
			}
			if (cAttr.isNumeric()) {
				// generate not binary child for each attribute
				TreeIndividual attrIndividual = new TreeIndividual(false,treeInit.getAutoDepth());
				individuals[attrIndex] = attrIndividual;
				MultiWayNode rootNode = (MultiWayNode)attrIndividual.getRootNode();
				// configure of root node with attribute and count of child nodes
				rootNode.setAttribute(attrIndex);
				rootNode.setChildCount(2);				
				MultiWayNode[] childs = rootNode.getChilds();
				// distribution of classes for each child
				Distribution distribution = splitCriteria.handleNumericAttribute(data,attrIndex,data.numClasses());
				// It should be checked if distribution is null but for simple stumps it's not needed
				// Numeric attribute is two-divided 
				childs[0] = new MultiWayNode();
				childs[1] = new MultiWayNode();
				childs[0].setValue(distribution.maxClass(0));				
				childs[1].setValue(distribution.maxClass(1));
				rootNode.setValue(splitCriteria.getSplitPoint());																
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

	public boolean isWekaDependent() {
		return true;
	}
	
	@Override
	public String getInfo() {
		return "Single tree generator, extended for weka use, that generates stump decision trees";
	}	

	public String getGenName() {
		return initName;
	}

	@Override
	public WekaSimpleStumpGenerator copy() {
		return new WekaSimpleStumpGenerator(this);
	}

	@Override
	public void setAdditionalOptions(String[] options) throws Exception {}

	@Override
	public void setParam(String param) {}
	
}
