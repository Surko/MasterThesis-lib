package genlib.annotations;

import genlib.evolution.selectors.Selector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation definition for selectors. It must be used from
 * package.info and implement methods: <br>
 * <ul>
 * <li>toInjectNames - names of plugins to be injected</li>
 * <li>toInjectClasses - classes that represents new selectors</li>
 * <li>toInjectField - hashmap to which we inject classes</li>
 * <li>toInjectClass - class with hashmap to which we want to inject new
 * selectors</li>
 * </ul>
 * 
 * @author Lukas Surin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface MateSelectAnnot {	
	/**
	 * Names of new selectors to be injected
	 * 
	 * @return array of strings with names
	 */
	String[] toInjectNames();
	
	/**
	 * Classes that represents new selectors
	 * 
	 * @return array of classes for selectors
	 */
	Class<? extends Selector>[] toInjectClasses();
	
	/**
	 * Object/Hashmap to which we inject classes
	 * 
	 * @return name of the field
	 */
	String toInjectField();
	
	/**
	 * Class with hashmap to which we want to inject new selectors
	 * 
	 * @return class to which we inject selectors
	 */
	Class<?> toInjectClass();
}
