package genlib.evolution.operators;

import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.population.IPopulation;
import genlib.structures.Data;

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
public interface Operator<T extends Individual> extends Serializable {	
	/** hashmap with crossover tree operators */
	public static final HashMap<String, Class<Operator<TreeIndividual>>> tXOper = new HashMap<>();
	/** hashmap with mutation tree operators */
	public static final HashMap<String, Class<Operator<TreeIndividual>>> tMOper = new HashMap<>();

	/**
	 * Method should return the tag of compatibility with weka for operator.
	 * 
	 * @return true iff operator is compatible with weka
	 */
	public boolean isWekaCompatible();

	/**
	 * Method should return the tag of dependency on weka for operator.
	 * 
	 * @return true iff operator is dependent on weka
	 */
	public boolean isWekaDependent();

	/**
	 * Method should set the probability of this operator
	 * 
	 * @param prob
	 *            probability of this operator
	 */
	public void setOperatorProbability(double prob);

	/**
	 * Method should get the probability of this operator
	 * 
	 * @return probability of this operator
	 */
	public double getOperatorProbability();

	/**
	 * Method which should set the random generator for this operator.
	 * 
	 * @param random Random object 
	 */
	public void setRandomGenerator(Random random);

	/**
	 * Method should set the data for operator.
	 * @param data to be set (GenLibInstances or Instances)
	 */
	public void setData(Data data);
	
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
	public void execute(IPopulation<T> parents, IPopulation<T> childs);

	/**
	 * Method which should return the string representation of this object (info
	 * about this object). It should be identical to that inside config file.
	 * 
	 * @return info about operator
	 */
	public String objectInfo();

}
