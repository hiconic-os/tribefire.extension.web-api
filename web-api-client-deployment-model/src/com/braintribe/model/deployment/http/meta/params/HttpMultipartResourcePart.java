// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// ============================================================================
package com.braintribe.model.deployment.http.meta.params;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Maps a {@code Resource}, {@code List<Resource>} or {@code Set<Resource>} property to binary multipart form-data parts. Collections are
 * represented by repeated parts with the same name.
 */
public interface HttpMultipartResourcePart extends HttpMultipartPart {

	EntityType<HttpMultipartResourcePart> T = EntityTypes.T(HttpMultipartResourcePart.class);
}
