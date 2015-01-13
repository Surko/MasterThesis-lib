package genlib.evolution.fitness;

import genlib.evolution.individuals.TreeIndividual;

public class TreeDepthFitness extends FitnessFunction<TreeIndividual> {

	@Override
	public void computeFitness(TreeIndividual individual) {		
		individual.setFitnessValue(TREE_SIZE_INDEX, individual.getRootNode().getTreeDepth());
	}

}
