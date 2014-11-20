package genlib.utils;

import java.util.Enumeration;

import weka.core.Instance;
import weka.core.Instances;

public class WekaUtils {

	public double[][] instacesToArray(Instances instances) {
		double[][] insArray = new double[instances.numInstances()][instances.numAttributes()];
		Enumeration<Instance> insEnum = instances.enumerateInstances();
		
		int index = 0;
		while (insEnum.hasMoreElements()) {			
			insArray[index++] = insEnum.nextElement().toDoubleArray();		
		}
		return insArray;
	}
	
}
