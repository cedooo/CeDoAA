package com.cedoo.springcloud;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 手动配置注解
 * 自动配置位置：spring.factories
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({SwaggerAutoConfiguration.class})
@Documented
public @interface EnableApiDoc {
}
