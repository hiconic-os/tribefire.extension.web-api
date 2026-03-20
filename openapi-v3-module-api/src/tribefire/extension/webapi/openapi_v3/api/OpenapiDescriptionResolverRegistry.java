package tribefire.extension.webapi.openapi_v3.api;

public interface OpenapiDescriptionResolverRegistry {
	void registerDescriptionResolver(String name, OpenapiDescriptionResolver resolver);
	void orderDescriptionResolvers(String... names);
}
