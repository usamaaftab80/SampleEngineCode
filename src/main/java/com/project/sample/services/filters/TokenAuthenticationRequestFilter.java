package com.project.sample.services.filters;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.persistence.EntityManager;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.sample.helpers.EntityManagerHelper;
import com.project.sample.services.authentication.AuthenticationHelper;
import com.project.sample.services.authentication.TokenUser;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class TokenAuthenticationRequestFilter implements ContainerRequestFilter {
	private static Logger LOGGER = Logger.getLogger(TokenAuthenticationRequestFilter.class.getName());

	private static final String TOKEN_KEY = "Authorization";
	private static final String JWT_IDENTIFIER = "Bearer";
	private static final String SCOPE_CLAIM = "scope";
	private static final String GROUP_CLAIM = "https://www.sample.com/API/groups";

	public TokenAuthenticationRequestFilter() {
		LOGGER.info("Initializing Token Authorization Filter...");
	}

	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {
		EntityManager entityManager = EntityManagerHelper.getEntityManager();
		containerRequestContext.setProperty(EntityManagerHelper.KEY, entityManager);
		
		containerRequestContext.setSecurityContext(new SecurityContext() {
			
			private TokenUser principal = new TokenUser();
			
			@Override
			public Principal getUserPrincipal() {
				return principal;
			}

			@Override
			public boolean isUserInRole(String rolesStr) {
				List<String> authorizedVendors = new ArrayList<>();
				String[] roles = rolesStr.split(" ");				
				Boolean allowedAccess = false;
				
				// Check that an authorization token exists with the request
				String token = containerRequestContext.getHeaderString(TOKEN_KEY);
				
				if (token == null || token.length() == 0 || !token.startsWith(JWT_IDENTIFIER)) {
					throw new ForbiddenException("Missing or invalid authorization token");
				}				
				
				// Remove the "Bearer" portion of the token
				token = AuthenticationHelper.parseToken(token);
				
				// Verify that the access token is valid
				DecodedJWT jwt = null;
				try {
					 jwt = AuthenticationHelper.verifyToken(token);
				} catch (JWTVerificationException e) {
					throw new NotAuthorizedException(e);
				}
				// Set the vendor so it can be used in services
				principal = TokenUser.parseFrom(jwt);
				
				// Verify that token provides the required scopes
				Claim scopeClaim = jwt.getClaim(SCOPE_CLAIM);							
				String[] permissions = scopeClaim.asString().split(" ");
				for (String role : roles) {
					if (Arrays.asList(permissions).contains(role)) {
						allowedAccess = true;
						break;
					}
				}

				// Get the vendors this user is authorized to see information for
				Claim groupClaim = jwt.getClaim(GROUP_CLAIM);				
				authorizedVendors = groupClaim.asList(String.class);

				// Get the vendor code from the path
				String pathVendorCode = containerRequestContext.getUriInfo().getPathParameters().getFirst("vendorCode");
				
				// Verify that the access token provides permission to view info for this vendor
				if (pathVendorCode != null && authorizedVendors.contains(pathVendorCode) == false) {
					throw new ForbiddenException("Vendor code mismatch: Check that the vendor code matches a group in Auth0 Authorization");
				}
				
				return allowedAccess;
			}

			@Override
			public boolean isSecure() {
				return containerRequestContext.getSecurityContext().isSecure();
			}

			@Override
			public String getAuthenticationScheme() {
				return "custom";
			}
		});
	}
}