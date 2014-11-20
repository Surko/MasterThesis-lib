package genlib.structures;

public class MultiWayNode implements Node {

	/** for serialization */
	private static final long serialVersionUID = -6016127157752225320L;
	private MultiWayNode parent;
	private MultiWayNode[] childs;
	private double attribute = -1;
	private double value = -1;
	private double criteriaValue = 0;

	public MultiWayNode() {}

	public MultiWayNode(MultiWayNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		if (!toCopy.isLeaf()) {
			this.childs = new MultiWayNode[toCopy.childs.length];
			for (int i = 0; i < toCopy.childs.length; i++) {
				childs[i] = new MultiWayNode(toCopy.childs[i]);
				childs[i].setParent(this);
			}
		}
	}
	
	public MultiWayNode(int childLength) {
		if (childLength > 0)
			this.childs = new MultiWayNode[childLength];
	}
	
	
	public MultiWayNode(double attribute, double value) {
		this.attribute = attribute;
		this.value = value;
	}
	
	public void setAttribute(double attribute) {
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
		if (index < childs.length)
			childs[index] = (MultiWayNode)node;
	}
	
	public MultiWayNode getChildAt(int index) {
		if (index < childs.length)
			return childs[index];
		return null;
	}
	
	public MultiWayNode[] getChilds() {
		return childs;		
	}

	public double getAttribute() {
		return attribute;
	}

	public double getValue() {
		return value;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = (MultiWayNode)parent;
	}

	public boolean isLeaf() {
		return attribute == -1;
	}
	
	public void makeLeaf() {
		this.childs = null;
	}
	
	/**
	 * Set up criteria value for this node. Value can be of different measures (Information Gain,
	 * GainRatio, GiniIndex,...) and can be computed from different objects of criteria (EntropyBasedCritera,...)
	 * @param criteriaValue value of measure that was used to split data
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
	
	public MultiWayNode copy() {
		return new MultiWayNode(this);
	}
}
