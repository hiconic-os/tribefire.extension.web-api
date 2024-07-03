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

import static com.braintribe.model.processing.query.tools.PreparedTcs.everythingTc;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.braintribe.ddra.endpoints.api.api.v1.DdraMappings;
import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.ddra.DdraMapping;
import com.braintribe.model.ddra.DdraUrlMethod;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.processing.ddra.endpoints.AbstractDdraRestServletTest;
import com.braintribe.model.processing.ddra.endpoints.ioc.TestExceptionHandlerSpace;
import com.braintribe.model.processing.ddra.endpoints.ioc.TestMarshallerRegistry;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.ioc.TestAccessSpace;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.ioc.TestSessionFactory;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.ioc.TestTraversingCriteriaMap;
import com.braintribe.model.processing.ddra.endpoints.wire.DdraTestModule;
import com.braintribe.model.processing.ddra.endpoints.wire.contract.DdraTestContract;
import com.braintribe.model.processing.meta.oracle.BasicModelOracle;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.query.fluent.EntityQueryBuilder;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;
import com.braintribe.model.processing.test.impl.session.TestModelAccessory;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.domain.ServiceDomain;
import com.braintribe.utils.CollectionTools;
import com.braintribe.utils.stream.api.StreamPipes;
import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;

public abstract class AbstractApiV1RestServletTest extends AbstractDdraRestServletTest {
	protected static WebApiV1Server servlet;
	private static WireContext<DdraTestContract> wireContext;

	@BeforeClass
	public static void beforeClass() {
		wireContext = Wire.context(DdraTestModule.INSTANCE);

		servlet = new WebApiV1Server();
		Evaluator<ServiceRequest> serviceRequestEvaluator = wireContext.contract().serviceRequestEvaluator();
		servlet.setEvaluator(serviceRequestEvaluator);
		servlet.setExceptionHandler(TestExceptionHandlerSpace.exceptionHandler());
		servlet.setMarshallerRegistry(TestMarshallerRegistry.getMarshallerRegistry());
		servlet.setTraversingCriteriaMap(TestTraversingCriteriaMap.traversingCriteriaMap());
		// servlet.setAccessAvailability(new TestBasicDeployRegistry()::isDeployed);
		IncrementalAccess incrementalAccess = TestAccessSpace.testAccess(true, true);
		TestSessionFactory sessionFactory = new TestSessionFactory(serviceRequestEvaluator);
		sessionFactory.reset(incrementalAccess);
		IncrementalAccess cortexAccess = TestAccessSpace.cortexAccess();
		sessionFactory.addAccess(cortexAccess);
		servlet.setUsersSessionFactory(sessionFactory);
		servlet.setSystemSessionFactory(sessionFactory);
		servlet.setPollMappings(false);
		servlet.setStreamPipeFactory(StreamPipes.simpleFactory());
		servlet.setRestServletUtils(new ApiV1RestServletUtils());

		ModelAccessoryFactory modelAccessoryFactory = new ModelAccessoryFactory() {
			@Override
			public ModelAccessory getForAccess(String accessId) {
				// PersistenceGmSession session = sessionFactory.newSession("cortex");
				throw new NotImplementedException();

			}
			@Override
			public ModelAccessory getForServiceDomain(String serviceDomainId) {
				// serviceDomainId = "test.domain1";
				EntityQuery query = EntityQueryBuilder.from(ServiceDomain.T).where().property("externalId").eq(serviceDomainId).tc(everythingTc)
						.done();
				List<GenericEntity> entities = cortexAccess.queryEntities(query).getEntities();
				if (entities.isEmpty()) {
					throw new IllegalArgumentException("Service domain '" + serviceDomainId + "' not found.");
				}
				ServiceDomain serviceDomain = (ServiceDomain) entities.get(0);
				GmMetaModel model = serviceDomain.getServiceModel();
				ModelOracle modelOracle = new BasicModelOracle(model);
				TestModelAccessory modelAccessory = new TestModelAccessory(modelOracle);
				modelAccessory.build();
				return modelAccessory;
			}
		};
		servlet.setModelAccessoryFactory(modelAccessoryFactory);
		DdraMappings mappings = new DdraMappings();

		WebApiConfigurationInitializer dci = new WebApiConfigurationInitializer();
		servlet.setMappings(mappings);

		setupStandardUndertowServer("/tribefire-services", "service-servlet", servlet, "/api/v1/*", "/api/v1");
	}

	@AfterClass
	public static void afterClass() throws Exception {
		destroy();
		wireContext.close();
	}

	@After
	public void afterEach() {
		setMappings(); // clear mappings
	}

	static DdraMapping mapping(String path, DdraUrlMethod method, EntityType<?> requestType) {
		return mapping(path, method, requestType, null, null, null);
	}

	static DdraMapping mapping(String path, DdraUrlMethod method, EntityType<?> requestType, ServiceRequest transformRequest) {
		return mapping(path, method, requestType, transformRequest, null, null);
	}

	static DdraMapping mapping(String path, DdraUrlMethod method, EntityType<?> requestType, ServiceRequest transformRequest,
			String defaultProjection, String defaultMimeType) {
		return mapping(path, method, requestType, transformRequest, defaultProjection, defaultMimeType, null);
	}

	static DdraMapping mapping(String path, DdraUrlMethod method, EntityType<?> requestType, ServiceRequest transformRequest,
			String defaultProjection, String defaultMimeType, String defaultServiceDomain) {
		DdraMapping mapping = DdraMapping.T.create();

		mapping.setPath(path);
		mapping.setMethod(method);
		mapping.setTransformRequest(transformRequest);
		mapping.setDefaultProjection(defaultProjection);
		mapping.setDefaultMimeType(defaultMimeType);
		mapping.setDefaultServiceDomain(defaultServiceDomain);

		if (requestType != null) {
			GmEntityType requestGmType = GmEntityType.T.create();
			requestGmType.setTypeSignature(requestType.getTypeSignature());
			mapping.setRequestType(requestGmType);
		}

		return mapping;
	}

	static void setMappings(DdraMapping... mappings) {
		DdraMappings ddraMappings = new DdraMappings();
		servlet.setMappings(ddraMappings);

		ddraMappings.setMappings(CollectionTools.getList(mappings));
		ddraMappings.setDdraMappingsInitialized(true);
	}
}
