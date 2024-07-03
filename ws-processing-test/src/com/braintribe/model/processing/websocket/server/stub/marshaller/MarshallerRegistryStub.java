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
package com.braintribe.model.processing.websocket.server.stub.marshaller;

import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.codec.marshaller.api.MarshallerRegistryEntry;
import com.braintribe.codec.marshaller.common.BasicConfigurableMarshallerRegistry;
import com.braintribe.codec.marshaller.common.BasicMarshallerRegistryEntry;
import com.braintribe.codec.marshaller.json.JsonStreamMarshaller;
import com.braintribe.codec.marshaller.stax.StaxMarshaller;

public class MarshallerRegistryStub extends BasicConfigurableMarshallerRegistry {

	@Override
	public Marshaller getMarshaller(String mimeType) {
		switch (mimeType) {
			case "gm/json":
				return new JsonStreamMarshaller();
			case "application/json":
				return new JsonStreamMarshaller();
			case "application/xml":
				return new StaxMarshaller();
			default:
				return null;
		}
	}

	@Override
	public MarshallerRegistryEntry getMarshallerRegistryEntry(String mimeType) {
		switch (mimeType) {
			case "gm/json":
				return new BasicMarshallerRegistryEntry("gm/json", new JsonStreamMarshaller());
			case "application/json":
				return new BasicMarshallerRegistryEntry("application/json", new JsonStreamMarshaller());
			case "application/xml":
				return new BasicMarshallerRegistryEntry("application/xml", new StaxMarshaller());
			default:
				return null;
		}
	}

}
