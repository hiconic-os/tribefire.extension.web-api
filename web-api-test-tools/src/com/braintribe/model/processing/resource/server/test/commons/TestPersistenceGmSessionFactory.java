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
package com.braintribe.model.processing.resource.server.test.commons;

import java.util.function.Supplier;

import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;

public class TestPersistenceGmSessionFactory implements PersistenceGmSessionFactory {

	private Supplier<PersistenceGmSession> gmSessionProvider;

	public void setGmSessionProvider(Supplier<PersistenceGmSession> gmSessionProvider) {
		this.gmSessionProvider = gmSessionProvider;
	}

	@Override
	public PersistenceGmSession newSession(String accessId) throws GmSessionException {
		try {
			return gmSessionProvider.get();
		} catch (Exception e) {
			throw new GmSessionException(e);
		}
	}

}
