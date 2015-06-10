package genlib.evolution.operators;

import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

/**
 * Operator class serves as a type definition for different types of operators
 * (crossover or mutation). It has the main execute method which should apply
 * the operator on parens population and make the child population. </p> There
 * are few instructions when implementing
 * {@link #execute(IPopulation, IPopulation)} method for different types of
 * operators :
 * <ul>
 * <li>CrossOver - should apply the crossover with some probability and add the
 * newly created childs into childs array. If crossover isn't applied than the
 * parents are simply copied into childs array.</li>
 * <li>Mutation - should apply the mutation with some probability on parent.
 * Defaultly it does not add the changed parent into childs (when the childs
 * parameter is null).</li>
 * </ul </p> Other must be implemented methods are {@link #isWekaCompatible()}
 * and {@link #isWekaDependent()} that chooses if the operator can be used with
 * weka. The initialization uses these methods.
 * 
 * @author Lukas Surin
 *
 */
public abstract class Operator<T extends Individual> implements Serializable {
	/** for serialization */
	private static final long serialVersionUID = -2281373501886300299L;
	/** hashmap with crossover tree operators */
	public static final HashMap<String, Class<Operator<TreeIndividual>>> tXOper = new HashMap<>();
	/** hashmap with mutation tree operators */
	public static final HashMap<String, Class<Operator<TreeIndividual>>> tMOper = new HashMap<>();

	/**
	 * Method should return the tag of compatibility with weka for operator.
	 * 
	 * @return true iff operator is compatible with weka
	 */
	public abstract boolean isWekaCompatible();

	/**
	 * Method should return the tag of dependency on weka for operator.
	 * 
	 * @return true iff operator is dependent on weka
	 */
	public abstract boolean isWekaDependent();

	/**
	 * Method should set the probability of this operator
	 * 
	 * @param prob
	 *            probability of this operator
	 */
	public abstract void setOperatorProbability(double prob);

	/**
	 * Method should get the probability of this operator
	 * 
	 * @return probability of this operator
	 */
	public abstract double getOperatorProbability();

	/**
	 * Method which should set the random generator for this operator.
	 * 
	 * @param random Random object 
	 */
	public abstract void setRandomGenerator(Random random);

	/**
	 * Method which should apply the operator on parents population and add the
	 * newly created individuals into childs array. If the childs array is null
	 * then it should change the parents population.
	 * 
	 * @param parents
	 *            population of parents
	 * @param childs
	 *            population of created childs
	 */
	public abstract void execute(IPopulation<T> parents, IPopulation<T> childs);

	/**
	 * Method which should return the string representation of this object (info
	 * about this object). It should be identical to that inside config file.
	 * 
	 * @return info about operator
	 */
	public abstract String objectInfo();

}
