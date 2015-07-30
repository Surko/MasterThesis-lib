package genlib.evolution.fitness.tree.look;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.Data;
import genlib.utils.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which is used to compute size of the tree.
 * 
 * @author Lukas Surin
 *
 */
public class TreeSizeFitness extends FitnessFunction<TreeIndividual> {

	public enum TreeSizeEnum {
		BASE, MEAN;

		public static TreeSizeEnum value(String name) {
			if (name.equals(BASE.name())) {
				return BASE;
			}

			if (name.equals(MEAN.name())) {
				return MEAN;
			}

			return null;
		}
	}

	/** for serialization */
	private static final long serialVersionUID = -3857040653870276306L;
	private static final Logger LOG = Logger.getLogger(TreeSizeFitness.class
			.getName());
	/** name of the function */
	public static final String initName = "tSize";
	/** base of relative fitness function */
	private double base = -1;
	/** mean of relative fitness function */
	private double mean = -1;

	/**
	 * This method that overrides computeFitness from FitnessFunction class
	 * computes tree depth for an individual handed as parameter. If the
	 * individual hasn't changed then we can return value of this fitness right
	 * away from individual. Method calls node method getTreeHeight in that case
	 * if the node is instance of DepthExtension. Otherwise the height is
	 * computed by Utils method computeHeight. Because we want to use this
	 * fitness in max optimalization then we need to invert this value.
	 */
	@Override
	public double computeFitness(TreeIndividual individual) {
		if (!individual.hasChanged()) {
			return individual.getFitnessValue(index);
		}

		double size = individual.getTreeSize();

		if (base == -1 || mean == -1) {
			individual.setFitnessValue(index, -size);
		} else {
			individual.setFitnessValue(index,
				1d / Math.pow(base, Math.abs(size - mean)));
		}
		return individual.getFitnessValue(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setData(Data data) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParam(String param) {
		this.base = -1d;
		this.mean = -1d;

		if (param.equals(PermMessages._blank_param)) {
			return;
		}

		String[] parts = param.split(Utils.pDELIM);

		// some kind of exception if it's not divisible by two

		for (int i = 0; i < parts.length; i += 2) {
			TreeSizeEnum accEnum = TreeSizeEnum.value(parts[i]);

			if (accEnum == null) {
				LOG.log(Level.INFO, String.format(TextResource
						.getString(TextKeys.iExcessParam), String.format(
						PermMessages._param_format, parts[i], parts[i + 1])));
				continue;
			}

			switch (accEnum) {
			case BASE:
				this.base = Double.parseDouble(parts[i + 1]);
				break;
			case MEAN:
				this.mean = Double.parseDouble(parts[i + 1]);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String objectInfo() {
		return String.format(PermMessages._fit_format, initName,
				PermMessages._blank_param);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canHandleNumeric() {
		return true;
	}
}
