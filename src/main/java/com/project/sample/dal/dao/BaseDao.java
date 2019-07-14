package com.project.sample.dal.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.project.sample.exceptions.DaoException;
import com.project.sample.helpers.EntityManagerHelper;

/**
 * define basic Dao operation:find, insert, update,delete,all
 */
public class BaseDao<T> {

	private static Logger LOGGER = Logger.getLogger(BaseDao.class.getName());

	private Class<T> type;

	public BaseDao(Class<T> type) {
		this.type = type;
	}

	public T get(EntityManager entityManager, int id) throws DaoException {

		Cache cache = EntityManagerHelper.getCache();		
		
		T entity = null;
		try {
			LOGGER.log(Level.INFO, "Cache contains entity " + id + " of type " + type.getCanonicalName() + ": " + cache.contains(type, id));
			entity = entityManager.find(type, id);
		} catch (IllegalArgumentException e) {
			String message = "Entity Manager unable to perform find on " + type.getCanonicalName();
			LOGGER.log(Level.SEVERE, message, e);
			throw new DaoException(message, e);
		}
		return entity;
	}

	public T insert(EntityManager entityManager, T entity) throws DaoException {
		try {
			entityManager.persist(entity);
		} catch (Exception e) {
			String message = "Unable to persist entity " + type.getCanonicalName();
			LOGGER.log(Level.SEVERE, message, e);
			throw new DaoException(message, e);
		}
		return entity;
	}

	public T update(EntityManager entityManager, T entity) throws DaoException {
		try {
			entityManager.merge(entity);
		} catch (Exception e) {
			String message = "Unable to merge entity " + type.getCanonicalName();
			LOGGER.log(Level.SEVERE, message, e);
			throw new DaoException(message, e);
		}
		return entity;
	}

	public void delete(EntityManager entityManager, T entity) throws DaoException {
		try {
			Integer id = (Integer) EntityManagerHelper.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
			entity = entityManager.find(type, id);
			entityManager.remove(entity);
		} catch (Exception e) {
			String message = "Unable to remove entity " + type.getCanonicalName();
			LOGGER.log(Level.SEVERE, message, e);
			throw new DaoException(message, e);
		}
	}

	public List<T> all(EntityManager entityManager) throws DaoException {
		List<T> results = null;
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(type);
			Root<T> rootEntry = query.from(type);
			CriteriaQuery<T> all = query.select(rootEntry);
			TypedQuery<T> allQuery = entityManager.createQuery(all);
			results = allQuery.getResultList();
		} catch (Exception e) {
			String message = "Unable to perform all query for " + type.getCanonicalName();
			LOGGER.log(Level.SEVERE, message, e);
			throw new DaoException(message, e);
		}

		return results;
	}

}
