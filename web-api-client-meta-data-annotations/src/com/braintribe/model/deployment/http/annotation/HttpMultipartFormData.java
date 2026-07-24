package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface HttpMultipartFormData {
	String globalId() default "";
	/** Optional name of the part containing the remaining marshalled request. */
	String value() default "";
	String requestPartMimeType() default "application/json";
}
