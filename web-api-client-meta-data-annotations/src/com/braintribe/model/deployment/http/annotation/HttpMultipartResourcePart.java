package com.braintribe.model.deployment.http.annotation;
import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD, ElementType.FIELD}) @Documented
public @interface HttpMultipartResourcePart {
	String globalId() default "";
	/** Part name; defaults to the property name when empty. */
	String value() default "";
	boolean ignoreEmptyValue() default true;
	/** Overrides the Resource MIME type when set. */
	String mimeType() default "";
	/** Overrides the Resource name when set. */
	String fileName() default "";
	/** Additional part headers in {@code Name: value} form. */
	String[] headers() default {};
}
