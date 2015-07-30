package genlib.structures.extensions;

import genlib.structures.trees.BinaryHeightNode;
import genlib.structures.trees.MultiWayHeightNode;

/**
 * Simple, almost blank interface that distincts Nodes from being automated in
 * depth counting and being dependent on manually counting.
 * 
 * @see BinaryHeightNode
 * @see MultiWayHeightNode
 *
 */
public interface HeightExtension {
	
	/**
	 * Gets the tree height of the tree starting from this node
	 * @return
	 */
	public int getTreeHeight();
	
	/**
	 * Methods used to update tree height in this node and its ascendents.
	 * @param possibleMax new max value of childs
	 */
	public void updateTreeHeight(int possibleMax);
	
}
