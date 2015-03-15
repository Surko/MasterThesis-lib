package genlib.evolution.fitness;

import genlib.evolution.individuals.TreeIndividual;
import genlib.structures.Node;
import genlib.structures.extensions.DepthExtension;
import genlib.utils.Utils;

public class TreeDepthFitness extends FitnessFunction<TreeIndividual> {
	
	public static final String initName = "tDepth";
	/** 
	 * Constructor that will set index of tree depth fitness which is used in an individual fitness array.
	 * It is same as for tree height because it's almost the same.
	 */ 
	public TreeDepthFitness() {
		this.index = TREE_DEPTH;
	}
	
	/**
	 * This method that overrides computeFitness from FitnessFunction class computes
	 * tree depth for an individual handed as parameter. If the individual hasn't changed then
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
			individual.setFitnessValue(index, individual.getRootNode().getTreeHeight() - 1);
		} else {
			Node root = individual.getRootNode();
			individual.setFitnessValue(index, Utils.computeHeight(root) - 1);
		}
		
		// set to unchange for speeding up next time computation if the individual stays the same.
		individual.unchange();
		return individual.getFitnessValue(index);
	}
	
	@Override
	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}
}
