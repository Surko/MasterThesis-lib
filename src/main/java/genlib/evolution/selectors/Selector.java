package genlib.evolution.selectors;

import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.population.IPopulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public interface Selector extends Serializable {	
	
	public static final HashMap<String,Class<Selector>> selectors = new HashMap<>();
	public static final HashMap<String,Class<Selector>> envSelectors = new HashMap<>();
	
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin, FitnessComparator<T> comp, int count);
	public <T extends Individual> ArrayList<T> select(ArrayList<T> origin, ArrayList<T> dest, FitnessComparator<T> comp, int count);
	public <T extends Individual> IPopulation<T> select(IPopulation<T> origin, int count);	
	public <T extends Individual> IPopulation<T> select(IPopulation<T> origin, IPopulation<T> dest, int count);
	public void setRandomGenerator(Random random);
	public void setParam(String s);
}
