package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.braintribe.gm.model.reason.Reason;

/** Decodes a matching HTTP response body as the authoritative top-level reason. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(HttpBodyReasoning.List.class)
public @interface HttpBodyReasoning {
	String globalId() default "";
	String status();
	Class<? extends Reason> reasonType();
	String mimeType() default "";
	boolean useOriginalStatusCode() default false;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	@interface List { HttpBodyReasoning[] value(); }
}
