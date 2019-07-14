package com.project.sample.services.filters;

import java.io.IOException;

import javax.annotation.Priority;
import javax.persistence.EntityManager;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.project.sample.helpers.EntityManagerHelper;

@Provider
@Priority(200)
public class CleanupFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		EntityManager entityManager = (EntityManager) requestContext.getProperty(EntityManagerHelper.KEY);
		if (entityManager != null) {
			entityManager.close();
			requestContext.removeProperty(EntityManagerHelper.KEY);
		}
	}

}
