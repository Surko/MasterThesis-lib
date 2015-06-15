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

	public int getTreeSize();

	public void setTreeSize(int treeSize);

	public void updateTreeSize(int treeSizeToUpdate);

}
