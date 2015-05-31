package genlib.classifier;

import java.util.HashMap;
import java.util.Random;

import genlib.structures.data.GenLibInstances;

public interface Classifier {
	public static final HashMap<String, Class<? extends Classifier>> classifiers = new HashMap<>();
	
	public void buildClassifier(GenLibInstances data) throws Exception;
	public void makePropsFromString(boolean isNumeric) throws Exception;	
	public int getSeed();
	public Random getRandom();	
}
