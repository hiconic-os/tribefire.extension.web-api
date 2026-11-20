package com.braintribe.model.deployment.http.annotation;

import java.lang.annotation.*;
import com.braintribe.model.generic.annotation.meta.AnnotationDefaults;
import com.braintribe.model.generic.annotation.meta.NullDefault;

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

	/** Optional DateTimeFormatter pattern for this parameter, independent of JSON body formatting. */
	@NullDefault
	String dateFormat() default AnnotationDefaults.NULL_STRING;
	/** Zone used for date encoding; requires dateFormat. */
	@NullDefault
	String dateZone() default AnnotationDefaults.NULL_STRING;
}
