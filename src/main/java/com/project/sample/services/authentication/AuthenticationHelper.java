package com.project.sample.services.authentication;

import java.security.interfaces.RSAKey;
import java.util.logging.Logger;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.sample.helpers.PropertyHelper;

public class AuthenticationHelper {

	private static final PropertyHelper propertyHelper = PropertyHelper.getInstance();
	private static Logger LOGGER = Logger.getLogger(AuthenticationHelper.class.getName());
	private static int SECONDS_OF_LEEWAY = 300;

	private static JWTVerifier verifier = null;

	public static void initializeJWTVerifier() throws JwkException {
		LOGGER.info("Initializing Token Authorization Filter...");
		
		JwkProvider provider = new UrlJwkProvider(propertyHelper.getProperty("auth0.domain"));
		Jwk jwk = provider.get(propertyHelper.getProperty("auth0.kid"));
		
		verifier = JWT.require(Algorithm.RSA256((RSAKey) jwk.getPublicKey()))
				.withIssuer(propertyHelper.getProperty("auth0.domain"))
				.acceptLeeway(SECONDS_OF_LEEWAY)
				.build(); //Reusable verifier instance
	}

	public static JWTVerifier getJWTVerifier() {
		return verifier;
	}

	public static void destroyJWTVerifier() {
		verifier = null;
	}

	public static String parseToken(String authorizationHeader) {
		String token = null;
		if (authorizationHeader != null) {
			String[] authorizationParts = authorizationHeader.trim().split(" ");
			if (authorizationParts.length == 2) {
				// This is to remove the term "Bearer" from the Authorization Header
				token = authorizationParts[1];
			}
		}
		return token;
	}

	public static DecodedJWT verifyToken(String token) throws JWTVerificationException {
		return getJWTVerifier().verify(token);
	}
}
