package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.*;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.meta.NullDefault;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(HttpProduces.List.class)
public @interface HttpProduces {
	String globalId() default "";
	String mimeType() default "application/json";
	int responseCode() default 200;
	@NullDefault
	Class<? extends GenericEntity> responseType() default GenericEntity.class;
	boolean useOriginalStatusCode() default false;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	@interface List { HttpProduces[] value(); }
}
