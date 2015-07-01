package genlib.classifier;

import genlib.GenLib;
import genlib.classifier.classifierextensions.GenLibClassifierExtension;
import genlib.classifier.common.EvolutionTreeClassifier;
import genlib.evolution.individuals.TreeIndividual;
import genlib.exceptions.WrongDataException;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.Data;
import genlib.structures.data.GenLibInstance;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.Node;
import genlib.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;

import weka.core.OptionHandler;
import weka.core.Randomizable;
import weka.core.TechnicalInformationHandler;

/**
 * Classifier which extends from weka class {@link Classifier}. It implements
 * usual methods from there. Here in this class, we follow some established
 * principles for example implementing necessary methods </br>
 * <p>
 * {@link #buildClassifier} - to build our classifier, </br> {@link #setOptions}
 * - to set options from inside the weka, </br> {@link #listOptions} - to list
 * options from weka GUI, and others... </br> {@link #classifyInstance} - to
 * classify instance with builded classifier </br> and others... </br>
 * </p>
 * For this purpose (correct functionality of methods) it even implements from
 * some of the weka interfaces {@link Randomizable}, {@link OptionHandler},
 * {@link TechnicalInformationHandler}. </br> Name of this classifier inside the
 * weka is the same as this class name.
 * 
 * @see Classifier
 * @author Lukas Surin
 *
 */
public abstract class AbstractGenLibEvolution implements Serializable,
		Classifier, GenLibClassifierExtension {

	/** for serialization */
	private static final long serialVersionUID = 4737451154277087874L;
	/** Evolution classificator not dependant on weka. */
	private EvolutionTreeClassifier e_tree_class;

	/**
	 * Constructor for EvolutionClassifier which initialize
	 * EvolutionTreeClasifier class that is used as main classificator not
	 * dependant on weka. This approach let you use the library in weka as well
	 * as in different projects.
	 * 
	 * @throws Exception
	 *             Throws exception if
	 */
	public AbstractGenLibEvolution() throws Exception {
		GenLib.reconfig();
		this.e_tree_class = new EvolutionTreeClassifier(false);
	}

	@Override
	public void buildClassifier(GenLibInstances data) throws Exception {
		e_tree_class.buildClassifier(data);
	}

	@Override
	public void buildClassifier(Data data) throws Exception {
		if (data.isGenLibInstances()) {
			buildClassifier(data.toGenLibInstances());
		} else {
			throw new WrongDataException(String.format(
					TextResource.getString(TextKeys.eTypeParameter),
					GenLibInstances.class.getName(), data.getData().getClass()
							.getName()));
		}
	}

	/**
	 * Classifies an {@link GenLibInstance} with newly created
	 * {@link TreeIndividual}.
	 *
	 * @param instance
	 *            the instance to classify
	 * @return the classification for the instance
	 * @throws Exception
	 *             if instance can't be classified successfully
	 */
	public double classifyInstance(GenLibInstance instance) throws Exception {
		int treesToClassify = e_tree_class.getClassify();
		
		if (treesToClassify == 1) {
			TreeIndividual bestIndividual = e_tree_class.getBestIndividuals().get(0);
			Node root = bestIndividual.getRootNode();
			while (!root.isLeaf()) {
				if (instance.getAttribute(root.getAttribute()).isNumeric()) {
					if (Utils.isValueProper(
							instance.getValueOfAttribute(root.getAttribute()),
							root.getSign(), root.getValue())) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance.getValueOfAttribute(root
							.getAttribute()));
				}								
			}
			
			return root.getValue();
		}
		
		boolean nominal = instance.getClassAttribute().isNominal();
		double[] numOfClassifications;
		ArrayList<TreeIndividual> bestIndividuals = e_tree_class.getBestIndividuals();
		
		if (nominal) {
			numOfClassifications = new double[instance.getClassAttribute().numOfValues()];
		} else {
			numOfClassifications = new double[1];
		}

		for (int i = 0; i < treesToClassify; i++) {
			TreeIndividual bestIndividual = bestIndividuals.get(i);
			Node root = bestIndividual.getRootNode();
			while (!root.isLeaf()) {
				if (instance.getAttribute(root.getAttribute()).isNumeric()) {
					if (Utils.isValueProper(
							instance.getValueOfAttribute(root.getAttribute()),
							root.getSign(), root.getValue())) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance.getValueOfAttribute(root
							.getAttribute()));
				}								
			}
			
			if (nominal) {
				numOfClassifications[(int) root.getValue()]++;
			} else {
				numOfClassifications[0] += root.getValue();
			}
		}
		
		int maxIndex = 0;
		if (nominal) {
			double maxValue = numOfClassifications[0];					
			for (int i = 1; i < numOfClassifications.length; i++) {
				if (numOfClassifications[i] > maxValue) {
					maxValue = numOfClassifications[i];
					maxIndex = i;
				}
			}			
		} else {
			// only one value in numOfClassifications
			numOfClassifications[0] /= treesToClassify;
		}
		
		return numOfClassifications[maxIndex];

	}

	@Override
	public double[] classifyData(Data data) throws Exception {
		if (data.isGenLibInstances()) {
			double[] classifications = new double[data.numInstances()];

			GenLibInstances instances = data.toGenLibInstances();

			// should be enumeration of genlibinstances
			Enumeration<GenLibInstance> enumeration = instances.getInstances();

			int index = 0;
			while (enumeration.hasMoreElements()) {
				classifications[index++] = classifyInstance(enumeration
						.nextElement());
			}
			return classifications;
		} else {
			throw new WrongDataException(String.format(
					TextResource.getString(TextKeys.eTypeParameter),
					GenLibInstances.class.getName(), data.getData().getClass()
							.getName()));
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void setOptions(String[] options);
}
