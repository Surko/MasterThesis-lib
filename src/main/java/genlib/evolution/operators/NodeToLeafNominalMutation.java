package genlib.evolution.operators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;
import genlib.locales.PermMessages;
import genlib.structures.Data;
import genlib.structures.trees.Node;
import genlib.utils.Utils;
import genlib.utils.WekaUtils;

import java.util.ArrayList;
import java.util.Random;

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
	/** name of this operator */
	public static final String initName = "ntlNomM";
	/** random object for this operator */
	private Random random;
	/** probability of this operator */
	private double mProb = 0d;
	/** data from which we take the most frequent class */
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
	 * {@inheritDoc} </p> This mutation will change the randomly chosen 
	 * node to leaf with the most frequent class.
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
			classes = Utils.getFilteredInstancesClasses(data.toGenLibInstances(), toMutate);			
		} else if (data.isInstances()) {
			classes = WekaUtils.getFilteredInstancesClasses(data.toInstances(), toMutate);			
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
	 * {@inheritDoc} </p> This operator sets the Data object into field data.
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
		return String.format(PermMessages._fit_format, initName, mProb);
	}

	/**
	 * {@inheritDoc} <\p> This operator does not set the Random object.
	 */
	@Override
	public void setRandomGenerator(Random random) {
		this.random = random;
	}

}
