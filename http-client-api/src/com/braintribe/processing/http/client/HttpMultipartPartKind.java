// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// ============================================================================
package com.braintribe.processing.http.client;

/** Determines how an HTTP client writes multipart part content. */
public enum HttpMultipartPartKind {
	RESOURCE,
	TEXT,
	MARSHALLED
}
