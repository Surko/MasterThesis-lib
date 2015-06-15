package genlib.evolution.individuals;

import java.util.Arrays;

import genlib.evolution.fitness.FitnessFunction;
import genlib.structures.extensions.HeightExtension;
import genlib.structures.extensions.NodeCountExtension;
import genlib.structures.extensions.SizeExtension;
import genlib.structures.trees.BinaryHeightNode;
import genlib.structures.trees.BinaryNode;
import genlib.structures.trees.MultiWayHeightNode;
import genlib.structures.trees.MultiWayNode;
import genlib.structures.trees.Node;
import genlib.utils.Utils;

public class TreeIndividual extends Individual {	
	
	/** for serialization */
	private static final long serialVersionUID = -3453723130233326900L;
	protected Node root;	

	public TreeIndividual(TreeIndividual toCopy) {
		this.root = toCopy.root.copy();
		this.hasChanged = toCopy.hasChanged;
		this.complexFitness = toCopy.complexFitness;
		this.fitness = new double[toCopy.fitness.length];
		System.arraycopy(toCopy.fitness, 0, this.fitness, 0, this.fitness.length);
	}
	
	public TreeIndividual(Node root) {
		this.root = root;
		this.fitness = new double[FitnessFunction.registeredFunctions];
	}
	
	public TreeIndividual(boolean binary, boolean autoHeight) {
		if (binary) {
			if (autoHeight) {
				this.root = new BinaryHeightNode();
			} else {
				this.root = new BinaryNode();
			}
		} else {
			if (autoHeight) {
				this.root = new MultiWayHeightNode();
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
		if (root instanceof SizeExtension) {
			return ((SizeExtension)root).getTreeSize();
		} else {
			return Utils.computeNumNodes(root);
		}		
	}
	
	public int getNumLeaves() {
		if (root instanceof SizeExtension && root instanceof NodeCountExtension) {
			return ((SizeExtension)root).getTreeSize() - ((NodeCountExtension)root).getNumNodes(); 
		}
		return 0;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
	
	public String toString() {
		String format = Arrays.toString(fitness);		
		if (root != null) {
			format = String.format("%s;%s", format, root.toString());
		}
		return format;
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
