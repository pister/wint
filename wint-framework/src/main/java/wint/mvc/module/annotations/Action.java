package wint.mvc.module.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Action标记后某个方法后，这个就是web的action目标
 * @author pister 2012-2-26 05:59:56
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {
	
	/**
	 * defaultTarget 设置默认目标
	 * @return
	 */
	String defaultTarget() default "";

}
