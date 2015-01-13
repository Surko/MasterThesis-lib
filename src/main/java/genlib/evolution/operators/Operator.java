package genlib.evolution.operators;

import java.util.ArrayList;

import genlib.evolution.individuals.Individual;

public interface Operator<T extends Individual> {

	public enum MutationOperators {
		DEFAULT
	}
	
	public enum XoverOperators {
		DEFAULT
	}
	
	public void setOperatorProbability(double prob);
	public double getOperatorProbability();	
	public void execute(ArrayList<T> parents, ArrayList<T> childs);
	public String objectInfo();
	
}
