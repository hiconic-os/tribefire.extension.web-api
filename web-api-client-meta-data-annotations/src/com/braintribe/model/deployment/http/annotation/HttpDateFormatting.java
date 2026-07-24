package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface HttpDateFormatting {
	String globalId() default "";
	String dateFormat();
	String defaultZone() default "";
	String defaultLocale() default "";
}
