package com.braintribe.model.deployment.http.annotation;
import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @Documented
public @interface HttpPost { String globalId() default ""; }
