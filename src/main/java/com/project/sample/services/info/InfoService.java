package com.project.sample.services.info;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.project.sample.dal.dao.InfoDao;
import com.project.sample.helpers.PropertyHelper;

@Path("info")
@Produces(MediaType.APPLICATION_JSON)
public class InfoService {
	private static Logger LOGGER = Logger.getLogger(InfoService.class.getName());

	/**
	 * @param httpServletRequest
	 * @return
	 */
	@GET
	public Response getInfo(@Context HttpServletRequest httpServletRequest) {
		Response response = null;
		try {
			ServiceInfoMessage serviceInfoMessage = new ServiceInfoMessage();
			String hostName = InetAddress.getLocalHost().getHostName();
			PropertyHelper propertyHelper = PropertyHelper.getInstance();
			
			serviceInfoMessage.setBaseServiceURL(httpServletRequest != null ? httpServletRequest.getServerName() : null); //handle null httpServletRequest for test environment
			serviceInfoMessage.setHostName(hostName);
			serviceInfoMessage.setTimeStamp(new Date().toString());
			serviceInfoMessage.setBuildNumber(propertyHelper.getProperty("build.number"));
			serviceInfoMessage.setBaseDataBaseURL(propertyHelper.getProperty("db.host"));

			response = Response.status(Response.Status.OK).entity(serviceInfoMessage).build();
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, "Caught UnknownHostException Exception: ", e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Caught Unexpected Exception: ", e);
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}

	/**
	 * Designed for use with an external monitoring tool, this service responds
	 * with HTTP status codes to describe the availability of the database.
	 * 
	 * @param httpServletRequest
	 * @param dataBaseName
	 *            the database name as given in the ENTITY_MANAGER_TYPE Enum
	 * @return
	 */
	@GET
	@Path("SAMPLE")
	public Response getSpecificDBStatus() {
		Response response;
		try {
			boolean isAlive = InfoDao.checkIfDataBaseIsAlive();
			DatabaseInfoMessage databaseInfoMessage = new DatabaseInfoMessage();
			if (isAlive == true) {
				// The response if the database is available
				databaseInfoMessage.setMessage("The database is alive");
				response = Response.status(Response.Status.OK).entity(databaseInfoMessage).build();
			} else {
				// The response if the database is down
				databaseInfoMessage.setMessage("The database did not respond");
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(databaseInfoMessage).build();
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Caught Unexpected Exception: ", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return response;
	}
}
