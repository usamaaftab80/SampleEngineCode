package com.project.sample.dal.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.project.sample.dal.entities.Resource;
import com.project.sample.dal.entities.ResourceLine;
import com.project.sample.exceptions.DaoException;
import com.project.sample.helpers.EntityManagerHelper;

public class ResourceDaoTest extends BaseDaoTest {

	@Test
	public void testInsertListUpdateDeleteResource() throws DaoException {
		EntityManager entityManager = EntityManagerHelper.getEntityManager();
		ResourceDao resourceDao = new ResourceDao();
		
		entityManager.getTransaction().begin();
		
		Resource resource = Resource.createNewResource(1, "Resource Name", "DESCRIPTION", new ArrayList<ResourceLine>());
		ResourceLine resourceLine = ResourceLine.createNewResource("Resource Line Name", "KEY", "VALUE");
		resourceLine.setResource(resource);
		resource.getResourceLines().add(resourceLine);
		resourceDao.insert(entityManager, resource);
		
		assertNotNull(resource.getId());
		assertNotNull(resource.getResourceLines().get(0).getId());
		
		entityManager.getTransaction().commit();
		
		//-----------------------------------------------------------
		entityManager.getTransaction().begin();
		
		Resource resourceFromDB = resourceDao.get(resource.getId());
		resourceFromDB.setDescription("Resource Description");
		resourceFromDB.getResourceLines().get(0).setValue("NEW_VALUE");
		resourceDao.update(entityManager, resourceFromDB);
		
		Resource resource2 = Resource.createNewResource(2, "Resource 2 Name", "DESCRIPTION", new ArrayList<ResourceLine>());
		ResourceLine resourceLine2 = ResourceLine.createNewResource("Resource Line Name", "KEY", "VALUE");
		resourceLine2.setResource(resource2);
		resource2.getResourceLines().add(resourceLine2);
		resourceDao.insert(entityManager, resource2);
		
		entityManager.getTransaction().commit();
		
		
		List<Resource> resources = resourceDao.getByUserId(entityManager, 1);
		assertEquals(2, resources.size());
		
		List<Resource> resources2 = resourceDao.getByResourceLineKeyValue("KEY", "NEW_VALUE");
		assertEquals(1, resources2.size());
		
		
		entityManager.getTransaction().begin();
		resourceDao.delete(entityManager, resource);
		resourceDao.delete(entityManager, resource2);
		entityManager.getTransaction().commit();
		
		entityManager.close();
	}

}
