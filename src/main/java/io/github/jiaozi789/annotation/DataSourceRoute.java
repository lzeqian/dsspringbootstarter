package io.github.jiaozi789.annotation;

import java.lang.annotation.*;

/**
 * @author 廖敏
 * 创建日期： 2019-03-01 14:46
 **/
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceRoute {
    String value() default "";
    boolean write() default false;
    boolean read() default true;
}
