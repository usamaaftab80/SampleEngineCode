package com.project.sample.dal.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.project.sample.dal.entities.ResourceLine;
import com.project.sample.exceptions.DaoException;
import com.project.sample.helpers.EntityManagerHelper;

public class ResourceLineDao extends BaseDao<ResourceLine> {

	private static Logger LOGGER = Logger.getLogger(ResourceLineDao.class.getName());

	public ResourceLineDao() {
		super(ResourceLine.class);
	}
	
	/**
	 * Retrieves a list of Resource Lines associated to a Resource which is associated to supplied
	 * userId.
	 * 
	 * @param resourceUserId
	 * @return
	 * @throws DaoException
	 */
	public List<ResourceLine> getByResourceUserId(int resourceUserId) throws DaoException {
		EntityManager entityManager = EntityManagerHelper.getEntityManager();
		List<ResourceLine> resourceLines = null;
		try {
			TypedQuery<ResourceLine> query = entityManager.createNamedQuery("ResourceLine.findByResourceUserId", ResourceLine.class);
			query.setParameter("resourceUserId", resourceUserId);
			resourceLines = query.getResultList();
		} catch (NoResultException ignore) {
			// intentionally ignored
		} catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "Unable to execute Named Query", e);
			throw new DaoException("Named Query Unsuccessful", e);
		} finally {
			entityManager.close();
		}

		return resourceLines;
	}

}
