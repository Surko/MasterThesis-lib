package genlib.plugins;

import genlib.evolution.individuals.Individual;
import genlib.evolution.operators.Operator;

import java.util.HashMap;

public abstract class OperatorPlugin implements
		Plugin<Class<? extends Operator<? extends Individual>>> {

	public enum OperatorEnum {
		MUTATION, CROSSOVER
	}

	/** oper enum with which we distinct what kind of operator we load */
	public OperatorEnum operEnum;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, Class<? extends Operator<? extends Individual>>> getStorage() {
		if (operEnum == null) {
			throw new NullPointerException();
		}

		switch (operEnum) {
		case CROSSOVER:
			return PluginManager.xOper;
		case MUTATION:
			return PluginManager.mutOper;
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPlugin(String key,
			Class<? extends Operator<? extends Individual>> pluginClass) {
		if (operEnum == null) {
			throw new NullPointerException();
		}

		switch (operEnum) {
		case CROSSOVER:
			PluginManager.xOper.put(key, pluginClass);
		case MUTATION:
			PluginManager.mutOper.put(key, pluginClass);
			;
			break;
		}
	}

	/**
	 * Method that is called from plugin manager which initializes the
	 * operators. It is done by calling addPlugin method.
	 */
	public abstract void initOperators();
}
