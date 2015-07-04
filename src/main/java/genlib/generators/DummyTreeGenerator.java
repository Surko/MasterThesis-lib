package genlib.generators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.splitfunctions.SplitCriteria;
import genlib.structures.trees.MultiWayHeightNode;

public class DummyTreeGenerator extends TreeGenerator {	/** for serialization */
	private static final long serialVersionUID = 6608488805261224234L;
	public static final String initName = "dumGen";
	
	@Override
	public TreeIndividual[] createPopulation() throws Exception {
		TreeIndividual[] individuals = new TreeIndividual[2];
		individuals[0] = new TreeIndividual(MultiWayHeightNode.makeLeaf(0));
		individuals[0] = new TreeIndividual(MultiWayHeightNode.makeLeaf(1));
		return individuals;
	}

	@Override
	public String getInfo() {
		return PermMessages._gen_dummy;
	}	
	
	@Override
	public void setAdditionalOptions(String[] options) throws Exception {}

	@Override
	public void setParam(String param) throws Exception {}
	
	@Override
	public void setSplitCriteria(SplitCriteria<?,?> splitCriteria) {}
	
	@Override
	public boolean isWekaDependent() {
		return false;
	}

	@Override
	public String getGenName() {
		return initName; 
	}
	
	@Override
	public TreeGenerator copy() {
		return new DummyTreeGenerator();
	}	

}
