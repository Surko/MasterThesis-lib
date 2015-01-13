package genlib.evolution.operators;

import genlib.evolution.individuals.TreeIndividual;

import java.util.ArrayList;

public class DefaultTreeCrossover implements Operator<TreeIndividual> {

	@Override
	public void setOperatorProbability(double prob) {}

	@Override
	public double getOperatorProbability() { 
		return 0;
	}

	@Override
	public void execute(ArrayList<TreeIndividual> parents, ArrayList<TreeIndividual> childs) {}

	@Override
	public String objectInfo() {
		return "DEFAULT=0";
	}

}
