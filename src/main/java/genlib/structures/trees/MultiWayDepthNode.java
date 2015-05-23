package genlib.structures.trees;

import genlib.exceptions.NodeCreationException;
import genlib.exceptions.NotInitializedFieldException;
import genlib.locales.TextResource;
import genlib.structures.extensions.HeightExtension;
import genlib.utils.Utils.Sign;

public class MultiWayDepthNode extends MultiWayNode implements HeightExtension {
	/** for serialization */
	private static final long serialVersionUID = 6475434862235450574L;

	protected int treeHeight = 0;

	/**
	 * Static factory that creates leaf with value of classification. It only
	 * sets this values because all other fields are defaultly set.
	 * 
	 * @param value
	 *            value of classification
	 * @return node leaf
	 */
	public static MultiWayDepthNode makeLeaf(double value) {
		MultiWayDepthNode leaf = new MultiWayDepthNode();
		leaf.value = value;
		return leaf;
	}

	/**
	 * Static factory that creates node with childs, attribute on which to split
	 * sign of split and value of attribute on which to split
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
	public static MultiWayDepthNode makeNode(int childCount, int attribute,
			Sign sign, double value) {
		if (attribute == -1) {
			throw new NodeCreationException(String.format(
					TextResource.getString("eNodeCreation"), "attribute"));
		}

		if (childCount <= 0) {
			throw new NodeCreationException(String.format(
					TextResource.getString("eNodeCreation"), "childCount"));
		}

		MultiWayDepthNode node = new MultiWayDepthNode();
		node.childs = new MultiWayDepthNode[childCount];
		node.sign = sign;
		node.attribute = attribute;
		node.value = value;
		return node;
	}

	/**
	 * Parameterless constructor for serialization and static methods
	 */
	public MultiWayDepthNode() {
	}

	public MultiWayDepthNode(MultiWayDepthNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.treeHeight = toCopy.treeHeight;
		this.treeSize = toCopy.treeSize;
		this.sign = toCopy.sign;
		if (!toCopy.isLeaf()) {
			this.childs = new MultiWayDepthNode[toCopy.childs.length];
			for (int i = 0; i < toCopy.childs.length; i++) {
				childs[i] = toCopy.childs[i].copy();
				childs[i].setParent(this);
			}
		}
	}

	public MultiWayDepthNode(int childCount) {
		this(childCount, 0, null, Integer.MIN_VALUE);
	}
	
	public MultiWayDepthNode(int childCount, int attribute, Sign sign,
			double value) {
		if (childCount > 0 && attribute != -1) {
			this.childs = new MultiWayDepthNode[childCount];
			this.sign = sign;
			this.attribute = attribute;
		}

		this.value = value;
	}

	// GETTERS
	/**
	 * Getter which provides treeHeight of this tree.
	 * 
	 * @return height of tree
	 */
	@Override
	public int getTreeHeight() {
		return treeHeight;
	}

	// SETTERS
	public void setChildAt(int index, Node node) {
		if (childs == null) {
			throw new NotInitializedFieldException("field");
		}

		int nodeExtendDepth = ((HeightExtension) node).getTreeHeight() + 1;

		if (childs[index] == null) {
			// set the child
			childs[index] = (MultiWayDepthNode) node;
			// set the parent of a child
			node.setParent(this);
			// only if node depth is bigger than up to now
			if (treeHeight < nodeExtendDepth) {
				// set the max depth
				this.treeHeight = nodeExtendDepth;
				// if parent not null => propagate to parent
				if (parent != null)
					((HeightExtension) parent).updateTreeHeight(treeHeight + 1);
			}
		} else {
			// set the child
			childs[index] = (MultiWayDepthNode) node;
			// set the parent of a child
			node.setParent(this);
			// node depth has to be different
			if (nodeExtendDepth != treeHeight) {
				updateTreeHeight(nodeExtendDepth);
			}
		}

	}

	public void setChilds(Node[] childs) {
		super.setChilds(childs);
		updateTreeHeight(0);
	}

	public void setChildCount(int count) {
		if (count > 0) {
			this.childs = new MultiWayDepthNode[count];
		}
	}

	// OTHER METHODS
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
		for (MultiWayDepthNode node : (MultiWayDepthNode[]) childs) {
			if (node == null)
				continue;
			max = node.treeHeight > max ? node.treeHeight : max;
		}
		max++;
		if (max != this.treeHeight) {
			this.treeHeight = max;
			if (parent != null) {
				((HeightExtension) parent).updateTreeHeight(treeHeight + 1);
			}
		}
	}

	/**
	 * Method which will clear the childs in this node. Treeheight of this node
	 * will be reseted to zero (it behaves as normal leaf). UpdateTreeHeight for
	 * this parent will be called.
	 * 
	 */
	public void clearChilds() {
		super.clearChilds();
		treeHeight = 0;
		if (parent != null)
			((HeightExtension) parent).updateTreeHeight(1);
	}

	/**
	 * {@inheritDoc}. Calling method {@link MultiWayNode#makeLeaf()}
	 */
	public void makeLeaf() {
		super.makeLeaf();
		this.treeHeight = 0;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public MultiWayDepthNode copy() {
		return new MultiWayDepthNode(this);
	}

	/**
	 * {@inheritDoc} </br> In addition to parent class it provides comparison of
	 * treeHeight that this extended class automatically compute.
	 */
	@Override
	public boolean equals(Object obj) {
		return (((MultiWayDepthNode) obj).treeHeight == this.treeHeight)
				&& super.equals(obj);
	}

}
