package genlib.structures;

import genlib.exceptions.NodeCreationException;
import genlib.locales.TextResource;
import genlib.structures.extensions.DepthExtension;
import genlib.utils.Utils.Sign;

public class MultiWayDepthNode extends MultiWayNode implements DepthExtension {
	/** for serialization */
	private static final long serialVersionUID = 6475434862235450574L;

	public static MultiWayDepthNode makeLeaf(double value) {
		MultiWayDepthNode leaf = new MultiWayDepthNode();
		leaf.setAttribute(-1);
		leaf.setValue(value);
		return leaf;		
	}
	
	public static MultiWayDepthNode makeNode(int childCount, int attribute, Sign sign, double value) {
		if (attribute == -1) {
			throw new NodeCreationException(String.format(TextResource.getString("eNodeCreation"), "attribute"));
		}
		MultiWayDepthNode node = new MultiWayDepthNode();
		node.setChildCount(childCount);
		node.setAttribute(attribute);
		node.setSign(sign);
		node.setValue(value);
		return node;		
	}
	
	public MultiWayDepthNode() {
		super();
	}

	public MultiWayDepthNode(MultiWayNode toCopy) {
		this.attribute = toCopy.attribute;
		this.value = toCopy.value;
		this.treeHeight = toCopy.treeHeight;
		this.sign = toCopy.sign;		
		if (!toCopy.isLeaf()) {
			this.childs = new MultiWayDepthNode[toCopy.childs.length];
			for (int i = 0; i < toCopy.childs.length; i++) {
				childs[i] = toCopy.childs[i].copy();
				childs[i].setParent(this);
			}
		}
	}

	public MultiWayDepthNode(int childLength) {
		super(childLength);
	}

	public MultiWayDepthNode(int attribute, Sign sign, double value) {
		super(attribute, sign, value);
	}

	public void setChildAt(int index, Node node) {
		if (childs == null) {
			return;
		}
		
		int nodeExtendDepth = node.getTreeHeight() + 1;
		if (childs[index] == null) {
			// set the child
			childs[index] = (MultiWayNode) node;
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
			childs[index] = (MultiWayNode) node;
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

	public void clearChilds() {
		super.clearChilds();
		if (parent != null)
			parent.recount(1);
	}

	public void setChilds(Node[] childs) {
		super.setChilds(childs);
		recount(0);
		if (parent != null) {
			parent.recount(treeHeight + 1);
		}
	}

	public void setChildCount(int count) {
		if (count > 0)
			this.childs = new MultiWayDepthNode[count];
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public MultiWayDepthNode copy() {
		return new MultiWayDepthNode(this);
	}

	/**
	 * {@inheritDoc}
	 * </br>
	 * In addition to parent class it provides comparison of treeHeight that this 
	 * extended class automatically compute.
	 */
	@Override
	public boolean equals(Object obj) {		
		return (((MultiWayDepthNode)obj).treeHeight == this.treeHeight) && super.equals(obj) ;
	}

	
	
}
