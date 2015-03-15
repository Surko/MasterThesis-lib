package genlib.structures.extensions;

import genlib.structures.BinaryDepthNode;
import genlib.structures.MultiWayDepthNode;

/**
 * Simple, almost blank interface that distincts Nodes from being automatic in depth counting and
 * begin manually computed.   
 * @see BinaryDepthNode
 * @see MultiWayDepthNode
 *
 */
public interface DepthExtension {

	public void recount(int possibleMax);
	
}
