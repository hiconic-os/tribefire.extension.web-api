package com.braintribe.model.deployment.http.annotation;
import java.lang.annotation.*;
import com.braintribe.model.generic.annotation.meta.AnnotationDefaults;
import com.braintribe.model.generic.annotation.meta.NullDefault;
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD, ElementType.FIELD}) @Documented
public @interface HttpPathParam {
	String globalId() default "";
	String value() default "";
	/** The placeholder must occupy a complete path segment. */
	boolean omitSegmentIfNull() default false;

	/** Optional DateTimeFormatter pattern for this parameter, independent of JSON body formatting. */
	@NullDefault
	String dateFormat() default AnnotationDefaults.NULL_STRING;
	/** Zone used for date encoding; requires dateFormat. */
	@NullDefault
	String dateZone() default AnnotationDefaults.NULL_STRING;
}
