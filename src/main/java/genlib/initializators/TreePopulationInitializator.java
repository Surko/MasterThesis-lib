package genlib.initializators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.generators.Generator;
import genlib.generators.TreeGenerator;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.Data;
import genlib.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

public abstract class TreePopulationInitializator implements
		PopulationInitializator<TreeIndividual> {

	enum PopInitEnum {
		RESAMPLE, AUTOHEIGHT, DIVIDEPARAM, MAXHEIGHT, DATA;

		public static PopInitEnum value(String name) {
			if (RESAMPLE.name().equals(name)) {
				return RESAMPLE;
			}
			if (AUTOHEIGHT.name().equals(name)) {
				return AUTOHEIGHT;
			}
			if (DIVIDEPARAM.name().equals(name)) {
				return DIVIDEPARAM;
			}
			if (MAXHEIGHT.name().equals(name)) {
				return MAXHEIGHT;
			}

			return null;
		}
	}

	/** for serialization */
	private static final long serialVersionUID = 7222530982342870829L;
	/** logger */
	private static final Logger LOG = Logger
			.getLogger(TreePopulationInitializator.class.getName());
	/** depth of generated trees. */
	protected int maxHeight = 1;
	/** number of division of trainin data */
	protected int divideParam = 10;
	/** only resampling instead of dividing */
	protected boolean resample = true;
	/** recounting of depth inside trees */
	protected boolean autoHeight = false;
	/** Individuals that makes this population */
	protected TreeIndividual[] population;
	/** Number of threads that will be creating population */
	protected int nThreads;

	/**
	 * Generator of tree population. It contains all of the generated trees that
	 * are used to combine.
	 */
	protected TreeGenerator gen;
	/**
	 * Random seeded object for this run of algorithm. Default from Utils. Can
	 * be changed.
	 */
	protected Random random;
	/** Object ({@link Data}) of all instances */
	protected Data data;
	/** Final population size */
	protected int popSize;
	/** type of data split used in creating population */
	protected int typeOfData = -1;

	/**
	 * Method that returns population of generated individuals from
	 * TreeGenerator.
	 */
	public TreeIndividual[] getPopulation() {
		return population;
	}

	@Override
	public TreeIndividual[] getOriginPopulation() {
		return gen.getIndividuals();
	}

	/**
	 * Gets the depth of the combined trees (population trees) from generated
	 * trees.
	 * 
	 * @return Depth of the trees in population.
	 */
	public int getMaxHeight() {
		return maxHeight;
	}

	public int getDivideParam() {
		return divideParam;
	}

	public boolean getAutoHeight() {
		return autoHeight;
	}

	@Override
	public TreeGenerator getGenerator() {
		return gen;
	}

	public HashMap<String, Integer>[] getAttrValueIndexMap() {
		return data.getAttrValueIndexMap();
	}

	public HashMap<String, Integer> getAttrIndexMap() {
		return data.getAttrIndexMap();
	}

	public boolean isResampling() {
		return resample;
	}

	@Override
	public int getPopulationSize() {
		return popSize;
	}

	public void setRandomGenerator(Random random) {
		this.random = random;
	}

	public void setDepth(int depth) {
		this.maxHeight = depth;
	}

	public void setDivideParam(int divideParam) {
		this.divideParam = divideParam;
	}

	public void setResample(boolean resample) {
		this.resample = resample;
	}

	public void setAutoDepth(boolean autoDepth) {
		this.autoHeight = autoDepth;
	}

	@Override
	public void setGenerator(
			ArrayList<? extends Generator<TreeIndividual>> gen) {
		if (gen.size() > 0) {
			this.gen = (TreeGenerator) gen.get(0);
		}
	}

	@Override
	public void setData(Data data) {
		this.data = data.getDataOfType(typeOfData);
	}

	@Override
	public void setPopulationSize(int popSize) {
		this.popSize = popSize;
	}

	public void setNumOfThreads(int nThreads) {
		this.nThreads = nThreads;
	}

	public void setParam(String param) {
		String[] params = param.split(Utils.pDELIM);

		for (int i = 0; i < params.length; i += 2) {
			PopInitEnum popInitEnum = PopInitEnum.value(params[i]);

			if (popInitEnum == null) {
				LOG.warning(String.format(
						TextResource.getString(TextKeys.iExcessParam),
						params[i]));
				continue;
			}

			switch (popInitEnum) {
			case AUTOHEIGHT:
				this.autoHeight = Boolean.parseBoolean(params[i + 1]);
				break;
			case DIVIDEPARAM:
				this.divideParam = Integer.parseInt(params[i + 1]);
				break;
			case MAXHEIGHT:
				this.maxHeight = Integer.parseInt(params[i + 1]);
				break;
			case RESAMPLE:
				this.resample = Boolean.parseBoolean(params[i + 1]);
				break;
			case DATA:
				this.typeOfData = Integer.parseInt(params[i + 1]);
				break;
			default:
				break;
			}

		}
	}

}
