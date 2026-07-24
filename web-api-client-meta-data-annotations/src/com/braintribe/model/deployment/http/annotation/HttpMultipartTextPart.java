package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface HttpMultipartTextPart {
	String globalId() default "";
	String value() default "";
	boolean ignoreEmptyValue() default true;
	String mimeType() default "text/plain; charset=UTF-8";
	String fileName() default "";
	String[] headers() default {};
}
