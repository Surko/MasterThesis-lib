package genlib.generators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.PermMessages;
import genlib.splitfunctions.SplitCriteria;
import genlib.structures.trees.MultiWayHeightNode;

/**
 * Class that implements TreeGenerator and generates static dummy trees which
 * are worthless for classification problems.
 * 
 * @author Lukas Surin
 *
 */
public class DummyTreeGenerator extends TreeGenerator {
	/** for serialization */
	private static final long serialVersionUID = 6608488805261224234L;
	/** name of the generator */
	public static final String initName = "dumGen";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeIndividual[] createPopulation() throws Exception {
		TreeIndividual[] individuals = new TreeIndividual[2];
		individuals[0] = new TreeIndividual(MultiWayHeightNode.makeLeaf(0));
		individuals[0] = new TreeIndividual(MultiWayHeightNode.makeLeaf(1));
		return individuals;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInfo() {
		return PermMessages._gen_dummy;
	}

	/**
	 * {@inheritDoc} <br>
	 * This method is not used
	 */
	@Override
	public void setAdditionalOptions(String[] options) throws Exception {
	}

	/**
	 * {@inheritDoc} <br>
	 * This method is not used
	 */
	@Override
	public void setParam(String param) throws Exception {
	}

	/**
	 * {@inheritDoc} <br>
	 * This method is not used
	 */
	@Override
	public void setSplitCriteria(SplitCriteria<?, ?> splitCriteria) {
	}

	/**
	 * {@inheritDoc} <br>
	 * Always false
	 */
	@Override
	public boolean isWekaDependent() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGenName() {
		return initName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeGenerator copy() {
		return new DummyTreeGenerator();
	}

}
