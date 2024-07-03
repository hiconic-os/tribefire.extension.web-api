// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.model.processing.ddra.endpoints;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;

import java.net.URISyntaxException;
import java.net.URL;

public class TestHttpRequestFactory {

	private final HttpClient client;

	private final URL serverUrl;

	public TestHttpRequestFactory(HttpClient client, URL serverUrl) {
		super();
		this.client = client;
		this.serverUrl = serverUrl;
	}

	public TestHttpRequest get() {
		return request(RequestMethod.get);
	}

	public TestHttpRequest post() {
		return request(RequestMethod.post);
	}

	public TestHttpRequest put() {
		return request(RequestMethod.put);
	}

	public TestHttpRequest patch() {
		return request(RequestMethod.patch);
	}

	public TestHttpRequest delete() {
		return request(RequestMethod.delete);
	}

	public TestHttpRequest get(String path) {
		return get().path(path);
	}

	public TestHttpRequest post(String path) {
		return post().path(path);
	}

	public TestHttpRequest put(String path) {
		return put().path(path);
	}

	public TestHttpRequest patch(String path) {
		return patch().path(path);
	}

	public TestHttpRequest delete(String path) {
		return delete().path(path);
	}

	public TestHttpRequest serviceGet(EntityType<? extends GenericEntity> type) {
		return get().path("tribefire-services/api/v1/test.access/" + type.getTypeName());
	}

	public TestHttpRequest servicePost(EntityType<? extends GenericEntity> type) {
		return post().path("tribefire-services/api/v1/test.access/" + type.getTypeName());
	}

	public TestHttpRequest servicePut(EntityType<? extends GenericEntity> type) {
		return put().path("tribefire-services/api/v1/test.access/" + type.getTypeName());
	}

	public TestHttpRequest request(RequestMethod method) {
		try {
			switch (method) {
				case get:
					return new TestHttpRequest(client, new HttpGet(serverUrl.toURI()));
				case post:
					return new TestHttpRequest(client, new HttpPost(serverUrl.toURI()));
				case put:
					return new TestHttpRequest(client, new HttpPut(serverUrl.toURI()));
				case patch:
					return new TestHttpRequest(client, new HttpPatch(serverUrl.toURI()));
				case delete:
					return new TestHttpRequest(client, new HttpDelete(serverUrl.toURI()));
				default:
					throw new UnsupportedOperationException("Method " + method + " not supported.");
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
