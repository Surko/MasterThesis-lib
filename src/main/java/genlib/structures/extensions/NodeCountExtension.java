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
public interface NodeCountExtension {

	public int getNumNodes();	
	public void updateNumNodes(int numNodesToUpdate);
	
}
