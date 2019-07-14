package com.project.sample.dal.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.project.sample.dal.entities.Resource;
import com.project.sample.exceptions.DaoException;
import com.project.sample.helpers.EntityManagerHelper;

public class ResourceDao extends BaseDao<Resource> {

	private static Logger LOGGER = Logger.getLogger(ResourceDao.class.getName());

	public ResourceDao() {
		super(Resource.class);
	}
	
	/**
	 * Retrieves a single Resource by resourceId and includes its child Resource Lines.
	 *
	 * @param resourceId
	 * @return
	 * @throws DaoException
	 */
	public Resource get(int resourceId) throws DaoException {
		EntityManager entityManager = EntityManagerHelper.getEntityManager();
		Resource resource = null;
		try {
			TypedQuery<Resource> query = entityManager.createNamedQuery("Resource.findByResourceId", Resource.class);
			query.setParameter("id", resourceId);
			resource = query.getSingleResult();
		} catch (NoResultException ignore) {
			// intentionally ignored
		} catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "Unable to execute Named Query", e);
			throw new DaoException("Named Query Unsuccessful", e);
		} finally {
			entityManager.close();
		}

		return resource;
	}

	/**
	 * Retrieves a list of Resources associated to the supplied userId. Child Resource Lines are not returned.
	 * 
	 * @param userId
	 * @return
	 * @throws DaoException
	 */
	public List<Resource> getByUserId(EntityManager entityManager, int userId) throws DaoException {

		List<Resource> resources = null;
		try {
			TypedQuery<Resource> query = entityManager.createNamedQuery("Resource.findByUserId", Resource.class);
			query.setParameter("userId", userId);
			resources = query.getResultList();
		} catch (NoResultException ignore) {
			// intentionally ignored
		} catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE,"Unable to execute Named Query", e);
			throw new DaoException("Named Query Unsuccessful", e);
		}
		return resources;
	}

	/**
	 * Retrieves a list of Resources that have child Resource Lines matching the supplied resourceLineKey
	 * and resourceLineValue. Child Resource Lines are not included. 
	 * 
	 * @param resourceLineKey
	 * @param resourceLineValue
	 * @return
	 * @throws DaoException
	 */
	public List<Resource> getByResourceLineKeyValue(String resourceLineKey, String resourceLineValue) throws DaoException {
		EntityManager entityManager = EntityManagerHelper.getEntityManager();
		List<Resource> resources = null;
		try {
			TypedQuery<Resource> query = entityManager.createNamedQuery("Resource.findByResourceLineValue", Resource.class);
			query.setParameter("resourceLineKey", resourceLineKey);
			query.setParameter("resourceLineValue", resourceLineValue);
			resources = query.getResultList();
		} catch (NoResultException ignore) {
			// intentionally ignored
		} catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE,"Unable to execute Named Query", e);
			throw new DaoException("Named Query Unsuccessful", e);
		} finally {
			entityManager.close();
		}

		return resources;
	}
}
