package genlib.structures.trees;

import genlib.utils.Utils.Sign;

public class MultiWayNode implements Node {

	/** for serialization */
	private static final long serialVersionUID = -6016127157752225320L;
	protected MultiWayNode parent;
	protected MultiWayNode[] childs;
	protected int treeHeight = 0;
	protected int attribute = -1;
	protected double value = Integer.MIN_VALUE;
	protected double criteriaValue = 0;
	protected Sign sign;

	public static MultiWayNode makeLeaf(double value) {
		MultiWayNode leaf = new MultiWayNode();
		leaf.setAttribute(-1);
		leaf.setValue(value);
		return leaf;		
	}
	
	public static MultiWayNode makeNode(int childCount, int attribute, Sign sign, double value) {
		MultiWayNode node = new MultiWayNode();
		node.setChildCount(childCount);
		node.setAttribute(-1);
		node.setSign(sign);
		node.setValue(value);
		return node;		
	}
	
	public MultiWayNode() {
		this.treeHeight = 1;
	}

	public MultiWayNode(MultiWayNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.treeHeight = toCopy.treeHeight;
		this.sign = toCopy.sign;
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
		this.treeHeight = 1;
	}
	
	public MultiWayNode(int attribute, Sign sign, double value) {
		this.sign = sign;
		this.attribute = attribute;
		this.value = value;
		this.treeHeight = 1;
	}
	
	/**** SETTERS ****/
	
	/**
	 * Set node as child at index.
	 * @param index Index in an array of a new child
	 * @param node Child to add 
	 */
	@Override
	public void setChildAt(int index, Node node) {
		if (childs == null) {
			return;
		}
		
		childs[index] = (MultiWayNode)node;	
		node.setParent(this);
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
	
	public void setChildCount(int count) {
		if (count > 0)
			this.childs = new MultiWayNode[count];
	}	

	public void setChilds(Node[] childs) {
		this.childs = (MultiWayNode[]) childs;		
	}

	public void setParent(Node parent) {
		this.parent = (MultiWayNode) parent;
	}	

	public void setTreeHeightForced(int treeDepth) {
		this.treeHeight = treeDepth;
	}

	
	/**** GETTERS ****/
	
	/**
	 * Getter for a specific child at index
	 * @return Child (MultiWayNode) at index
	 */
	public MultiWayNode getChildAt(int index) {
		return childs[index];		
	}

	/**
	 * Method which will return childs at this node. Child amount is two if the attribute
	 * at this node is numeric or this amount depends on different values of this attribute if 
	 * it's nominal.
	 * @return childs at this node
	 */
	public MultiWayNode[] getChilds() {
		return childs;
	}

	/**
	 * Gets attributes index at this node.
	 * @return attribute index
	 */
	public int getAttribute() {
		return attribute;
	}

	/**
	 * Gets values at this node. Value is class index if it's leaf or decision
	 * boundary if attribute at this node is numeric. 
	 * @return value at this node.
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Gets sign at this node. It's used in conjuction with value field.
	 * @see Sign
	 * @return sign at this node
	 */
	public Sign getSign() {
		return sign;
	}
	
	/**
	 * Gets the parent of this node. For root it's null.
	 * @return parent(node) of this node
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Method which returns if this node is considered a leaf.
	 * @return true/false == leaf/not-leaf
	 */
	public boolean isLeaf() {
		return attribute == -1;
	}

	/**
	 * Make this node a leaf.
	 */
	public void makeLeaf() {
		this.attribute = -1;
		this.childs = null;
		this.treeHeight = 1;
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

	/**
	 * Gets the criteria value computed at this node
	 * @return criteria value at this node
	 */
	public double getCriteriaValue() {
		return criteriaValue;
	}

	/**
	 * Gets the amount of children at this node.
	 * @return amount of children nodes
	 */
	public int getChildCount() {
		return childs.length;
	}

	/**
	 * Getter which provides treeHeight of this tree.
	 * @return height of tree 
	 */
	@Override
	public int getTreeHeight() {
		return treeHeight;
	}


	/**** OTHER METHODS ****/
	
	/**
	 * Clear/Reset all the childs to null. Changes depth and parent depth
	 * because of this reset.
	 */
	@Override
	public void clearChilds() {
		childs = new MultiWayNode[childs.length];
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
		for (MultiWayNode node : childs) {
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

	/**
	 * Making copy of this object via calling copy constructor and returning 
	 * new instance.
	 * @return new, copied instance of this object
	 */
	public MultiWayNode copy() {
		return new MultiWayNode(this);
	}

	
	/**
	 * Equal method for this type of instances which provide correct comparison 
	 * which was intended for it. There is the comparison of main fields such as attribute, 
	 * value, sign, criteria value and childs of this node. Equal method is called for each child
	 * respectively. 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		MultiWayNode node = ((MultiWayNode)obj);
		
		if (this.attribute != node.attribute || this.value != node.value ||
				this.sign != node.sign || this.criteriaValue != node.criteriaValue) {									
			return false;
		}
		
		if ((this.childs == null && node.childs != null) || (this.childs != null && node.childs == null)) {
			return false;
		}
		
		if (this.childs != null && node.childs != null && this.childs.length == node.childs.length) {
			for (int i = 0; i < this.childs.length; i++) {								
				if (!this.childs[i].equals(node.childs[i]))
					return false;
			}
		}			
		
		return true;
		
	}

	
}
