package genlib.structures.extensions;

import genlib.structures.trees.BinaryDepthNode;
import genlib.structures.trees.MultiWayDepthNode;

/**
 * Simple, almost blank interface that distincts Nodes from being automated in
 * node counting and being dependent on manually counting.
 * 
 * @see BinaryDepthNode
 * @see MultiWayDepthNode
 *
 */
public interface NodeCountExtension {

	public int getNumNodes();	
	public void updateNumNodes(int numNodesToUpdate);
	
}
