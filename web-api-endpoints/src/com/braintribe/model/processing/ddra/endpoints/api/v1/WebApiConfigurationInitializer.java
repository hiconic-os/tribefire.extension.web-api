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
package com.braintribe.model.processing.ddra.endpoints.api.v1;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.braintribe.cfg.Configurable;
import com.braintribe.model.ddra.DdraConfiguration;
import com.braintribe.model.ddra.DdraMapping;
import com.braintribe.model.ddra.DdraUrlMethod;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.processing.itw.analysis.JavaTypeAnalysis;
import com.braintribe.model.processing.query.fluent.SelectQueryBuilder;
import com.braintribe.model.processing.session.api.collaboration.DataInitializer;
import com.braintribe.model.processing.session.api.collaboration.ManipulationPersistenceException;
import com.braintribe.model.processing.session.api.collaboration.PersistenceInitializationContext;
import com.braintribe.model.processing.session.api.managed.ManagedGmSession;
import com.braintribe.model.query.SelectQuery;
import com.braintribe.model.service.api.ServiceRequest;

public class WebApiConfigurationInitializer implements DataInitializer {

	private BiConsumer<ManagedGmSession, DdraMappingRegistry> configurer;
	
	@Configurable
	public void setConfigurer(BiConsumer<ManagedGmSession, DdraMappingRegistry> configurer) {
		this.configurer = configurer;
	}
	
	// Helper constructs
	
	/**
	 * This registry implementation creates {@link DdraMapping DdraMappings} on the session directly.
	 * Some of the create methods apply a consumer which enables the possibility to further customize the mapping
	 * to be created with arguments that are not part of the method parameters (e.g. depth or default prettiness). 
	 * 
	 */
	private class DdraMappingRegistryImpl implements DdraMappingRegistry {
		private ManagedGmSession session;
		private Set<DdraMapping> ddraMappings;

		public DdraMappingRegistryImpl(ManagedGmSession session, Set<DdraMapping> ddraMappings) {
			this.session = session;
			this.ddraMappings = ddraMappings;
		}

		@Override
		public DdraMapping create(String path, EntityType<? extends ServiceRequest> requestType) {
			DdraMapping m = session.create(DdraMapping.T, "ddra:" + path.replace('/', '_'));
			m.setRequestType(queryGmEntityType(session, requestType));
			m.setPath(path);
			
			ddraMappings.add(m);
			
			return m;
		}
		
		@Override
		public DdraMapping create(String path, EntityType<? extends ServiceRequest> requestType, DdraUrlMethod method) {
			DdraMapping m = session.create(DdraMapping.T, "ddra:" + path.replace('/', '_') + "/" + method.name());
			m.setRequestType(queryGmEntityType(session, requestType));
			m.setPath(path);
			
			ddraMappings.add(m);
			
			return m;
		}
		
		@Override
		public DdraMapping create(String path, EntityType<? extends ServiceRequest> requestType, DdraUrlMethod method,
				String defaultProjection, String defaultMimeType, String defaultServiceDomain, Set<String> tags) {

			DdraMapping m = create(path, requestType, method);
			m.setMethod(method);
			m.setDefaultProjection(defaultProjection);
			m.setDefaultMimeType(defaultMimeType);
			m.setDefaultServiceDomain(defaultServiceDomain);
			
			if (tags != null) {
				m.setTags(tags);
			}
			
			return m;
		}

		// Consumer support
		
		@Override
		public DdraMapping create(String path, EntityType<? extends ServiceRequest> requestType, Consumer<DdraMapping> configurer) {
			DdraMapping m = create(path, requestType);
			configurer.accept(m);
			
			return m;
		}
		
		@Override
		public DdraMapping create(String path, EntityType<? extends ServiceRequest> requestType, DdraUrlMethod method,
				String defaultProjection, String defaultMimeType, String defaultServiceDomain, Set<String> tags,
				Consumer<DdraMapping> configurer) {

			DdraMapping m = create(path, requestType, method, defaultProjection, defaultMimeType, defaultServiceDomain, tags);
			configurer.accept(m);
			
			return m;
		}

	}
	
	// Initialization
	
	@Override
	public void initialize(PersistenceInitializationContext context) {
		ManagedGmSession session = context.getSession();
		
		DdraConfiguration ddraConfig = session.acquire(DdraConfiguration.T, "ddra:config");
		Set<DdraMapping> ddraMappings = ddraConfig.getMappings();

		if (configurer != null) {
			DdraMappingRegistryImpl registry = new DdraMappingRegistryImpl(session, ddraMappings);
			configurer.accept(session, registry);
		}
	}
	
	
	private GmEntityType queryGmEntityType(ManagedGmSession session, EntityType<?> entityType) throws ManipulationPersistenceException {
		GmEntityType result = session.findEntityByGlobalId(JavaTypeAnalysis.resolveGlobalId(entityType.getJavaType()));
		if (result != null)
			return result;
		else
			// Just in case there is an entity type with non-standard globalId, probably only possible for legacy installations
			return session.query().select(queryGmEntityTypeFor(entityType)).unique();
	}

	private SelectQuery queryGmEntityTypeFor(EntityType<?> requestType) {
		return new SelectQueryBuilder().from(GmEntityType.T, "e").where().property("e", "typeSignature").eq(requestType.getTypeSignature()).done();
	}
	
}
