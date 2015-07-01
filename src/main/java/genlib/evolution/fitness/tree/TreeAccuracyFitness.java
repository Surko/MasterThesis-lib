package genlib.evolution.fitness.tree;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.tree.confusion.TreePrevalenceFitness;
import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.Data;
import genlib.structures.data.GenLibInstances;
import genlib.structures.trees.Node;
import genlib.utils.Utils;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instance;
import weka.core.Instances;

public class TreeAccuracyFitness extends FitnessFunction<TreeIndividual> {

	/**
	 * AccuracyEnum that defines what kind of parameters are possible for
	 * accuracy fitness function.</p> Defined kinds of parameters: </br>
	 * {@link AccuracyEnum#DATA} </br> {@link AccuracyEnum#INDEX}
	 * 
	 * @author Lukas Surin
	 *
	 */
	protected enum AccuracyEnum {
		/**
		 * Index parameter of attribute for which we compute fitness
		 */
		INDEX,
		/**
		 * Data parameter to define what kind of splitting we use.
		 */
		DATA;

		public static AccuracyEnum value(String name) {
			if (name.equals(INDEX.name())) {
				return INDEX;
			}

			if (name.equals(DATA.name())) {
				return DATA;
			}

			return null;
		}
	}

	private static final Logger LOG = Logger
			.getLogger(TreeAccuracyFitness.class.getName());
	/** for serialization */
	private static final long serialVersionUID = 7900391876130772354L;
	public static final String initName = "tAcc";
	private Data data;
	private int attrIndex = -1;
	/** type of data split used in computing fitness */
	protected int typeOfData = -1;

	/**
	 * This method that overrides computeFitness from FitnessFunction class
	 * computes accuracy for an individual handed as parameter. If the
	 * individual hasn't changed then we can return value of this fitness right
	 * away from individual. Method calls other method with the same name that
	 * depends on type of data on which this fitness function works (weka or
	 * built-in type).
	 */
	@Override
	public final double computeFitness(TreeIndividual individual) {
		if (!individual.hasChanged()) {
			return individual.getFitnessValue(index);
		}

		double fitness = 0d;

		if (data.isInstances()) {
			fitness = computeFitness(data.toInstances(), individual);
		}
		if (data.isGenLibInstances()) {
			fitness = computeFitness(data.toGenLibInstances(), individual);
		}

		individual.setFitnessValue(index, fitness);
		return fitness;

	}

	@SuppressWarnings("unchecked")
	private double computeFitness(Instances instances, TreeIndividual individual) {
		Node root = individual.getRootNode();
		double allData = instances.numInstances();
		double correct = 0;

		Enumeration<Instance> eInstances = instances.enumerateInstances();
		while (eInstances.hasMoreElements()) {
			Instance instance = eInstances.nextElement();
			while (!root.isLeaf()) {
				if (instance.attribute(root.getAttribute()).isNumeric()) {
					if (genlib.utils.Utils.isValueProper(
							instance.value(root.getAttribute()), root.getSign(),
							root.getValue())) {
						root = root.getChildAt(0);
					} else {
						root = root.getChildAt(1);
					}
				} else {
					root = root.getChildAt((int) instance.value(root
							.getAttribute()));
				}
			}
			if (instance.classValue() == root.getValue()) {
				correct++;
			}
			root = individual.getRootNode();
		}
		double val = correct / allData;
		return val;
	}

	@SuppressWarnings("unchecked")
	private double computeFitness(GenLibInstances instances,
			TreeIndividual individual) {
		Node root = individual.getRootNode();
		double allData = instances.numInstances();
		double correct = 0;
		// TODO
		double val = correct / allData;
		return correct;
	}

	@Override
	public void setData(Data data) {
		this.data = data.getDataOfType(typeOfData);
	}

	@Override
	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}

	@Override
	public void setParam(String param) {
		this.attrIndex = -1;

		if (param.equals(PermMessages._blank_param)) {
			return;
		}

		String[] parts = param.split(Utils.pDELIM);

		// some kind of exception if it's not divisible by two

		for (int i = 0; i < parts.length; i += 2) {
			AccuracyEnum accEnum = AccuracyEnum.value(parts[i]);

			if (accEnum == null) {
				LOG.log(Level.INFO, String.format(TextResource
						.getString(TextKeys.iExcessParam), String.format(
						PermMessages._param_format, parts[i], parts[i + 1])));
				continue;
			}

			switch (accEnum) {
			case INDEX:
				this.attrIndex = Integer.parseInt(parts[i + 1]);
				break;
			case DATA:
				this.typeOfData = Integer.parseInt(parts[i + 1]);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public String objectInfo() {
		String paramString = "";
		if (attrIndex > -1) {
			paramString = String.format(PermMessages._param_format,
					AccuracyEnum.INDEX, attrIndex);
		}
		if (typeOfData == 0 || typeOfData == 1) {
			if (paramString.isEmpty()) {
				paramString = String.format(PermMessages._param_format,
						AccuracyEnum.DATA, typeOfData);
			} else {
				paramString = String.format(PermMessages._param_format,
						paramString, AccuracyEnum.DATA + "," + typeOfData);
			}
		}
		if (paramString.isEmpty()) {
			return String.format(PermMessages._fit_format, initName,
					PermMessages._blank_param);
		}
		return String.format(PermMessages._fit_format, initName, paramString);
	}

	@Override
	public boolean canHandleNumeric() {
		return true;
	}

}
