package genlib.structures.trees;

import genlib.utils.Utils.Sign;

import java.io.Serializable;

public interface Node extends Serializable {	
	
	public Node[] getChilds();
	public Node getChildAt(int index);
	public void setChildAt(int index, Node node);
	public void setChilds(Node[] childs);
	public void setChildCount(int childCount);
	public int getChildCount();
	public void clearChilds();
	public Sign getSign();
	public void setSign(Sign sign);	
	public int getAttribute();
	public void setAttribute(int attribute);
	public double getValue();
	public void setValue(double value);
	public Node getParent();
	public void setParent(Node parent);
	public boolean isLeaf();
	public void makeLeaf();
	public double getCriteriaValue();
	public void setTreeHeightForced(int depth);
	public int getTreeHeight();	
	public Node copy();
	
	
}
