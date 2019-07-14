package com.project.sample.dal.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.project.sample.dal.entities.Resource;
import com.project.sample.exceptions.DaoException;
import com.project.sample.helpers.EntityManagerHelper;

public class InfoDao {

	private static Logger LOGGER = Logger.getLogger(InfoDao.class.getName());

	private InfoDao() {
	}

	public static boolean checkIfDataBaseIsAlive() throws DaoException {
		boolean isDataBaseAlive = false;

		EntityManager entityManager = EntityManagerHelper.getEntityManager();

		try {
			Object queryResult = null;

			entityManager.createNativeQuery("SHUTDOWN");

			Query query = entityManager.createNamedQuery("Resource.checkIfDatabaseIsAlive", Resource.class);
			queryResult = query.getResultList();

			if (queryResult != null) {
				isDataBaseAlive = true;
			}
		} catch (Exception err) {
			LOGGER.log(Level.SEVERE, "Cannot execute query: " + err.getStackTrace());
		} finally {
			entityManager.close();
		}

		return isDataBaseAlive;
	}

}