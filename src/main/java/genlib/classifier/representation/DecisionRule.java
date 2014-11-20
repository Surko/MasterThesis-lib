package genlib.classifier.representation;

public class DecisionRule {

	enum Operator {
		NEQ(0),
		EQ(1),
		LT(2),
		GT(4),
		LET(5),
		GET(3);
		
		private int repr;
		
		private Operator(int repr) {
			this.repr = repr;
		}
		
		public int getValue() {
			return repr;
		}
	}
	
	private double[] genes; 
	
	public DecisionRule(int attrCount) {
		genes = new double[attrCount * 4];
	}
	
	public void setOperatorAt(int index, Operator op) {
		genes[index * 4 + 2] = op.getValue();
	}
	
	public void setOperatorAt(int index, int op) {
		genes[index * 4 + 2] = op;
	}
	
	public void setOperatorAt(int index, String sop) {		
		Operator op = Operator.valueOf(sop);		
		genes[index * 4 + 2] = op.getValue();
	}
	
	public void setValueAt(int index, double value) {
		genes[index * 4 + 3] = value;
	}
	
	public void setAttrUsage(int index,boolean used) {
		genes[index * 4 + 1] = used ? 1 : 0;
	}	
	
}
