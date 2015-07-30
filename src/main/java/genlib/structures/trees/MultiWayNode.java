package genlib.structures.trees;

import genlib.exceptions.NodeCreationException;
import genlib.exceptions.NotInitializedFieldException;
import genlib.locales.TextResource;
import genlib.structures.extensions.SizeExtension;
import genlib.utils.Utils.Sign;

/**
 * Class that represents Node with multiple childs. It implements {@link Node}
 * and {@link SizeExtension} so it recomputes its size automatically.
 * 
 * @author Lukas Surin
 */
public class MultiWayNode implements Node, SizeExtension {

	/** for serialization */
	private static final long serialVersionUID = -6016127157752225320L;
	/** parent of this node */
	protected MultiWayNode parent;
	/** childs of this node */
	protected MultiWayNode[] childs;
	/** tree size of this node */
	protected int treeSize = 1;
	/** attribute of this node */
	protected int attribute = -1;
	/** value of this node */
	protected double value = Integer.MIN_VALUE;
	/** criteria value NOT USED */
	protected double criteriaValue = 0;
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
	public static MultiWayNode makeLeaf(double value) {
		MultiWayNode leaf = new MultiWayNode();
		leaf.value = value;
		return leaf;
	}

	/**
	 * Static factory that creates node with childs, attribute on which to split
	 * sign of split and value of attribute on which to split
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
	public static MultiWayNode makeNode(int childCount, int attribute,
			Sign sign, double value) {
		if (attribute == -1) {
			throw new NodeCreationException(String.format(
					TextResource.getString("eNodeCreation"), "attribute"));
		}

		if (childCount <= 0) {
			throw new NodeCreationException(String.format(
					TextResource.getString("eNodeCreation"), "childCount"));
		}

		MultiWayNode node = new MultiWayNode();
		node.childs = new MultiWayNode[childCount];
		node.sign = sign;
		node.attribute = attribute;
		node.value = value;
		return node;
	}

	/**
	 * Parameterless constructor for serialization and static methods
	 */
	public MultiWayNode() {
	}

	/**
	 * Copy constructor
	 * 
	 * @param toCopy
	 *            instance
	 */
	public MultiWayNode(MultiWayNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.treeSize = toCopy.treeSize;
		this.sign = toCopy.sign;
		if (!toCopy.isLeaf()) {
			this.childs = new MultiWayNode[toCopy.childs.length];
			for (int i = 0; i < toCopy.childs.length; i++) {
				childs[i] = new MultiWayNode(toCopy.childs[i]);
				childs[i].setParent(this);
			}
		}
	}

	/**
	 * Constructor of MultiWayNode that will have childCount childs
	 * 
	 * @param childCount
	 *            number of childs
	 */
	public MultiWayNode(int childCount) {
		this(childCount, 0, null, Integer.MIN_VALUE);
	}

	/**
	 * Constructor of MultiWayNode that sets the node fields in advance.
	 * @param childCount number of childs
	 * @param attribute on which we test instances
	 * @param sign Sign
	 * @param value of split
	 */
	public MultiWayNode(int childCount, int attribute, Sign sign, double value) {
		if (childCount > 0 && attribute != -1) {
			this.childs = new MultiWayNode[childCount];
			this.sign = sign;
			this.attribute = attribute;
		}

		this.value = value;
	}

	/**** SETTERS ****/

	/**
	 * Set node as child at index.
	 * 
	 * @param index
	 *            Index in an array of a new child
	 * @param node
	 *            Child to add
	 */
	@Override
	public void setChildAt(int index, Node node) {
		if (childs == null) {
			throw new NotInitializedFieldException("field");
		}

		int oldTreeSize = 0;
		int newTreeSize = 0;

		if (childs[index] != null) {
			oldTreeSize = childs[index].getTreeSize();
		}

		childs[index] = (MultiWayNode) node;
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
	public void setChildCount(int count) {
		if (count > 0)
			this.childs = new MultiWayNode[count];
	}

	/**
	 * {@inheritDoc}
	 */
	public void setChilds(Node[] childs) {
		this.childs = (MultiWayNode[]) childs;
		this.treeSize = 1;
		for (MultiWayNode node : this.childs) {
			treeSize += node.treeSize;
			node.setParent(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setParent(Node parent) {
		this.parent = (MultiWayNode) parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTreeSize(int treeSize) {
		if (parent != null) {
			// TODO
		}
		this.treeSize = treeSize;
	}

	/**** GETTERS ****/

	/**
	 * Getter for a specific child at index
	 * 
	 * @return Child (MultiWayNode) at index
	 */
	public MultiWayNode getChildAt(int index) {
		return childs[index];
	}

	/**
	 * Method which will return childs at this node. Child amount is two if the
	 * attribute at this node is numeric or this amount depends on different
	 * values of this attribute if it's nominal.
	 * 
	 * @return childs at this node
	 */
	public MultiWayNode[] getChilds() {
		return childs;
	}

	/**
	 * Gets attributes index at this node.
	 * 
	 * @return attribute index
	 */
	public int getAttribute() {
		return attribute;
	}

	/**
	 * Gets values at this node. Value is class index if it's leaf or decision
	 * boundary if attribute at this node is numeric.
	 * 
	 * @return value at this node.
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Gets sign at this node. It's used in conjuction with value field.
	 * 
	 * @see Sign
	 * @return sign at this node
	 */
	public Sign getSign() {
		return sign;
	}

	/**
	 * Gets the parent of this node. For root it's null.
	 * 
	 * @return parent(node) of this node
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Method which returns if this node is considered a leaf.
	 * 
	 * @return true/false == leaf/not-leaf
	 */
	public boolean isLeaf() {
		return attribute == -1;
	}

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
	 * 
	 * @return criteria value at this node
	 */
	public double getCriteriaValue() {
		return criteriaValue;
	}

	/**
	 * Gets the amount of children at this node.
	 * 
	 * @return amount of children nodes
	 */
	public int getChildCount() {
		if (childs == null) {
			return 0;
		}

		return childs.length;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return treeSize
	 */
	public int getTreeSize() {
		return treeSize;
	}

	/**** OTHER METHODS ****/

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

	/**
	 * Clear/Reset all the childs to null. Changes treeSize and parents treeSize
	 * because of this reset.
	 */
	@Override
	public void clearChilds() {
		childs = new MultiWayNode[childs.length];
		int treeSizeToUpdate = 1 - treeSize;
		updateTreeSize(treeSizeToUpdate);
	}

	/**
	 * Making copy of this object via calling copy constructor and returning new
	 * instance.
	 * 
	 * @return new, copied instance of this object
	 */
	public MultiWayNode copy() {
		return new MultiWayNode(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public MultiWayNode newInstance() {
		return new MultiWayNode();
	}

	/**
	 * Equal method for this type of instances which provide correct comparison
	 * which was intended for it. There is the comparison of main fields such as
	 * attribute, value, sign, criteria value and childs of this node. Equal
	 * method is called for each child respectively.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		MultiWayNode node = ((MultiWayNode) obj);

		if (this.attribute != node.attribute || this.value != node.value
				|| this.sign != node.sign
				|| this.criteriaValue != node.criteriaValue) {
			return false;
		}

		if ((this.childs == null && node.childs != null)
				|| (this.childs != null && node.childs == null)) {
			return false;
		}

		if (this.childs != null && node.childs != null
				&& this.childs.length == node.childs.length) {
			for (int i = 0; i < this.childs.length; i++) {
				if (!this.childs[i].equals(node.childs[i]))
					return false;
			}
		}

		return true;

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
