package genlib.classifier.gens;

import genlib.classifier.splitting.SplitCriteria;
import genlib.evolution.individuals.TreeIndividual;
import genlib.exceptions.NotDefParamException;
import genlib.exceptions.PopulationInitializationException;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.plugins.PluginManager;
import genlib.structures.trees.Node;
import genlib.utils.Utils;
import weka.classifiers.trees.j48.Distribution;
import weka.core.Attribute;
import weka.core.Instances;

public class WekaSimpleStumpGenerator extends TreeGenerator {

	enum SimpleStumpEnum {
		SPLIT;

		public static SimpleStumpEnum value(String name) {
			if (SPLIT.name().equals(name)) {
				return SPLIT;
			}

			return null;
		}
	}

	/** for serialization */
	private static final long serialVersionUID = 9199025876883616284L;
	public static final String initName = "wSSGen";
	/**
	 * Splitting criteria at nodes for attributes, computes information gain,
	 * etc...
	 */
	protected SplitCriteria<Instances, Distribution> splitCriteria;

	public WekaSimpleStumpGenerator() {
		this.genHeight = 1;
	}

	public WekaSimpleStumpGenerator(WekaSimpleStumpGenerator gen) {
		this.genHeight = 1;
		this.splitCriteria = gen.splitCriteria;
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
				Node[] childs = rootNode.getChilds();
				// distribution of classes for each child
				Distribution distribution = splitCriteria
						.handleEnumeratedAttribute(data, attrIndex,
								attr.numValues());
				for (int bagIndex = 0; bagIndex < attr.numValues(); bagIndex++) {
					Node classNode = rootNode.newInstance();
					classNode.setValue(distribution.maxClass(bagIndex));
					childs[bagIndex] = classNode;
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
				Node[] childs = rootNode.getChilds();
				// distribution of classes for each child
				Distribution distribution = splitCriteria
						.handleNumericAttribute(data, attrIndex,
								data.numClasses());
				// It should be checked if distribution is null but for simple
				// stumps it's not needed
				// Numeric attribute is two-divided
				childs[0] = rootNode.newInstance();
				childs[1] = rootNode.newInstance();
				childs[0].setValue(distribution.maxClass(0));
				childs[1].setValue(distribution.maxClass(1));
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

	public SplitCriteria<Instances, Distribution> getSplitCriteria() {
		return splitCriteria;
	}

	@Override
	public void setAdditionalOptions(String[] options) throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setParam(String param) {
		String[] params = param.split(Utils.pDELIM);

		for (int i = 0; i < params.length; i += 2) {
			SimpleStumpEnum simpleStumpEnum = SimpleStumpEnum.value(params[i]);

			if (simpleStumpEnum == null) {
				throw new NotDefParamException(String.format(
						TextResource.getString(TextKeys.iExcessParam),
						params[i]));
			}

			switch (simpleStumpEnum) {
			case SPLIT:
				SplitCriteria<?, ?> genericCriteria = PluginManager.splitCriterias
						.get(params[i + 1]);

				if (genericCriteria == null) {
					// not defined class for split criteria
					throw new NotDefParamException(String.format(
							TextResource.getString(TextKeys.eNotDefParam),
							params[i + 1]));
				}
				this.splitCriteria = (SplitCriteria<Instances, Distribution>) genericCriteria;
				break;
			default:
				break;
			}

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSplitCriteria(SplitCriteria<?, ?> splitCriteria) {
		this.splitCriteria = (SplitCriteria<Instances, Distribution>) splitCriteria;
	}

	@Override
	public WekaSimpleStumpGenerator copy() {
		return new WekaSimpleStumpGenerator(this);
	}
}
