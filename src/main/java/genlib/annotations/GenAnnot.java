package genlib.annotations;

import genlib.generators.Generator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface GenAnnot {
	boolean[] wekaCompatibility();
	String[] toInjectNames();
	Class<? extends Generator>[] toInjectClasses();
	String toInjectField();
	Class<?> toInjectClass();
}
