package com.braintribe.model.deployment.http.meta;

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Decodes the HTTP response body as the authoritative top-level reason. It outranks status reasoning at otherwise equal precedence and owns the body,
 * so no body-detail rule is applied. A {@code HttpStatusReason} is still appended as the deepest transport cause.
 */
@Description("Decodes the HTTP response body as the top-level Reason.")
public interface HttpBodyReasoning extends HttpRootReasoning, HttpBodyBasedReasoning {
	EntityType<HttpBodyReasoning> T = EntityTypes.T(HttpBodyReasoning.class);

	@Description("Type signature of the Reason decoded from the HTTP response body.")
	String getReasonTypeSignature();
	void setReasonTypeSignature(String reasonTypeSignature);
}
