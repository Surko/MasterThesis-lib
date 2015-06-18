package genlib.classifier.classifierextensions;

import genlib.structures.data.GenLibInstance;
import genlib.structures.data.GenLibInstances;

public interface GenLibClassifierExtension {
	public void buildClassifier(GenLibInstances data) throws Exception;
	public double classifyInstance(GenLibInstance instance) throws Exception;
}
