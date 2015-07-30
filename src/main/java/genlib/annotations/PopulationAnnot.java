package genlib.annotations;

import genlib.evolution.population.IPopulation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation definition for population containers. It must be used from
 * package.info and implement methods: <br>
 * <ul>
 * <li>toInjectNames - names of plugins to be injected</li>
 * <li>toInjectClasses - classes that represents new population containers</li>
 * <li>toInjectField - hashmap to which we inject classes</li>
 * <li>toInjectClass - class with hashmap to which we want to inject new
 * population containers</li>
 * </ul>
 * 
 * @author Lukas Surin
 *
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PopulationAnnot {
	/**
	 * Booleans to decide if the population containers are compatible with weka
	 * 
	 * @return array of booleans
	 */
	boolean[] wekaCompatibility();
	
	/**
	 * Names of new population containers to be injected
	 * 
	 * @return array of strings with names
	 */
	String[] toInjectNames();
	
	/**
	 * Classes that represents new population containers
	 * 
	 * @return array of classes for population containers
	 */
	Class<? extends IPopulation<?>>[] toInjectClasses();
	
	/**
	 * Object/Hashmap to which we inject classes
	 * 
	 * @return name of the field
	 */
	String toInjectField();
	
	/**
	 * Class with hashmap to which we want to inject new population containers
	 * 
	 * @return class to which we inject population containers
	 */
	Class<?> toInjectClass();
}
