package genlib.classifier;

import genlib.classifier.gens.SimpleStumpGenerator;
import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.gens.TreeGenerator.TreeGenerators;
import genlib.classifier.gens.WekaSimpleStumpGenerator;
import genlib.classifier.popinit.CombStumpsInitializator;
import genlib.classifier.popinit.PopulationInitializator;
import genlib.classifier.popinit.WekaCombStumpsInitializator;
import genlib.classifier.splitting.GainCriteria;
import genlib.classifier.splitting.InformationGainCriteria;
import genlib.classifier.splitting.SplitCriteria;
import genlib.configurations.Config;
import genlib.evolution.Population;
import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.TextResource;
import genlib.structures.ArrayInstances;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;

import weka.core.Instances;

public class EvolutionTreeClassifier implements Serializable {

	enum InitPopulationType {
		DECISION_STUMP;
	}		

	/**
	 * for serialization
	 */
	private static final long serialVersionUID = -762451118775604722L;
	private double mutProb, mutBitProb, xoverProb;
	private int populationSize;
	private int seed;
	private Random random;
	private InitPopulationType popInitType;
	private PopulationInitializator<TreeGenerator> popInit;	

	public EvolutionTreeClassifier() {
		Config c = Config.getInstance();
		this.mutProb = c.getMutationProbability();
		this.mutBitProb = 0.02d;
		this.xoverProb = c.getXoverProbability();
		this.seed = c.getSeed();
		this.popInitType = InitPopulationType.DECISION_STUMP;
		this.populationSize = c.getPopulationSize();
		this.random = new Random();
	}

	public void buildClassifier(Instances data) throws Exception {
		random.setSeed(seed);
		// Shuffle data
		data.randomize(random);
				
		initPopulation(data);
		// initial population of trees
		TreeIndividual[] initPopulation = (TreeIndividual[]) popInit.getPopulation();
		ArrayList<TreeIndividual> l = new ArrayList<TreeIndividual>(Arrays.asList(initPopulation));
		Population<TreeIndividual> population = new Population<>(initPopulation, populationSize);
		
		// dalsie evolucne prikazy s populaciou volane z EvolutionTreeClassifier
		// TODO
	}
	
	public void initPopulation(Instances data) throws Exception {
		// initialization of population		
		popInit.setRandomGenerator(random);
		popInit.setInstances(data);
		popInit.initPopulation();
	}

	public void buildClassifier(ArrayInstances data) throws Exception {

	}

	public Random getRandom() {
		return random;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		if (this.seed != seed) {			
			this.seed = seed;	
		}			
	}

	public double getMutProb() {
		return mutProb;
	}

	public void setMutProb(double mutProb) {
		this.mutProb = mutProb;
	}

	public double getMutBitProb() {
		return mutBitProb;
	}

	public void setMutBitProb(double mutBitProb) {
		this.mutBitProb = mutBitProb;
	}

	public double getXoverProb() {
		return xoverProb;
	}

	public void setXoverProb(double xoverProb) {
		this.xoverProb = xoverProb;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public InitPopulationType getPopInitType() {
		return popInitType;
	}

	public void setPopInitializator(Properties prop, boolean isWeka) throws Exception {
		if (prop==null || prop.size() == 0) {
			return;
		} else {
			popInitType = InitPopulationType.valueOf(prop.getProperty("type"));
			if (popInitType == null) return;
			switch (popInitType) {
			// simple combination of stumps into tree.
			case DECISION_STUMP :					
				// Depth of generated trees in all of the population
				int depth = Integer.parseInt(prop.getProperty("depth","1"));				
				int divideParam = Integer.parseInt(prop.getProperty("divideParam","10"));

				TreeGenerator treeGen = null;
				CombStumpsInitializator stump = null;
				if (isWeka) {
					stump = new WekaCombStumpsInitializator(populationSize, 2, 10, false);					
				} else {
					stump = new CombStumpsInitializator(populationSize, 2, 10, false);;					
				}

				boolean resample = Boolean.parseBoolean(prop.getProperty("resample", "true"));

				// TreeGenerator for our stump population
				// treegenerators should be generating trees of depth 1 because those are stumps
				switch (TreeGenerators.valueOf(prop.getProperty("gen","SSGEN"))) {
				case SSGEN :
					if (isWeka) {
						treeGen = new WekaSimpleStumpGenerator();
					} else {
						treeGen = new SimpleStumpGenerator();
					}
					break;
					// here add more stump treegenerators 
				}
								
				// not consistent generator with population initializator, throwing exception
				if (treeGen.getGeneratorDepth() != 1) throw 
					new Exception(String.format(TextResource.getString("stumpNConGen"),treeGen.getClass().getName()));

				// Split criteria for our tree generator					
				switch (SplitCriteria.Criterias.valueOf(prop.getProperty("criteria", "INFORATIO"))) {
				case GAINRATIO :
					treeGen.setSplitCriteria(new GainCriteria());
					break;
				case INFORATIO :
					treeGen.setSplitCriteria(new InformationGainCriteria());
					break;
					// here add more measures for splitting 
				}			
				
				stump.setResample(resample);
				stump.setGenerator(treeGen);
				stump.setDepth(depth);
				stump.setDivideParam(divideParam);

				popInit = stump;
				break;
				// here add more population initializators
			}
		}
	}

	public PopulationInitializator<TreeGenerator> getPopInitializator() {
		return popInit;
	}
}
