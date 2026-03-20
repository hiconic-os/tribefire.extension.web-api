package com.braintribe.model.openapi.v3_0.export;

import java.util.function.Consumer;

public class DescriptionBuilder implements Consumer<String> {
	private StringBuilder builder;
	
	@Override
	public void accept(String text) {
		add(text);
	}
	
	public void add(String text) {
		if (builder == null)
			builder = new StringBuilder();
		
		builder.append(text);
	}
	
	public String asString() {
		return builder != null? builder.toString(): null;
	}
	
	
}
