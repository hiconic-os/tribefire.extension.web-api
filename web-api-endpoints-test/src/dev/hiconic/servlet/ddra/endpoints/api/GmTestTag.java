package dev.hiconic.servlet.ddra.endpoints.api;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface GmTestTag extends GenericEntity {

	EntityType<GmTestTag> T = EntityTypes.T(GmTestTag.class);

	String getName();
	void setName(String name);

	GmTestTag getTag();
	void setTag(GmTestTag tag);

}
