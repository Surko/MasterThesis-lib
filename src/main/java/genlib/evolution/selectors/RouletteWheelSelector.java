package genlib.evolution.selectors;

import java.util.ArrayList;
import java.util.Random;

import genlib.evolution.Population;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;

public class RouletteWheelSelector implements Selector {
	public static final String initName = "RW";
	private Random rng;
	private int fitnessIndex = 0;
	
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin, FitnessComparator<T> comp, int count) {
		return select(origin,null,comp,count);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin, ArrayList<T> dest,
			FitnessComparator<T> comp, int count) {
		if (dest == null) {
			dest = new ArrayList<>();
		}
		
		FitnessFunction<T> function = comp.getFitnessFuncs().get(fitnessIndex);
		int individualFitIndex = function.getIndex();
		int length = origin.size();
		
		double fitSum = 0d;
		double[] fitnesses = new double[length];
		
		for (int i = 0; i < length; i++) {
			fitnesses[i] = origin.get(i).getFitnessValue(individualFitIndex);
			fitSum += fitnesses[i];
		}		
		
		for (int i = 0; i < length; i++) {
			fitnesses[i] /= fitSum;
		}
		
		for (int i = 0; i < count; i++) {
			double roulette = rng.nextDouble();
			fitSum = 0;
			
			for (int j = 0; j < fitnesses.length; j++) {
				fitSum += fitnesses[j];
				if (fitSum > roulette) {
					dest.add((T)origin.get(j).copy());
					break;
				}
			}
		}	
		return dest;
	}
	
	public <T extends Individual> Population<T> select(Population<T> origin, int count) {
		return select(origin,null,count);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Individual> Population<T> select(Population<T> origin, Population<T> dest, int count) {
		if (dest == null) {
			dest = new Population<T>();
		}
		
		FitnessFunction<T> function = origin.getFitnessComparator().getFitnessFuncs().get(fitnessIndex);
		int individualFitIndex = function.getIndex();
		int length = origin.getPopulationSize();
		
		double fitSum = 0d;
		double[] fitnesses = new double[length];
		
		for (int i = 0; i < length; i++) {
			fitnesses[i] = origin.getIndividual(i).getFitnessValue(individualFitIndex);
			fitSum += fitnesses[i];
		}			
		
		for (int i = 0; i < length; i++) {
			fitnesses[i] /= fitSum;
		}
		
		for (int i = 0; i < count; i++) {
			double roulette = rng.nextDouble();
			fitSum = 0;
			
			for (int j = 0; j < fitnesses.length; j++) {
				fitSum += fitnesses[j];
				if (fitSum > roulette) {
					dest.add((T)origin.getIndividual(j).copy());
					break;
				}
			}
		}			
			
		return dest;
	}
	
	public void setRandomGenerator(Random random) {
		this.rng = random;
	}
	
	public void setParam(String s) {
		this.fitnessIndex = Integer.parseInt(s);
	}
}
