package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface HttpMultipartMarshalledPart {
	String globalId() default "";
	String value() default "";
	boolean ignoreEmptyValue() default true;
	String mimeType() default "application/json";
	String fileName() default "";
	String[] headers() default {};
}
