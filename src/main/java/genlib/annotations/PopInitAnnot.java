package genlib.annotations;

import genlib.initializators.PopulationInitializator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PopInitAnnot {
	boolean[] wekaCompatibility();
	String[] toInjectNames();
	Class<? extends PopulationInitializator<?>>[] toInjectClasses();
	String toInjectField();
	Class<?> toInjectClass();
}
