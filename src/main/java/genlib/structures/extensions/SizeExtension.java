package genlib.structures.extensions;

import genlib.structures.trees.BinaryDepthNode;
import genlib.structures.trees.MultiWayDepthNode;

/**
 * Simple, almost blank interface that distincts Nodes from being automated in
 * tree size counting and being dependent on manually counting.
 * 
 * @see BinaryDepthNode
 * @see MultiWayDepthNode
 *
 */
public interface SizeExtension {

	public int getTreeSize();

	public void setTreeSize(int treeSize);

	public void updateTreeSize(int treeSizeToUpdate);

}
