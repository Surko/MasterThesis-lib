package genlib.annotations;

import genlib.generators.Generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation definition for individual generators. It must be used from
 * package.info and implement methods: <br>
 * <ul>
 * <li>toInjectNames - names of plugins to be injected</li>
 * <li>toInjectClasses - classes that represents new individual generators</li>
 * <li>toInjectField - hashmap to which we inject classes</li>
 * <li>toInjectClass - class with hashmap to which we want to inject new
 * individual generators</li>
 * </ul>
 * 
 * @author Lukas Surin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface GenAnnot {
	/**
	 * Booleans to decide if the generator is compatible with weka
	 * 
	 * @return array of booleans
	 */
	boolean[] wekaCompatibility();

	/**
	 * Names of new individual generators to be injected
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
	Class<? extends Generator>[] toInjectClasses();

	/**
	 * Object/Hashmap to which we inject classes
	 * 
	 * @return name of the field
	 */
	String toInjectField();

	/**
	 * Class with hashmap to which we want to inject new environmental selectors
	 * 
	 * @return class to which we inject environmental selectors
	 */
	Class<?> toInjectClass();
}
