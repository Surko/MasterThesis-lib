package genlib.annotations;

import genlib.evolution.operators.Operator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation definition for crossover operators. It must be used from
 * package.info and implement methods: <br>
 * <ul>
 * <li>toInjectNames - names of plugins to be injected</li>
 * <li>toInjectClasses - classes that represents new crossover operators</li>
 * <li>toInjectField - hashmap to which we inject classes</li>
 * <li>toInjectClass - class with hashmap to which we want to inject new
 * crossover operators</li>
 * </ul>
 * 
 * @author Lukas Surin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface XOperatorAnnot {	
	/**
	 * Names of new crossover operators to be injected
	 * 
	 * @return array of strings with names
	 */
	String[] toInjectNames();
	
	/**
	 * Classes that represents new crossover operators
	 * 
	 * @return array of classes for mutation operators
	 */
	Class<? extends Operator<?>>[] toInjectClasses();
	
	/**
	 * Object/Hashmap to which we inject classes
	 * 
	 * @return name of the field
	 */
	String toInjectField();
	
	/**
	 * Class with hashmap to which we want to inject new crossover operators
	 * 
	 * @return class to which we inject crossover operators
	 */
	Class<?> toInjectClass();
}
