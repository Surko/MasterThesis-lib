package genlib.classifier;

import genlib.structures.Data;

public interface Classifier {

	public void buildClassifier(Data data) throws Exception;

	public void setOptions(String[] options) throws Exception;

	public double[] classifyData(Data data) throws Exception;
}
