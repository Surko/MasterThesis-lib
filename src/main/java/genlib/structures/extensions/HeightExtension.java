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
	
	public int getTreeHeight();
	public void updateTreeHeight(int possibleMax);
	
}
