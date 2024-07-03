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
package com.braintribe.model.processing.ddra.endpoints.api.v1.ioc;

import com.braintribe.common.lcd.NotImplementedException;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.EvalContextAspect;
import com.braintribe.model.generic.eval.EvalException;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.TestFailingServiceRequest;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.processing.async.api.AsyncCallback;

public class TestRestEvaluator implements Evaluator<ServiceRequest> {

	private static class TestEvalContext<T> implements EvalContext<T> {
		private final T result;

		public TestEvalContext(T result) {
			this.result = result;
		}

		@Override
		public T get() throws EvalException {
			if (result instanceof TestFailingServiceRequest) {
				throw new EvalException(((TestFailingServiceRequest) result).getMessage());
			}
			return result;
		}

		@Override
		public void get(AsyncCallback<? super T> callback) {
			throw new NotImplementedException();
		}

		@Override
		public <U, A extends EvalContextAspect<? super U>> EvalContext<T> with(Class<A> aspect, U value) {
			throw new NotImplementedException();
		}
	}

	@Override
	public <T> EvalContext<T> eval(ServiceRequest evaluable) {
		return new TestEvalContext<T>((T) evaluable);
	}

}
