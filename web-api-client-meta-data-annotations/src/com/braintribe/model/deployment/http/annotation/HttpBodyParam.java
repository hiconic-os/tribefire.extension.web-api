package com.braintribe.model.deployment.http.annotation;
import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD, ElementType.FIELD}) @Documented
public @interface HttpBodyParam {
	String globalId() default "";
	String value() default "";
	boolean ignoreEmptyValue() default false;
}
