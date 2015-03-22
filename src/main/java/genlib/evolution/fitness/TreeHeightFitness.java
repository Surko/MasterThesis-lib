package genlib.evolution.fitness;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.Node;
import genlib.structures.extensions.DepthExtension;
import genlib.utils.Utils;

public class TreeHeightFitness extends FitnessFunction<TreeIndividual> {
	
	public static final String initName = "tHeight";
	/** 
	 * Constructor that will set index of tree height fitness which is used in an individual fitness array.
	 * It is same as for tree depth because it's almost the same.
	 */
	public TreeHeightFitness() {
		this.index = FitnessIndeces.TREE_HEIGHT.getIndex();
	}
	
	/**
	 * This method that overrides computeFitness from FitnessFunction class computes
	 * tree height for an individual handed as parameter. If the individual hasn't changed then
	 * we can return value of this fitness right away from individual. Method calls node method getTreeHeight
	 * in that case if the node is instance of DepthExtension. Otherwise the height is computed by Utils 
	 * method computeHeight. If we want to use this fitness in max optimalization then we need to invert this value.  
	 */
	@Override
	public double computeFitness(TreeIndividual individual) {
		if (!individual.hasChanged()) {
			return individual.getFitnessValue(index);
		}
		
		if (individual.getRootNode() instanceof DepthExtension) {
			individual.setFitnessValue(index, individual.getRootNode().getTreeHeight());
		} else {
			Node root = individual.getRootNode();
			individual.setFitnessValue(index, Utils.computeHeight(root));
		}

		return individual.getFitnessValue(index);
	}
	
	@Override
	public void setData(Object data) {}
	
	@Override
	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}

	public void setIndex(int index) {}
	
	@Override
	public void setParam(String param) {}
}
