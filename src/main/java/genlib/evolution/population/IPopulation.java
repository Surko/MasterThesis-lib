package genlib.evolution.population;

import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.operators.Operator;
import genlib.evolution.selectors.Selector;

import java.util.ArrayList;
import java.util.HashMap;

public interface IPopulation<T extends Individual> {

	@SuppressWarnings("rawtypes")
	public static final HashMap<String, Class<? extends IPopulation>> populationTypes = new HashMap<>();	
	
	public <S extends Individual> IPopulation<S> makeNewInstance();
	
	public IPopulation<T> createNewInstance();

	/**
	 * Method which updates population. Usually it's not used, but it can
	 * possibly remove old individuals of change the max population in some
	 * cases of variable population.
	 */
	public void update();
	public void clear();
	public void add(T individual);
	public void addAll(IPopulation<T> population);
	public void deepCopy(IPopulation<T> population);
	public void setFitnessComparator(FitnessComparator<T> fitComp);
	public void setMaxPopulationSize(int maxPopSize);
	public FitnessComparator<T> getFitnessComparator();
	public ArrayList<T> getIndividuals();
	public T getBestIndividual();
	public int getActualPopSize();
	public int getMaxPopSize();
	public T getIndividual(int index);

	/**
	 * Method which resamples population individuals.
	 */
	public void resample();

	public void computeFitness(final int nThreads, final int blockSize);

	public void sortIndividuals();

	public IPopulation<T> selectionPhase(ArrayList<Selector> selectors);

	public IPopulation<T> selectionPhase(IPopulation<T> population,
			ArrayList<Selector> selectors);

	public IPopulation<T> operatorPhaseMates(
			ArrayList<Operator<T>> crossOperators,
			ArrayList<Operator<T>> mutationOperators);

	public IPopulation<T> operatorPhaseMates(IPopulation<T> offspring,
			ArrayList<Operator<T>> crossOperators,
			ArrayList<Operator<T>> mutationOperators);

	// public Population<T> operatorPhaseWithChilds(
	// ArrayList<Operator<T>> crossOperators,
	// ArrayList<Operator<T>> mutationOperators);

	public IPopulation<T> elitePhase(double elitismRate);

	public IPopulation<T> elitePhase(IPopulation<T> elite, double elitismRate);

	public IPopulation<T> envSelectionPhase(ArrayList<Selector> envSelectors);

	public IPopulation<T> envSelectionPhase(IPopulation<T> envSelected,
			ArrayList<Selector> envSelectors);
}
