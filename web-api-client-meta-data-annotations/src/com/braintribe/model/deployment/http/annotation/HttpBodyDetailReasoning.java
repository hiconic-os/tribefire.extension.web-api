package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.braintribe.model.generic.GenericEntity;

/** Decodes a matching HTTP error body as structured evidence on the selected HTTP status reason. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(HttpBodyDetailReasoning.List.class)
public @interface HttpBodyDetailReasoning {
	String globalId() default "";
	String status();
	/** Legacy name retained for compatibility; this is the entity type expected in the response body. */
	Class<? extends GenericEntity> reasonType();
	String mimeType() default "";

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	@interface List { HttpBodyDetailReasoning[] value(); }
}
