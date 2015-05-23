package genlib.structures.extensions;

import genlib.structures.trees.BinaryDepthNode;
import genlib.structures.trees.MultiWayDepthNode;

/**
 * Simple, almost blank interface that distincts Nodes from being automatic in depth counting and
 * begin manually computed.   
 * @see BinaryDepthNode
 * @see MultiWayDepthNode
 *
 */
public interface HeightExtension {
	
	public int getTreeHeight();
	public void updateTreeHeight(int possibleMax);
	
}
