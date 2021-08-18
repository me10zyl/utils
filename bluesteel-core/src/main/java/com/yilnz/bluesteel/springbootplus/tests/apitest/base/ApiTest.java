package com.yilnz.bluesteel.springbootplus.tests.apitest.base;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * api测试
 *
 * @author zyl
 * @date 2021/06/08
 * @return
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ApiTest {
    String controllerUrl() default "";
    Class entityClass() default Object.class;
    String baseUrl() default "";
    String dbName() default "";
}
