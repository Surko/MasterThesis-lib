package genlib.classifier.splitting;

import java.io.Serializable;

public interface SplitCriteria extends Serializable {
	
	enum Criterias {
		GAINRATIO,
		INFORATIO
	}	
		
	public double getSplitPoint();
	public double getCriteriaValue();
	public double computeCriteria();
	public void setInstanceOrDistribution(Object insOrDist);
	public <I extends Object> double computeCriteria(I data, int classIndex) throws Exception;
	public <D extends Object> double computeCriteria(D distribution) throws Exception;
	public <D extends Object> double computeCriteria(D distribution, double totalIns) throws Exception;
	public <I extends Object, D extends Object> D handleEnumeratedAttribute(I dataPart, int attIndex, int complexity) throws Exception;
	public <I extends Object, D extends Object> D handleNumericAttribute(I dataPart, int attIndex, int complexity) throws Exception;
	public SplitCriteria copy();
}
