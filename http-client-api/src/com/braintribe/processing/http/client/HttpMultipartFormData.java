// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// ============================================================================
package com.braintribe.processing.http.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Description of a multipart form-data body attached to an {@link HttpRequestContext}. */
public class HttpMultipartFormData {

	private final String requestPartName;
	private final String requestPartMimeType;
	private final List<HttpMultipartPart> parts;

	public HttpMultipartFormData(String requestPartName, String requestPartMimeType, List<HttpMultipartPart> parts) {
		this.requestPartName = requestPartName;
		this.requestPartMimeType = requestPartMimeType;
		this.parts = Collections.unmodifiableList(new ArrayList<>(parts));
	}

	public String getRequestPartName() {
		return requestPartName;
	}

	public String getRequestPartMimeType() {
		return requestPartMimeType;
	}

	public List<HttpMultipartPart> getParts() {
		return parts;
	}
}
