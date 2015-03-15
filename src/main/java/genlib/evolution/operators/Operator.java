package genlib.evolution.operators;

import java.util.ArrayList;
import java.util.HashMap;

import genlib.evolution.EvolutionAlgorithm;
import genlib.evolution.Population;
import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;

public abstract class Operator<T extends Individual> {

	public static final HashMap<String,Class<Operator<TreeIndividual>>> tXOper = new HashMap<>();
	public static final HashMap<String,Class<Operator<TreeIndividual>>> tMOper = new HashMap<>();
	
	protected EvolutionAlgorithm<T> ea;
	
	public abstract void setOperatorProbability(double prob);
	public abstract double getOperatorProbability();
	public abstract void execute(Population<T> parents, Population<T> childs);
	public abstract String objectInfo();
	
	public void setEvolutionAlgorithm(EvolutionAlgorithm<T> ea) {
		this.ea = ea;
	}
	
}
