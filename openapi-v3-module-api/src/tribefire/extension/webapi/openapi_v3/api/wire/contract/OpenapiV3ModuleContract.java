package tribefire.extension.webapi.openapi_v3.api.wire.contract;

import com.braintribe.wire.api.space.WireSpace;

import tribefire.extension.webapi.openapi_v3.api.OpenapiDescriptionResolverRegistry;

public interface OpenapiV3ModuleContract extends WireSpace {
	OpenapiDescriptionResolverRegistry descriptionResolverRegistry();
}
