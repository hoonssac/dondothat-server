package org.bbagisix.oauth2.service;

public interface OAuth2Service {
	String getGoogleAccessToken(String code);
	// String getNaverAccessToken(String code, String state);
}
