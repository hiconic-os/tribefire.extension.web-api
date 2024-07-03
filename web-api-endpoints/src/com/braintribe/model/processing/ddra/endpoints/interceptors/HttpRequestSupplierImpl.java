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
package com.braintribe.model.processing.ddra.endpoints.interceptors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.braintribe.model.processing.service.api.HttpRequestSupplier;
import com.braintribe.model.service.api.ServiceRequest;

public class HttpRequestSupplierImpl implements HttpRequestSupplier {
	Map<String, HttpServletRequest> registry = new HashMap<>();

	public HttpRequestSupplierImpl() {

	}

	public HttpRequestSupplierImpl(ServiceRequest serviceRequest, HttpServletRequest httpRequest) {
		put(serviceRequest, httpRequest);
	}

	public void put(ServiceRequest serviceRequest, HttpServletRequest httpRequest) {
		Objects.requireNonNull(serviceRequest, "serviceRequest must not be null");

		if (serviceRequest.getGlobalId() == null) {
			serviceRequest.setGlobalId(UUID.randomUUID().toString());
		}

		registry.put( //
				serviceRequest.getGlobalId(), //
				Objects.requireNonNull(httpRequest, "httpRequest must not be null") //
		);
	}

	@Override
	public Optional<HttpServletRequest> getFor(ServiceRequest request) {
		return Optional.ofNullable(registry.get(request.getGlobalId()));
	}

	@Override
	public Collection<HttpServletRequest> getAll() {
		return registry.values();
	}
}
