// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// ============================================================================
package com.braintribe.processing.http.client;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** A streamed part of a multipart form-data request. The content is interpreted by the concrete HTTP client. */
public class HttpMultipartPart {

	private final String name;
	private final String fileName;
	private final String mimeType;
	private final Object content;
	private final HttpMultipartPartKind kind;
	private final Map<String, String> headers;

	public HttpMultipartPart(String name, String fileName, String mimeType, Object content) {
		this(name, fileName, mimeType, content, HttpMultipartPartKind.RESOURCE, Collections.emptyMap());
	}

	public HttpMultipartPart(String name, String fileName, String mimeType, Object content, HttpMultipartPartKind kind,
			Map<String, String> headers) {
		this.name = name;
		this.fileName = fileName;
		this.mimeType = mimeType;
		this.content = content;
		this.kind = kind;
		this.headers = headers == null ? Collections.emptyMap()
				: Collections.unmodifiableMap(new LinkedHashMap<>(headers));
	}

	public String getName() {
		return name;
	}

	public String getFileName() {
		return fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public Object getContent() {
		return content;
	}

	public HttpMultipartPartKind getKind() {
		return kind;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
}
