package genlib.classifier.gens;

import java.util.ArrayList;

import genlib.evolution.individuals.TreeIndividual;
import genlib.utils.Utils;
import genlib.utils.WekaUtils;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.AdditionalMeasureProducer;
import weka.core.Drawable;
import weka.core.Instances;

public class WekaTreeGenerator extends TreeGenerator  {
	
	/** for serialization */
	private static final long serialVersionUID = -266323902512927931L;	
	public static final String initName = "wGen";
	private static final String _type = "TYPE";
	private static final String _numNodes = "measureTreeSize";
	private static final String _numLeaves = "measureNumLeaves";
	private Classifier classifier;
	
	public WekaTreeGenerator() {		
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
		classifier.buildClassifier(data);		
		String sTree = ((Drawable)classifier).graph();
		double treeSize = ((AdditionalMeasureProducer)classifier).getMeasure(_numNodes);		
		
		individuals[0] = WekaUtils.constructTreeIndividual(sTree, treeSize, data.numInstances(), treeInit.getAttrIndexMap(),
				treeInit.getAttrValueIndexMap(), treeInit.getAutoDepth());		
		return individuals;
	}
	
	@Override
	public TreeGenerator copy() {
		return null;
	}

	public Classifier getClassifier() {
		return classifier;
	}
	
	@Override
	public String getGenName() {		
		return initName;
	}
	
	@Override
	public String getInfo() {
		return "Tree generator which generates trees via weka algorithms";
	}

	public boolean isWekaDependent() {
		return true;
	}
	
	@Override
	public void setAdditionalOptions(String[] options) throws Exception {
		this.classifier.setOptions(options);
	}

	public void setParam(String param) throws Exception {
		String[] options = param.split(Utils.pDELIM);
		
		String classifierName = "";
		ArrayList<String> otherOptions = new ArrayList<>();
		for (int i = 0; i < options.length; i++) {
			if (options[i].equals(_type)) {
				classifierName = options[i+1];
				i++;
				continue;
			}
			
			otherOptions.add(options[i]);
		}			
		
		classifier = Classifier.forName(classifierName,
				otherOptions.toArray(new String[otherOptions.size()]));
		if (!(classifier instanceof Drawable)) {
			throw new Exception();
		}
		
		if (!(classifier instanceof AdditionalMeasureProducer)) {
			throw new Exception();
		}
		
	}
	
}
