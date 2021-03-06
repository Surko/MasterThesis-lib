package genlib.structures.trees;

import genlib.exceptions.NodeCreationException;
import genlib.locales.TextResource;
import genlib.structures.extensions.HeightExtension;
import genlib.utils.Utils.Sign;

/**
 * Class that represents Node with 2 childs. It implements {@link BinaryNode}
 * and {@link HeightExtension} so it recomputes its size and height
 * automatically.
 * 
 * @author Lukas Surin
 */
public class BinaryHeightNode extends BinaryNode implements HeightExtension {

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
	public static BinaryHeightNode makeLeaf(double value) {
		BinaryHeightNode leaf = new BinaryHeightNode();
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
	public static BinaryHeightNode makeNode(int attribute, Sign sign,
			double value) {
		if (attribute == -1) {
			throw new NodeCreationException(String.format(
					TextResource.getString("eNodeCreation"), "attribute"));
		}

		BinaryHeightNode node = new BinaryHeightNode();
		node.childs = new BinaryHeightNode[2];
		node.sign = sign;
		node.attribute = attribute;
		node.value = value;
		return node;
	}

	/**
	 * Parameterless constructor for serialization and static methods
	 */
	public BinaryHeightNode() {
	}

	/**
	 * Copy constructor
	 * 
	 * @param toCopy
	 *            instance
	 */
	public BinaryHeightNode(BinaryHeightNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.treeHeight = toCopy.treeHeight;
		if (!toCopy.isLeaf()) {
			this.childs = new BinaryHeightNode[2];
			this.childs[0] = new BinaryHeightNode(
					(BinaryHeightNode) toCopy.childs[0]);
			this.childs[1] = new BinaryHeightNode(
					(BinaryHeightNode) toCopy.childs[1]);
			this.childs[0].parent = this;
			this.childs[1].parent = this;
		}
	}

	/**
	 * Constructor of BinaryNode that sets the node fields in advance.
	 * 
	 * @param attribute
	 *            on which we test instances
	 * @param sign
	 *            Sign
	 * @param value
	 *            of split
	 */
	public BinaryHeightNode(int attribute, Sign sign, double value) {
		super(attribute, sign, value);
	}

	// GETTERS
	/**
	 * {@inheritDoc}
	 */
	public int getTreeHeight() {
		return treeHeight;
	}

	// SETTERS
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChildAt(int index, Node node) {
		super.setChildAt(index, node);

		int nodeExtendDepth = ((HeightExtension) node).getTreeHeight() + 1;
		if (childs[index] == null) {
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
			max = ((BinaryHeightNode) childs[0]).treeHeight;
		}

		if (childs[1] != null) {
			max = Math.max(max, ((BinaryHeightNode) childs[0]).treeHeight);
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
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	public BinaryHeightNode copy() {
		return new BinaryHeightNode(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public BinaryHeightNode newInstance() {
		return new BinaryHeightNode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		// NO HASH COMPUTING
		return (((BinaryHeightNode) obj).treeHeight == this.treeHeight)
				&& super.equals(obj);
	}

}
