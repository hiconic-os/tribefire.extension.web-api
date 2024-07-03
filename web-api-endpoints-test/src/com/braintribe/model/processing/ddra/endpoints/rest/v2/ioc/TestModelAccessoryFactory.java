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

import com.braintribe.common.lcd.NotImplementedException;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.meta.cmd.CmdResolverImpl;
import com.braintribe.model.processing.meta.oracle.BasicModelOracle;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;

public class TestModelAccessoryFactory implements ModelAccessoryFactory, ModelAccessory {

	public static TestModelAccessoryFactory testModelAccessoryFactory(IncrementalAccess access) {
		return new TestModelAccessoryFactory(access.getMetaModel());
	}

	private final GmMetaModel model;

	private final ModelOracle modelOracle;

	private final CmdResolver cmdResolver;

	public TestModelAccessoryFactory(GmMetaModel model) {
		this.model = model;
		this.modelOracle = new BasicModelOracle(model);
		this.cmdResolver = new CmdResolverImpl(this.modelOracle);
	}

	@Override
	public ModelAccessory getForAccess(String accessId) {
		return this;
	}
	
	@Override
	public ModelAccessory getForServiceDomain(String serviceDomainId) {
		return this;
	}

	@Override
	public CmdResolver getCmdResolver() {
		return cmdResolver;
	}

	@Override
	public ManagedGmSession getModelSession() {
		throw new NotImplementedException();
	}

	@Override
	public GmMetaModel getModel() {
		return model;
	}

	@Override
	public ModelOracle getOracle() {
		return modelOracle;
	}
}
