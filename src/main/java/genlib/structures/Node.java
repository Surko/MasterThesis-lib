package genlib.structures;

import java.io.Serializable;

public interface Node extends Serializable {
	
	public Node[] getChilds();
	public Node getChildAt(int index);
	public void setChildAt(int index, Node node);
	public void setChilds(Node[] childs);
	public int getChildCount();
	public void clearChilds();
	public int getAttribute();
	public double getValue();
	public Node getParent();
	public void setParent(Node parent);
	public boolean isLeaf();
	public void makeLeaf();
	public double getCriteriaValue();
	public void setTreeDepthForced(int depth);
	public int getTreeDepth();	
	public Node copy();
	
}
