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
package com.braintribe.model.processing.web.rest;

import java.util.function.Supplier;

import com.braintribe.codec.CodecException;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.web.rest.impl.UrlPathCodecImpl;

public interface UrlPathCodec<E extends GenericEntity> {

	static <E extends GenericEntity> UrlPathCodec<E> create() {
		return new UrlPathCodecImpl<>();
	}
	
	static <E extends GenericEntity> UrlPathCodec<E> create(EntityType<E> type) {
		return create();
	}
	
	UrlPathCodec<E> constantSegment(String segment);

	UrlPathCodec<E> mappedSegment(String propertyName);

	UrlPathCodec<E> mappedSegment(String propertyName, boolean optional);

	String encode(E value) throws CodecException;
	
	E decode(Supplier<E> entitySupplier, String encodedValue) throws CodecException;

}
