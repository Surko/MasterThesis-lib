package genlib.evolution.operators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;
import genlib.locales.PermMessages;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.Data;
import genlib.structures.trees.Node;
import genlib.utils.Utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Value Change Mutation is mutation class that implements the Operator
 * interface. When the mutation is applied, the randomly chosen leaf value is
 * changed.
 * 
 * @see Operator
 * @author Lukas Surin
 *
 */
public class ValueChangeMutation implements Operator<TreeIndividual> {


	/** for serialization */
	private static final long serialVersionUID = -8545768446148248971L;
	/** logger for this class */
	private static final Logger LOG = Logger
			.getLogger(ValueChangeMutation.class.getName());
	/** name of this operator */
	public static final String initName = "valueMut";
	/** random object for this operator */
	private Random random;
	/** probability of this operator */
	private double mProb = 0d;
	/** data from which we create stumps */
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
	 * {@inheritDoc} </p> This mutation will change the randomly chosen leaf to
	 * randomly chosen decision stump.
	 */
	@Override
	public void execute(IPopulation<TreeIndividual> parents,
			IPopulation<TreeIndividual> childs) {
		ArrayList<Node> leaves = new ArrayList<>();
		for (TreeIndividual child : parents.getIndividuals()) {
			if (random.nextDouble() < mProb) {
				Utils.getLeavesRecursive(child.getRootNode(), leaves);

				int lIndex = random.nextInt(leaves.size());
				Node leaf = leaves.get(lIndex);

				if (data.numClasses() > 1) {
					leaf.setValue(random.nextInt(data.numClasses()));
				} else {
					leaf.setValue(leaf.getValue() + 2 * random.nextDouble()
							- 0.5);
				}

				// change to recompute fitness
				child.change();
			}

			leaves.clear();
		}

		if (childs != null) {
			childs.addAll(parents);
		}
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
	 * {@inheritDoc} </p> This operator does set the Data object but uses it
	 * only to create decision stumps from them.
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
		return String.format(PermMessages._fit_format, initName, paramString);
	}

	/**
	 * {@inheritDoc} <\p> This operator does not set the Random object.
	 */
	@Override
	public void setRandomGenerator(Random random) {
		this.random = random;
	}

}
