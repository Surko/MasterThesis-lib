package genlib.classifier.splitting;

import java.io.Serializable;
import java.util.HashMap;

public interface SplitCriteria<I,D> extends Serializable {		
	
	public double computeCriteria(I data, int classIndex) throws Exception;
	public double computeCriteria(D distribution) throws Exception;
	public double computeCriteria(D distribution, double totalIns) throws Exception;
	public D handleEnumeratedAttribute(I dataPart, int attIndex, int complexity) throws Exception;
	public D handleNumericAttribute(I dataPart, int attIndex, int complexity) throws Exception;
	public SplitCriteria<I,D> copy();
}
