package com.braintribe.model.deployment.http.meta;

import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Creates a configured logical reason for a matching HTTP status. Unless a body-detail rule consumes the body, its raw text becomes the reason text.
 * A {@code HttpStatusReason} describing the observed transport status is always appended as the deepest cause.
 */
@Description("Maps matching HTTP statuses to a configured logical reason type.")
public interface HttpStatusReasoning extends HttpRootReasoning {
	EntityType<HttpStatusReasoning> T = EntityTypes.T(HttpStatusReasoning.class);

	@Description("Type signature of the Reason created for the matching HTTP status.")
	String getReasonTypeSignature();
	void setReasonTypeSignature(String reasonTypeSignature);
}
