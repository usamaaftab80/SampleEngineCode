package com.project.sample.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;

public class EntityManagerHelper {
	
	public static final String KEY = "ENTITY_MANAGER";

	private static Logger LOGGER = Logger.getLogger(EntityManagerHelper.class.getName());

	private static EntityManagerFactory emf = null;

	public static void openEntityManagerFactory(String persistenceUnitName, String password) {
		if (emf == null) {
			LOGGER.log(Level.INFO, "Creating EntityManagerFactory for Persistence Unit " + persistenceUnitName);

			PropertyHelper propertyHelper = PropertyHelper.getInstance();
			
			Map<String, Object> properties = new HashMap<String, Object>();

			if (password == null) {
				password = propertyHelper.getProperty("db.password");
			}
			properties.put("javax.persistence.jdbc.password", password);
			
			String host = propertyHelper.getProperty("db.host");
			String name = propertyHelper.getProperty("db.name");
			if (host != null && name != null) {
				properties.put("javax.persistence.jdbc.url", "jdbc:sqlserver://" + host + ";databaseName=" + name);
			}
			
			String username = propertyHelper.getProperty("db.username");
			if (username != null) {
				properties.put("javax.persistence.jdbc.user", username);
			}
				
			emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
		}
	}

	public static void closeEntityManagerFactory() {
		if (emf != null) {
			emf.close();
			emf = null;
		}
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			throw new IllegalStateException("EntityManagerFactory has not been created");
		}
		return emf;
	}

	public static EntityManager getEntityManager() {
		EntityManager em = emf.createEntityManager();
		return em;
	}

	public static Cache getCache() {
		return emf.getCache();
	}

	public static boolean isLoaded(Object entity, String field) {
		PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();
		return persistenceUnitUtil.isLoaded(entity, field);
	}
}
