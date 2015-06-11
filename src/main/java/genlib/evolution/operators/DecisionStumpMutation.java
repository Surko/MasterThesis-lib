package genlib.evolution.operators;

import genlib.classifier.gens.WekaSimpleStumpGenerator;
import genlib.classifier.splitting.InformationGainCriteria;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;
import genlib.locales.PermMessages;
import genlib.structures.Data;
import genlib.structures.extensions.HeightExtension;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.structures.trees.MultiWayNode;
import genlib.structures.trees.Node;
import genlib.utils.Utils;
import genlib.utils.WekaUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Decision Stump Mutation is mutation class that implements the Operator
 * interface. When the mutation is applied, the randomly chosen leaf is replaced
 * with decison stump.
 * 
 * @see Operator
 * @author Lukas Surin
 *
 */
public class DecisionStumpMutation implements Operator<TreeIndividual> {

	/** for serialization */
	private static final long serialVersionUID = -2553678098269930119L;
	/** name of this operator */
	public static final String initName = "dSM";
	/** random object for this operator */
	private Random random;
	/** probability of this operator */
	private double mProb;
	/** data from which we take the most frequent class */
	private TreeIndividual[] stumps;
	/** data from which we create stumps */
	private Data data;

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
				if (stumps == null) {
					createStumps(child.getRootNode());
				}

				Node root = child.getRootNode();
				Utils.getNodes(root, leaves);

				int lIndex = random.nextInt(leaves.size());
				int sIndex = random.nextInt(stumps.length);

				Node leafToMutate = leaves.get(lIndex);
				Node leafToMutateParent = leafToMutate.getParent();

				for (int i = 0; i < leafToMutateParent.getChilds().length; i++) {
					if (leafToMutateParent.getChildAt(i) == leafToMutate) {
						leafToMutateParent.setChildAt(i,
								stumps[sIndex].getRootNode());
						break;
					}
				}

			}
			leaves.clear();
		}
	}

	private void createStumps(Node node) {
		WekaSimpleStumpGenerator wekaSSGen = new WekaSimpleStumpGenerator();
		wekaSSGen.setInstances(data);
		wekaSSGen.setSplitCriteria(new InformationGainCriteria());
		if (node instanceof MultiWayNode) {
			wekaSSGen.setAutoDepth(false);
		}

		if (node instanceof MultiWayDepthNode) {
			wekaSSGen.setAutoDepth(true);
		}
		
		try {
			this.stumps = wekaSSGen.createPopulation();
		} catch (Exception e) {

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
	 * {@inheritDoc} </p> This operator does set the Data object but uses it
	 * only to create decision stumps from them.
	 */
	@Override
	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String objectInfo() {
		return String.format(PermMessages._fit_format, initName, 1.0);
	}

	/**
	 * {@inheritDoc} <\p> This operator does not set the Random object.
	 */
	@Override
	public void setRandomGenerator(Random random) {
		this.random = random;
	}

}
