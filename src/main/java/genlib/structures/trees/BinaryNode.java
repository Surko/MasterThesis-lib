package genlib.structures.trees;

import genlib.exceptions.NodeCreationException;
import genlib.exceptions.NotInitializedFieldException;
import genlib.locales.TextResource;
import genlib.structures.extensions.SizeExtension;
import genlib.utils.Utils.Sign;

/**
 * Class that represents Node with 2 childs. It implements {@link Node} and
 * {@link SizeExtension} so it recomputes its size automatically.
 * 
 * @author Lukas Surin
 */
public class BinaryNode implements Node, SizeExtension {

	/** for serialization */
	private static final long serialVersionUID = -5557876005762565687L;
	/** parent of this node */
	protected BinaryNode parent;
	/** childs of this node */
	protected BinaryNode[] childs;
	/** tree size of this node */
	protected int treeSize = 1;
	/** attribute of this node */
	protected int attribute = -1;
	/** value of this node */
	protected double value = Integer.MIN_VALUE;
	/** criteria value of this node NOT USED */
	protected double criteriaValue;
	/** sign of this node */
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
		node.value = value;
		return node;
	}

	/**
	 * Parameterless constructor for serialization and static methods
	 */
	public BinaryNode() {
	}

	/**
	 * Copy constructor
	 * 
	 * @param toCopy
	 *            instance
	 */
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

	/**
	 * Constructor of BinaryNode that sets the node fields in advance.
	 * 
	 * @param attribute
	 *            on which we test instances
	 * @param sign
	 *            Sign
	 * @param value
	 *            of split
	 */
	public BinaryNode(int attribute, Sign sign, double value) {
		if (attribute != -1) {
			this.attribute = attribute;
			this.sign = sign;
		}
		this.value = value;
	}

	// SETTERS

	/**
	 * {@inheritDoc}
	 */
	public void setParent(Node parent) {
		this.parent = (BinaryNode) parent;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSign(Sign sign) {
		this.sign = sign;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setChildAt(int index, Node node) {
		if (childs == null) {
			throw new NotInitializedFieldException("field");
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

	/**
	 * {@inheritDoc}
	 */
	public void setChildCount(int childCount) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void setChilds(Node[] childs) {
		this.childs = (BinaryNode[]) childs;
		this.childs[0].setParent(this);
		this.childs[1].setParent(this);
		this.treeSize = 1 + this.childs[0].treeSize + this.childs[1].treeSize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTreeSize(int treeSize) {
		// TODO Auto-generated method stub

	}

	// GETTERS

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAttribute() {
		return attribute;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	public Sign getSign() {
		return sign;
	}

	/**
	 * Gets the right child
	 * 
	 * @return right child
	 */
	public Node getRightNode() {
		return childs[0];
	}

	/**
	 * Gets the left child
	 * 
	 * @return left child
	 */
	public Node getLeftNode() {
		return childs[1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	public BinaryNode getChildAt(int index) {
		return childs[index];
	}

	/**
	 * {@inheritDoc}
	 */
	public BinaryNode[] getChilds() {
		return childs;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLeaf() {
		return attribute == -1;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getChildCount() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTreeSize() {
		return treeSize;
	}

	// OTHER METHODS

	/**
	 * Make this node a leaf.
	 */
	public void makeLeaf() {
		this.sign = null;
		this.attribute = -1;
		this.childs = null;
		updateTreeSize(1 - this.treeSize);
	}

	/**
	 * Clear/Reset all the childs to null. Changes treeSize and parents treeSize
	 * because of this reset.
	 */
	@Override
	public void clearChilds() {
		childs = new BinaryNode[childs.length];
		int treeSizeToUpdate = 1 - treeSize;
		updateTreeSize(treeSizeToUpdate);
	}

	/**
	 * {@inheritDoc}
	 */
	public BinaryNode copy() {
		return new BinaryNode(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public BinaryNode newInstance() {
		return new BinaryNode();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTreeSize(int treeSizeToUpdate) {
		this.treeSize += treeSizeToUpdate;
		if (parent != null) {
			parent.updateTreeSize(treeSizeToUpdate);
		}
	}

	@Override
	public String toString() {
		if (isLeaf()) {
			return String.format("c%s", value);
		}

		String done = "";
		for (Node child : childs) {
			if (done.isEmpty()) {
				done = child.toString();
				continue;
			}
			done = String.format("%s,%s", done,
					child == null ? null : child.toString());
		}
		return String.format("(%s)a%s%s%s", done, attribute, sign == null ? ""
				: sign.getValue(), sign == null ? "" : value);
	}

}
