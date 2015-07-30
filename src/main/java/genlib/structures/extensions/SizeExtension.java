package genlib.structures.extensions;

import genlib.structures.trees.BinaryHeightNode;
import genlib.structures.trees.MultiWayHeightNode;

/**
 * Simple, almost blank interface that distincts Nodes from being automated in
 * tree size counting and being dependent on manually counting.
 * 
 * @see BinaryHeightNode
 * @see MultiWayHeightNode
 *
 */
public interface SizeExtension {

	/**
	 * Gets the tree size
	 * @return
	 */
	public int getTreeSize();

	/**
	 * Sets the tree size.
	 * 
	 * @param treeSize
	 *            size of the tree starting from this node
	 */
	public void setTreeSize(int treeSize);

	/**
	 * Method automatically updates the treesize of this node and its
	 * ascendants.
	 * 
	 * @param treeSizeToUpdate update value (negative, positive)
	 */
	public void updateTreeSize(int treeSizeToUpdate);

}
