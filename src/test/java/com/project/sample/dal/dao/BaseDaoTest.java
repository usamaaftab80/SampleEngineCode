package com.project.sample.dal.dao;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

import com.project.sample.helpers.EntityManagerHelper;

public class BaseDaoTest {
	
	@Before
	public void initializeTestEnvironment() {
		EntityManagerHelper.openEntityManagerFactory("ENTITY_MANAGER_TEST_FACTORY", null);
		EntityManager entityManager = EntityManagerHelper.getEntityManager();
		
		String persistenceUnitName = entityManager.getEntityManagerFactory().getProperties().get("hibernate.ejb.persistenceUnitName").toString();
		assertEquals("ENTITY_MANAGER_TEST_FACTORY", persistenceUnitName);
	}
	
	@After
	public void teardownTestEnvironment() {
		EntityManagerHelper.closeEntityManagerFactory();
	}
	
}
