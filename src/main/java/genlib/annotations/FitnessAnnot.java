package genlib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import genlib.evolution.fitness.FitnessFunction;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface FitnessAnnot {	
	String[] toInjectNames();
	@SuppressWarnings("rawtypes")
	Class<? extends FitnessFunction>[] toInjectClasses();
	String toInjectField();
	Class<?> toInjectClass();
}
