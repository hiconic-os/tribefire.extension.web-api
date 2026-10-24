package com.braintribe.model.deployment.http.meta;

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Decodes a structured error body as evidence carried by the selected HTTP status reason. This rule is resolved independently of root reasoning and
 * therefore enriches status mappings without repeating them. It is ignored when {@link HttpBodyReasoning} owns the body.
 */
@Description("Decodes a structured HTTP error body as evidence on the HTTP status reason.")
public interface HttpBodyDetailReasoning extends HttpBodyBasedReasoning {
	EntityType<HttpBodyDetailReasoning> T = EntityTypes.T(HttpBodyDetailReasoning.class);

	@Description("Entity type expected in the HTTP response body. The legacy property name is retained for compatibility.")
	String getReasonTypeSignature();
	void setReasonTypeSignature(String reasonTypeSignature);
}
