package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.*;

import com.braintribe.model.generic.annotation.meta.AnnotationDefaults;
import com.braintribe.model.generic.annotation.meta.NullDefault;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface HttpDateFormatting {
	String globalId() default "";
	String dateFormat();
	@NullDefault
	String defaultZone() default AnnotationDefaults.NULL_STRING;
	@NullDefault
	String defaultLocale() default AnnotationDefaults.NULL_STRING;
}
