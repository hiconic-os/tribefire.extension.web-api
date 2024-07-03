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

import com.braintribe.model.openapi.v3_0.OpenapiComponents;
import com.braintribe.model.openapi.v3_0.OpenapiParameter;
import com.braintribe.model.openapi.v3_0.OpenapiSchema;
import com.braintribe.model.openapi.v3_0.reference.utils.AbstractComponentsTest;
import com.braintribe.model.openapi.v3_0.reference.utils.TestApiContext;

public class SimpleComponentsTest extends AbstractComponentsTest {
	@Test
	public void testSimple() {

		// An OpenapiSchema of type OpenapiComponents which again references a schema of Type OpenapiParameter
		OpenapiSchema schemaRef = schemaRef(rootContext, OpenapiComponents.T, c -> {
			OpenapiSchema ref = schemaRef(c, OpenapiParameter.T);

			OpenapiSchema comp = OpenapiSchema.T.create();
			comp.setItems(ref);
			return comp;
		});

		OpenapiSchema schemaComponent = getSchemaComponent(OpenapiComponents.T, rootContext, schemaRef);

		OpenapiSchema parameterRef = schemaComponent.getItems();

		OpenapiSchema childComponent = getSchemaComponent(OpenapiParameter.T, rootContext, parameterRef);

		assertThat(childComponent.getDescription()).isEqualTo(OpenapiParameter.T.getTypeSignature());

	}

	@Test
	public void testMultipleContexts() {

		TestApiContext childContext = rootContext.childContext("CHILD");

		// An OpenapiSchema of type OpenapiComponents which again references a schema of Type OpenapiParameter
		OpenapiSchema schemaRef = schemaRef(childContext, OpenapiComponents.T, c -> {
			OpenapiSchema ref = schemaRef(c, OpenapiParameter.T);

			OpenapiSchema comp = OpenapiSchema.T.create();
			comp.setItems(ref);
			return comp;
		});

		// Because the child context doesn't introduce any changes the components should be registered under the root
		// context
		OpenapiSchema schemaComponent = getSchemaComponent(OpenapiComponents.T, rootContext, schemaRef);

		OpenapiSchema parameterRef = schemaComponent.getItems();

		OpenapiSchema childComponent = getSchemaComponent(OpenapiParameter.T, rootContext, parameterRef);

		assertThat(childComponent.getDescription()).isEqualTo(OpenapiParameter.T.getTypeSignature());
	}

	@Test
	public void testManyContexts() {

		TestApiContext childContext = rootContext.childContext("CHILD");
		TestApiContext grandchildContext = childContext.childContext("CHILD");
		TestApiContext greatgrandchildContext = grandchildContext.childContext("CHILD");
		TestApiContext greatgreatgrandchildContext = greatgrandchildContext.childContext("CHILD");

		// An OpenapiSchema of type OpenapiComponents which again references a schema of Type OpenapiParameter
		OpenapiSchema schemaRef = schemaRef(greatgreatgrandchildContext, OpenapiComponents.T, c -> {
			OpenapiSchema ref = schemaRef(c, OpenapiParameter.T);

			OpenapiSchema comp = OpenapiSchema.T.create();
			comp.setItems(ref);
			return comp;
		});

		// Because the child context doesn't introduce any changes the components should be registered under the root
		// context
		OpenapiSchema schemaComponent = getSchemaComponent(OpenapiComponents.T, rootContext, schemaRef);

		OpenapiSchema parameterRef = schemaComponent.getItems();

		OpenapiSchema childComponent = getSchemaComponent(OpenapiParameter.T, rootContext, parameterRef);

		assertThat(childComponent.getDescription()).isEqualTo(OpenapiParameter.T.getTypeSignature());
	}

	@Test
	public void testMultipleContextsWithChanges() {

		TestApiContext childContext = rootContext.childContext("CHILD");

		// An OpenapiSchema of type OpenapiComponents which again references a schema of Type OpenapiParameter
		OpenapiSchema schemaRef = schemaRef(childContext, OpenapiComponents.T, c -> {
			OpenapiSchema ref = schemaRef(c, OpenapiParameter.T);

			OpenapiSchema comp = OpenapiSchema.T.create();
			comp.setItems(ref);

			if (c == childContext) {
				comp.setDefault("Child context changes default");
			}

			return comp;
		});

		// Because the child context introduces changes to the OpenapiComponents schema it should be registered under
		// the child context
		OpenapiSchema schemaComponent = getSchemaComponent(OpenapiComponents.T, childContext, schemaRef);

		OpenapiSchema parameterRef = schemaComponent.getItems();

		// But no changes affecting the OpenapiParameter schema
		OpenapiSchema childComponent = getSchemaComponent(OpenapiParameter.T, rootContext, parameterRef);

		assertThat(childComponent.getDescription()).isEqualTo(OpenapiParameter.T.getTypeSignature());
	}

	@Test
	public void testManyContextsWithChanges() {

		TestApiContext childContext = rootContext.childContext("CHILD");
		TestApiContext grandchildContext = childContext.childContext("CHILD");
		TestApiContext greatgrandchildContext = grandchildContext.childContext("CHILD");
		TestApiContext greatgreatgrandchildContext = greatgrandchildContext.childContext("CHILD");

		// An OpenapiSchema of type OpenapiComponents which again references a schema of Type OpenapiParameter
		OpenapiSchema schemaRef = schemaRef(greatgreatgrandchildContext, OpenapiComponents.T, c -> {
			OpenapiSchema ref = schemaRef(c, OpenapiParameter.T);

			OpenapiSchema comp = OpenapiSchema.T.create();
			comp.setItems(ref);

			if (c == greatgrandchildContext) {
				comp.setDefault("Child context changes default");
			}

			return comp;
		});

		// Because the child context introduces changes to the OpenapiComponents schema it should be registered under
		// the child context
		OpenapiSchema schemaComponent = getSchemaComponent(OpenapiComponents.T, greatgrandchildContext, schemaRef);

		OpenapiSchema parameterRef = schemaComponent.getItems();

		// But no changes affecting the OpenapiParameter schema
		OpenapiSchema childComponent = getSchemaComponent(OpenapiParameter.T, rootContext, parameterRef);

		assertThat(childComponent.getDescription()).isEqualTo(OpenapiParameter.T.getTypeSignature());
	}

	@Test
	public void testMultipleContextsWithChanges2() {

		TestApiContext childContext = rootContext.childContext("CHILD");

		// An OpenapiSchema of type OpenapiComponents which again references a schema of Type OpenapiParameter
		OpenapiSchema schemaRef = schemaRef(childContext, OpenapiComponents.T, c -> {
			OpenapiSchema ref = schemaRef(c, OpenapiParameter.T, cc -> {
				OpenapiSchema cs = schemaFactory(OpenapiParameter.T).apply(cc);

				if (cc == childContext) {
					cs.setDefault("Child context changes default");
				}

				return cs;
			});

			OpenapiSchema comp = OpenapiSchema.T.create();
			comp.setItems(ref);

			return comp;
		});

		// Because the child context introduces changes to the OpenapiParameter schema which the OpenapiComponents
		// schema references,
		// also this schema needs to be registered under the child context
		OpenapiSchema schemaComponent = getSchemaComponent(OpenapiComponents.T, childContext, schemaRef);

		OpenapiSchema parameterRef = schemaComponent.getItems();

		// Because the child context introduces changes to the OpenapiParameter schema it should be registered under the
		// child context
		OpenapiSchema childComponent = getSchemaComponent(OpenapiParameter.T, childContext, parameterRef);

		assertThat(childComponent.getDescription()).isEqualTo(OpenapiParameter.T.getTypeSignature());
	}

	@Test
	public void testBuilder() {

		TestApiContext childContext = rootContext.childContext("CHILD");

		ComponentTestBuilder builder = new ComponentTestBuilder(this);
		builder.add(OpenapiParameter.T).changeForContext(childContext);

		// An OpenapiSchema of type OpenapiComponents which again references a schema of Type OpenapiParameter
		OpenapiSchema schemaRef = builder.buildRef(childContext);

		// Because the child context introduces changes to the OpenapiParameter schema which the OpenapiComponents
		// schema references,
		// also this schema needs to be registered under the child context
		OpenapiSchema schemaComponent = getSchemaComponent(OpenapiComponents.T, childContext, schemaRef);

		OpenapiSchema parameterRef = schemaComponent.getItems();

		// Because the child context introduces changes to the OpenapiParameter schema it should be registered under the
		// child context
		OpenapiSchema childComponent = getSchemaComponent(OpenapiParameter.T, childContext, parameterRef);

		assertThat(childComponent.getDescription()).isEqualTo(OpenapiParameter.T.getTypeSignature());
	}

}
