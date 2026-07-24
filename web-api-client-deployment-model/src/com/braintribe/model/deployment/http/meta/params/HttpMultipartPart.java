// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// ============================================================================
package com.braintribe.model.deployment.http.meta.params;

import java.util.List;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/** Common configuration for a property mapped to its own multipart part. */
@Abstract
public interface HttpMultipartPart extends HttpBodyParam {

	EntityType<HttpMultipartPart> T = EntityTypes.T(HttpMultipartPart.class);

	/** Optional Content-Type. Concrete part types define their respective fallback. */
	String getMimeType();
	void setMimeType(String mimeType);

	/** Optional filename for the Content-Disposition header. */
	String getFileName();
	void setFileName(String fileName);

	/** Additional part headers in {@code Name: value} form. */
	List<String> getHeaders();
	void setHeaders(List<String> headers);
}
