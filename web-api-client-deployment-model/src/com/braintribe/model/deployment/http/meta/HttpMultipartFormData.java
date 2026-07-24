// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// ============================================================================
package com.braintribe.model.deployment.http.meta;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.EntityTypeMetaData;

/**
 * Maps a request to {@code multipart/form-data}. The regular request payload is marshalled into the named request part while properties
 * mapped with a concrete {@link com.braintribe.model.deployment.http.meta.params.HttpMultipartPart} are emitted as separate parts.
 */
public interface HttpMultipartFormData extends EntityTypeMetaData {

	EntityType<HttpMultipartFormData> T = EntityTypes.T(HttpMultipartFormData.class);

	/** Optional name of the part containing the marshalled remaining request payload. If blank, only explicitly mapped property parts are sent. */
	String getRequestPartName();
	void setRequestPartName(String requestPartName);

	/** MIME type used to choose the marshaller and as Content-Type of the request part. */
	@Initializer("'application/json'")
	String getRequestPartMimeType();
	void setRequestPartMimeType(String requestPartMimeType);
}
