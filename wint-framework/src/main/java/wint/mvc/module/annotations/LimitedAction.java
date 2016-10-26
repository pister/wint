package wint.mvc.module.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 限制action访问。
 * 一旦某个Action上增加了这个注解，并设置了allowSuffix，那么这个action只能被这些suffix访问
 *
 * User: huangsongli
 * Date: 16/10/26
 * Time: 下午7:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LimitedAction {

    String[] allowSuffix();

}
