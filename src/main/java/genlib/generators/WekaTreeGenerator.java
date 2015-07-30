package genlib.generators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.splitfunctions.SplitCriteria;
import genlib.utils.Utils;
import genlib.utils.WekaUtils;

import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.core.AdditionalMeasureProducer;
import weka.core.Drawable;
import weka.core.Instances;

/**
 * Class with tree generator that generates trees from classifiers (their graph
 * structure) that are defined in weka library. Created trees are converted into
 * individuals with method from {@link WekaUtils}.
 * 
 * @author Lukas Surin
 *
 */
public class WekaTreeGenerator extends TreeGenerator {

	/** for serialization */
	private static final long serialVersionUID = -266323902512927931L;
	/** name of this generator */
	public static final String initName = "wGen";
	/** */
	private static final String _type = "TYPE";
	/** weka parameter to earn tree size */
	private static final String _numNodes = "measureTreeSize";
	/** classifier from weka used to generate trees */
	private Classifier classifier;

	/**
	 * Default constructor
	 */
	public WekaTreeGenerator() {
	}

	/**
	 * Copy constructor
	 * 
	 * @param wekaTreeGenerator
	 *            instance
	 */
	public WekaTreeGenerator(WekaTreeGenerator wekaTreeGenerator) {
		this.classifier = wekaTreeGenerator.classifier;
	}

	/**
	 * {@inheritDoc}
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
	 * Method that create individuals from instances. Trees are generated with
	 * classifier from weka and converted to TreeIndividual with
	 * {@link WekaUtils#constructTreeIndividual(String, double, int, java.util.HashMap, java.util.HashMap[], boolean)}
	 * method.
	 * 
	 * @param data
	 *            instances
	 * @return individuals generated by this generator
	 * @throws Exception
	 *             if some problem occured creating population
	 */
	private TreeIndividual[] createPopulation(Instances data) throws Exception {
		individuals = new TreeIndividual[1];

		// TODO ONLY ONE INDIVIDUAL IS HERE. ADD SOME SORT OF ATTRIBUTE REMOVING
		// OR DIFFERENT PRUNING MECHANISMS?
		classifier.buildClassifier(data);
		String sTree = ((Drawable) classifier).graph();
		double treeSize = ((AdditionalMeasureProducer) classifier)
				.getMeasure(_numNodes);

		individuals[0] = WekaUtils.constructTreeIndividual(sTree, treeSize,
				data.numInstances(), treeInit.getAttrIndexMap(),
				treeInit.getAttrValueIndexMap(), treeInit.getAutoHeight());
		return individuals;
	}

	/**
	 * Method gets the classifier used to generate trees.
	 * 
	 * @return Classifier from weka
	 */
	public Classifier getClassifier() {
		return classifier;
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
		return "Tree generator which generates trees via weka algorithms";
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
		this.classifier.setOptions(options);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setParam(String param) throws Exception {
		String[] options = param.split(Utils.pDELIM);

		String classifierName = "";
		ArrayList<String> otherOptions = new ArrayList<>();
		for (int i = 0; i < options.length; i++) {
			if (options[i].equals(_type)) {
				classifierName = options[i + 1];
				i++;
				continue;
			}

			otherOptions.add(options[i]);
		}

		classifier = Classifier.forName(classifierName,
				otherOptions.toArray(new String[otherOptions.size()]));
		if (!(classifier instanceof Drawable)) {
			throw new Exception();
		}

		if (!(classifier instanceof AdditionalMeasureProducer)) {
			throw new Exception();
		}

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
	public TreeGenerator copy() throws Exception {
		return new WekaTreeGenerator(this);
	}
}
