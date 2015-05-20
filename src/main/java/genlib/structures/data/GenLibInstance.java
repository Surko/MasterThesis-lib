package genlib.structures.data;

public interface GenLibInstance {
	public GenLibAttribute getAttribute(int index);
	public double getValueOfAttribute(int index);
	public GenLibAttribute getClassAttribute();
	public double getValueOfClass();
}
