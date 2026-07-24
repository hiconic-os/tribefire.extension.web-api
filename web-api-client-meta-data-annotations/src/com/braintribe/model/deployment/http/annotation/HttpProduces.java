package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(HttpProduces.List.class)
public @interface HttpProduces {
	String globalId() default "";
	String mimeType() default "application/json";
	int responseCode() default 200;
	String responseType() default "object";
	boolean useOriginalStatusCode() default false;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	@interface List { HttpProduces[] value(); }
}
