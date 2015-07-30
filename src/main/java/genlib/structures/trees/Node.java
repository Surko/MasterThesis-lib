package genlib.structures.trees;

import genlib.utils.Utils.Sign;

import java.io.Serializable;

/**
 * Interface for the nodes that represents tree nodes. It is implemented with
 * different type of nodes with different number of childs.
 * 
 * @see BinaryNode
 * @see MultiWayNode
 * @author Lukas Surin
 */
public interface Node extends Serializable {

	/**
	 * Method returns the childs of node
	 * 
	 * @return
	 */
	public Node[] getChilds();

	/**
	 * Method returns the child at specific index
	 * 
	 * @param index
	 *            of the node
	 * @return node at specific index
	 */
	public Node getChildAt(int index);

	/**
	 * Method sets the child at specific index
	 * 
	 * @param index
	 *            of the node
	 * @param node
	 *            to be set into child at index
	 */
	public void setChildAt(int index, Node node);

	/**
	 * Method hard sets the childs of the node
	 * 
	 * @param childs
	 */
	public void setChilds(Node[] childs);

	/**
	 * Method sets the child count (initializes the array of childs).
	 * 
	 * @param childCount
	 *            number of childs
	 */
	public void setChildCount(int childCount);

	/**
	 * Method gets the child count
	 * 
	 * @return
	 */
	public int getChildCount();

	/**
	 * Method removes the childs from node
	 */
	public void clearChilds();

	/**
	 * Method gets the sign at node (if it's not leaf)
	 * 
	 * @return Sign at node
	 */
	public Sign getSign();

	/**
	 * Method sets the sign at node
	 * 
	 * @param sign
	 *            at node
	 */
	public void setSign(Sign sign);

	/**
	 * Method gets the attribute at node
	 * 
	 * @return attribute of the node
	 */
	public int getAttribute();

	/**
	 * Method sets the attribute of the node
	 * 
	 * @param attribute
	 *            of the node
	 */
	public void setAttribute(int attribute);

	/**
	 * Method gets the value of the node
	 * 
	 * @return value of the node
	 */
	public double getValue();

	/**
	 * Method sets the value of the node
	 * 
	 * @param value
	 *            of the node
	 */
	public void setValue(double value);

	/**
	 * Method gets the parent of the same type
	 * 
	 * @return parent of this node
	 */
	public Node getParent();

	/**
	 * Method sets the parent of this node
	 * 
	 * @param parent
	 *            of this node
	 */
	public void setParent(Node parent);

	/**
	 * Method tests if the node is a leaf (attribute == -1)
	 * 
	 * @return true iff the node is leaf
	 */
	public boolean isLeaf();

	/**
	 * Method converts the node into leaf
	 */
	public void makeLeaf();

	/**
	 * Method deep copies the node and returns it. Called from TreeIndividual
	 * 
	 * @return deep copy of the node
	 */
	public Node copy();

	/**
	 * Method makes new instance of node with the same type.
	 * 
	 * @return node of the same type
	 */
	public Node newInstance();

}
