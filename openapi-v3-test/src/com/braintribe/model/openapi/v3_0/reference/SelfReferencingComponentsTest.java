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
package com.braintribe.model.openapi.v3_0.reference;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.braintribe.model.openapi.v3_0.OpenapiParameter;
import com.braintribe.model.openapi.v3_0.OpenapiSchema;
import com.braintribe.model.openapi.v3_0.reference.utils.AbstractComponentsTest;
import com.braintribe.model.openapi.v3_0.reference.utils.TestApiContext;

public class SelfReferencingComponentsTest extends AbstractComponentsTest {
	@Test
	public void testSimple() {

		// An OpenapiSchema of type OpenapiParameter which again references a schema of Type OpenapiParameter
		// Both schemas should be the exact same instance -> a self-reference
		OpenapiSchema schemaRef = schemaRef(rootContext, OpenapiParameter.T, c -> {
			OpenapiSchema ref = alreadyPresentSchemaRef(c, OpenapiParameter.T);

			OpenapiSchema comp = OpenapiSchema.T.create();
			comp.setItems(ref);
			return comp;
		});

		OpenapiSchema schemaComponent = getSchemaComponent(OpenapiParameter.T, rootContext, schemaRef);

		OpenapiSchema parameterRef = schemaComponent.getItems();

		OpenapiSchema childComponent = getSchemaComponent(OpenapiParameter.T, rootContext, parameterRef);

		assertThat(childComponent).isSameAs(schemaComponent);

	}

	@Test
	public void testMultipleContexts() {

		TestApiContext childContext = rootContext.childContext("CHILD");

		OpenapiSchema schemaRef = schemaRef(childContext, OpenapiParameter.T, c -> {
			OpenapiSchema ref = alreadyPresentSchemaRef(c, OpenapiParameter.T);

			OpenapiSchema comp = OpenapiSchema.T.create();
			comp.setItems(ref);
			return comp;
		});

		// Because the child context doesn't introduce any changes the components should be registered under the root
		// context
		OpenapiSchema schemaComponent = getSchemaComponent(OpenapiParameter.T, rootContext, schemaRef);

		OpenapiSchema parameterRef = schemaComponent.getItems();

		OpenapiSchema childComponent = getSchemaComponent(OpenapiParameter.T, rootContext, parameterRef);

		assertThat(childComponent).isSameAs(schemaComponent);
	}

	@Test
	public void testMultipleContextsWithChanges() {

		TestApiContext childContext = rootContext.childContext("CHILD");

		OpenapiSchema schemaRef = schemaRef(childContext, OpenapiParameter.T, c -> {
			OpenapiSchema ref = alreadyPresentSchemaRef(c, OpenapiParameter.T);

			OpenapiSchema comp = OpenapiSchema.T.create();
			comp.setItems(ref);

			if (c == childContext) {
				comp.setDefault("Child context changes default");
			}

			return comp;
		});

		// Because the child context introduces changes to the OpenapiComponents schema it should be registered under
		// the child context
		OpenapiSchema schemaComponent = getSchemaComponent(OpenapiParameter.T, childContext, schemaRef);

		OpenapiSchema parameterRef = schemaComponent.getItems();

		// Because child and parent component are identical this should return the same
		OpenapiSchema childComponent = getSchemaComponent(OpenapiParameter.T, childContext, parameterRef);

		assertThat(childComponent).isSameAs(schemaComponent);

	}

}
