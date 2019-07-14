package com.project.sample.services;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.jwk.JwkException;
import com.auth0.net.AuthRequest;
import com.project.sample.helpers.EntityManagerHelper;
import com.project.sample.helpers.PropertyHelper;
import com.project.sample.services.authentication.AuthenticationHelper;

public class BaseServiceTest extends JerseyTest {
	private static final Logger LOGGER = Logger.getLogger(BaseServiceTest.class.getName());
	private static final PropertyHelper propertyHelper = PropertyHelper.getInstance();
	public static String authorizedAccessToken;
	public static String unAuthorizedAccessToken;
	
	@Before
	public void runBeforeTestMethod() throws JwkException {
		EntityManager entityManager = EntityManagerHelper.getEntityManager();
		String persistenceUnitName = entityManager.getEntityManagerFactory().getProperties().get("hibernate.ejb.persistenceUnitName").toString();
		assertEquals("DB NAME", persistenceUnitName);
	}
	
	@After
	public void runAfterTestMethod() {		
		EntityManagerHelper.closeEntityManagerFactory();
	}
	
	@BeforeClass
	public static void setup() throws JwkException {
		LOGGER.log(Level.INFO, "Initializing Test Environment");
		LOGGER.log(Level.INFO, "Finished loading config.properties");
		
		AuthenticationHelper.initializeJWTVerifier();
		
		if (authorizedAccessToken == null) {
			authorizedAccessToken = getAccessToken(propertyHelper.getProperty("auth0.authorized.username"), propertyHelper.getProperty("auth0.authorized.password"));
		}
		
		if (unAuthorizedAccessToken == null) {
			unAuthorizedAccessToken = getAccessToken(propertyHelper.getProperty("auth0.unauthorized.username"), propertyHelper.getProperty("auth0.unauthorized.password"));
		}
	}
	
	@AfterClass
	public static void teardown() {
		AuthenticationHelper.destroyJWTVerifier();
	}
	
    @Override
    protected Application configure() {
        return new WebServicesApplication();
    }
	
    private static String getAccessToken(String username, String password) {
    	AuthAPI client = new AuthAPI(propertyHelper.getProperty("auth0.domain"), propertyHelper.getProperty("auth0.clientId"), propertyHelper.getProperty("auth0.clientSecret"));
    	AuthRequest request = client.login(username, password)
		    .setAudience(propertyHelper.getProperty("auth0.audience"))
		    .setScope("openid");
    	
		try {
		    TokenHolder holder = request.execute();
		    return holder.getAccessToken();
		} catch (APIException exception) {
			LOGGER.log(Level.SEVERE, "APIException", exception);
		} catch (Auth0Exception exception) {
			LOGGER.log(Level.SEVERE, "Auth0Exception", exception);
		}
		
		return null;
    }
}
