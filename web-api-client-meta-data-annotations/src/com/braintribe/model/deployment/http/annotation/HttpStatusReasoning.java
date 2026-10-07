package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.braintribe.gm.model.reason.Reason;

/** Maps matching HTTP statuses to a configured logical reason type. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(HttpStatusReasoning.List.class)
public @interface HttpStatusReasoning {
	String globalId() default "";
	String status();
	Class<? extends Reason> reasonType();
	boolean useOriginalStatusCode() default false;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	@interface List { HttpStatusReasoning[] value(); }
}
