package genlib.evolution.operators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.operators.Operator.OperEnum;
import genlib.evolution.population.IPopulation;
import genlib.locales.PermMessages;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.Data;
import genlib.structures.trees.Node;
import genlib.utils.Utils;
import genlib.utils.WekaUtils;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Node to Leaf Mutation is mutation class that implements the Operator
 * interface. When the mutation is applied, the randomly chosen tree is changed
 * into leaf (with most frequent class)
 * 
 * @see Operator
 * @author Lukas Surin
 *
 */
public class NodeToLeafNominalMutation implements Operator<TreeIndividual> {

	/** for serialization */
	private static final long serialVersionUID = 6616737516801855707L;
	/** logger for this class */
	private static final Logger LOG = Logger
			.getLogger(NodeToLeafNominalMutation.class.getName());
	/** name of this operator */
	public static final String initName = "ntlNomM";
	/** random object for this operator */
	private Random random;
	/** probability of this operator */
	private double mProb = 0d;
	/** data from which we take the most frequent class */
	private Data data;
	/** type of data split used in operator */
	protected int typeOfData = -1;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOperatorProbability(double mProb) {
		this.mProb = mProb;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getOperatorProbability() {
		return mProb;
	}

	/**
	 * {@inheritDoc} </p> This mutation will change the randomly chosen node to
	 * leaf with the most frequent class.
	 */
	@Override
	public void execute(IPopulation<TreeIndividual> parents,
			IPopulation<TreeIndividual> childs) {
		ArrayList<Node> nodes = new ArrayList<>();
		for (TreeIndividual child : parents.getIndividuals()) {
			if (random.nextDouble() < mProb) {
				Node root = child.getRootNode();
				Utils.getNodes(root, nodes);
				if (nodes.size() > 0) {
					mutateNode(nodes);
					// change to recompute fitness
					child.change();
				}

			}
			nodes.clear();
		}

		if (childs != null) {
			childs.addAll(parents);
		}

	}

	private void mutateNode(ArrayList<Node> nodes) {
		int i = random.nextInt(nodes.size());

		Node toMutate = nodes.get(i);
		double[] classes = null;

		if (data.isGenLibInstances()) {
			classes = Utils.getFilteredInstancesClasses(
					data.toGenLibInstances(), toMutate);
		} else if (data.isInstances()) {
			classes = WekaUtils.getFilteredInstancesClasses(data.toInstances(),
					toMutate);
		}

		if (classes.length == 0) {
			return;
		}

		double max = 0;
		double maxIndex = 0;
		for (int index = 0; index < classes.length; index++) {
			if (classes[index] > max) {
				max = classes[index];
				maxIndex = index;
			}
		}

		toMutate.makeLeaf();
		toMutate.setValue(maxIndex);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWekaCompatible() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWekaDependent() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}

	/**
	 * {@inheritDoc} </p> It does use all enums from {@link OperEnum}.
	 */
	public void setParam(String param) {
		this.mProb = 0;
		this.typeOfData = -1;

		if (param.equals(PermMessages._blank_param)) {
			return;
		}

		String[] parts = param.split(Utils.pDELIM);

		// some kind of exception if it's not divisible by two

		for (int i = 0; i < parts.length; i += 2) {
			OperEnum opEnum = OperEnum.value(parts[i]);

			if (opEnum == null) {
				LOG.log(Level.INFO,
						String.format(
								TextResource.getString(TextKeys.iExcessParam),
								parts[i]));
				continue;
			}

			switch (opEnum) {
			case PROB:
				this.mProb = Double.parseDouble(parts[i + 1]);
				break;
			case DATA:
				this.typeOfData = Integer.parseInt(parts[i + 1]);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * {@inheritDoc} </p> This operator sets the Data object into field data.
	 */
	@Override
	public void setData(Data data) {
		this.data = data.getDataOfType(typeOfData);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String objectInfo() {
		String paramString = "";
		if (mProb >= 0 && mProb <= 1) {
			paramString = String.format(PermMessages._param_format,
					OperEnum.PROB, mProb);
		}
		if (typeOfData == 0 || typeOfData == 1) {
			if (paramString.isEmpty()) {
				paramString = String.format(PermMessages._param_format,
						OperEnum.DATA, typeOfData);
			} else {
				paramString = String.format(PermMessages._param_format,
						paramString, OperEnum.DATA + "," + typeOfData);
			}
		}
		if (paramString.isEmpty()) {
			return String.format(PermMessages._fit_format, initName,
					PermMessages._blank_param);
		}
		return String.format(PermMessages._fit_format, initName,
				paramString);
	}

	/**
	 * {@inheritDoc} <\p> This operator does not set the Random object.
	 */
	@Override
	public void setRandomGenerator(Random random) {
		this.random = random;
	}

}
