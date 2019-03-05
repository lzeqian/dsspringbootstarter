package io.github.jiaozi789.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import io.github.jiaozi789.config.RouteDataSourceAutoConfiguration;

/**
 * @author 廖敏
 * 创建日期： 2019-03-01 14:46
 **/
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RouteDataSourceAutoConfiguration.class)
public @interface EnableDataSourceRoute {
}
