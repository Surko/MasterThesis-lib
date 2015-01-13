package genlib.classifier.popinit;

import genlib.locales.PermMessages;
import weka.core.Instances;

public class WekaCompletedTrees extends CompletedTrees {

	/** for serialization */
	private static final long serialVersionUID = 6362474859353821356L;

	private void initPopulation(Instances data) throws Exception {
		
	}
	
	@Override
	public void initPopulation() throws Exception {
		if (data instanceof Instances) {
			initPopulation((Instances) data);
		} else {
			throw new Exception(PermMessages._exc_badins);
		}
	}
	
}
