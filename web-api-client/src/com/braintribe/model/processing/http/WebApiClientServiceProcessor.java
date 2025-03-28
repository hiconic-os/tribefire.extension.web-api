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

import java.util.function.Consumer;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.exception.HttpException;
import com.braintribe.gm.model.http.reason.HttpReason;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.UnsatisfiedMaybeTunneling;
import com.braintribe.logging.Logger;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.api.aspect.HttpStatusCodeNotification;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.processing.http.client.HttpClient;
import com.braintribe.processing.http.client.HttpRequestContext;
import com.braintribe.processing.http.client.HttpResponse;
import com.braintribe.utils.lcd.StopWatch;

public class WebApiClientServiceProcessor implements ServiceProcessor<ServiceRequest, Object> {

	private static final Logger logger = Logger.getLogger(WebApiClientServiceProcessor.class);

	private HttpContextResolver httpContextResolver;

	// ***************************************************************************************************
	// Setter
	// ***************************************************************************************************

	@Required
	@Configurable
	public void setHttpContextResolver(HttpContextResolver httpContextResolver) {
		this.httpContextResolver = httpContextResolver;
	}

	// ***************************************************************************************************
	// ServiceProcessor
	// ***************************************************************************************************

	@Override
	public Object process(ServiceRequestContext context, ServiceRequest request) {
		StopWatch watch = stopWatch();
		try {

			HttpRequestContext httpContext = this.httpContextResolver.resolve(context, request);
			logger.trace(() -> "Context creation for HTTP execution of ServiceRequest: " + request + " took: " + watch.getElapsedTime() + "ms.");

			HttpClient httpClient = httpContext.httpClient();
			HttpResponse response = httpClient.sendRequest(httpContext);
			logger.trace(() -> "Sending the http request for: " + request + " took: " + watch.getElapsedTime() + "ms.");

			return response.combinedResponse();

		} catch (HttpException e) {
			logger.debug(() -> "HTTP execution for ServiceRequest: " + (request != null ? request.entityType().getTypeSignature() : "null")
					+ " failed with status code: " + e.getStatusCode() + " and payload " + e.getPayload() + " after: " + watch.getElapsedTime()
					+ " ms.");

			Consumer<Integer> aspect = context.findAspect(HttpStatusCodeNotification.class);
			if (aspect != null) {
				logger.debug(() -> "Notifying HttpStatusCodeNotification aspect about HTTP status code: " + e.getStatusCode());
				aspect.accept(e.getStatusCode());
				return e.getPayload();
			}
			HttpReason reason = HttpReason.T.create();
			reason.setHttpCode(e.getStatusCode());
			reason.setHttpPayload(e.getPayload() != null ? e.getPayload().toString() : null);
			UnsatisfiedMaybeTunneling exc = new UnsatisfiedMaybeTunneling(Maybe.empty(reason));
			throw exc;
		} finally {
			logger.debug(() -> "Finished HTTP execution for ServiceRequest: " + (request != null ? request.entityType().getTypeSignature() : "null")
					+ " after: " + watch.getElapsedTime() + "ms.");
		}
	}

	// ***************************************************************************************************
	// Helper
	// ***************************************************************************************************

	private StopWatch stopWatch() {
		StopWatch watch = new StopWatch();
		watch.setAutomaticResetEnabled(true);
		return watch;
	}

}
