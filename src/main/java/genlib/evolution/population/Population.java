package genlib.evolution.population;

import genlib.configurations.Config;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.operators.Operator;
import genlib.evolution.selectors.Selector;
import genlib.exceptions.EmptyConfigParamException;
import genlib.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Population<T extends Individual> implements Serializable, IPopulation<T> {

	/** for serialization */
	private static final long serialVersionUID = -8405304400658666989L;
	/** name of this population */
	public static final String initName = "typical";
	private FitnessComparator<T> comparator;
	private ArrayList<T> individuals;
	private int maxPopSize, actualSize;
	private Random randomGen;		

	public Population() {
		this.individuals = new ArrayList<>();
		this.maxPopSize = Config.getInstance().getPopulationSize();
		this.actualSize = 0;
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}

	@SuppressWarnings("unchecked")
	public Population(Population<T> population) {
		this.comparator = population.comparator;
		this.maxPopSize = population.maxPopSize;		
		this.individuals = new ArrayList<>();
		for (T ind : population.getIndividuals()) {
			individuals.add((T)ind.copy());
		}
		this.actualSize = individuals.size();
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}

	public Population(ArrayList<T> individuals) {
		this.individuals = individuals;
		this.maxPopSize = Config.getInstance().getPopulationSize();
		this.actualSize = individuals.size();
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}

	public Population(ArrayList<T> individuals, int maxPopSize) {
		this.individuals = individuals;
		this.actualSize = individuals.size();
		this.maxPopSize = maxPopSize;
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}

	public Population(T[] individuals, int maxPopSize) {
		this.individuals = new ArrayList<>(Arrays.asList(individuals));
		this.actualSize = individuals.length;
		this.maxPopSize = maxPopSize;
		this.randomGen = new Random(Utils.randomGen.nextLong());
	}	

	@Override
	public <S extends Individual> IPopulation<S> makeNewInstance() {
		return new Population<S>();
	}
	
	public Population<T> createNewInstance() {
		return new Population<T>();
	}
	
	/**
	 * {@inheritDoc}
	 * </p>
	 * This implementation leaves the population alone.  
	 */
	public void update() {}
	
	/**
	 * Method which clears individuals arraylist.
	 */
	@Override
	public void clear() {
		this.actualSize = 0;
		this.individuals.clear();
	}

	@Override
	public void add(T individual) {		
		individuals.add(individual);
		actualSize++;
	}

	/**
	 * 
	 * @param population
	 */
	@Override
	public void addAll(IPopulation<T> population) {
		individuals.addAll(population.getIndividuals());
		actualSize = individuals.size();
	}
	
	/**
	 * Deep copy of population from parameter into this population.
	 * @param population Population from which we copy individuals
	 */
	@SuppressWarnings("unchecked")
	public void deepCopy(IPopulation<T> population) {
		for (T ind : population.getIndividuals()) {
			individuals.add((T)ind.copy());
		}
		actualSize = individuals.size();
	}

	@Override
	public void resample() {
		Collections.shuffle(individuals, randomGen);
	}

	/**
	 * Sort individuals inside this object.
	 */
	@Override
	public void sortIndividuals() {
		// TODO WE ARE AFTER DESCDENDING ORDER INSTEAD OF ASCENDING.
		Collections.sort(individuals, comparator);
	}

	/**
	 * Getting sorted individuals without change of placing in the object field
	 * individuals. Sorting is done by default comparator provided by
	 * individuals.
	 * 
	 * @return Sorted individuals
	 */
	public ArrayList<T> getSortedIndividuals() {
		ArrayList<T> sorted = new ArrayList<>(individuals);
		Collections.sort(sorted, comparator);
		return sorted;
	}

	/**
	 * Getting sorted individuals without change of placing in the object field
	 * individuals. Sorting is done by comparator provided by parameter
	 * comparator
	 * 
	 * @param comparator
	 *            Comparator that sorts individuals
	 * @return Sorted individuals
	 */
	public ArrayList<T> getSortedIndividuals(Comparator<T> comparator) {
		ArrayList<T> sorted = new ArrayList<>(individuals);
		Collections.sort(sorted, comparator);
		return sorted;
	}		
	
	public void computeFitness(final int nThreads, final int blockSize) {
		if (nThreads > 1) {
			ExecutorService es = Executors.newFixedThreadPool(nThreads);
			
			if (blockSize == 1) {
				// execution of individual one by one
				for (final FitnessFunction<T> function : comparator
						.getFitnessFuncs()) {
					for (final T individual : individuals) {
						es.submit(new Runnable() {
							@Override
							public void run() {
								function.computeFitness(individual);
							}
						});
					}
				}
			} else {
				// block execution of individuals
				for (final FitnessFunction<T> function : comparator
						.getFitnessFuncs()) {
					for (int i = 0; i < actualSize; i+=blockSize) {
						final int start = i;
						es.submit(new Runnable() {
							@Override
							public void run() {
								function.computeFitness(individuals, start, start+blockSize);
							}
						});
					}
				}
			}

			es.shutdown();

			try {
				es.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				
			}
		} else {
			// in this case block execution is worthless
			for (FitnessFunction<T> function : comparator.getFitnessFuncs()) {
				function.computeFitness(this);
			}
		}

		// unchange individuals, because all of the fitness functions has been computed.
		// next call of this method will be really fast 
  		// for (final T individual : individuals) {
  		//	individual.unchange();
  		// }
	}

	@Override
	public T getIndividual(int index) {
		return individuals.get(index);
	}

	@Override
	public T getBestIndividual() {
		return getSortedIndividuals().get(0);
	}

	@Override
	public int getActualPopSize() {
		return actualSize;
	}
	
	@Override
	public int getMaxPopSize() {
		return maxPopSize;
	}

	@Override
	public ArrayList<T> getIndividuals() {
		return individuals;
	}

	@Override
	public FitnessComparator<T> getFitnessComparator() {
		return comparator;
	}

	@Override
	public void setFitnessComparator(FitnessComparator<T> fitComp) {
		this.comparator = fitComp;
	}

	/* SELECTIONPHASE METHODS*/

	@Override	
	public IPopulation<T> selectionPhase(ArrayList<Selector> selectors) {
		return selectionPhase(null, selectors);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public IPopulation<T> selectionPhase(IPopulation<T> selected, ArrayList<Selector> selectors) {
		if (selected == null) {
			selected = createNewInstance();
		}
				
		selected.setFitnessComparator(comparator);
		if (selectors.size() > 0) {
			int selSize = selectors.size();
			int toSel = maxPopSize / selSize;

			for (int i = 0; i < selSize; i++) {				
				IPopulation<T> toAdd = selectors.get(i).select(
						this, toSel);
				selected.addAll(toAdd);
			}

			// consider the case when selectors did not create enough mates => adding the rest.
			int missing = maxPopSize - selected.getActualPopSize();

			if (missing > 0) {
				IPopulation<T> toFill = selectors.get(selSize - 1).select(
						this, missing);
				selected.addAll(toFill);
			}
		} else {
			selected = new Population<>(this);
			selected.resample();
		}

		return selected;
	}

	/* OPERATORPHASE METHODS*/

	/**
	 * 
	 * @param mates
	 * @return
	 */
	@Override
	public IPopulation<T> operatorPhaseMates(ArrayList<Operator<T>> crossOperators,
			ArrayList<Operator<T>> mutationOperators) {
		return operatorPhaseMates(null, crossOperators, mutationOperators);
	}

	@Override
	public IPopulation<T> operatorPhaseMates(IPopulation<T> offspring, ArrayList<Operator<T>> crossOperators,
			ArrayList<Operator<T>> mutationOperators) {
		if (offspring == null) {
			offspring = createNewInstance();
		}		
		
		offspring.setFitnessComparator(comparator);
		IPopulation<T> parents = this;
		
		for (Operator<T> o : crossOperators) {			
			o.execute(parents, offspring);
			parents = offspring;			
		}

		for (Operator<T> o : mutationOperators) {
			o.execute(offspring, null);
		}

		return offspring;
	}

//	/**
//	 * 
//	 * @param mates
//	 * @return
//	 */	
//	public Population<T> operatorPhaseWithChilds(ArrayList<Operator<T>> crossOperators,
//			ArrayList<Operator<T>> mutationOperators) {				
//		Population<T> offspring = null;
//		for (Operator<T> o : crossOperators) {
//			offspring = new Population<T>();
//			offspring.setFitnessComparator(comparator);
//			o.execute(this, offspring);
//			this.individuals = offspring.getIndividuals();
//		}
//
//		for (Operator<T> o : mutationOperators) {
//			offspring = new Population<T>();
//			offspring.setFitnessComparator(comparator);
//			o.execute(this, offspring);
//			this.individuals = offspring.getIndividuals();
//		}
//
//		return this;
//	}

	/* ELITEPHASE METHODS*/ 

	/**
	 * 
	 * @return
	 */
	@Override
	public Population<T> elitePhase(double elitismRate) {
		return elitePhase(elitismRate);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public IPopulation<T> elitePhase(IPopulation<T> elite, double elitismRate) {
		if (elite == null) {
			elite = createNewInstance();
		}
		
		elite.setFitnessComparator(comparator);
		
		for (int i = 0; i < elitismRate * actualSize; i++) {
			elite.add(individuals.get(i));
		}
		return elite;
	}

	/* ENVSELECTIONPHASE METHODS */

	/**
	 * Environmental selection phase method without parameters. This one chooses/selects new individuals from
	 * actualPopulation. It does not fill existing population (that's why there's null parameter
	 * inside called method) so it has to create new one which is returned.
	 * @return Newly created population from selected individuals
	 */	
	@Override
	public IPopulation<T> envSelectionPhase(ArrayList<Selector> envSelectors) {
		return envSelectionPhase(null, envSelectors);
	}	

	/**
	 * Environmental selection phase method with two provided parameters. It chooses/selects new individuals from
	 * first parameter. If the second population parameter is not null then it does fill it
	 * and returns. Returning is here just for the sake of other sibling methods envSelectionPhase which 
	 * use this feature.
	 * @param population Parameter from which we select new individuals
	 * @param envSelected Population parameter to which we add selected individuals
	 * @return Newly created or already existing population with selected individuals.
	 */
	@Override
	public IPopulation<T> envSelectionPhase(IPopulation<T> envSelected, ArrayList<Selector> envSelectors) {
		if (envSelected == null) {
			envSelected = createNewInstance();
		}
		
		envSelected.setFitnessComparator(comparator);

		if (envSelectors.size() > 0) {
			int envSize = envSelectors.size();
			int toSel = (maxPopSize - envSelected.getActualPopSize()) / envSize;

			for (int i = 0; i < envSize; i++) {				
				IPopulation<T> toAdd = envSelectors.get(i).select(
						this, toSel);
				envSelected.addAll(toAdd);				
			}

			// consider the case when selectors did not create enough mates => adding the rest.
			int missing = maxPopSize - envSelected.getActualPopSize();

			if (missing > 0) {
				IPopulation<T> toFill = envSelectors.get(envSize - 1).select(
						this, missing);
				envSelected.addAll(toFill);
			}		
		} else {
			// should not get in here
			throw new EmptyConfigParamException();
		}
		return envSelected;
	}
	
}
