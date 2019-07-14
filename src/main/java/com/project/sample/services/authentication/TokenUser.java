package com.project.sample.services.authentication;

import java.security.Principal;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenUser implements Principal {

	private DecodedJWT jwt = null;
	private Integer userId = null;

	public TokenUser() {
	}

	@Override
	public String getName() {
		Claim nameClaim = jwt.getClaim("nickname");
		return nameClaim.asString();
	}

	public int getUserId() {
		return userId;
	}

	public static TokenUser parseFrom(DecodedJWT jwt) {
		TokenUser tokenUser = new TokenUser();
		tokenUser.jwt = jwt;
		tokenUser.userId = parseUserId(jwt);
		return tokenUser;
	}

	private static int parseUserId(DecodedJWT jwt) {
		Claim subClaim = jwt.getClaim("sub");

		int id = 0;
		if (subClaim instanceof Claim) {
			String str = subClaim.asString();
			if (str != null && !str.endsWith("@clients")) {
				int pipeIndex = str.indexOf("|");
				String subString = str.substring(pipeIndex + 1);
				id = Integer.parseInt(subString);
			}
		}

		return id;
	}
}
