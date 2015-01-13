package genlib.structures;

public class MultiWayNode implements Node {

	/** for serialization */
	private static final long serialVersionUID = -6016127157752225320L;
	private MultiWayNode parent;
	private MultiWayNode[] childs;
	private int treeDepth = 0;
	private int attribute = -1;
	private double value = Integer.MIN_VALUE;
	private double criteriaValue = 0;

	public MultiWayNode() {
	}

	public MultiWayNode(MultiWayNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.treeDepth = toCopy.treeDepth;
		if (!toCopy.isLeaf()) {
			this.childs = new MultiWayNode[toCopy.childs.length];
			for (int i = 0; i < toCopy.childs.length; i++) {
				childs[i] = new MultiWayNode(toCopy.childs[i]);
				childs[i].setParent(this);
			}
		}
	}

	public MultiWayNode(int childLength) {
		this.childs = new MultiWayNode[childLength];
		this.treeDepth = 0;
	}

	public MultiWayNode(int attribute, double value) {
		this.attribute = attribute;
		this.value = value;
		this.treeDepth = 0;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setChildLength(int childLength) {
		if (childLength > 0)
			this.childs = new MultiWayNode[childLength];
	}

	public void setChildAt(int index, Node node) {
		if (index < childs.length) {
			int nodeExtendDepth = node.getTreeDepth() + 1;
			if (childs[index] == null) {
				// set the child
				childs[index] = (MultiWayNode) node;
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
				childs[index] = (MultiWayNode) node;
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
		this.childs = (MultiWayNode[]) childs;
		recount(0);
		if (parent != null) {
			parent.recount(treeDepth + 1);
		}
	}

	public MultiWayNode getChildAt(int index) {
		if (index < childs.length)
			return childs[index];
		return null;
	}

	public MultiWayNode[] getChilds() {
		return childs;
	}

	public int getAttribute() {
		return attribute;
	}

	public double getValue() {
		return value;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = (MultiWayNode) parent;
	}

	public boolean isLeaf() {
		return attribute == -1;
	}

	public void makeLeaf() {
		this.attribute = -1;
		this.childs = new MultiWayNode[childs.length];
		this.treeDepth = 0;
	}

	/**
	 * Set up criteria value for this node. Value can be of different measures
	 * (Information Gain, GainRatio, GiniIndex,...) and can be computed from
	 * different objects of criteria (EntropyBasedCritera,...)
	 * 
	 * @param criteriaValue
	 *            value of measure that was used to split data
	 */
	public void setCriteriaValue(double criteriaValue) {
		this.criteriaValue = criteriaValue;
	}

	public double getCriteriaValue() {
		return criteriaValue;
	}

	public int getChildCount() {
		return childs.length;
	}

	@Override
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
		childs = new MultiWayNode[childs.length];
		treeDepth = 0;
		if (parent != null)
			parent.recount(1);
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
		for (MultiWayNode node : childs) {
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

	public MultiWayNode copy() {
		return new MultiWayNode(this);
	}
}
