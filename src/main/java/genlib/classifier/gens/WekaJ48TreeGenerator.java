package genlib.classifier.gens;

import genlib.classifier.splitting.SplitCriteria;
import genlib.evolution.individuals.TreeIndividual;
import genlib.utils.Utils;
import genlib.utils.WekaUtils;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class WekaJ48TreeGenerator extends TreeGenerator  {
	
	/** for serialization */
	private static final long serialVersionUID = -266323902512927931L;	
	public static final String initName = "wJ48Gen";
	private J48 j48Tree;
	
	public WekaJ48TreeGenerator() {
		this.j48Tree = new J48();			
	}
	
	public WekaJ48TreeGenerator(String[] options) throws Exception {
		this.j48Tree = new J48();
		this.j48Tree.setOptions(options);			
	}

	@Override
	public TreeIndividual[] createPopulation() throws Exception {
		if (data instanceof Instances) {
			return createPopulation((Instances)data);
		} else {
			throw new Exception("Bad type of instances");
		}
	}

	private TreeIndividual[] createPopulation(Instances data) throws Exception {				
		individuals = new TreeIndividual[1];		
		// TODO ONLY ONE INDIVIDUAL IS HERE. ADD SOME SORT OF ATTRIBUTE REMOVING OR DIFFERENT PRUNING MECHANISMS?
		j48Tree.buildClassifier(data);		
		String sTree = j48Tree.graph();
		individuals[0] = WekaUtils.constructTreeIndividual(sTree, j48Tree.measureTreeSize(), data.numInstances(), treeInit.getAttrIndexMap(),
				treeInit.getAttrValueIndexMap(), treeInit.getAutoHeight());		
		return individuals;
	}
	
	public void setOptions(String[] options) throws Exception{
		this.j48Tree.setOptions(options);			
	}	

	public J48 getJ48() {
		return j48Tree;
	}
	
	@Override
	public String getGenName() {		
		return initName;
	}
	
	@Override
	public String getInfo() {
		return "Tree generator which generates C4.5 trees via weka J48 algorithm";
	}

	public boolean isWekaDependent() {
		return true;
	}
	
	@Override
	public void setAdditionalOptions(String[] options) throws Exception {
		this.j48Tree.setOptions(options);		
	}

	public void setParam(String param) throws Exception {
		this.j48Tree.setOptions(param.split(Utils.pDELIM));
	}
	
	@Override
	public void setSplitCriteria(SplitCriteria<?,?> splitCriteria) {}
	
	@Override
	public TreeGenerator copy() throws Exception {		
		return new WekaJ48TreeGenerator(j48Tree.getOptions());		
	}
}
