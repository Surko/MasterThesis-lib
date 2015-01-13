package genlib.structures;

public class BinaryNode implements Node {

	/** for serialization */
	private static final long serialVersionUID = -5557876005762565687L;
	private BinaryNode parent;
	private BinaryNode[] childs;
	private int treeDepth;
	private int attribute = -1;
	private double value = Integer.MIN_VALUE;
	private double criteriaValue;

	public BinaryNode(BinaryNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.treeDepth = toCopy.treeDepth;
		if (!toCopy.isLeaf()) {
			this.childs = new BinaryNode[2];
			this.childs[0] = new BinaryNode(toCopy.childs[0]);
			this.childs[1] = new BinaryNode(toCopy.childs[1]);
			this.childs[0].parent = this;
			this.childs[1].parent = this;
		}
	}

	public BinaryNode() {
		this.childs = new BinaryNode[2];
		this.treeDepth = 0;
	}

	@Override
	public int getAttribute() {
		return attribute;
	}

	@Override
	public double getValue() {
		return value;
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

	public void setChildAt(int index, Node node) {
		if (index < 2 && node != null) {
			int nodeExtendDepth = node.getTreeDepth() + 1;
			if (childs[index] == null) {
				// set the child
				childs[index] = (BinaryNode) node;
				// set the parent of a child
				node.setParent(this);
				// only if node depth is bigger than up to now
				if (treeDepth < nodeExtendDepth) {
					// set the max depth
					this.treeDepth = nodeExtendDepth;
					// if parent not null => propagate to parent
					if (parent != null)
						parent.recount(treeDepth + 1);
				}
			} else {
				// set the child
				childs[index] = (BinaryNode) node;
				// set the parent of a child
				node.setParent(this);

				// node depth has to be different
				if (nodeExtendDepth != treeDepth) {
					recount(nodeExtendDepth);

					if (parent != null)
						parent.recount(treeDepth + 1);
				}
			}
		}
	}

	public void setChilds(Node[] childs) {
		this.childs = (BinaryNode[]) childs;
		recount(0);
		if (parent != null) {
			parent.recount(treeDepth);
		}
	}

	public BinaryNode getChildAt(int index) {
		if (index < 2)
			return childs[index];
		return null;
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

	public int getTreeDepth() {
		return treeDepth;
	}

	public void setTreeDepthForced(int treeDepth) {
		this.treeDepth = treeDepth;
	}

	/**
	 * Clear/Reset all the childs to null. Changes depth and parent depth
	 * because of this reset.
	 */
	@Override
	public void clearChilds() {
		childs = new BinaryNode[childs.length];
		treeDepth = 0;
		if (parent != null)
			parent.recount(0);
	}

	/**
	 * Reconfigure/recounts actual depth of a tree from its childs.
	 */
	private void recount(int possibleMax) {
		if (possibleMax > treeDepth) {
			this.treeDepth = possibleMax;
			if (parent != null) {
				parent.recount(treeDepth);
			}
			return;
		}
		int max = -1;
		for (BinaryNode node : childs) {
			if (node == null)
				continue;
			max = node.getTreeDepth() > max ? node.getTreeDepth() : max;
		}
		max++;
		if (max != this.treeDepth) {
			this.treeDepth = max;
			if (parent != null) {
				parent.recount(treeDepth);
			}
		}
	}

	public BinaryNode copy() {
		return new BinaryNode(this);
	}

}
