package com.braintribe.model.deployment.http.meta;

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Decodes a structured error body as a reason and inserts it as a cause beneath the selected root reason. This rule is resolved independently of root
 * reasoning and therefore enriches status mappings without repeating them. It is ignored when {@link HttpBodyReasoning} owns the body.
 */
@Description("Decodes a structured HTTP error body as a detail cause.")
public interface HttpBodyDetailReasoning extends HttpBodyBasedReasoning {
	EntityType<HttpBodyDetailReasoning> T = EntityTypes.T(HttpBodyDetailReasoning.class);

	@Description("Type signature of the detail Reason decoded from the HTTP response body.")
	String getReasonTypeSignature();
	void setReasonTypeSignature(String reasonTypeSignature);
}
