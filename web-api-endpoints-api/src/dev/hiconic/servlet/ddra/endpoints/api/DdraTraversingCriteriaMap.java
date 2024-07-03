// ============================================================================
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
package dev.hiconic.servlet.ddra.endpoints.api;

import java.util.HashMap;
import java.util.Map;

import com.braintribe.cfg.Configurable;
import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.model.DdraEndpointDepth;
import com.braintribe.model.DdraEndpointDepthKind;
import com.braintribe.model.processing.web.rest.HttpExceptions;

public class DdraTraversingCriteriaMap {

	private final Map<Object, TraversingCriterion> criterias = new HashMap<>();
	
	@Configurable
	public void addDefaultCriterion(DdraEndpointDepthKind kind, TraversingCriterion criterion) {
		criterias.put(kind, criterion);
	}
	
	public TraversingCriterion getCriterion(DdraEndpointDepthKind kind) {
		TraversingCriterion result = criterias.get(kind);
		if(result == null) {
			throw new IllegalStateException("Unexpected state: DdraTraversingCriteriaMap has not been initialized with a criterion for " + kind);
		}
		return result;
	}
	
	public TraversingCriterion getCriterion(int depth) {
		TraversingCriterion result = criterias.get(depth);
		if(result == null) {
			result = createFor(depth);
			criterias.put(depth, result);
		}
		return result;
	}
	
	public TraversingCriterion getCriterion(DdraEndpointDepth depth) {
		switch(depth.getKind()) {
			case custom:
				return getCriterion(depth.getCustomDepth());
			case shallow:
				return getCriterion(DdraEndpointDepthKind.shallow);
			case reachable:
				return getCriterion(DdraEndpointDepthKind.reachable);
			default:
				HttpExceptions.internalServerError("Unexpected enpoint depth kind %s", depth.getKind());
				return null;
		}
	}
	
	private TraversingCriterion createFor(int depth) {
		TraversingCriterion shallow = getCriterion(DdraEndpointDepthKind.shallow);
		
		// @formatter:off
		return TC.create()
				.pattern() // pattern 1
					.recursion(depth, depth)
						.pattern() // pattern 2
							.entity()
							.disjunction() // disjunction 1
								.property()
								.pattern() // pattern 3
									.property()
									.disjunction() // disjunction 2
										.listElement()
										.setElement()
										.pattern().map().mapKey().close()
										.pattern().map().mapValue().close()
									.close() // disjunction 2
								.close() // pattern 3
							.close() // disjunction 1
						.close() // pattern 2
					.criterion(shallow)
				.close() // pattern 1
			.done();
		// @formatter:on
	}
}
