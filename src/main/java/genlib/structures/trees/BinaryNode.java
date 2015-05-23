package genlib.structures.trees;

import genlib.exceptions.NodeCreationException;
import genlib.locales.TextResource;
import genlib.structures.extensions.SizeExtension;
import genlib.utils.Utils.Sign;

public class BinaryNode implements Node, SizeExtension {

	/** for serialization */
	private static final long serialVersionUID = -5557876005762565687L;
	protected BinaryNode parent;
	protected BinaryNode[] childs;
	protected int treeSize;
	protected int attribute = -1;
	protected double value = Integer.MIN_VALUE;
	protected double criteriaValue;
	protected Sign sign;

	/**
	 * Static factory that creates leaf with value of classification. It only
	 * sets this values because all other fields are defaultly set.
	 * 
	 * @param value
	 *            value of classification
	 * @return node leaf
	 */
	public static BinaryNode makeLeaf(double value) {
		BinaryNode leaf = new BinaryNode();
		leaf.value = value;
		return leaf;
	}

	/**
	 * Static factory that creates node with two childs, attribute on which to
	 * split sign of split and value of attribute on which to split
	 * 
	 * @param childCount
	 *            number of childs
	 * @param attribute
	 *            split attribute
	 * @param sign
	 *            split sign
	 * @param value
	 *            split value of attribute
	 * @return node
	 */
	public static BinaryNode makeNode(int attribute, Sign sign, double value) {
		if (attribute == -1) {
			throw new NodeCreationException(String.format(
					TextResource.getString("eNodeCreation"), "attribute"));
		}

		BinaryNode node = new BinaryNode();
		node.childs = new BinaryNode[2];
		node.sign = sign;
		node.attribute = attribute;
		node.treeSize = 3;
		return node;
	}

	/**
	 * Parameterless constructor for serialization and static methods
	 */
	public BinaryNode() {
	}

	public BinaryNode(BinaryNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.sign = toCopy.sign;
		if (!toCopy.isLeaf()) {
			this.childs = new BinaryNode[2];
			this.childs[0] = new BinaryNode(toCopy.childs[0]);
			this.childs[1] = new BinaryNode(toCopy.childs[1]);
			this.childs[0].parent = this;
			this.childs[1].parent = this;
		}
	}

	public BinaryNode(int attribute, Sign sign, double value) {
		if (attribute == -1) {
			this.attribute = attribute;
			this.sign = sign;
			this.value = value;
		} else {

		}
	}

	// SETTERS

	public void setParent(Node parent) {
		this.parent = (BinaryNode) parent;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public void setChildAt(int index, Node node) {
		if (childs == null) {
			childs = new BinaryNode[2];
		}

		int oldTreeSize = 0;
		int newTreeSize = 0;

		if (childs[index] != null) {
			oldTreeSize = childs[index].getTreeSize();
		}

		childs[index] = (BinaryNode) node;
		newTreeSize = childs[index].getTreeSize();
		node.setParent(this);

		// it will serve the purpose of diff
		newTreeSize = newTreeSize - oldTreeSize;
		if (newTreeSize != 0) {
			updateTreeSize(newTreeSize);
		}
	}

	public void setChildCount(int childCount) {
	}

	public void setChilds(Node[] childs) {
		this.childs = (BinaryNode[]) childs;
		this.treeSize = 1 + this.childs[0].treeSize + this.childs[1].treeSize;
	}

	@Override
	public void setTreeSize(int treeSize) {
		// TODO Auto-generated method stub

	}

	// GETTERS

	@Override
	public int getAttribute() {
		return attribute;
	}

	@Override
	public double getValue() {
		return value;
	}

	public Sign getSign() {
		return sign;
	}

	public Node getRightNode() {
		return childs[0];
	}

	public Node getLeftNode() {
		return childs[1];
	}

	@Override
	public Node getParent() {
		return parent;
	}

	public BinaryNode getChildAt(int index) {
		return childs[index];
	}

	public BinaryNode[] getChilds() {
		return childs;
	}

	public boolean isLeaf() {
		return attribute == -1;
	}

	public int getChildCount() {
		return 2;
	}

	public int getTreeSize() {
		return treeSize;
	}

	// OTHER METHODS

	/**
	 * Make this node a leaf.
	 */
	public void makeLeaf() {
		this.attribute = -1;
		this.childs = null;
	}

	/**
	 * Clear/Reset all the childs to null. Changes depth and parent depth
	 * because of this reset.
	 */
	@Override
	public void clearChilds() {
		childs = new BinaryNode[childs.length];
	}

	public BinaryNode copy() {
		return new BinaryNode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		BinaryNode node = ((BinaryNode) obj);

		if (this.attribute != node.attribute || this.value != node.value
				|| this.sign != node.sign
				|| this.criteriaValue != node.criteriaValue) {
			return false;
		}

		if ((this.childs == null && node.childs != null)
				|| (this.childs != null && node.childs == null)) {
			return false;
		}

		if (this.childs != null && node.childs != null) {
			if (!this.childs[0].equals(node.childs[0])
					|| !this.childs[1].equals(node.childs[1]))
				return false;
		}

		return true;

	}

	@Override
	public void updateTreeSize(int treeSizeToUpdate) {
		this.treeSize += treeSizeToUpdate;
		parent.updateTreeSize(treeSizeToUpdate);
	}

}
