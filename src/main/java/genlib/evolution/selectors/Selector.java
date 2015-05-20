package genlib.evolution.selectors;

import genlib.evolution.Population;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public interface Selector extends Serializable {	
	
	public static final HashMap<String,Class<Selector>> selectors = new HashMap<>();
	public static final HashMap<String,Class<Selector>> envSelectors = new HashMap<>();
	
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin, FitnessComparator<T> comp, int count);
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin, ArrayList<T> dest, FitnessComparator<T> comp, int count);
	public <T extends Individual> Population<T> select(Population<T> origin, int count);	
	public <T extends Individual> Population<T> select(Population<T> origin, Population<T> dest, int count);
	public void setRandomGenerator(Random random);
	public void setParam(String s);
}
