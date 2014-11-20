package genlib.evolution.individuals;

import genlib.structures.BinaryNode;
import genlib.structures.MultiWayNode;
import genlib.structures.Node;

public class TreeIndividual extends Individual {

	/** for serialization */
	private static final long serialVersionUID = -3453723130233326900L;
	private Node root;

	public TreeIndividual(TreeIndividual toCopy) {
		if (toCopy.getRootNode() instanceof BinaryNode) {
			this.root = new BinaryNode((BinaryNode)toCopy.root);
		} else {
			this.root = new MultiWayNode((MultiWayNode)toCopy.root);
		}
	}
	
	public TreeIndividual(boolean binary) {
		if (binary) {
			this.root = new BinaryNode();
		} else {
			this.root = new MultiWayNode();
		}
	}
	
	public Node getRootNode() {
		return root;
	}

}
