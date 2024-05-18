// ============================================================================
package dev.hiconic.servlet.decoder.api;

import com.braintribe.exception.HttpException;

import jakarta.servlet.http.HttpServletResponse;

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
