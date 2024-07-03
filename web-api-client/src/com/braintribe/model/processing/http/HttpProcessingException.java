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
package com.braintribe.model.processing.http;

public class HttpProcessingException extends RuntimeException {

	private static final long serialVersionUID = -7279663953432763826L;

	public HttpProcessingException() {
	}

	public HttpProcessingException(String message) {
		super(message);
	}

	public HttpProcessingException(Throwable cause) {
		super(cause);
	}

	public HttpProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpProcessingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
