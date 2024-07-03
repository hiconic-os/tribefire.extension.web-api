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

import com.braintribe.model.generic.pr.criteria.PatternCriterion;
import com.braintribe.model.generic.pr.criteria.PropertyTypeCriterion;
import com.braintribe.model.generic.pr.criteria.TraversingCriterion;
import com.braintribe.model.generic.pr.criteria.typematch.CollectionTypeMatch;
import com.braintribe.model.generic.pr.criteria.typematch.EntityTypeMatch;
import com.braintribe.model.generic.processing.pr.fluent.TC;
import com.braintribe.ddra.endpoints.api.DdraTraversingCriteriaMap;
import com.braintribe.model.DdraEndpointDepthKind;

import static com.braintribe.wire.api.util.Lists.list;
import static com.braintribe.wire.api.util.Sets.set;

@SuppressWarnings("deprecation")
public class TestTraversingCriteriaMap {

	private static DdraTraversingCriteriaMap INSTANCE;

	public static DdraTraversingCriteriaMap traversingCriteriaMap() {
		if (INSTANCE == null) {
			INSTANCE = new DdraTraversingCriteriaMap();
			INSTANCE.addDefaultCriterion(DdraEndpointDepthKind.shallow, standard());
			INSTANCE.addDefaultCriterion(DdraEndpointDepthKind.reachable, TC.create().negation().joker().done());
		}

		return INSTANCE;
	}

	private static TraversingCriterion standard() {
		// this code is taken from com.braintribe.cartridge.master.wire.space.cortex.TraversingCriteriaSpace
		PropertyTypeCriterion ptc = PropertyTypeCriterion.T.create();
		ptc.setTypes(set(EntityTypeMatch.T.create(), CollectionTypeMatch.T.create()));

		PatternCriterion bean = PatternCriterion.T.create();
		bean.setCriteria(list(com.braintribe.model.generic.pr.criteria.EntityCriterion.T.create(), ptc));
		return bean;

	}
}
