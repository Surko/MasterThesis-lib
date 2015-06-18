package genlib.classifier.classifierextensions;

import weka.core.Instance;
import weka.core.Instances;

public interface WekaClassifierExtension {
	public void buildClassifier(Instances data) throws Exception;
	public double classifyInstance(Instance instance) throws Exception;
}
