package com.braintribe.model.deployment.http.annotation;
import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD, ElementType.FIELD}) @Documented
public @interface HttpPathParam {
	String globalId() default "";
	String value() default "";
	/** The placeholder must occupy a complete path segment. */
	boolean omitSegmentIfNull() default false;
}
