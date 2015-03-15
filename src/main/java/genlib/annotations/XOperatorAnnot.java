package genlib.annotations;

import genlib.evolution.operators.Operator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface XOperatorAnnot {	
	String[] toInjectNames();
	Class<? extends Operator<?>>[] toInjectClasses();
	String toInjectField();
	Class<?> toInjectClass();
}
