package genlib.classifier.weka;

import weka.core.Instances;

public interface WekaClassifierExtension {
	public void buildClassifier(Instances data) throws Exception;
}
