package genlib.evolution.individuals;

import genlib.structures.BinaryDepthNode;
import genlib.structures.BinaryNode;
import genlib.structures.MultiWayDepthNode;
import genlib.structures.MultiWayNode;
import genlib.structures.Node;

public class TreeIndividual extends Individual {	
	
	/** for serialization */
	private static final long serialVersionUID = -3453723130233326900L;
	protected Node root;
	protected int numNodes, numLeaves; 

	public TreeIndividual(TreeIndividual toCopy) {
		if (toCopy.getRootNode() instanceof BinaryNode) {
			this.root = new BinaryNode((BinaryNode)toCopy.root);
		} else {
			this.root = new MultiWayNode((MultiWayNode)toCopy.root);
		}
		this.fitness = new double[toCopy.fitness.length];
		System.arraycopy(toCopy.fitness, 0, this.fitness, 0, this.fitness.length);
	}
	
	public TreeIndividual(Node root) {
		this.root = root;
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
