package genlib.classifier.gens;

import genlib.classifier.splitting.SplitCriteria;
import genlib.evolution.individuals.TreeIndividual;
import genlib.exceptions.PopulationInitializationException;
import genlib.structures.trees.Node;
import genlib.utils.Utils;
import genlib.utils.Utils.Sign;
import weka.classifiers.trees.j48.C45Split;
import weka.classifiers.trees.j48.Distribution;
import weka.core.Attribute;
import weka.core.Instances;

public class WekaSimpleStumpGenerator extends TreeGenerator {

	/** for serialization */
	private static final long serialVersionUID = 9199025876883616284L;
	public static final String initName = "wSSGen";

	public WekaSimpleStumpGenerator() {
		this.genHeight = 1;
	}

	public WekaSimpleStumpGenerator(WekaSimpleStumpGenerator gen) {
		this.genHeight = 1;
	}

	private TreeIndividual[] createPopulation(Instances data) throws Exception {
		individuals = new TreeIndividual[data.numAttributes() - 1];
		individualCount = 0;

		for (int attrIndex = 0; attrIndex < individuals.length; attrIndex++) {
			Attribute attr = data.attribute(attrIndex);
			if (attr.isNominal()) {
				// generate not binary child for each attribute
				TreeIndividual attrIndividual = new TreeIndividual(false,
						autoHeight);
				individuals[attrIndex] = attrIndividual;
				Node rootNode = attrIndividual.getRootNode();
				// configure root node with attribute and count of child nodes
				rootNode.setAttribute(attrIndex);
				rootNode.setChildCount(attr.numValues());
				// distribution of classes for each child
				C45Split split = new C45Split(attrIndex, 2, data.sumOfWeights());
				split.buildClassifier(data);
				Distribution distribution = split.distribution();
				for (int bagIndex = 0; bagIndex < attr.numValues(); bagIndex++) {
					Node classNode = rootNode.newInstance();
					classNode.setValue(distribution.maxClass(bagIndex));
					rootNode.setChildAt(bagIndex, classNode);
				}
				// rootNode.setCriteriaValue(splitCriteria.computeCriteria(distribution));
			}
			if (attr.isNumeric()) {
				// generate not binary child for each attribute
				TreeIndividual attrIndividual = new TreeIndividual(false,
						autoHeight);
				individuals[attrIndex] = attrIndividual;
				Node rootNode = attrIndividual.getRootNode();
				// configure of root node with attribute and count of child
				// nodes
				rootNode.setAttribute(attrIndex);
				rootNode.setChildCount(2);
				// distribution of classes for each child
				C45Split split = new C45Split(attrIndex, 2, data.sumOfWeights());
				split.buildClassifier(data);
				Distribution distribution = split.distribution();

				String[] splitPointString = split.rightSide(0, data).split(
						Utils.oDELIM);
				rootNode.setValue(Double.parseDouble(splitPointString[2]));
				rootNode.setSign(Sign.LESSEQ);

				Node child0 = rootNode.newInstance();
				Node child1 = rootNode.newInstance();
				child0.setValue(distribution.maxClass(0));
				child1.setValue(distribution.maxClass(1));
				rootNode.setChildAt(0, child0);
				rootNode.setChildAt(1, child1);
				// rootNode.setValue(splitCriteria.getSplitPoint());
				// rootNode.setCriteriaValue(splitCriteria.getCriteriaValue());
			}
			individualCount++;
		}

		return individuals;
	}

	@Override
	public TreeIndividual[] createPopulation() throws Exception {
		if (data instanceof Instances) {
			return createPopulation((Instances) data);
		} else {
			throw new PopulationInitializationException("Bad type of instances");
		}
	}

	/**
	 * Method which returns true iff weka lib is needed to generate trees.
	 * Without weka, runtime exception should be thrown when initializing this
	 * generator
	 * 
	 * @return true iff is weka dependent
	 */
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
	public void setAdditionalOptions(String[] options) throws Exception {
	}

	@Override
	public void setParam(String param) {
	}

	@Override
	public void setSplitCriteria(SplitCriteria<?, ?> splitCriteria) {
	}

	@Override
	public WekaSimpleStumpGenerator copy() {
		return new WekaSimpleStumpGenerator(this);
	}
}
