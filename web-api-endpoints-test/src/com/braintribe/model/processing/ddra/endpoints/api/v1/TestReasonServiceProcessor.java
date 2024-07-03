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
package com.braintribe.model.processing.ddra.endpoints.api.v1;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.TestReasoningServiceRequest;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.reason.IncompleteReason;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.reason.TestReason;
import com.braintribe.model.processing.service.api.ReasonedServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;

public class TestReasonServiceProcessor implements ReasonedServiceProcessor<TestReasoningServiceRequest, String>{

	public static final String REASON_MESSSAGE_INCOMPLETE = "Forgot whom to greet";
	public static final String REASON_MESSSAGE_EMPTY = "Not able to process";
	public static final String VALUE_INCOMPLETE = "Hello ...";
	public static final String VALUE_COMPLETE = "Hello World!";

	@Override
	public Maybe<String> processReasoned(ServiceRequestContext requestContext, TestReasoningServiceRequest request) {
		switch (request.getMaybeOption()) {
		case complete:
			return Maybe.complete(VALUE_COMPLETE);
		case incomplete:
			return Reasons.build(IncompleteReason.T) //
				.text(REASON_MESSSAGE_INCOMPLETE) //
				.enrich(r -> r.setMessage(REASON_MESSSAGE_INCOMPLETE)) //
				.toMaybe(VALUE_INCOMPLETE);
		case empty:
			return Reasons.build(TestReason.T) //
				.text(REASON_MESSSAGE_EMPTY) //
				.enrich(r -> r.setMessage(REASON_MESSSAGE_EMPTY)) //
				.toMaybe();
		default:
			throw new IllegalStateException();
		}
	}
}
