package genlib.structures;

import genlib.exceptions.WrongDataException;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.data.GenLibInstances;
import genlib.utils.Utils;
import genlib.utils.WekaUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import weka.classifiers.trees.j48.Distribution;
import weka.core.Instances;

/**
 * Class which serves purpose of adapter around the data object which can be of
 * type Instances from weka or GenLibInstances from this project. It is the
 * common ground if some method or algorithms want to access either one of those
 * types.
 * 
 * @author Lukas Surin
 */
public class Data implements Serializable {

	/** for serialization */
	private static final long serialVersionUID = 4437040173554934907L;
	private final Object data;
	private final int numInstances;
	private boolean isInstances = false;
	private double[] classCounts;

	/** Index of attribute values to access correct array values */
	public HashMap<String, Integer>[] attrValueIndexMap;
	/** Index of attribute to access correct attribute from String */
	public HashMap<String, Integer> attrIndexMap;

	public Data(Instances data) {
		this.data = data;
		this.numInstances = data.numInstances();
		this.isInstances = true;
	}

	public Object getData() {
		return data;
	}

	public Data(GenLibInstances data) {
		this.data = data;
		this.numInstances = data.numInstances();
		this.isInstances = false;
	}

	public int numInstances() {
		return numInstances;
	}

	public boolean isInstances() {
		return isInstances;
	}

	public boolean isGenLibInstances() {
		return !isInstances;
	}

	/**
	 * Method which will type the object into Instances. If it's not possible
	 * then exception is thrown.
	 * 
	 * @return weka.core.Instances from data
	 */
	public Instances toInstances() {
		if (isInstances) {
			return (Instances) data;
		}
		throw new WrongDataException(String.format(TextResource
				.getString(TextKeys.eBadTypeConversion), data.getClass()
				.getName(), Instances.class.getName()));
	}

	/**
	 * Method which will type the object into GenLibInstances. If it's not
	 * possible then exception is thrown.
	 * 
	 * @return GenLibInstances from data
	 */
	public GenLibInstances toGenLibInstances() {
		if (!isInstances) {
			return (GenLibInstances) data;
		}
		throw new WrongDataException(String.format(TextResource
				.getString(TextKeys.eBadTypeConversion), data.getClass()
				.getName(), GenLibInstances.class.getName()));
	}

	/**
	 * Method which randomizes instances inside data field. It throws unchecked
	 * exception if any other type from Instances or GenLibInstances is pushed
	 * into data field.
	 * 
	 * @param random
	 *            object used to randomize
	 * @see Instances
	 * @see GenLibInstances
	 * @see WrongDataException
	 */
	public void randomize(Random random) {
		if (isInstances) {
			((Instances) data).randomize(random);
			return;
		} else {
			((GenLibInstances) data).randomize(random);
			return;
		}
	}

	/**
	 * Method which creates new data from instances pulled out of data object.
	 * It throws unchecked exception if any other type from Instances or
	 * GenLibInstances is pushed into data field.
	 * 
	 * @param numFolds
	 *            Number of folds separating this instances
	 * @return newly created data from test instances
	 * 
	 */
	public Data makeTestData(int numFolds) {
		if (isInstances) {
			Instances testInstances = ((Instances) data).testCV(numFolds,
					numFolds - 1);
			return new Data(testInstances);
		} else {
			GenLibInstances testInstances = ((GenLibInstances) data).testData(
					numFolds, numFolds - 1);
			return new Data(testInstances);
		}
	}

	public double[] getClassCounts() {
		if (classCounts == null) {
			if (isInstances) {
				try {
					classCounts = new Distribution((Instances) data).matrix()[0];
				} catch (Exception e) {
				}
			} else {
				try {
					classCounts = ((GenLibInstances) data).getDistribution()
							.getClassCounts();
				} catch (Exception e) {
				}
			}
		}

		return classCounts;
	}

	public HashMap<String, Integer>[] getAttrValueIndexMap() {
		if (attrValueIndexMap != null) {
			return attrValueIndexMap;
		}

		makeAttrValueIndexMap();
		return attrValueIndexMap;
	}

	public HashMap<String, Integer> getAttrIndexMap() {
		if (attrIndexMap != null) {
			return attrIndexMap;
		}

		makeAttrIndexMap();
		return attrIndexMap;
	}

	public void makeAttrValueIndexMap() {
		if (isInstances) {
			attrValueIndexMap = WekaUtils
					.makeAttrValueIndexMap((Instances) data);
		} else {
			attrValueIndexMap = Utils
					.makeAttrValueIndexMap((GenLibInstances) data);
		}
	}

	public void makeAttrIndexMap() {
		if (isInstances) {
			attrIndexMap = WekaUtils.makeAttrIndexMap((Instances) data);
		} else {
			attrIndexMap = Utils.makeAttrIndexMap((GenLibInstances) data);
		}

	}

}
