package genlib.structures;


public class BinaryNode implements Node{

	/** for serialization */
	private static final long serialVersionUID = -5557876005762565687L;
	private BinaryNode parent;
	private BinaryNode[] childs;
	private double attribute = -1;
	private double value;	
	private double criteriaValue;

	public BinaryNode(BinaryNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;		
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
	}
	
	@Override
	public double getAttribute() {
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
		this.parent = (BinaryNode)parent;
	}
	
	public void setChildAt(int index, Node node) {
		if (index < 2)
			childs[index] = (BinaryNode)node;
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
	
	public BinaryNode copy() {
		return new BinaryNode(this);
	}

}
