package genlib.evolution.individuals;

import genlib.evolution.fitness.FitnessFunction;
import genlib.structures.trees.Node;

/**
 * Class that extends TreeIndividual which has two new fields age and lifeSpan.
 * Those two fields specifiec how old is the individual.
 * 
 * @author Lukas Surin
 *
 */
public class TreeIndividualAging extends TreeIndividual {
	/** for serialization */
	private static final long serialVersionUID = 4387746146244032309L;
	/** age of the individual */
	private int age;
	/** lifespan of the individual */
	private int lifeSpan;

	/**
	 * Constructor with two booleans as its parameters. It creates new instance
	 * and set the root of it depending on those boolean parameters. It
	 * initialize the fitness array with the amount of
	 * {@link FitnessFunction#registeredFunctions}.
	 * 
	 * @param binary
	 *            if the node should be binary or multi way
	 * @param autoHeight
	 *            if the node should compute height automatically or not
	 */
	public TreeIndividualAging(boolean binary, boolean countDepth) {
		super(binary, countDepth);
	}

	/**
	 * Constructor with root as its parameter. It creates new instance and set
	 * the root of it. It initialize the fitness array with the amount of
	 * {@link FitnessFunction#registeredFunctions}.
	 * 
	 * @param root
	 *            root node to be set
	 */
	public TreeIndividualAging(Node root) {
		super(root);
	}

	/**
	 * Copy constructor
	 * 
	 * @param toCopy
	 *            instance
	 */
	public TreeIndividualAging(TreeIndividualAging toCopy) {
		super(toCopy);
		this.age = toCopy.age;
		this.lifeSpan = toCopy.lifeSpan;
	}

	/**
	 * Gets the age of the individual.
	 * 
	 * @return age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Gets the lifeSpan of the individual
	 * 
	 * @return lifeSpan
	 */
	public int getLifeSpan() {
		return lifeSpan;
	}

	/**
	 * Increase the age of the individual by one
	 */
	public void increaseAge() {
		this.age++;
	}

	/**
	 * Decrease the age of the individual by one
	 */
	public void decreaseLifeSpan() {
		this.lifeSpan--;
	}

	/**
	 * Tests if the individual is still alive (lifeSpan > 0)
	 * 
	 * @return true iff individual is still alive
	 */
	public boolean isAlive() {
		return lifeSpan > 0;
	}
}
