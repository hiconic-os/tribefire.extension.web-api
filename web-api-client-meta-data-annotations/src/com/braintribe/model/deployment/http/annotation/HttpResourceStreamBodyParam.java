package com.braintribe.model.deployment.http.annotation;
import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD, ElementType.FIELD}) @Documented
public @interface HttpResourceStreamBodyParam {
	String globalId() default "";
	String value() default "";
	boolean ignoreEmptyValue() default false;
}
