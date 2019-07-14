package com.project.sample.services.resources;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.core.GenericType;

import org.junit.Test;

import com.project.sample.dal.dao.ResourceDao;
import com.project.sample.dal.entities.Resource;
import com.project.sample.dal.entities.ResourceLine;
import com.project.sample.exceptions.DaoException;
import com.project.sample.helpers.EntityManagerHelper;
import com.project.sample.services.BaseServiceTest;

public class ResourceServiceTest extends BaseServiceTest {

	@Test
	public void testGetResource() throws DaoException {
		EntityManager entityManager = EntityManagerHelper.getEntityManager();
		ResourceDao resourceDao = new ResourceDao();
		
		entityManager.getTransaction().begin();
		Resource resource0 = Resource.createNewResource(1, "Resource Name", "DESCRIPTION", new ArrayList<ResourceLine>());
		resourceDao.insert(entityManager, resource0);
		entityManager.getTransaction().commit();
		entityManager.close();

		List<ResourceMessage> resources = target("resources")
				.request()
				.header("Authorization", "Bearer " + BaseServiceTest.authorizedAccessToken)
				.get(new GenericType<List<ResourceMessage>>() {});

		assertEquals(1, resources.size());
	}
	
	//testInsertResource and testUpdateResource is left intentionally due to time constraints.
}
