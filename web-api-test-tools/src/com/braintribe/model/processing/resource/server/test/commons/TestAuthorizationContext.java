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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.braintribe.model.usersession.UserSession;

public class TestAuthorizationContext implements Supplier<String>, Consumer<Throwable> {
	
	protected ThreadLocal<List<Throwable>> notifiedExceptions = new ThreadLocal<List<Throwable>>();
	protected Supplier<UserSession> userSessionProvider;
	private boolean invalidated;

	public void setUserSessionProvider(Supplier<UserSession> userSessionProvider) {
		this.userSessionProvider = userSessionProvider;
	}

	@Override
	public String get() throws RuntimeException {
		if (invalidated) {
			return "invalid";
		}
		UserSession userSession = provideUserSession();
		return (userSession != null) ? userSession.getSessionId() : null;
	}
	
	@Override
	public void accept(Throwable object) throws RuntimeException {

		if (notifiedExceptions.get() == null) {
			notifiedExceptions.set(new ArrayList<Throwable>());
		}

		notifiedExceptions.get().add(object);
		
		reset();

	}

	protected UserSession provideUserSession() throws RuntimeException {
		UserSession userSession = userSessionProvider.get();
		return userSession;
	}
	
	public List<Throwable> getNotifiedFailures() {
		return notifiedExceptions.get();
	}
	
	public void invalidate() {
		invalidated = true;
	}
	
	public void reset() {
		invalidated = false;
	}

}
