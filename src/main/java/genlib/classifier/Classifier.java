package genlib.classifier;

import java.util.Random;

import genlib.structures.ArrayInstances;

public interface Classifier {
	public void buildClassifier(ArrayInstances data) throws Exception;
	public void makePropsFromString() throws Exception;
	public int getSeed();
	public Random getRandom();
}
