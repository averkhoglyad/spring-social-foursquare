package org.springframework.social.foursquare.api.impl;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public abstract class AbstractFoursquareOperations {
	
	private final boolean isAuthorized;
	
	protected final FoursquareTemplate foursquare;
	
	public AbstractFoursquareOperations(FoursquareTemplate foursquare, boolean isAuthorized) {
		this.foursquare = foursquare;
		this.isAuthorized = isAuthorized;
	}
	
	protected <T> T get(URI uri, Class<T> responseType) {
		return foursquare.getRestTemplate().getForObject(uri, responseType);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void post(URI uri, MultiValueMap<String,String> data,  Class responseType) {
	    MultiValueMap<String, String> requestData = new LinkedMultiValueMap<String, String>(data);
	    foursquare.getRestTemplate().postForObject(uri, requestData, responseType);
	}
	
	protected void delete(URI uri) {
		foursquare.getRestTemplate().delete(uri);
	}
	
	protected void requireUserAuthorization() {
		if(!isAuthorized) {
			throw new MissingAuthorizationException();
		}
	}
	
	protected URI buildUri(String path) {
		return buildUri(path, Collections.<String, String>emptyMap());
	}
	
	protected URI buildUri(String path, Map<String, String> params) {
		URIBuilder uriBuilder = foursquare.withAccessToken(API_URL_BASE + path);
		for (String paramName : params.keySet()) {
			uriBuilder.queryParam(paramName, String.valueOf(params.get(paramName)));
		}
		URI uri = uriBuilder.build();
		return uri;
	}
	
	private static final String API_URL_BASE = "https://api.foursquare.com/v2/";
}
