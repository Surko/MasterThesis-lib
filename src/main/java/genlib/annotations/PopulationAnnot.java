package genlib.annotations;

import genlib.evolution.population.IPopulation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PopulationAnnot {
	boolean[] wekaCompatibility();
	String[] toInjectNames();
	Class<? extends IPopulation<?>>[] toInjectClasses();
	String toInjectField();
	Class<?> toInjectClass();
}
