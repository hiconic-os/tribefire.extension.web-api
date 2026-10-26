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
package dev.hiconic.servlet.decoder.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.braintribe.gm.model.reason.UnsatisfiedMaybeTunneling;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.reason.essential.ParseError;
import com.braintribe.testing.model.test.technical.features.SimpleTypesEntity;

import dev.hiconic.servlet.decoder.api.HttpRequestEntityDecoderOptions;

public class QueryParamDecoderTest {

	@Test
	public void invalidValueProducesInvalidArgumentCausedByParseError() {
		String invalidValue = "\"2009-05-16T04:15:25.443Z\"";
		SimpleTypesEntity entity = SimpleTypesEntity.T.create();
		QueryParamDecoder decoder = new QueryParamDecoder(HttpRequestEntityDecoderOptions.defaults());
		decoder.registerTarget("testTarget", entity);

		try {
			decoder.decode("dateProperty=" + invalidValue);
			fail("Expected an unsatisfied InvalidArgument");
		} catch (UnsatisfiedMaybeTunneling e) {
			assertThat(e.getMaybe().isUnsatisfiedBy(InvalidArgument.T)).isTrue();
			var reason = e.getMaybe().whyUnsatisfied();
			assertThat(reason.getText()).contains("dateProperty").contains(invalidValue);
			assertThat(reason.getReasons()).singleElement().isInstanceOf(ParseError.class);
		}
	}
}
