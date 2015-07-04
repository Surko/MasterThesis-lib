package genlib.evolution.operators;

import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;
import genlib.exceptions.PopulationInitializationException;
import genlib.generators.WekaSimpleStumpGenerator;
import genlib.locales.PermMessages;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.splitfunctions.InformationGainCriteria;
import genlib.structures.Data;
import genlib.structures.extensions.SizeExtension;
import genlib.structures.trees.MultiWayHeightNode;
import genlib.structures.trees.MultiWayNode;
import genlib.structures.trees.Node;
import genlib.utils.Utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Decision Stump Mutation is mutation class that implements the Operator
 * interface. When the mutation is applied, the randomly chosen leaf is replaced
 * with decison stump.
 * 
 * @see Operator
 * @author Lukas Surin
 *
 */
public class DecisionStumpMutation implements Operator<TreeIndividual> {

	/** for serialization */
	private static final long serialVersionUID = -2553678098269930119L;
	/** logger for this class */
	private static final Logger LOG = Logger
			.getLogger(DecisionStumpMutation.class.getName());
	/** name of this operator */
	public static final String initName = "wekaDSM";
	/** random object for this operator */
	private Random random;
	/** probability of this operator */
	private double mProb = 0d;
	/** data from which we take the most frequent class */
	private TreeIndividual[] stumps;
	/** data from which we create stumps */
	private Data data;
	/** type of data split used in operator */
	protected int typeOfData = -1;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOperatorProbability(double mProb) {
		this.mProb = mProb;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getOperatorProbability() {
		return mProb;
	}

	/**
	 * {@inheritDoc} </p> This mutation will change the randomly chosen leaf to
	 * randomly chosen decision stump.
	 */
	@Override
	public void execute(IPopulation<TreeIndividual> parents,
			IPopulation<TreeIndividual> childs) {
		ArrayList<Node> leaves = new ArrayList<>();
		for (TreeIndividual child : parents.getIndividuals()) {
			if (random.nextDouble() < mProb) {
				if (stumps == null) {
					createStumps(child.getRootNode());
				}

				Node root = child.getRootNode();
				Node nodeToMutate = null;
				
				int treeSize = 0;
				if (root instanceof SizeExtension) {
					treeSize = ((SizeExtension) root).getTreeSize();
					int nIndex = random.nextInt(treeSize);
					nodeToMutate = Utils.getExtensionNode(root, nIndex);
				} else {
					treeSize = Utils.computeSize(root);
					int nIndex = random.nextInt(treeSize);
					nodeToMutate = Utils.getNode(root, nIndex);
				}								
				
				int sIndex = random.nextInt(stumps.length);				
				Node nodeToMutateParent = nodeToMutate.getParent();

				if (nodeToMutateParent == null) {
					child.setRoot(stumps[sIndex].getRootNode().copy());
				} else {
					for (int i = 0; i < nodeToMutateParent.getChilds().length; i++) {
						if (nodeToMutateParent.getChildAt(i) == nodeToMutate) {
							nodeToMutateParent.setChildAt(i, stumps[sIndex]
									.getRootNode().copy());
							Utils.fixNode(nodeToMutateParent.getChildAt(i));
							break;
						}
					}
				}
				
				
				// change to recompute fitness
				child.change();
			}

			leaves.clear();
		}

		if (childs != null) {
			childs.addAll(parents);
		}
	}

	/**
	 * Method which creates the stumps with {@link WekaSimpleStumpGenerator}. It
	 * uses {@link InformationGainCriteria} as a split criterion.
	 * 
	 * @param node
	 *            that serves as a template for created stumps
	 */
	private void createStumps(Node node) {
		WekaSimpleStumpGenerator wekaSSGen = new WekaSimpleStumpGenerator();
		wekaSSGen.setInstances(data.toInstances());
		if (node instanceof MultiWayNode) {
			wekaSSGen.setAutoHeight(false);
		}

		if (node instanceof MultiWayHeightNode) {
			wekaSSGen.setAutoHeight(true);
		}

		try {
			this.stumps = wekaSSGen.createPopulation();
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new PopulationInitializationException(e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWekaCompatible() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWekaDependent() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<TreeIndividual> getIndividualClassType() {
		return TreeIndividual.class;
	}

	/**
	 * {@inheritDoc} </p> It does use all enums from {@link OperEnum}.
	 */
	public void setParam(String param) {
		this.mProb = 0;
		this.typeOfData = -1;

		if (param.equals(PermMessages._blank_param)) {
			return;
		}

		String[] parts = param.split(Utils.pDELIM);

		// some kind of exception if it's not divisible by two

		for (int i = 0; i < parts.length; i += 2) {
			OperEnum opEnum = OperEnum.value(parts[i]);

			if (opEnum == null) {
				LOG.log(Level.INFO,
						String.format(
								TextResource.getString(TextKeys.iExcessParam),
								parts[i]));
				continue;
			}

			switch (opEnum) {
			case PROB:
				this.mProb = Double.parseDouble(parts[i + 1]);
				break;
			case DATA:
				this.typeOfData = Integer.parseInt(parts[i + 1]);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * {@inheritDoc} </p> This operator does set the Data object but uses it
	 * only to create decision stumps from them.
	 */
	@Override
	public void setData(Data data) {
		this.data = data.getDataOfType(typeOfData);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String objectInfo() {
		String paramString = "";
		if (mProb >= 0 && mProb <= 1) {
			paramString = String.format(PermMessages._param_format,
					OperEnum.PROB, mProb);
		}
		if (typeOfData == 0 || typeOfData == 1) {
			if (paramString.isEmpty()) {
				paramString = String.format(PermMessages._param_format,
						OperEnum.DATA, typeOfData);
			} else {
				paramString = String.format(PermMessages._param_format,
						paramString, OperEnum.DATA + "," + typeOfData);
			}
		}
		if (paramString.isEmpty()) {
			return String.format(PermMessages._fit_format, initName,
					PermMessages._blank_param);
		}
		return String.format(PermMessages._fit_format, initName, paramString);
	}

	/**
	 * {@inheritDoc} <\p> This operator does not set the Random object.
	 */
	@Override
	public void setRandomGenerator(Random random) {
		this.random = random;
	}

}
