package genlib.classifier;

import genlib.structures.Data;

public interface Classifier {

	public void buildClassifier(Data data) throws Exception;

	public void setOptions(String[] options) throws Exception;

	public double[] classifyData(Data data) throws Exception;

	/**
	 * Method which creates the dataset which should be classified.
	 * 
	 * @param sFile
	 *            string path of file
	 * @return Data object with loaded dataset
	 * @throws Exception
	 *             if some problem occured when loading file
	 */
	public Data makeDataFromFile(String sFile) throws Exception;
}
