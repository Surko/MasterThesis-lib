package genlib.annotations;

import genlib.evolution.selectors.Selector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface EnvSelectAnnot {	
	String[] toInjectNames();
	Class<? extends Selector>[] toInjectClasses();
	String toInjectField();
	Class<?> toInjectClass();
}
