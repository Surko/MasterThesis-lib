package genlib.structures.extensions;

import genlib.structures.trees.BinaryHeightNode;
import genlib.structures.trees.MultiWayHeightNode;

/**
 * Simple, almost blank interface that distincts Nodes from being automated in
 * node counting and being dependent on manually counting.
 * 
 * @see BinaryHeightNode
 * @see MultiWayHeightNode
 *
 */
public interface LeavesCountExtension {

	/**
	 * Gets the number of leaves in subtree defined by this node
	 * @return number of leaves
	 */
	public int getNumLeaves();	
	
	/**
	 * Method updates number of leaves in subtree
	 * @param numLeavesToUpdate value to update leaves 
	 */
	public void updateNumLeaves(int numLeavesToUpdate);
	
}
