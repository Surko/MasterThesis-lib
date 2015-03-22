package genlib.structures;

import genlib.utils.Utils.Sign;

public class BinaryNode implements Node {

	/** for serialization */
	private static final long serialVersionUID = -5557876005762565687L;
	protected BinaryNode parent;
	protected BinaryNode[] childs;
	protected int treeHeight;
	protected int attribute = -1;
	protected double value = Integer.MIN_VALUE;
	protected double criteriaValue;
	protected Sign sign;

	public BinaryNode() {
		this.treeHeight = 1;
	}

	public BinaryNode(BinaryNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.treeHeight = toCopy.treeHeight;
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
		this.attribute = attribute;
		this.sign = sign;
		this.treeHeight = 1;
		this.value = value;
	}

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

		if (index < 2) {
			childs[index] = (BinaryNode) node;
			node.setParent(this);
		}		
	}

	public void setChildCount(int childCount) {
	}

	public void setChilds(Node[] childs) {
		this.childs = (BinaryNode[]) childs;
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

	public void makeLeaf() {
		this.attribute = -1;
		this.childs = null;
		this.treeHeight = 1;
	}

	public void setInfoGain(double criteriaValue) {
		this.criteriaValue = criteriaValue;
	}

	public double getCriteriaValue() {
		return criteriaValue;
	}

	public int getChildCount() {
		return 2;
	}

	public int getTreeHeight() {
		return treeHeight;
	}

	public void setTreeHeightForced(int treeDepth) {
		this.treeHeight = treeDepth;
	}

	/**
	 * Clear/Reset all the childs to null. Changes depth and parent depth
	 * because of this reset.
	 */
	@Override
	public void clearChilds() {
		childs = new BinaryNode[childs.length];
		treeHeight = 1;
	}

	/**
	 * Reconfigure/recounts actual depth of a tree from its childs.
	 */
	public void recount(int possibleMax) {
		if (possibleMax > treeHeight) {
			this.treeHeight = possibleMax;
			if (parent != null) {
				parent.recount(treeHeight);
			}
			return;
		}
		int max = -1;
		for (BinaryNode node : childs) {
			if (node == null)
				continue;
			max = node.getTreeHeight() > max ? node.getTreeHeight() : max;
		}
		max++;
		if (max != this.treeHeight) {
			this.treeHeight = max;
			if (parent != null) {
				parent.recount(treeHeight);
			}
		}
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

}
