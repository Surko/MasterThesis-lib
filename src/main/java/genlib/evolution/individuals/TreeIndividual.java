package genlib.evolution.individuals;

import genlib.evolution.fitness.FitnessFunction;
import genlib.structures.extensions.HeightExtension;
import genlib.structures.extensions.SizeExtension;
import genlib.structures.trees.BinaryDepthNode;
import genlib.structures.trees.BinaryNode;
import genlib.structures.trees.MultiWayDepthNode;
import genlib.structures.trees.MultiWayNode;
import genlib.structures.trees.Node;
import genlib.utils.Utils;

public class TreeIndividual extends Individual {	
	
	/** for serialization */
	private static final long serialVersionUID = -3453723130233326900L;
	protected Node root;	

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
	public double getTreeHeight() {
		if (root instanceof HeightExtension) {
			return ((HeightExtension) root).getTreeHeight();
		} else {			
			return Utils.computeHeight(root);
		}
	}
	
	public int getTreeSize() {
		if (root instanceof SizeExtension) {
			return ((SizeExtension) root).getTreeSize();
		} else {			
			return Utils.computeSize(root);
		}
	}
	
	public int getNumNodes() {
		return 0;
	}
	
	public int getNumLeaves() {
		return 0;
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
