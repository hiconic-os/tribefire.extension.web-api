package com.braintribe.model.deployment.http.meta;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/** Creates the top-level reason for an HTTP failure. Exactly one matching root strategy is selected. */
@Abstract
@Description("Creates the top-level reason for a matching HTTP failure.")
public interface HttpRootReasoning extends HttpReasoning {
	EntityType<HttpRootReasoning> T = EntityTypes.T(HttpRootReasoning.class);

	/** Preserves the remote status as the outward HTTP status instead of letting the resulting reason determine it. */
	boolean getUseOriginalStatusCode();
	void setUseOriginalStatusCode(boolean useOriginalStatusCode);
}
