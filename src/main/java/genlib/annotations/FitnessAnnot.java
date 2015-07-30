package genlib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import genlib.evolution.fitness.FitnessFunction;

/**
 * Annotation definition for fitness functions. It must be used from
 * package.info and implement methods: <br>
 * <ul>
 * <li>toInjectNames - names of plugins to be injected</li>
 * <li>toInjectClasses - classes that represents new fitness functions</li>
 * <li>toInjectField - hashmap to which we inject classes</li>
 * <li>toInjectClass - class with hashmap to which we want to inject new
 * fitness functions</li>
 * </ul>
 * 
 * @author Lukas Surin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface FitnessAnnot {	
	/**
	 * Names of new fitness functions to be injected
	 * 
	 * @return array of strings with names
	 */
	String[] toInjectNames();
	
	/**
	 * Classes that represents new fitness functions
	 * 
	 * @return array of classes for fitness functions
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends FitnessFunction>[] toInjectClasses();

	/**
	 * Object/Hashmap to which we inject classes
	 * 
	 * @return name of the field
	 */
	String toInjectField();
	
	/**
	 * Class with hashmap to which we want to inject new fitness functions
	 * 
	 * @return class to which we inject fitness functions
	 */
	Class<?> toInjectClass();
}
