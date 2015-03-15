package genlib.evolution.operators;

import genlib.evolution.Population;
import genlib.evolution.individuals.TreeIndividual;

import java.util.ArrayList;

public class DefaultTreeMutation extends Operator<TreeIndividual> {

	public static final String initName = "dtM";	
	
	@Override
	public void setOperatorProbability(double prob) {}

	@Override
	public double getOperatorProbability() { 
		return 0;
	}

	@Override
	public void execute(Population<TreeIndividual> parents, Population<TreeIndividual> childs) {
		//TODO default tree mutation
	}

	@Override
	public String objectInfo() {
		return "DEFAULT=0";
	}
	
}
