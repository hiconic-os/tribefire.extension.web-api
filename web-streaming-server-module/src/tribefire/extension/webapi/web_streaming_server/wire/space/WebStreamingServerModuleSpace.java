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
package tribefire.extension.webapi.web_streaming_server.wire.space;

import com.braintribe.model.processing.resource.server.WebStreamingServer;
import com.braintribe.web.api.registry.WebRegistries;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.api.WebRegistryConfiguration;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * This module's javadoc is yet to be written.
 */
@Managed
public class WebStreamingServerModuleSpace implements TribefireModuleContract {

	private static final String WEB_STREAMING_SERVLET_PATTERN = "/streaming";
	
	@Import
	private TribefireWebPlatformContract tfPlatform;

	//
	// Hardwired deployables
	//

	@Override
	public void bindHardwired() {
		WebRegistryConfiguration webRegistry = tfPlatform.hardwiredDeployables().webRegistry();
		
		webRegistry.addServlet( //
				 WebRegistries.servlet() //
				 .name("web-streaming-server") //
				 .instance(webStreamingServer()) //
				 .pattern(WEB_STREAMING_SERVLET_PATTERN) //
				 .multipart() //
		);
		
		webRegistry.strictAuthFilter().addPattern(WEB_STREAMING_SERVLET_PATTERN);
		webRegistry.threadRenamingFilter().addPattern(WEB_STREAMING_SERVLET_PATTERN);;
	}

	@Managed
	private WebStreamingServer webStreamingServer() {
		WebStreamingServer bean = new WebStreamingServer();
		bean.setSessionFactory(tfPlatform.requestUserRelated().sessionFactory());
		bean.setSystemSessionFactory(tfPlatform.systemUserRelated().sessionFactory());
		bean.setModelAccessoryFactory(tfPlatform.requestUserRelated().modelAccessoryFactory());
		bean.setMarshallerRegistry(tfPlatform.marshalling().registry());
		bean.setDefaultUploadResponseType("application/json");
		return bean;
	}
}
