package genlib.evolution.operators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.operators.Operator.OperEnum;
import genlib.evolution.population.IPopulation;
import genlib.locales.PermMessages;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.Data;
import genlib.structures.extensions.SizeExtension;
import genlib.structures.trees.Node;
import genlib.utils.Utils;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The most used Tree Crossover with very simple functionality. It chooses
 * random subtree in one individual and second individual. Those subtrees are
 * then crossovered with some probability. If not, then parents are put back
 * into offspring array without change.
 * 
 * @see Operator
 * @author Lukas Surin
 *
 */
public class SubTreeCrossover implements Operator<TreeIndividual> {

	/** for serialization */
	private static final long serialVersionUID = -8132560526319441150L;
	/** logger for this class */
	private static final Logger LOG = Logger.getLogger(SubTreeCrossover.class
			.getName());
	/** name of this operator */
	public static final String initName = "subTreeX";
	/** probability of crossover */
	private double xProb = 1d;
	/** random object for this operator */
	private Random random;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOperatorProbability(double prob) {
		this.xProb = prob;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getOperatorProbability() {
		return xProb;
	}

	/**
	 * {@inheritDoc} </p> This crossover will change the randomly chosen
	 * subtrees. If the operator isn't applied, than the parents are just added
	 * into childs array without changes (at least little bit optimized).
	 */
	@Override
	public void execute(IPopulation<TreeIndividual> parents,
			IPopulation<TreeIndividual> childs) {
		int size = parents.getActualPopSize();

		for (int i = 0; i < size / 2; i++) {
			TreeIndividual parent1 = parents.getIndividual(2 * i);
			TreeIndividual parent2 = parents.getIndividual(2 * i + 1);

			TreeIndividual child1 = parent1.copy();
			TreeIndividual child2 = parent2.copy();

			if (random.nextDouble() < xProb) {
				crossIndividuals(child1, child2);

				child1.change();
				child2.change();
			}

			childs.add(child1);
			childs.add(child2);
		}
	}

	/**
	 * Method which apply the crossover on individuals. Random chosen subtree is
	 * changed between individuals.
	 * 
	 * @param c1
	 *            First child individual
	 * @param c2
	 *            Second child individual
	 */
	private void crossIndividuals(TreeIndividual c1, TreeIndividual c2) {
		Node r1 = c1.getRootNode();
		Node r2 = c2.getRootNode();

		int treeSize1 = 0;
		Node subTree1 = null, subTree2 = null;
		if (r1 instanceof SizeExtension) {
			treeSize1 = ((SizeExtension) r1).getTreeSize();
			int i1 = random.nextInt(treeSize1);
			subTree1 = Utils.getExtensionNode(r1, i1);
		} else {
			treeSize1 = Utils.computeSize(r1);
			int i1 = random.nextInt(treeSize1);
			subTree1 = Utils.getNode(r1, i1);
		}

		int treeSize2 = 0;
		if (r2 instanceof SizeExtension) {
			treeSize2 = ((SizeExtension) r2).getTreeSize();
			int i2 = random.nextInt(treeSize2);
			subTree2 = Utils.getExtensionNode(r2, i2);
		} else {
			treeSize2 = Utils.computeSize(r2);
			int i2 = random.nextInt(treeSize2);
			subTree2 = Utils.getNode(r2, i2);
		}

		Node parent1 = subTree1.getParent();
		Node parent2 = subTree2.getParent();

		if (parent1 == null) {
			c1.setRoot(subTree2);
		} else {
			for (int i = 0; i < parent1.getChildCount(); i++) {
				if (parent1.getChildAt(i) == subTree1) {
					parent1.setChildAt(i, subTree2);
				}
			}
		}

		if (parent2 == null) {
			c2.setRoot(subTree1);
		} else {
			for (int i = 0; i < parent2.getChildCount(); i++) {
				if (parent2.getChildAt(i) == subTree2) {
					parent2.setChildAt(i, subTree1);
				}
			}
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
	 * {@inheritDoc} </p> It does not use {@link OperEnum#DATA} enum from {@link OperEnum}.
	 */
	public void setParam(String param) {
		xProb = 0;

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
				this.xProb = Double.parseDouble(parts[i + 1]);
				break;
			case DATA:
				LOG.log(Level.INFO,
						String.format(
								TextResource.getString(TextKeys.iExcessParam),
								parts[i]));
				break;
			default:
				break;
			}
		}
	}

	/**
	 * {@inheritDoc} </p> This operator does not set the Data object.
	 */
	@Override
	public void setData(Data data) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String objectInfo() {
		String paramString = "";
		if (xProb >= 0 && xProb <= 1) {
			paramString = String.format(PermMessages._param_format,
					OperEnum.PROB, xProb);
		}		
		if (paramString.isEmpty()) {
			return String.format(PermMessages._fit_format, initName,
					PermMessages._blank_param);
		}
		return String.format(PermMessages._fit_format, initName,
				paramString);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRandomGenerator(Random random) {
		this.random = random;
	}

}
