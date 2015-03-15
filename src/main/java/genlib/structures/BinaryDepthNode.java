package genlib.structures;

import genlib.structures.extensions.DepthExtension;
import genlib.utils.Utils.Sign;

public class BinaryDepthNode extends BinaryNode implements DepthExtension {

	/** for serialization */
	private static final long serialVersionUID = -5699769468571434662L;

	public BinaryDepthNode() {
		super();
	}

	public BinaryDepthNode(BinaryNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.treeHeight = toCopy.treeHeight;
		if (!toCopy.isLeaf()) {
			this.childs = new BinaryDepthNode[2];
			this.childs[0] = new BinaryDepthNode(toCopy.childs[0]);
			this.childs[1] = new BinaryDepthNode(toCopy.childs[1]);
			this.childs[0].parent = this;
			this.childs[1].parent = this;
		}
	}

	public BinaryDepthNode(int attribute, Sign sign, double value) {
		super(attribute, sign, value);
	}

	@Override
	public void setChildAt(int index, Node node) {
		if (childs == null) {
			childs = new BinaryNode[2];
		}
		
		int nodeExtendDepth = node.getTreeHeight() + 1;
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
					parent.recount(treeHeight + 1);
			}
		} else {
			// set the child
			childs[index] = (BinaryNode) node;
			// set the parent of a child
			node.setParent(this);

			// node depth has to be different
			if (nodeExtendDepth != treeHeight) {
				recount(nodeExtendDepth);

				if (parent != null)
					parent.recount(treeHeight + 1);
			}
		}
	}

	@Override
	public void setChilds(Node[] childs) {
		super.setChilds(childs);
		recount(0);
		if (parent != null) {
			parent.recount(treeHeight);
		}
	}

	@Override
	public void clearChilds() {
		super.clearChilds();
		if (parent != null)
			parent.recount(0);
	}

	public BinaryDepthNode copy() {
		return new BinaryDepthNode(this);
	}
	
	@Override
	public boolean equals(Object obj) {		
		// NO HASH COMPUTING
		return (((BinaryDepthNode)obj).treeHeight == this.treeHeight) && super.equals(obj);
	}

}
