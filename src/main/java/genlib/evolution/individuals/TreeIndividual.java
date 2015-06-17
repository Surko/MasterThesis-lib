package genlib.evolution.individuals;

import genlib.evolution.fitness.FitnessFunction;
import genlib.structures.extensions.HeightExtension;
import genlib.structures.extensions.LeavesCountExtension;
import genlib.structures.extensions.SizeExtension;
import genlib.structures.trees.BinaryHeightNode;
import genlib.structures.trees.BinaryNode;
import genlib.structures.trees.MultiWayHeightNode;
import genlib.structures.trees.MultiWayNode;
import genlib.structures.trees.Node;
import genlib.utils.Utils;

import java.util.Arrays;

public class TreeIndividual extends Individual {

	/** for serialization */
	private static final long serialVersionUID = -3453723130233326900L;
	/** root node of this tree individual */
	protected Node root;

	/**
	 * Copy constructor that copies the TreeIndividual from the parameters. It
	 * copies the {@link #root}, {@link Individual#complexFitness},
	 * {@link Individual#hasChanged} and {@link Individual#fitness} array.
	 * 
	 * @param toCopy
	 *            TreeIndividual to copy.
	 */
	public TreeIndividual(TreeIndividual toCopy) {
		this.root = toCopy.root.copy();
		this.hasChanged = toCopy.hasChanged;
		this.complexFitness = toCopy.complexFitness;
		this.fitness = new double[toCopy.fitness.length];
		System.arraycopy(toCopy.fitness, 0, this.fitness, 0,
				this.fitness.length);
	}

	/**
	 * Constructor with root as its parameter. It creates new instance and set
	 * the root of it. It initialize the fitness array with the amount of
	 * {@link FitnessFunction#registeredFunctions}.
	 * 
	 * @param root
	 *            root node to be set
	 */
	public TreeIndividual(Node root) {
		this.root = root;
		this.fitness = new double[FitnessFunction.registeredFunctions];
	}

	/**
	 * Constructor with two booleans as its parameters. It creates new instance
	 * and set the root of it depending on those boolean parameters. It
	 * initialize the fitness array with the amount of
	 * {@link FitnessFunction#registeredFunctions}.
	 * 
	 * @param binary
	 *            if the node should be binary or multi way
	 * @param autoHeight
	 *            if the node should compute height automatically or not
	 */
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
	 * 
	 * @return Root node of this individual
	 */
	public Node getRootNode() {
		return root;
	}

	/**
	 * Get tree height of a created tree individual. If the root is instance of
	 * {@link HeightExtension} than it uses the value from the root. Otherwise
	 * uses {@link Utils#computeHeight(Node)}
	 * 
	 * @return Height of tree in this individual
	 */
	public double getTreeHeight() {
		if (root instanceof HeightExtension) {
			return ((HeightExtension) root).getTreeHeight();
		} else {
			return Utils.computeHeight(root);
		}
	}

	/**
	 * Get tree size of a created tree individual. If the root is instance of
	 * {@link SizeExtension} than it uses the value from the root. Otherwise
	 * uses {@link Utils#computeSize(Node)}
	 * 
	 * @return Size of tree in this individual
	 */
	public int getTreeSize() {
		if (root instanceof SizeExtension) {
			return ((SizeExtension) root).getTreeSize();
		} else {
			return Utils.computeSize(root);
		}
	}

	/**
	 * Get num of leaves of a created tree individual. If the root is instance
	 * of {@link LeavesCountExtension} than it uses the value from the root.
	 * Otherwise uses {@link Utils#computeNumLeaves(Node)}
	 * 
	 * @return Num of leaves in this individual
	 */
	public int getNumLeaves() {
		if (root instanceof LeavesCountExtension) {
			return ((LeavesCountExtension) root).getNumLeaves();
		} else {
			return Utils.computeNumLeaves(root);
		}
	}

	/**
	 * Get num of nodes of a created tree individual. If the root is instance of
	 * {@link SizeExtension} and {@link LeavesCountExtension} than it uses the
	 * value from the root. Otherwise uses {@link Utils#computeNumNodes(Node)}
	 * 
	 * @return Num of nodes in this individual
	 */
	public int getNumNodes() {
		if (root instanceof SizeExtension
				&& root instanceof LeavesCountExtension) {
			return ((SizeExtension) root).getTreeSize()
					- ((LeavesCountExtension) root).getNumLeaves();
		} else {
			return Utils.computeNumNodes(root);
		}

	}

	/**
	 * Method that set the root of this individual.
	 * 
	 * @param root
	 *            node to set in this individual
	 */
	public void setRoot(Node root) {
		this.root = root;
	}

	/**
	 * {@inheritDoc} </p> It consists of fitness array in text format and
	 * {@link Node#toString()}.
	 */
	public String toString() {
		String format = Arrays.toString(fitness);
		if (root != null) {
			format = String.format("%s;%s", format, root.toString());
		}
		return format;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		return this.root.equals(((TreeIndividual) obj).root);
	}

	/**
	 * {@inheritDoc} </p> It just calls the copy constructor with
	 * <strong>this</strong> instance.
	 * 
	 */
	public TreeIndividual copy() {
		return new TreeIndividual(this);
	}

}
