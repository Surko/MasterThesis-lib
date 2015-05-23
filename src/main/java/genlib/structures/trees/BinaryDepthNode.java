package genlib.structures.trees;

import genlib.exceptions.NodeCreationException;
import genlib.exceptions.NotInitializedFieldException;
import genlib.locales.TextResource;
import genlib.structures.extensions.HeightExtension;
import genlib.utils.Utils.Sign;

public class BinaryDepthNode extends BinaryNode implements HeightExtension {

	/** for serialization */
	private static final long serialVersionUID = -5699769468571434662L;
	protected int treeHeight = 0;

	/**
	 * Static factory that creates leaf with value of classification. It only
	 * sets this values because all other fields are defaultly set.
	 * 
	 * @param value
	 *            value of classification
	 * @return node leaf
	 */
	public static BinaryDepthNode makeLeaf(double value) {
		BinaryDepthNode leaf = new BinaryDepthNode();
		leaf.value = value;
		return leaf;
	}

	/**
	 * Static factory that creates node with two childs, attribute on which to
	 * split sign of split and value of attribute on which to split
	 * 
	 * @param childCount
	 *            number of childs
	 * @param attribute
	 *            split attribute
	 * @param sign
	 *            split sign
	 * @param value
	 *            split value of attribute
	 * @return node
	 */
	public static BinaryDepthNode makeNode(int attribute, Sign sign,
			double value) {
		if (attribute == -1) {
			throw new NodeCreationException(String.format(
					TextResource.getString("eNodeCreation"), "attribute"));
		}

		BinaryDepthNode node = new BinaryDepthNode();
		node.childs = new BinaryDepthNode[2];
		node.sign = sign;
		node.attribute = attribute;	
		node.value = value;
		return node;
	}

	/**
	 * Parameterless constructor for serialization and static methods
	 */
	public BinaryDepthNode() {		
	}

	public BinaryDepthNode(BinaryDepthNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.treeHeight = toCopy.treeHeight;
		if (!toCopy.isLeaf()) {
			this.childs = new BinaryDepthNode[2];
			this.childs[0] = new BinaryDepthNode(
					(BinaryDepthNode) toCopy.childs[0]);
			this.childs[1] = new BinaryDepthNode(
					(BinaryDepthNode) toCopy.childs[1]);
			this.childs[0].parent = this;
			this.childs[1].parent = this;
		}
	}

	public BinaryDepthNode(int attribute, Sign sign, double value) {
		super(attribute, sign, value);
	}

	// GETTERS
	public int getTreeHeight() {
		return treeHeight;
	}
	
	// SETTERS
	@Override
	public void setChildAt(int index, Node node) {
		if (childs == null) {
			throw new NotInitializedFieldException("field");
		}

		int nodeExtendDepth = ((HeightExtension) node).getTreeHeight() + 1;
		if (childs[index] == null) {
			// set the child
			childs[index] = (BinaryNode) node;
			// set the parent of a child
			node.setParent(this);
			// only if node depth is bigger than up to now
			if (treeHeight < nodeExtendDepth) {
				// set the max depth
				this.treeHeight = nodeExtendDepth;
				// if parent not null => propagate to parent
				if (parent != null)
					((HeightExtension)parent).updateTreeHeight(treeHeight + 1);
			}
		} else {
			// set the child
			childs[index] = (BinaryNode) node;
			// set the parent of a child
			node.setParent(this);

			// node depth has to be different
			if (nodeExtendDepth != treeHeight) {
				updateTreeHeight(nodeExtendDepth);
			}
		}
	}
	
	/**
	 * Reconfigure/recounts actual depth of a tree from its childs.
	 */
	public void updateTreeHeight(int possibleMax) {				
		if (possibleMax > treeHeight) {
			this.treeHeight = possibleMax;
			if (parent != null) {
				((HeightExtension) parent).updateTreeHeight(treeHeight + 1);
			}
			return;
		}
		
		int max = -1;
		if (childs[0] != null) {					
			max = ((BinaryDepthNode)childs[0]).treeHeight;
		}
		
		if (childs[1] != null) {					
			max = Math.max(max, ((BinaryDepthNode)childs[0]).treeHeight);
		}		

		max++;
		if (max != this.treeHeight) {
			this.treeHeight = max;
			if (parent != null) {
				((HeightExtension) parent).updateTreeHeight(treeHeight + 1);
			}
		}
	}

	@Override
	public void setChilds(Node[] childs) {
		super.setChilds(childs);
		updateTreeHeight(0);	
	}

	// OTHER METHODS
	
	/**
	 * {@inheritDoc}. Calling method {@link BinaryNode#makeLeaf()}
	 */
	public void makeLeaf() {
		super.makeLeaf();
		this.treeHeight = 0;
	}
	
	/**
	 * Method which will clear the childs in this node. Treeheight of this node
	 * will be reseted to zero (it behaves as normal leaf). UpdateTreeHeight for
	 * this parent will be called.
	 * 
	 */
	@Override
	public void clearChilds() {
		super.clearChilds();
		treeHeight = 0;
		if (parent != null)
			((HeightExtension) parent).updateTreeHeight(1);
	}

	public BinaryDepthNode copy() {
		return new BinaryDepthNode(this);
	}

	@Override
	public boolean equals(Object obj) {
		// NO HASH COMPUTING
		return (((BinaryDepthNode) obj).treeHeight == this.treeHeight)
				&& super.equals(obj);
	}

}
