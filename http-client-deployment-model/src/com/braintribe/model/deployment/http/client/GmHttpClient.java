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
package com.braintribe.model.deployment.http.client;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.logging.LogLevel;

/**
 * Connection settings are representing the options of org.apache.http.client.config.RequestConfig
 */
public interface GmHttpClient extends HttpClient {

	final EntityType<GmHttpClient> T = EntityTypes.T(GmHttpClient.class);

	String requestLogging = "requestLogging";
	String responseLogging = "responseLogging";

	String getProxy();
	void setProxy(String proxy);

	String getLocalAddress();
	void setLocalAddress(String address);

	String getCookieSpec();
	void setCookieSpec(String cookieSpec);

	Integer getConnectTimeout();
	void setConnectTimeout(Integer connectTimeout);

	Integer getConnectionRequestTimeout();
	void setConnectionRequestTimeout(Integer connectionRequestTimeout);

	Integer getSocketTimeout();
	void setSocketTimeout(Integer socketTimeout);

	@Initializer("50")
	Integer getMaxRedirects();
	void setMaxRedirects(Integer maxRedirects);

	@Initializer("true")
	boolean getAuthenticationEnabled();
	void setAuthenticationEnabled(boolean authenticationEnabled);

	@Initializer("true")
	boolean getRedirectsEnabled();
	void setRedirectsEnabled(boolean redirectsEnabled);

	@Initializer("true")
	boolean getRelativeRedirectsAllowed();
	void setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed);

	@Initializer("true")
	boolean getContentCompressionEnabled();
	void setContentCompressionEnabled(boolean contentCompressionEnabled);

	@Name("Request Logging")
	@Description("Dynamic LogLevel for request logging")
	LogLevel getRequestLogging();
	void setRequestLogging(LogLevel requestLogging);

	@Name("Response Logging")
	@Description("Dynamic LogLevel for response logging")
	LogLevel getResponseLogging();
	void setResponseLogging(LogLevel responseLogging);

}
