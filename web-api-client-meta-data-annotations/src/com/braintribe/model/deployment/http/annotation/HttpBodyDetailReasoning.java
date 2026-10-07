package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.braintribe.gm.model.reason.Reason;

/** Decodes a matching HTTP error body as a structured detail cause beneath the selected root reason. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(HttpBodyDetailReasoning.List.class)
public @interface HttpBodyDetailReasoning {
	String globalId() default "";
	String status();
	Class<? extends Reason> reasonType();
	String mimeType() default "";

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	@interface List { HttpBodyDetailReasoning[] value(); }
}
