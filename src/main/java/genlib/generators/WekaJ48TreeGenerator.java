package genlib.generators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.splitfunctions.SplitCriteria;
import genlib.utils.Utils;
import genlib.utils.WekaUtils;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 * Class with tree generator that generates trees with help from J48 classifier.
 * Created trees are converted into individuals with method from
 * {@link WekaUtils}.
 * 
 * @author Lukas Surin
 *
 */
public class WekaJ48TreeGenerator extends TreeGenerator {

	/** for serialization */
	private static final long serialVersionUID = -266323902512927931L;
	/** name of this generator */
	public static final String initName = "wJ48Gen";
	/** J48 classifier field */
	private J48 j48Tree;

	/**
	 * Default constructor
	 */
	public WekaJ48TreeGenerator() {
		this.j48Tree = new J48();
	}

	/**
	 * Constructor that sets the options for J48 classifier.
	 * 
	 * @param options
	 *            for J48 classifier
	 * @throws Exception
	 *             if problem occurs when setting the classifier
	 */
	public WekaJ48TreeGenerator(String[] options) throws Exception {
		this.j48Tree = new J48();
		this.j48Tree.setOptions(options);
	}

	/**
	 * {@inheritDoc} <br>
	 * Tree are created with J48 classifier and reconstructed into
	 * TreeIndividuals.
	 * 
	 * @throws Exception
	 *             if problem occurs when creating individuals
	 */
	@Override
	public TreeIndividual[] createPopulation() throws Exception {
		if (data instanceof Instances) {
			return createPopulation((Instances) data);
		} else {
			throw new Exception("Bad type of instances");
		}
	}

	/**
	 * Method that creates trees with J48 classifier with Instances from weka.
	 * 
	 * @param data
	 *            Instances from Weka
	 * @return created individuals
	 * @throws Exception
	 *             if problem occurs when creating individuals
	 */
	private TreeIndividual[] createPopulation(Instances data) throws Exception {
		individuals = new TreeIndividual[1];
		// TODO ONLY ONE INDIVIDUAL IS HERE. ADD SOME SORT OF ATTRIBUTE REMOVING
		// OR DIFFERENT PRUNING MECHANISMS?
		j48Tree.buildClassifier(data);
		String sTree = j48Tree.graph();
		individuals[0] = WekaUtils.constructTreeIndividual(sTree,
				j48Tree.measureTreeSize(), data.numInstances(),
				treeInit.getAttrIndexMap(), treeInit.getAttrValueIndexMap(),
				treeInit.getAutoHeight());
		return individuals;
	}

	/**
	 * Method sets the options for this generator
	 * 
	 * @param options
	 *            for this generator
	 * @throws Exception
	 *             if problem occurs when setting options
	 */
	public void setOptions(String[] options) throws Exception {
		this.j48Tree.setOptions(options);
	}

	/**
	 * Method gets the J48 classifier
	 * 
	 * @return J48 classifier
	 */
	public J48 getJ48() {
		return j48Tree;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGenName() {
		return initName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInfo() {
		return "Tree generator which generates C4.5 trees via weka J48 algorithm";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isWekaDependent() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAdditionalOptions(String[] options) throws Exception {
		this.j48Tree.setOptions(options);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setParam(String param) throws Exception {
		this.j48Tree.setOptions(param.split(Utils.pDELIM));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSplitCriteria(SplitCriteria<?, ?> splitCriteria) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeGenerator copy() throws Exception {
		return new WekaJ48TreeGenerator(j48Tree.getOptions());
	}
}
