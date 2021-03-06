package genlib.generators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.exceptions.PopulationInitializationException;
import genlib.splitfunctions.SplitCriteria;
import genlib.structures.trees.Node;
import genlib.utils.Utils;
import genlib.utils.Utils.Sign;
import weka.classifiers.trees.j48.C45Split;
import weka.classifiers.trees.j48.Distribution;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * Class with tree generator that generates simple stump individuals. It uses
 * C45Split class from Weka to make the best split at roots.
 * 
 * @author Lukas Surin
 *
 */
public class WekaSimpleStumpGenerator extends TreeGenerator {

	/** for serialization */
	private static final long serialVersionUID = 9199025876883616284L;
	/** name of this generator */
	public static final String initName = "wSSGen";

	/**
	 * Default constructor for this generator
	 */
	public WekaSimpleStumpGenerator() {
		this.genHeight = 1;
	}

	/**
	 * Copy constructor
	 * 
	 * @param gen
	 *            instance
	 */
	public WekaSimpleStumpGenerator(WekaSimpleStumpGenerator gen) {
		this.genHeight = 1;
	}

	/**
	 * Method that create individuals from instances. Splits are made with the
	 * help from weka using C45Split class. It creates as many individuals as
	 * many attributes the data set has.
	 * 
	 * @param data
	 *            instances
	 * @return individuals generated by this generator
	 * @throws Exception
	 *             if some problem occured creating population
	 */
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInfo() {
		return "Single tree generator, extended for weka use, that generates stump decision trees";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getGenName() {
		return initName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAdditionalOptions(String[] options) throws Exception {
	}

	/**
	 * {@inheritDoc} <br>
	 * This method is not used
	 */
	@Override
	public void setParam(String param) {
	}

	/**
	 * {@inheritDoc} <br>
	 * This method is not used
	 */
	@Override
	public void setSplitCriteria(SplitCriteria<?, ?> splitCriteria) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WekaSimpleStumpGenerator copy() {
		return new WekaSimpleStumpGenerator(this);
	}
}
