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
package com.braintribe.model.processing.ddra.endpoints.rest.v2.ioc;

import java.util.HashMap;
import java.util.Map;

import com.braintribe.exception.AuthorizationException;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.accessapi.ManipulationRequest;
import com.braintribe.model.accessapi.QueryEntities;
import com.braintribe.model.accessapi.QueryProperty;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.EvalContextAspect;
import com.braintribe.model.generic.eval.EvalException;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.service.api.AuthorizedRequest;
import com.braintribe.model.service.api.DomainRequest;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.processing.async.api.AsyncCallback;

public class TestRestEvaluator implements Evaluator<ServiceRequest> {

	private static class Context<T> implements EvalContext<T> {

		private final IncrementalAccess access;
		private final ServiceRequest evaluable;

		public Context(IncrementalAccess access, ServiceRequest evaluable) {
			this.access = access;
			this.evaluable = evaluable;
		}

		@Override
		public T get() throws EvalException {

			if (evaluable instanceof QueryEntities) {
				QueryEntities query = (QueryEntities) evaluable;
				return (T) access.queryEntities(query.getQuery());
			}
			if (evaluable instanceof ManipulationRequest) {
				ManipulationRequest manipulation = (ManipulationRequest) evaluable;
				return (T) access.applyManipulation(manipulation);
			}
			if (evaluable instanceof QueryProperty) {
				QueryProperty query = (QueryProperty) evaluable;
				return (T) access.queryProperty(query.getQuery());
			}

			throw new IllegalArgumentException("Unsupported service request type: " + evaluable.getClass().getName());
		}

		@Override
		public void get(AsyncCallback<? super T> callback) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <U, A extends EvalContextAspect<? super U>> EvalContext<T> with(Class<A> aspect, U value) {
			throw new UnsupportedOperationException();
		}

	}

	private final Map<String, IncrementalAccess> accesses = new HashMap<>();

	public void reset(IncrementalAccess access) {
		accesses.put(access.getAccessId(), access);
	}

	@Override
	public <T> EvalContext<T> eval(ServiceRequest evaluable) {
		if (!(evaluable instanceof AuthorizedRequest)) {
			throw new IllegalArgumentException("Expected service request to be AuthorizedRequest");
		}
		if (!(evaluable instanceof DomainRequest)) {
			throw new IllegalArgumentException("Expected service request to be DispatchableRequest");
		}

		String sessionId = ((AuthorizedRequest) evaluable).getSessionId();
		String accessId = ((DomainRequest) evaluable).getDomainId();

		if (sessionId == null) {
			throw new AuthorizationException("No provided sessionId.");
		}
		if (accessId == null) {
			throw new RuntimeException("No provided serviceId.");
		}

		IncrementalAccess access = accesses.get(accessId);
		if (access == null) {
			throw new RuntimeException("No access found with accessId=" + accessId);
		}

		return new Context<T>(access, evaluable);
	}

}
