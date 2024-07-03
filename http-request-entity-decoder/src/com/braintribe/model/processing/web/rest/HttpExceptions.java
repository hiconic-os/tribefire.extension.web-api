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
package com.braintribe.model.processing.web.rest;

import javax.servlet.http.HttpServletResponse;

import com.braintribe.exception.HttpException;

public class HttpExceptions {
	
	public static void methodNotAllowed(String message, Object ...params) {
		httpException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, message, params);
	}

	public static void notAcceptable(String message, Object ...params) {
		httpException(HttpServletResponse.SC_NOT_ACCEPTABLE, message, params);
	}

	public static void preConditionFaild(String message, Object ...params) {
		httpException(HttpServletResponse.SC_PRECONDITION_FAILED, message, params);
	}

	public static void expectationFailed(String message, Object ...params) {
		httpException(HttpServletResponse.SC_EXPECTATION_FAILED, message, params);
	}

	public static void unauthotized(String message, Object ...params) {
		httpException(HttpServletResponse.SC_UNAUTHORIZED, message, params);
	}

	public static void notFound(String message, Object ...params) {
		httpException(HttpServletResponse.SC_NOT_FOUND, message, params);
	}
	
	public static void badRequest(String message, Object ...params) {
		httpException(HttpServletResponse.SC_BAD_REQUEST, message, params);
	}

	public static void notImplemented(String message, Object ...params) {
		httpException(HttpServletResponse.SC_NOT_IMPLEMENTED, message, params);
	}

	public static void internalServerError(String message, Object ...params) {
		httpException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, params);
	}
	public static void httpException(int code, String message, Object ...params) {
		throw new HttpException(code, String.format(message, params));
	}
	
}
