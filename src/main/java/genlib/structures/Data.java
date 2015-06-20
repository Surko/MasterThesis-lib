package genlib.structures;

import genlib.exceptions.MissingParamException;
import genlib.exceptions.NotDefParamException;
import genlib.exceptions.WrongDataException;
import genlib.locales.PermMessages;
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

	/**
	 * DataEnum with enums of different params for creating train and validation
	 * data.Defined kinds of params: </br> {@link DataEnum#RESAMPLE} </br>
	 * {@link DataEnum#TRAINRATIO} </br>
	 * 
	 * 
	 * @author Lukas Surin
	 *
	 */
	public enum DataEnum {
		/**
		 * resample enum that shows if train and validation data should be
		 * resampled from data object
		 */
		RESAMPLE,
		/**
		 * trainratio enum that shows if train and validation data should be
		 * some ratio from data object
		 */
		TRAINRATIO;

		public static DataEnum value(String name) {
			if (name.equals(RESAMPLE.name())) {
				return RESAMPLE;
			}

			if (name.equals(TRAINRATIO.name())) {
				return TRAINRATIO;
			}

			return null;
		}
	}

	/** for serialization */
	private static final long serialVersionUID = 4437040173554934907L;
	/** data object, final which is initialized in constructor */
	private final Object data;
	/** train data from data object */
	private Data train;
	/** validation data from data object */
	private Data validation;
	/** random object for this data */
	private final Random random;
	/** number of instances in data object */
	private final int numInstances;
	/** boolean to know if data is of type {@link Instances} */
	private boolean isInstances = false;
	/** class counts in data object */
	private double[] classCounts;

	/** Index of attribute values to access correct array values */
	public HashMap<String, Integer>[] attrValueIndexMap;
	/** Index of attribute to access correct attribute from String */
	public HashMap<String, Integer> attrIndexMap;

	public Data(Instances data, Random random) {
		this.random = random;
		this.train = this;
		this.validation = this;
		this.data = data;
		this.numInstances = data.numInstances();
		this.isInstances = true;
	}

	public Object getData() {
		return data;
	}

	public Data(GenLibInstances data, Random random) {
		this.random = random;
		this.train = this;
		this.validation = this;
		this.data = data;
		this.numInstances = data.numInstances();
		this.isInstances = false;
	}

	/**
	 * Number of instances in data (GenLibInstances or Instances).
	 * 
	 * @return number of instances
	 */
	public int numInstances() {
		return numInstances;
	}

	/**
	 * Method that tests if data object is of type {@link Instances}.
	 * 
	 * @return true iff data is of type Instances
	 */
	public boolean isInstances() {
		return isInstances;
	}

	/**
	 * Method that tests if data object is of type {@link GenLibInstances}.
	 * 
	 * @return true iff instances are GenLibInstances
	 */
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
	public void randomize() {
		if (isInstances) {
			((Instances) data).randomize(random);
			return;
		} else {
			((GenLibInstances) data).randomize(random);
			return;
		}
	}

	/**
	 * Method which creates new datas from instances pulled out of this data
	 * object.
	 * 
	 * @param resample
	 *            if train and validation sets should be resamples
	 * @param trainRatio
	 *            what size should trainData have (respectively validation)
	 */
	private void createValidationAndTrainData(boolean resample,
			double trainRatio) {
		if (resample) {
			if (isInstances) {
				Instances instances = (Instances) data;
				train = new Data(instances.resample(random), new Random(
						random.nextLong()));
				validation = new Data(instances.resample(random), new Random(
						random.nextLong()));
			} else {
				GenLibInstances instances = (GenLibInstances) data;
				train = new Data(instances.resample(random), new Random(
						random.nextLong()));
				validation = new Data(instances.resample(random), new Random(
						random.nextLong()));
			}
		} else {
			if (isInstances) {
				Instances instances = (Instances) data;
				int trainCount = (int) (((double) instances.numInstances()) * trainRatio);

				Instances train = new Instances(instances, 0, trainCount);
				Instances validation = new Instances(instances, trainCount,
						instances.numInstances() - trainCount);

				this.train = new Data(train, new Random(random.nextLong()));
				this.validation = new Data(validation, new Random(
						random.nextLong()));

			} else {
				GenLibInstances instances = (GenLibInstances) data;
				int trainCount = (int) (((double) instances.numInstances()) * trainRatio);

				GenLibInstances train = instances.getPart(0, trainCount);
				GenLibInstances validation = instances.getPart(trainCount,
						instances.numInstances() - trainCount);

				this.train = new Data(train, new Random(random.nextLong()));
				this.validation = new Data(validation, new Random(
						random.nextLong()));
			}
		}

	}

	/**
	 * Getter to get trainData for this run.
	 * 
	 * @return train data of type Data
	 */
	public Data getTrainData() {
		return train;
	}

	/**
	 * Getter to get validationData for this run.
	 * 
	 * @return validation data of type Data
	 */
	public Data getValidationData() {
		return validation;
	}

	/**
	 * Getter which makes choice which data we return by its int param.
	 * @param dataType int param (-1=this, 0=train, 1=validation)
	 * @return data decided by param
	 */
	public Data getDataOfType(int dataType) {
		switch (dataType) {
		case -1:
			return this;			
		case 0:
			return train;			
		case 1:
			return validation;			
		default:
			return this;			
		}
	}
	
	/**
	 * Method which returns number of classes for this data object.
	 * 
	 * @return array with class counts
	 */
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

	/**
	 * Method which returns index of attribute values to access correct array
	 * values
	 * 
	 * @return hashmap with indeces 
	 */
	public HashMap<String, Integer>[] getAttrValueIndexMap() {
		if (attrValueIndexMap != null) {
			return attrValueIndexMap;
		}

		makeAttrValueIndexMap();
		return attrValueIndexMap;
	}

	/**
	 * Method which returns index of attribute to access correct attribute from
	 * String
	 * 
	 * @return hashmap with indeces of string attribute
	 */
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

	public void setParam(String param) {
		System.out.println(param);
		if (param.equals(PermMessages._blank_param)) {
			return;
		}
		
		String[] parameters = param.split(Utils.oDELIM);

		boolean resample = false;
		double trainRatio = 1;

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				throw new MissingParamException();
			}

			DataEnum dataEnum = DataEnum.value(parameters[i]);

			if (dataEnum == null) {
				throw new NotDefParamException();
			}

			switch (dataEnum) {
			case RESAMPLE:
				resample = Boolean.valueOf(parameters[i + 1]);
				break;
			case TRAINRATIO:
				trainRatio = Double.parseDouble(parameters[i + 1]);
				break;
			}
		}

		createValidationAndTrainData(resample, trainRatio);
	}

}
