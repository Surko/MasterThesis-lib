package genlib.evolution.individuals;

import genlib.evolution.fitness.FitnessFunction;
import genlib.structures.trees.BinaryDepthNode;
import genlib.structures.trees.BinaryNode;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.structures.trees.MultiWayNode;
import genlib.structures.trees.Node;

public class TreeIndividual extends Individual {	
	
	/** for serialization */
	private static final long serialVersionUID = -3453723130233326900L;
	protected Node root;
	protected int numNodes, numLeaves; 

	public TreeIndividual(TreeIndividual toCopy) {
		this.root = toCopy.root.copy();
		this.complexFitness = toCopy.complexFitness;
		this.fitness = new double[toCopy.fitness.length];
		System.arraycopy(toCopy.fitness, 0, this.fitness, 0, this.fitness.length);
	}
	
	public TreeIndividual(Node root) {
		this.root = root;
		this.fitness = new double[FitnessFunction.registeredFunctions];
	}
	
	public TreeIndividual(boolean binary, boolean countDepth) {
		if (binary) {
			if (countDepth) {
				this.root = new BinaryDepthNode();
			} else {
				this.root = new BinaryNode();
			}
		} else {
			if (countDepth) {
				this.root = new MultiWayDepthNode();
			} else {
				this.root = new MultiWayNode();
			}
		}
		this.fitness = new double[FitnessFunction.registeredFunctions];
	}
	
	/**
	 * Returns the root of a tree for this individual.
	 * @return Root node of this individual
	 */
	public Node getRootNode() {
		return root;
	}

	/**
	 * Get size of a created tree individual.
	 * @return Size of tree in this individual
	 */
	public double getTreeSize() {
		return root.getTreeHeight();		
	}
	
	public int getNumNodes() {
		return numNodes;
	}
	
	public int getNumLeaves() {
		return numLeaves;
	}

	@Override
	public boolean equals(Object obj) {		
		if (obj == null) {
			return false;
		}
		
		return this.root.equals(((TreeIndividual)obj).root);
	}
	
	public TreeIndividual copy() {
		return new TreeIndividual(this);
	}
	
}
