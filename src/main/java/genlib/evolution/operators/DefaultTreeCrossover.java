package genlib.evolution.operators;

import genlib.evolution.Population;
import genlib.evolution.individuals.TreeIndividual;

import java.util.ArrayList;

public class DefaultTreeCrossover extends Operator<TreeIndividual> {

	public static final String initName = "dtX";
	
	@Override
	public void setOperatorProbability(double prob) {}

	@Override
	public double getOperatorProbability() { 
		return 0;
	}

	@Override
	public void execute(Population<TreeIndividual> parents, Population<TreeIndividual> childs) {
		//TODO default xover
	}

	@Override
	public String objectInfo() {
		return "DEFAULT=0";
	}

}
