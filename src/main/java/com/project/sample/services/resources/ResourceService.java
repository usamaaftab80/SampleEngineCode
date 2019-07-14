package com.project.sample.services.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.project.sample.dal.dao.ResourceDao;
import com.project.sample.dal.entities.Resource;
import com.project.sample.exceptions.DaoException;
import com.project.sample.helpers.EntityManagerHelper;
import com.project.sample.services.authentication.TokenUser;

@Path("resources")
public class ResourceService {

	private static ResourceDao resourceDao = new ResourceDao();

	@GET
	@RolesAllowed("read:SP_resources")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResourceForUser(@Context ContainerRequestContext containerRequestContext) throws DaoException {
		EntityManager entityManager = (EntityManager) containerRequestContext.getProperty(EntityManagerHelper.KEY);

		List<Resource> resources = resourceDao.all(entityManager);
		return Response.status(Response.Status.OK).entity(ResourceMessage.convertFrom(resources)).build();
	}

	@GET
	@RolesAllowed("read:SP_resources")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resourceId}")
	public Response getResource(@Context ContainerRequestContext containerRequestContext, @PathParam("resourceId") int resourceId) throws DaoException {
		TokenUser tokenUser = (TokenUser) containerRequestContext.getSecurityContext().getUserPrincipal();
		EntityManager entityManager = (EntityManager) containerRequestContext.getProperty(EntityManagerHelper.KEY);
		
		Resource resource = resourceDao.get(entityManager, resourceId);
		if (resource == null) {
			throw new NotFoundException("Resource " + resourceId + " not found.");
		} else if (resource.getUserId() != tokenUser.getUserId()) {
			throw new ForbiddenException("Insufficient privilege to access Resource " + resourceId);
		}
		
		resource.getResourceLines().size(); // trigger loading of resource lines
		return Response.status(Response.Status.OK).entity(ResourceMessage.convertFrom(resource)).build();
	}


	@POST
	@RolesAllowed("write:SP_resources")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insert(
		@Context ContainerRequestContext containerRequestContext, ResourceMessage resourceMessage
	) throws DaoException {
		
		EntityManager entityManager = (EntityManager) containerRequestContext.getProperty(EntityManagerHelper.KEY);
		Resource resource = Resource.convertFromResourceMessage(resourceMessage);
		
		try {				
			entityManager.getTransaction().begin();
			resourceDao.insert(entityManager, resource);
			entityManager.getTransaction().commit();
		} catch (DaoException e) {
			entityManager.getTransaction().rollback();
			throw new InternalServerErrorException("DaoException: Unable to insert Resource " + resource.getName() + " in the database");
		}
		
		return Response.status(Response.Status.OK).entity(ResourceMessage.convertFrom(resource)).build();
	}
	
	@DELETE
	@RolesAllowed("write:SP_resources")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resourceId}")
	public Response delete(
		@Context ContainerRequestContext containerRequestContext,
		@PathParam("resourceId") int resourceId
	) throws DaoException {
		EntityManager entityManager = (EntityManager) containerRequestContext.getProperty(EntityManagerHelper.KEY);
		
		Resource resource = resourceDao.get(entityManager, resourceId);
		if (resource == null) {
			throw new InternalServerErrorException("Resource you are trying to delete, does not exist.");
		}
		
		try {
			entityManager.getTransaction().begin();
			resourceDao.delete(entityManager, resource);
			entityManager.getTransaction().commit();
		} catch (DaoException e) {
			entityManager.getTransaction().rollback();
			throw new ForbiddenException("DaoException: Could not delete the resource: " + resource.getName());
		}

		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@PUT
	@RolesAllowed("write:SP_resources")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{resourceId}")
	public Response update(
		@Context ContainerRequestContext containerRequestContext, @PathParam("resourceId") int resourceId, ResourceMessage resourceMessage
	) throws DaoException {
		
		EntityManager entityManager = (EntityManager) containerRequestContext.getProperty(EntityManagerHelper.KEY);
		Resource resource = resourceDao.get(entityManager, resourceId);
		if (resource == null) {
			throw new InternalServerErrorException("Resource you are trying to update, does not exist.");
		}
		
		resource = Resource.convertFromResourceMessage(resourceMessage);
		
		try {				
			entityManager.getTransaction().begin();
			resourceDao.update(entityManager, resource);
			entityManager.getTransaction().commit();
		} catch (DaoException e) {
			entityManager.getTransaction().rollback();
			throw new InternalServerErrorException("DaoException: Unable to insert Resource " + resource.getName() + " in the database");
		}
		
		return Response.status(Response.Status.OK).entity(ResourceMessage.convertFrom(resource)).build();
	}
}
